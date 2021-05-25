CREATE OR REPLACE TRIGGER TRG_ACTUALIZA_MONTO
    BEFORE INSERT
    ON PROVEEDOR_PEDIDO_PRODUCTO
    FOR EACH ROW
declare
    --VARIABLE PARA CALCULAR EL MONTO
    v_MONTO_CALCULADO    NUMBER(19, 2);
    --VARIABLE PARA OBTENER EL MONTO DE LA FACTURA
    v_MONTO_FACTURA      NUMBER(19, 2);
    v_CANTIDAD_PRODUCTOS NUMBER(19, 2);
    v_VALOR_PRODUCTO     NUMBER(19, 2);
BEGIN
    --v_MONTO_CALCULADO := 10.5;

    SELECT MONTO
    INTO v_MONTO_FACTURA
    FROM factura_orden_pago
    WHERE pedi_prov_id IN (select PEDI_PROV_ID from PEDIDO_PROVEEDOR where PEDI_PROV_ID = :NEW.PEDI_PROV_ID);

    SELECT cantidad
    INTO v_CANTIDAD_PRODUCTOS
    FROM pedido_producto
    where PEDIDO_ID = :NEW.PEDIDO_ID
      AND PRODUCTO_ID = :NEW.PRODUCTO_ID;

    SELECT PRECIO
    INTO v_VALOR_PRODUCTO
    FROM PRODUCTO_PROVEEDOR
    WHERE PRODUCTO_ID = : NEW.PRODUCTO_ID
      AND PROVEEDOR_ID IN (SELECT PROVEEDOR_ID FROM PEDIDO_PROVEEDOR WHERE PEDI_PROV_ID = : NEW.PEDI_PROV_ID);

    v_MONTO_CALCULADO := v_MONTO_FACTURA + (v_CANTIDAD_PRODUCTOS * v_VALOR_PRODUCTO);

    UPDATE factura_orden_pago
    SET Monto = v_MONTO_CALCULADO
    where pedi_prov_id IN (select PEDI_PROV_ID from PEDIDO_PROVEEDOR where PEDI_PROV_ID = :NEW.PEDI_PROV_ID);

end;
/

create or replace  procedure crear_pago(id_factura_orden_pago IN NUMBER, id_moneda in number,
                                                      id_forma_pago IN NUMBER, usuario in varchar2,
                                                      monto_pagado IN NUMBER)
    IS
    contador_factura    number;
    contador_moneda     number;
    contador_pago       number;
    monto_total_factura number(19, 2);
    monto_total_pagado  number(19, 2);

BEGIN
    --validamos que existan los registros que vienen de parametros
    select count(*)
    into contador_factura
    from factura_orden_pago
    where fact_orden_pago_id = id_factura_orden_pago
      and estado = 'A';
    select count(*) into contador_moneda from moneda where moneda_id = id_moneda and estado = 'A';
    select count(*) into contador_pago from forma_pago where forma_pago_id = id_forma_pago and estado = 'A';

    if (contador_factura = 1) then
        if (contador_moneda = 1) then
            if (contador_pago = 1) then
                insert into pago(pago_id, create_date, estado, monto, user_create, fact_orden_pago_id, forma_pago_id,
                                 moneda_id)
                values (pago_seq.nextval, sysdate, 'A', monto_pagado, usuario, id_factura_orden_pago, id_forma_pago,
                        id_moneda);
                select sum(MONTO) into monto_total_pagado from PAGO where FACT_ORDEN_PAGO_ID = id_factura_orden_pago;
                select MONTO
                into monto_total_factura
                from factura_orden_pago
                where FACT_ORDEN_PAGO_ID = id_factura_orden_pago;
                if (monto_total_pagado < monto_total_factura) then
                    update factura_orden_pago
                    set ESTADO_FACTURA='PAGADO PARCIALMENTE',
                        UPDATE_DATE=SYSDATE,
                        USER_UPDATE= usuario
                    WHERE FACT_ORDEN_PAGO_ID = id_factura_orden_pago;
                else
                    update factura_orden_pago
                    set ESTADO_FACTURA='PAGADO',
                        UPDATE_DATE=SYSDATE,
                        USER_UPDATE= usuario
                    WHERE FACT_ORDEN_PAGO_ID = id_factura_orden_pago;
                end if;
                commit;
            end if;
        end if;
    end if;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        ROLLBACK;
    WHEN OTHERS THEN
        ROLLBACK;
END crear_pago;
/

--PROCEDIMINETO PARA DENEGAR UN PEDIDO
--SE EJECUTA DESDE EL SISTEMA
create or replace PROCEDURE denegar_pedido(ID_Pedido NUMBER, usuario IN VARCHAR2)
IS
estado_pedido number;
BEGIN

   select pro_pedi_id into estado_pedido from Progreso_pedido where Estado_pedido ='Cancelado' and estado ='A';

   if(estado_pedido>0)then
    update pedido set pro_pedi_id=estado_pedido,update_date=sysdate,user_update=user,estado='N' where pedido_id=ID_Pedido;
   else
   insert into progreso_pedido(pro_pedi_id, estado_pedido, user_create,create_date,estado) values (prog_pedi_seq.nextval,'Cancelado', user, sysdate,'A');
    select pro_pedi_id into estado_pedido from Progreso_pedido where Estado_pedido ='Cancelado' and estado ='A';
     update pedido set pro_pedi_id=estado_pedido,update_date=sysdate,user_update=user,estado='N' where pedido_id=ID_Pedido;
     commit;
   end if;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
     DBMS_OUTPUT.PUT_LINE('si entro');
    ROLLBACK;
    WHEN OTHERS THEN
     ROLLBACK;

END denegar_pedido;
/

create or replace PROCEDURE APROBAR_PEDIDO(Id_Pedido IN NUMBER, usuario IN VARCHAR2)
    IS
    --Declaracion de variables
    estado_pedido             number;
    orden_compra              int;
    --declaracion del cursor y variables para el ingreso del pedido al proveedor

    CURSOR c_proveedor_pedido is (
        SELECT PRODUCTO_ID, CANTIDAD
        FROM PEDIDO_PRODUCTO
        WHERE PEDIDO_ID = Id_Pedido
    );
    resultado                 c_proveedor_pedido%ROWTYPE;
    ordenes_compra_id         number;
    precio1                   float;
    contador_orden_proveedor  int;
    prove_id                  number;
    pedido_producto_proveedor int;
    orden_provee              number;
    contador_facturas         int;

BEGIN

    select pro_pedi_id into estado_pedido from Progreso_pedido where Estado_pedido = 'Aprobado' and estado = 'A';

    --Realiza la actualizacion del pedido para que quede en estado Aprobado
    if (estado_pedido > 0) then
        update pedido
        set pro_pedi_id=estado_pedido, update_date=sysdate, user_update=usuario
        where pedido_id = Id_Pedido;

    else
        insert into progreso_pedido(pro_pedi_id, estado_pedido, user_create, create_date, estado)
        values (prog_pedi_seq.nextval, 'Aprobado', usuario, sysdate, 'A');

        select pro_pedi_id into estado_pedido from Progreso_pedido where Estado_pedido = 'Aprobado' and estado = 'A';
        update pedido
        set pro_pedi_id=estado_pedido, update_date=sysdate, user_update=usuario
        where pedido_id = Id_Pedido;

    end if;

    --ingresamos la orden de compra
    --verificamos que no exista la orden de compra
    select count(*) into orden_compra from INGRESO_ORDEN_COMPRA where PEDIDO_ID = Id_Pedido;
    if (orden_compra != 1) then
        insert into INGRESO_ORDEN_COMPRA(ORDEN_COMPRA_ID, create_date, estado, user_create, pedido_id)
        values (ING_ORDEN_COMPRA_SEQ.nextval, sysdate, 'A', usuario, Id_Pedido);


    end if;

    --INGRESAMOS EL PEDIDO AL PROVEEDOR y FACTURA ORDEN DE PAGO.
    open c_proveedor_pedido;

    LOOP
        fetch c_proveedor_pedido into resultado;
        exit when c_proveedor_pedido%NOTFOUND;
        --VERIFICAMOS EL PRECIO MINIMO DEL PRODUCTO
        SELECT min(PRECIO) into precio1 FROM PRODUCTO_PROVEEDOR WHERE PRODUCTO_ID = resultado.PRODUCTO_ID;
        select Orden_compra_id into ordenes_compra_id from INGRESO_ORDEN_COMPRA where pedido_id = Id_Pedido;
        select PROVEEDOR_ID
        into prove_id
        from PRODUCTO_PROVEEDOR
        where PRECIO = precio1
          and PRODUCTO_ID = resultado.PRODUCTO_ID
          and ROWNUM <= 1;
        select count(*)
        into contador_orden_proveedor
        from pedido_proveedor
        where ORDEN_COMPRA_ID = ordenes_compra_id
          and PROVEEDOR_ID = prove_id;
        if (contador_orden_proveedor = 0) then
            insert into PEDIDO_PROVEEDOR(PEDI_PROV_ID, create_date, estado, user_create, ORDEN_COMPRA_ID, proveedor_id)
            values (Pedi_Prove_seq.nextval, sysdate, 'A', usuario, ordenes_compra_id, prove_id);

            select PEDI_PROV_ID
            into orden_provee
            from pedido_proveedor
            where ORDEN_COMPRA_ID = ordenes_compra_id
              and PROVEEDOR_ID = prove_id;

            SELECT COUNT(*) INTO contador_facturas from FACTURA_ORDEN_PAGO WHERE PEDI_PROV_ID = orden_provee;
            select count(*)
            into pedido_producto_proveedor
            from proveedor_pedido_producto
            where PRODUCTO_ID = resultado.PRODUCTO_ID
              and PEDIDO_ID = Id_Pedido
              and PEDI_PROV_ID = orden_provee;
             IF (contador_facturas = 0) THEN
                INSERT INTO FACTURA_ORDEN_PAGO(fact_orden_pago_id, create_date, estado,monto,estado_factura, fecha_emision, numero_factura,
                                               user_create, pedi_prov_id)
                VALUES (FACT_ORDEN_PAGO_SEQ.nextval, sysdate, 'A', 0.0,'PENDIENTE',SYSDATE, FACT_ORDEN_PAGO_SEQ.nextval, usuario,
                        orden_provee);
            end if;
            if (pedido_producto_proveedor = 0) then
                DBMS_OUTPUT.PUT_LINE('si entro');
                insert into proveedor_pedido_producto(PEDIDO_ID, PRODUCTO_ID, PEDI_PROV_ID)
                values (Id_Pedido, resultado.PRODUCTO_ID, orden_provee);

            end if;

        end if;
        select PEDI_PROV_ID
        into orden_provee
        from pedido_proveedor
        where ORDEN_COMPRA_ID = ordenes_compra_id
          and PROVEEDOR_ID = prove_id;
        SELECT COUNT(*) INTO contador_facturas from FACTURA_ORDEN_PAGO WHERE PEDI_PROV_ID = orden_provee;
        select count(*)
        into pedido_producto_proveedor
        from proveedor_pedido_producto
        where PRODUCTO_ID = resultado.PRODUCTO_ID
          and PEDIDO_ID = Id_Pedido
          and PEDI_PROV_ID = orden_provee;
         IF (contador_facturas = 0) THEN
           INSERT INTO FACTURA_ORDEN_PAGO(fact_orden_pago_id, create_date, estado,monto,estado_factura, fecha_emision, numero_factura,
                                               user_create, pedi_prov_id)
                VALUES (FACT_ORDEN_PAGO_SEQ.nextval, sysdate, 'A', 0.0,'PENDIENTE',SYSDATE, FACT_ORDEN_PAGO_SEQ.nextval, usuario,
                        orden_provee);
        end if;
        if (pedido_producto_proveedor = 0) then
            DBMS_OUTPUT.PUT_LINE('si entro');
            insert into proveedor_pedido_producto(PEDIDO_ID, PRODUCTO_ID, PEDI_PROV_ID)
            values (Id_Pedido, resultado.PRODUCTO_ID, orden_provee);

        end if;


    end loop;
    CLOSE c_proveedor_pedido;
    commit;


EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('si entro');
        ROLLBACK;

    WHEN OTHERS THEN
        ROLLBACK;


END APROBAR_PEDIDO;
/

--Procedimiento para dar mantenimiento a la tabla tipo de producto.
--Declaracion del paquete
create or replace package mmto_tipo_producto is

    --Declaracion de los procedimientos
    procedure insertar_tipo_producto(tipoproducto in varchar2);

    procedure actualizar_tipo_producto(id number, tipoproducto tipo_producto.tipo%type);

    procedure eliminar_tipo_producto(id number);

end mmto_tipo_producto;
/
create or replace package body mmto_tipo_producto is

    procedure insertar_tipo_producto(tipoproducto in varchar2) is
        contador int;
    begin
        select count(*) into contador from TIPO_PRODUCTO where upper(TIPO) = upper(tipoproducto) and ESTADO = 'A';
        if (contador >= 1) then
            DBMS_OUTPUT.PUT_LINE('YA EXISTE');
        else
            insert into tipo_producto(tipo_prod_id, create_date, estado, user_create, tipo)
            values (tipo_prod_seq.nextval, sysdate, 'A', user, tipoproducto);
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO INGRESADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_tipo_producto;

    procedure actualizar_tipo_producto(id number, tipoproducto tipo_producto.tipo%type) is
        contador int;
        EXISTE   INT;
        yaexiste int;
    begin
        select count(*) into contador from TIPO_PRODUCTO where ESTADO = 'A' and tipo_prod_id = id;
        select count(*)
        into EXISTE
        from TIPO_PRODUCTO
        where upper(TIPO) = upper(tipoproducto)
          and ESTADO = 'A'
          AND TIPO_PROD_ID = id;
        select count(*) into yaexiste from TIPO_PRODUCTO where upper(TIPO) = upper(tipoproducto) and ESTADO = 'A';
        if (contador = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsIF (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE TIPO DE PRODUCTO YA EXISTE');
        ELSE
            update tipo_producto set tipo=tipoproducto, update_date=sysdate, user_update=user where tipo_prod_id = id;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_tipo_producto;

    procedure eliminar_tipo_producto(id number) is
        contador          int;
        contadorProductos int;
    begin
        select count(*) into contadorProductos from PRODUCTO where ESTADO = 'A' and tipo_prod_id = id;
        select count(*) into contador from TIPO_PRODUCTO where ESTADO = 'A' and tipo_prod_id = id;
        if (contadorProductos > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN PRODUCTOS ASOCIADOS');
        ELSif (contador >= 1) then
            update tipo_producto set estado='N', update_date=sysdate, user_update=user where tipo_prod_id = id;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_tipo_producto;

end mmto_tipo_producto;
/

--Procedimiento para dar mantenimiento a la tabla region.
--Declaracion del paquete
create or replace package mmto_region is

    --Declaracion de los procedimientos
    procedure insertar_region(nombre_region in varchar2);

    procedure actualizar_region(id number, nombre_region region.NOMBRE%type);

    procedure eliminar_region(id number);

end mmto_region;
/
create or replace package body mmto_region is

    procedure insertar_region(nombre_region in varchar2) is
        contador int;
    begin
        select count(*) into contador from REGION where upper(NOMBRE) = upper(nombre_region) and ESTADO = 'A';
        if (contador >= 1) then
            DBMS_OUTPUT.PUT_LINE('YA EXISTE');
        else
            insert into REGION(region_id, create_date, estado, user_create, nombre)
            values (reguion_seq.nextval, sysdate, 'A', user, nombre_region);
            commit;
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end insertar_region;

    procedure actualizar_region(id number, nombre_region region.NOMBRE%type) is
        contador int;
        EXISTE   INT;
        yaexiste int;
    begin
        select count(*) into contador from REGION where ESTADO = 'A' and REGION_ID = id;
        select count(*)
        into EXISTE
        from REGION
        where upper(NOMBRE) = upper(nombre_region)
          and ESTADO = 'A'
          AND REGION_ID = id;
        select count(*) into yaexiste from REGION where upper(NOMBRE) = upper(nombre_region) and ESTADO = 'A';
        if (contador = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');

        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE REGION YA EXISTE');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        ELSE
            update REGION set NOMBRE=nombre_region, update_date=sysdate, user_update=user where REGION_ID = id;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_region;

    procedure eliminar_region(id number) is
        contador     int;
        contadorPais int;
    begin
        select count(*) into contador from REGION where ESTADO = 'A' and REGION_ID = id;
        select count(*) into contadorPais from pais where ESTADO = 'A' and region_id = id;
        if (contadorPais > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN PAISES ASOCIADOS');
        ELSIF (contador >= 1) then
            update REGION set estado='N', update_date=sysdate, user_update=user where REGION_ID = id;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE LA REGION');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_region;

end mmto_region;
/

--Procedimiento para dar mantenimiento a la tabla pais.
--Declaracion del paquete
create or replace package mmto_pais is

    --Declaracion de los procedimientos
    procedure insertar_pais(nombre_pais in varchar2, id_region number);

    procedure actualizar_pais(id_pais number, nombre_pais in varchar2, id_region number);

    PROCEDURE eliminar_pais(id_pais number);

end mmto_pais;
/
create or replace package body mmto_pais is

    procedure insertar_pais(nombre_pais in varchar2, id_region number) is
        contadorPais   int;
        contadorRegion int;
    begin
        select count(*) into contadorPais from PAIS where upper(NOMBRE) = upper(nombre_pais) and ESTADO = 'A';
        select count(*) into contadorRegion from REGION where REGION_ID = id_region and ESTADO = 'A';
        if (contadorPais >= 1) then
            DBMS_OUTPUT.PUT_LINE('PAIS YA EXISTE');
        elsif (contadorRegion >= 1) then
            insert into PAIS(pais_id, create_date, estado, user_create, nombre, REGION_ID)
            values (pais_seq.nextval, sysdate, 'A', user, nombre_pais, id_region);
            commit;
        else
            DBMS_OUTPUT.PUT_LINE('REGION NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_pais;

    --Procedimiento para actualizar nombre del pais y region.
    procedure actualizar_pais(id_pais number, nombre_pais in varchar2, id_region number) is
        contadorPais   int;
        contadorRegion int;
        EXISTE         INT;
        yaexiste       int;
    begin
        select count(*) into contadorRegion from REGION where ESTADO = 'A' and REGION_ID = id_region;
        select count(*) into contadorPais from PAIS where ESTADO = 'A' and PAIS_ID = id_pais;
        select count(*)
        into EXISTE
        from PAIS
        where upper(NOMBRE) = upper(nombre_pais)
          and ESTADO = 'A'
          AND REGION_ID = id_region
          AND PAIS_ID = id_pais;
        select count(*)
        into yaexiste
        from PAIS
        where upper(NOMBRE) = upper(nombre_pais) and ESTADO = 'A' AND PAIS_ID != id_pais;
        if (contadorPais = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contadorRegion = 0) then
            DBMS_OUTPUT.PUT_LINE('LA REGION NO ES VALIDA');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE PAIS YA EXISTE');

        ELSE
            update PAIS
            set NOMBRE=nombre_pais,
                update_date=sysdate,
                user_update=user,
                REGION_ID=id_region
            where PAIS_ID = id_pais;
            commit;
            DBMS_OUTPUT.PUT_LINE('RGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_pais;

    procedure eliminar_pais(id_pais number) is
        contadorPais   int;
        contadorEstado int;
    begin
        select count(*) into contadorPais from PAIS where ESTADO = 'A' and PAIS_ID = id_pais;
        SELECT COUNT(*) INTO contadorEstado FROM ESTADO WHERE ESTADO = 'A' AND PAIS_ID = id_pais;
        if (contadorEstado > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN ESTADOS ASOCIADOS');
        ELSIF (contadorPais >= 1) then
            update PAIS set estado='N', update_date=sysdate, user_update=user where PAIS_ID = id_pais;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL PAIS');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_pais;


end mmto_pais;
/


--Procedimiento para dar mantenimiento a la tabla ESTADO.
--Declaracion del paquete
create or replace package mmto_estado is

    --Declaracion de los procedimientos
    procedure insertar_estado(nombre_estado in varchar2, id_pais number);

    PROCEDURE ACTUALIZAR_ESTADO(id_estado number, nombre_estado varchar2, id_pais number);

    PROCEDURE ELIMINAR_ESTADO(id_estado number);


end mmto_estado;
/
create or replace package body mmto_estado is

    procedure insertar_estado(nombre_estado in varchar2, id_pais number) is
        contadorPais   int;
        contadorEstado int;
    begin
        select count(*) into contadorEstado from ESTADO where upper(NOMBRE) = upper(nombre_estado) and ESTADO = 'A';
        select count(*) into contadorPais from PAIS where PAIS_ID = id_pais and ESTADO = 'A';
        if (contadorEstado >= 1) then
            DBMS_OUTPUT.PUT_LINE('ESTADO YA EXISTE');
        elsif (contadorPais >= 1) then
            insert into ESTADO(ESTADO_ID, create_date, estado, user_create, nombre, pais_id)
            values (ESTADO_SEQ.nextval, sysdate, 'A', user, nombre_estado, id_pais);
            DBMS_OUTPUT.PUT_LINE('PAIS INGRESADO CORRECTAMENTE');
            commit;
        else
            DBMS_OUTPUT.PUT_LINE('PAIS NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_estado;

    procedure ACTUALIZAR_ESTADO(id_estado number, nombre_estado varchar2, id_pais number) is
        contadorEstado int;
        contadorPais   int;
        existe         int;
        yaexiste       int;
    begin
        select count(*) into contadorPais from PAIS where ESTADO = 'A' and PAIS_ID = id_pais;
        select count(*) into contadorEstado from ESTADO where ESTADO = 'A' and ESTADO_ID = id_estado;
        select count(*)
        into existe
        from ESTADO
        where upper(NOMBRE) = upper(nombre_estado)
          and ESTADO = 'A'
          and ESTADO_ID = id_estado
          AND PAIS_ID = id_pais;
        select count(*)
        into yaexiste
        from ESTADO
        where upper(NOMBRE) = upper(nombre_estado)
          and ESTADO = 'A'
          AND ESTADO_ID != id_estado;
        if (contadorEstado = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contadorPais = 0) then
            DBMS_OUTPUT.PUT_LINE('El PAIS NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE PAIS YA EXISTE');
        eLSE
            update ESTADO
            set NOMBRE=nombre_estado,
                update_date=sysdate,
                user_update=user,
                PAIS_ID=id_pais
            where ESTADO_ID = id_estado;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end ACTUALIZAR_ESTADO;

    procedure eliminar_estado(id_estado number) is
        contadorEstado       int;
        contadordepartamento int;
    begin
        select count(*) into contadorEstado from ESTADO where ESTADO = 'A' and ESTADO_ID = id_estado;
        select count(*) into contadordepartamento from DEPARTAMENTO where ESTADO = 'A' and ESTADO_ID = id_estado;
        IF (contadordepartamento > 0) THEN
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN DEPARTAMENTOS ASOCIADOS');
        ELSIF (contadorEstado >= 1) THEN
            UPDATE ESTADO SET ESTADO='N', update_date=sysdate, user_update=user where ESTADO_ID = id_estado;
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ELIMINADO');
        ELSE
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL ESTADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_estado;


end mmto_estado;
/


--Procedimiento para dar mantenimiento a la tabla DEPARTAMENTO.
--Declaracion del paquete
create or replace package mmto_departamento is

    --Declaracion de los procedimientos
    procedure insertar_departamento(nombre_departamento in varchar2, id_estado number);

    procedure actualizar_departamento(id_departamento number, nombre_departamento varchar2, id_estado number);

    procedure eliminar_departamento(id_departamento number);

end mmto_departamento;
/
create or replace package body mmto_departamento is

    procedure insertar_departamento(nombre_departamento in varchar2, id_estado number) is
        contadorEstado       int;
        contadorDepartamento int;
    begin
        select count(*)
        into contadorDepartamento
        from DEPARTAMENTO
        where upper(NOMBRE) = upper(nombre_departamento)
          and ESTADO = 'A';
        select count(*) into contadorEstado from ESTADO where ESTADO_ID = id_estado and ESTADO = 'A';
        if (contadorDepartamento >= 1) then
            DBMS_OUTPUT.PUT_LINE('DEPARTAMENTO YA EXISTE');
        elsif (contadorEstado >= 1) then
            insert into DEPARTAMENTO(DEPARTAMENTO_ID, create_date, estado, user_create, nombre, ESTADO_ID)
            values (DEPARTAMENTO_SEQ.nextval, sysdate, 'A', user, nombre_departamento, id_estado);
            commit;
            DBMS_OUTPUT.PUT_LINE('DEPARTAMENTO INGRESADO');
        else
            DBMS_OUTPUT.PUT_LINE('ESTADO NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_departamento;

    procedure actualizar_departamento(id_departamento number, nombre_departamento varchar2, id_estado number) is
        contadorDepartamento int;
        contadorEstado       int;
        existe               int;
        yaexiste             int;
    begin
        select count(*) into contadorEstado from ESTADO where ESTADO = 'A' and ESTADO_ID = id_estado;
        select count(*)
        into contadorDepartamento
        from DEPARTAMENTO
        where ESTADO = 'A'
          and DEPARTAMENTO_ID = id_departamento;
        select count(*)
        into existe
        from DEPARTAMENTO
        where upper(NOMBRE) = upper(nombre_departamento)
          and ESTADO = 'A'
          and DEPARTAMENTO_ID = id_departamento
          AND ESTADO_ID = id_estado;
        select count(*)
        into yaexiste
        from DEPARTAMENTO
        where upper(NOMBRE) = upper(nombre_departamento)
          and ESTADO = 'A'
          AND DEPARTAMENTO_ID != id_departamento;

        if (contadorDepartamento = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contadorEstado = 0) then
            DBMS_OUTPUT.PUT_LINE('El ESTADO NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE PAIS YA EXISTE');
        eLSE
            update DEPARTAMENTO
            set NOMBRE=nombre_departamento,
                update_date=sysdate,
                user_update=user,
                ESTADO_ID=id_estado
            where DEPARTAMENTO_ID = id_departamento;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_departamento;

    procedure eliminar_departamento(id_departamento number)
        is
        contadordepartamento int;
        cintadoCiudad        int;
    begin
        select count(*)
        into contadordepartamento
        from DEPARTAMENTO
        where ESTADO = 'A' and DEPARTAMENTO_ID = id_departamento;
        select count(*) into cintadoCiudad from CIUDAD where ESTADO = 'A' and DEPARTAMENTO_ID = id_departamento;
        IF (cintadoCiudad > 0) THEN
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN CIUDADES ASOCIADOS');
        ELSIF (contadordepartamento >= 1) THEN
            UPDATE DEPARTAMENTO
            SET ESTADO='N', update_date=sysdate, user_update=user
            where DEPARTAMENTO_ID = id_departamento;
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ELIMINADO');
        ELSE
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL ESTADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_departamento;

end mmto_departamento;
/

--Procedimiento para dar mantenimiento a la tabla ciudad.
--Declaracion del paquete
create or replace package mmto_ciudad is

    --Declaracion de los procedimientos
    procedure insertar_ciudad(nombre_ciudad in varchar2, id_departamento number);

    procedure actualizar_ciudad(id_ciudad number, nombre_ciudad varchar2, id_departamento number);

    procedure eliminar_ciudad(id_ciudad number);

end mmto_ciudad;
/
create or replace package body mmto_ciudad is

    procedure insertar_ciudad(nombre_ciudad in varchar2, id_departamento number) is
        --contador para ver si existe la relacion
        contadorDepartamento int;
        --contador para verificar si ya existe verificar si ya existe
        contadorCiudad       int;
    begin
        select count(*)
        into contadorCiudad
        from CIUDAD
        where upper(NOMBRE) = upper(nombre_ciudad)
          and ESTADO = 'A';
        select count(*)
        into contadorDepartamento
        from DEPARTAMENTO
        where DEPARTAMENTO_ID = id_departamento and ESTADO = 'A';
        if (contadorCiudad >= 1) then
            DBMS_OUTPUT.PUT_LINE('CIUDAD YA EXISTE');
        elsif (contadorDepartamento >= 1) then
            insert into CIUDAD(CIUDAD_ID, create_date, estado, user_create, nombre, DEPARTAMENTO_ID)
            values (CIUDAD_SEQ.nextval, sysdate, 'A', user, nombre_ciudad, id_departamento);
            commit;
            DBMS_OUTPUT.PUT_LINE('CIUDAD INGRESADA');
        else
            DBMS_OUTPUT.PUT_LINE('ESTADO NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_ciudad;

    procedure actualizar_ciudad(id_ciudad number, nombre_ciudad varchar2, id_departamento number) is
        contadorCiudad       int;
        contadorDepartamento int;
        existe               int;
        yaexiste             int;
    begin
        select count(*)
        into contadorDepartamento
        from DEPARTAMENTO
        where ESTADO = 'A' and DEPARTAMENTO_ID = id_departamento;
        select count(*)
        into contadorCiudad
        from CIUDAD
        where ESTADO = 'A'
          and CIUDAD_ID = id_ciudad;
        select count(*)
        into existe
        from CIUDAD
        where upper(NOMBRE) = upper(nombre_ciudad)
          and ESTADO = 'A'
          and CIUDAD_ID = id_ciudad
          AND DEPARTAMENTO_ID = id_departamento;
        select count(*)
        into yaexiste
        from CIUDAD
        where upper(NOMBRE) = upper(nombre_ciudad)
          and ESTADO = 'A'
          AND CIUDAD_ID != id_ciudad;

        if (contadorCiudad = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contadorDepartamento = 0) then
            DBMS_OUTPUT.PUT_LINE('El ESTADO NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE PAIS YA EXISTE');
        eLSE
            update CIUDAD
            set NOMBRE=nombre_ciudad,
                update_date=sysdate,
                user_update=user,
                DEPARTAMENTO_ID=id_departamento
            where CIUDAD_ID = id_ciudad;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_ciudad;

    procedure eliminar_ciudad(id_ciudad number)
        is
        contadorciudad int;
        cintadoBarrio  int;
    begin
        select count(*) into contadorciudad from CIUDAD where ESTADO = 'A' and CIUDAD_ID = id_ciudad;
        select count(*) into cintadoBarrio from BARRIO where ESTADO = 'A' and CIUDAD_ID = id_ciudad;
        IF (cintadoBarrio > 0) THEN
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN BARRIOS ASOCIADOS');
        ELSIF (contadorciudad >= 1) THEN
            UPDATE CIUDAD SET ESTADO='N', update_date=sysdate, user_update=user where CIUDAD_ID = id_ciudad;
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ELIMINADO');
        ELSE
            DBMS_OUTPUT.PUT_LINE('NO EXISTE LA CIUDAD');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_ciudad;

end mmto_ciudad;
/

--Procedimiento para dar mantenimiento a la tabla Barrio.
--Declaracion del paquete
create or replace package mmto_barrio is

    --Declaracion de los procedimientos
    procedure insertar_barrio(nombre_barrio in varchar2, id_ciudad number);

    procedure actualizar_barrio(id_barrio number, nombre_barrio varchar2, id_ciudad number);

    procedure eliminar_barrio(id_barrio number);

end mmto_barrio;
/
create or replace package body mmto_barrio is

    procedure insertar_barrio(nombre_barrio in varchar2, id_ciudad number) is
        --contador para ver si existe la relacion
        contadorCiudad int;
        --contador para verificar si ya existe verificar si ya existe
        contadorBarrio      int;
    begin
        select count(*)
        into contadorBarrio
        from BARRIO
        where upper(NOMBRE) = upper(nombre_barrio)
          and ESTADO = 'A';
        select count(*)
        into contadorCiudad
        from CIUDAD
        where CIUDAD_ID = id_ciudad and ESTADO = 'A';
        if (contadorBarrio >= 1) then
            DBMS_OUTPUT.PUT_LINE('BARRIO YA EXISTE');
        elsif (contadorCiudad >= 1) then
            insert into BARRIO(BARRIO_ID, CREATE_DATE, ESTADO, USER_CREATE, NOMBRE, CIUDAD_ID)
            values (BARRIO_SEQ.nextval, sysdate, 'A', user, nombre_barrio, id_ciudad);
            commit;
            DBMS_OUTPUT.PUT_LINE('BARRIO INGRESADO');
        else
            DBMS_OUTPUT.PUT_LINE('CIUDAD NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_barrio;

    procedure actualizar_barrio(id_barrio number, nombre_barrio varchar2, id_ciudad number) is
        contadorBarrio       int;
        contadorCiudad int;
        existe               int;
        yaexiste             int;
    begin
        select count(*)
        into contadorCiudad
        from CIUDAD
        where ESTADO = 'A' and CIUDAD_ID = id_ciudad;
        select count(*)
        into contadorBarrio
        from BARRIO
        where ESTADO = 'A'
          and BARRIO_ID = id_barrio;
        select count(*)
        into existe
        from BARRIO
        where upper(NOMBRE) = upper(nombre_barrio)
          and ESTADO = 'A'
          and BARRIO_ID = id_barrio
          AND CIUDAD_ID = id_ciudad;
        select count(*)
        into yaexiste
        from BARRIO
        where upper(NOMBRE) = upper(nombre_barrio)
          and ESTADO = 'A'
          AND BARRIO_ID != id_barrio;

        if (contadorBarrio = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contadorCiudad = 0) then
            DBMS_OUTPUT.PUT_LINE('La CIUDAD NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE BARRIO YA EXISTE');
        eLSE
            update BARRIO
            set NOMBRE=nombre_barrio,
                update_date=sysdate,
                user_update=user,
                CIUDAD_ID=id_ciudad
            where BARRIO_ID = id_barrio;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_barrio;

    procedure eliminar_barrio(id_barrio number)
        is
        contadorBarrio int;
        contadorUbicacion int;
    begin
        select count(*) into contadorBarrio from BARRIO where ESTADO = 'A' and BARRIO_ID = id_barrio;
        select count(*) into contadorUbicacion from UBICACION where ESTADO = 'A' and BARRIO_ID = id_barrio;
        IF (contadorUbicacion > 0) THEN
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN UBICACIONES ASOCIADAS');
        ELSIF (contadorBarrio >= 1) THEN
            UPDATE BARRIO SET ESTADO='N', update_date=sysdate, user_update=user where BARRIO_ID = id_barrio;
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ELIMINADO');
        ELSE
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL BARRIO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_barrio;

end mmto_barrio;
/
--Procedimiento para dar mantenimiento a la tabla Ubicacion.
--Declaracion del paquete
create or replace package mmto_ubicacion is

    --Declaracion de los procedimientos
    procedure insertar_ubicacion(nombre_direccion in varchar2, id_barrio number);

    procedure actualizar_ubicacion(id_ubicacion number, nombre_direccion varchar2, id_barrio number);

    procedure eliminar_ubicacion(id_ubicacion number);

end mmto_ubicacion;
/
create or replace package body mmto_ubicacion is

    procedure insertar_ubicacion(nombre_direccion in varchar2, id_barrio number) is
        --contador para ver si existe la relacion
        contadorRelacion int;
        --contador para verificar si ya existe verificar si ya existe
        contadorRegistro int;
    begin
        select count(*)
        into contadorRegistro
        from UBICACION
        where upper(DIRECCION) = upper(nombre_direccion)
          and ESTADO = 'A';
        select count(*)
        into contadorRelacion
        from BARRIO
        where BARRIO_ID = id_barrio
          and ESTADO = 'A';
        if (contadorRegistro >= 1) then
            DBMS_OUTPUT.PUT_LINE('BARRIO YA EXISTE');
        elsif (contadorRelacion >= 1) then
            insert into UBICACION(UBICACION_ID, CREATE_DATE, ESTADO, USER_CREATE, DIRECCION, BARRIO_ID)
            values (UBICACION_SEQ.nextval, sysdate, 'A', user, nombre_direccion, id_barrio);
            commit;
            DBMS_OUTPUT.PUT_LINE('UBICACION INGRESADO');
        else
            DBMS_OUTPUT.PUT_LINE('BARRIO NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_ubicacion;

    procedure actualizar_ubicacion(id_ubicacion number, nombre_direccion varchar2, id_barrio number) is
        contadorRegistro int;
        contadorRelacion int;
        existe           int;
        yaexiste         int;
    begin
        select count(*)
        into contadorRelacion
        from BARRIO
        where ESTADO = 'A'
          and BARRIO_ID = id_barrio;
        select count(*)
        into contadorRegistro
        from UBICACION
        where ESTADO = 'A'
          and UBICACION_ID = id_ubicacion;
        select count(*)
        into existe
        from UBICACION
        where upper(DIRECCION) = upper(nombre_direccion)
          and ESTADO = 'A'
          AND UBICACION_ID = id_ubicacion
          and BARRIO_ID = id_barrio;
        select count(*)
        into yaexiste
        from UBICACION
        where upper(DIRECCION) = upper(nombre_direccion)
          and ESTADO = 'A'
          AND UBICACION.UBICACION_ID != id_ubicacion;

        if (contadorRegistro = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contadorRelacion = 0) then
            DBMS_OUTPUT.PUT_LINE('EL BARRIO NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE UBICACION YA EXISTE');
        eLSE
            update UBICACION
            set DIRECCION=nombre_direccion,
                update_date=sysdate,
                user_update=user,
                BARRIO_ID=id_barrio
            where UBICACION_ID = id_ubicacion;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_ubicacion;

    procedure eliminar_ubicacion(id_ubicacion number)
        is
        contadorRegistro int;
        contadorRelacion int;
    begin
        select count(*) into contadorRegistro from UBICACION where ESTADO = 'A' and UBICACION_ID = id_ubicacion;
        select count(*) into contadorRelacion from SUCURSAL where ESTADO = 'A' and UBICACION_ID = id_ubicacion;
        IF (contadorRelacion > 0) THEN
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN SUCURSALES ASOCIADAS');
        ELSIF (contadorRegistro >= 1) THEN
            UPDATE UBICACION SET ESTADO='N', update_date=sysdate, user_update=user where UBICACION_ID = id_ubicacion;
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ELIMINADO');
        ELSE
            DBMS_OUTPUT.PUT_LINE('NO EXISTE PLA UBICACION');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_ubicacion;

end mmto_ubicacion;
/

--Procedimiento para dar mantenimiento a la tabla tipo de producto.
--Declaracion del paquete
create or replace package mmto_tipo_horario is

    --Declaracion de los procedimientos
    procedure insertar_tipo_horario(tipohorario in varchar2);

    procedure actualizar_tipo_horario(id_tipo_horario number, tipohorario varchar2);

    procedure eliminar_tipo_horario(id_tipo_horario number);

end mmto_tipo_horario;
/
create or replace package body mmto_tipo_horario is

    procedure insertar_tipo_horario(tipohorario in varchar2) is
        contador int;
    begin
        select count(*) into contador from TIPO_HORARIO where upper(TIPO) = upper(tipohorario) and ESTADO = 'A';
        if (contador >= 1) then
            DBMS_OUTPUT.PUT_LINE('YA EXISTE');
        else
            insert into TIPO_HORARIO(TIPO_HORARIO_ID, CREATE_DATE, ESTADO, USER_CREATE, TIPO)
            values (TIPO_HORARIO_SEQ.nextval, sysdate, 'A', user, tipohorario);
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO INGRESADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_tipo_horario;

    procedure actualizar_tipo_horario(id_tipo_horario number, tipohorario varchar2) is
        contador int;
        EXISTE   INT;
        yaexiste int;
    begin
        select count(*) into contador from TIPO_HORARIO where ESTADO = 'A' and TIPO_HORARIO_ID = id_tipo_horario;
        select count(*)
        into EXISTE
        from TIPO_HORARIO
        where upper(TIPO) = upper(tipohorario)
          and ESTADO = 'A'
          AND TIPO_HORARIO_ID = id_tipo_horario;
        select count(*) into yaexiste from TIPO_HORARIO where upper(TIPO) = upper(tipohorario) and ESTADO = 'A';
        if (contador = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsIF (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE TIPO DE PRODUCTO YA EXISTE');
        ELSE
            update TIPO_HORARIO set tipo=tipohorario, update_date=sysdate, user_update=user where TIPO_HORARIO_ID = id_tipo_horario;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_tipo_horario;

    procedure  eliminar_tipo_horario(id_tipo_horario number) is
        contador          int;
        contadorRelacion int;
    begin
        select count(*) into contadorRelacion from HORARIO where ESTADO = 'A' and TIPO_HORARIO_ID = id_tipo_horario;
        select count(*) into contador from TIPO_HORARIO where ESTADO = 'A' and TIPO_HORARIO_ID = id_tipo_horario;
        if (contadorRelacion > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN HORARIOS ASOCIADOS');
        ELSif (contador >= 1) then
            update TIPO_HORARIO set estado='N', update_date=sysdate, user_update=user where TIPO_HORARIO_ID = id_tipo_horario;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_tipo_horario;

end mmto_tipo_horario;
/

--Procedimiento para dar mantenimiento a la tabla tipo de Identificacion.
--Declaracion del paquete
create or replace package mmto_tipo_identificacion is

    --Declaracion de los procedimientos
    procedure insertar_tipo_identificacion(tipoidentificacion in varchar2);

    procedure actualizar_tipo_identificacion(id_tipo_identificacion number, tipoidentificacion varchar2);

    procedure eliminar_tipo_identificacion(id_tipo_identificacion number);

end mmto_tipo_identificacion;
/
create or replace package body mmto_tipo_identificacion is

    procedure insertar_tipo_identificacion(tipoidentificacion in varchar2) is
        contador int;
    begin
        select count(*) into contador from TIPO_IDENTIFICACION where upper(TIPO) = upper(tipoidentificacion) and ESTADO = 'A';
        if (contador >= 1) then
            DBMS_OUTPUT.PUT_LINE('YA EXISTE');
        else
            insert into TIPO_IDENTIFICACION(TIPO_IDEN_ID, CREATE_DATE, ESTADO, USER_CREATE, TIPO)
            values (TIPO_IDEN_SEQ.nextval, sysdate, 'A', user, tipoidentificacion);
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO INGRESADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_tipo_identificacion;

    procedure actualizar_tipo_identificacion(id_tipo_identificacion number, tipoidentificacion varchar2)is
        contador int;
        EXISTE   INT;
        yaexiste int;
    begin
        select count(*) into contador from TIPO_IDENTIFICACION where ESTADO = 'A' and TIPO_IDEN_ID = id_tipo_identificacion;
        select count(*)
        into EXISTE
        from TIPO_IDENTIFICACION
        where upper(TIPO) = upper(tipoidentificacion)
          and ESTADO = 'A'
          AND TIPO_IDEN_ID = id_tipo_identificacion;
        select count(*) into yaexiste from TIPO_IDENTIFICACION where upper(TIPO) = upper(tipoidentificacion) and ESTADO = 'A';
        if (contador = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsIF (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE TIPO DE IDENTIFICACION YA EXISTE');
        ELSE
            update TIPO_IDENTIFICACION set tipo=tipoidentificacion, update_date=sysdate, user_update=user where TIPO_IDEN_ID = id_tipo_identificacion;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_tipo_identificacion;

    procedure  eliminar_tipo_identificacion(id_tipo_identificacion number) is
        contador          int;
        contadorRelacion int;
    begin
        select count(*) into contadorRelacion from DOCUMENTO where ESTADO = 'A' and TIPO_IDENTIFICACION_ID = id_tipo_identificacion;
        select count(*) into contador from TIPO_IDENTIFICACION where ESTADO = 'A' and TIPO_IDEN_ID = id_tipo_identificacion;
        if (contadorRelacion > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN DOCUMENTOS ASOCIADOS');
        ELSif (contador >= 1) then
            update TIPO_IDENTIFICACION set estado='N', update_date=sysdate, user_update=user where TIPO_IDEN_ID = id_tipo_identificacion;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_tipo_identificacion;

end mmto_tipo_identificacion;
/

--Procedimiento para dar mantenimiento a la tabla tipo de proveedor.
--Declaracion del paquete
create or replace package mmto_tipo_proveedor is

    --Declaracion de los procedimientos
    procedure insertar_tipo_proveedor(tipoProv in varchar2);

    procedure actualizar_tipo_proveedor(id_tipo_proveedor number, tipoproveedor varchar2);

    procedure eliminar_tipo_proveedor(id_tipo_proveedor number);

end mmto_tipo_proveedor;
/
create or replace package body mmto_tipo_proveedor is

    procedure insertar_tipo_proveedor(tipoProv in varchar2) is
        contador int;
    begin
        select count(*) into contador from TIPO_PROVEEDOR where upper(TIPO) = upper(tipoProv) and ESTADO = 'A';
        if (contador >= 1) then
            DBMS_OUTPUT.PUT_LINE('YA EXISTE');
        else
            insert into TIPO_PROVEEDOR(TIPO_PROV_ID, CREATE_DATE, ESTADO, USER_CREATE, TIPO)
            values (TIPO_PROVE_SEQ.nextval, sysdate, 'A', user, tipoProv);
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO INGRESADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_tipo_proveedor;

    procedure actualizar_tipo_proveedor(id_tipo_proveedor number, tipoproveedor varchar2)is
        contador int;
        EXISTE   INT;
        yaexiste int;
    begin
        select count(*) into contador from TIPO_PROVEEDOR where ESTADO = 'A' and TIPO_PROV_ID = id_tipo_proveedor;
        select count(*)
        into EXISTE
        from TIPO_PROVEEDOR
        where upper(TIPO) = upper(tipoproveedor)
          and ESTADO = 'A'
          AND TIPO_PROV_ID = id_tipo_proveedor;
        select count(*) into yaexiste from TIPO_PROVEEDOR where upper(TIPO) = upper(tipoproveedor) and ESTADO = 'A';
        if (contador = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsIF (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE TIPO DE IDENTIFICACION YA EXISTE');
        ELSE
            update TIPO_PROVEEDOR set tipo=tipoproveedor, update_date=sysdate, user_update=user where TIPO_PROV_ID = id_tipo_proveedor;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_tipo_proveedor;

    procedure  eliminar_tipo_proveedor(id_tipo_proveedor number) is
        contador          int;
        contadorRelacion int;
    begin
        select count(*) into contadorRelacion from PROVEEDOR where ESTADO = 'A' and TIPO_PROV_ID = id_tipo_proveedor;
        select count(*) into contador from TIPO_PROVEEDOR where ESTADO = 'A' and TIPO_PROV_ID = id_tipo_proveedor;
        if (contadorRelacion > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN DOCUMENTOS ASOCIADOS');
        ELSif (contador >= 1) then
            update TIPO_PROVEEDOR set estado='N', update_date=sysdate, user_update=user where TIPO_PROV_ID = id_tipo_proveedor;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_tipo_proveedor;

end mmto_tipo_proveedor;
/

--Procedimiento para dar mantenimiento a la tabla CATEGORIA DE PRODUCTO.
--Declaracion del paquete
create or replace package mmto_categoria_producto is

    --Declaracion de los procedimientos
    procedure insertar_categoria_producto(nombre_cat in varchar2);

    procedure actualizar_categoria_producto(id_categoria_producto number, nombreCat varchar2);

    procedure eliminar_categoria_producto(id_categoria_producto number);

end mmto_categoria_producto;
/
create or replace package body mmto_categoria_producto is

    procedure insertar_categoria_producto(nombre_cat in varchar2) is
        contador int;
    begin
        select count(*) into contador from CATEGORIA_PRODUCTO where upper(NOMBRE) = upper(nombre_cat) and ESTADO = 'A';
        if (contador >= 1) then
            DBMS_OUTPUT.PUT_LINE('YA EXISTE');
        else
            insert into CATEGORIA_PRODUCTO(CAT_PROD_ID, CREATE_DATE, ESTADO, USER_CREATE, NOMBRE)
            values (CAT_PROD_SEQ.nextval, sysdate, 'A', user, nombre_cat);
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO INGRESADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_categoria_producto;

    procedure actualizar_categoria_producto(id_categoria_producto number, nombreCat varchar2) is
        contador int;
        EXISTE   INT;
        yaexiste int;
    begin
        select count(*) into contador from CATEGORIA_PRODUCTO where ESTADO = 'A' and CAT_PROD_ID = id_categoria_producto;
        select count(*)
        into EXISTE
        from CATEGORIA_PRODUCTO
        where upper(NOMBRE) = upper(nombreCat)
          and ESTADO = 'A'
          AND CAT_PROD_ID = id_categoria_producto;
        select count(*) into yaexiste from CATEGORIA_PRODUCTO where upper(NOMBRE) = upper(nombreCat) and ESTADO = 'A';
        if (contador = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsIF (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE TIPO DE IDENTIFICACION YA EXISTE');
        ELSE
            update CATEGORIA_PRODUCTO set NOMBRE=nombreCat, update_date=sysdate, user_update=user where CAT_PROD_ID = id_categoria_producto;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_categoria_producto;

    procedure  eliminar_categoria_producto(id_categoria_producto number) is
        contador          int;
        contadorRelacion int;
    begin
        select count(*) into contadorRelacion from PRODUCTO where ESTADO = 'A' and CAT_PROD_ID  = id_categoria_producto;
        select count(*) into contador from CATEGORIA_PRODUCTO where ESTADO = 'A' and CAT_PROD_ID = id_categoria_producto;
        if (contadorRelacion > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN DOCUMENTOS ASOCIADOS');
        ELSif (contador >= 1) then
            update CATEGORIA_PRODUCTO set estado='N', update_date=sysdate, user_update=user where CAT_PROD_ID = id_categoria_producto;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_categoria_producto;

end mmto_categoria_producto;
/


--Procedimiento para dar mantenimiento a la tabla CATEGORIA EMPLEADO.
--Declaracion del paquete
create or replace package mmto_categoria_empleado is

    --Declaracion de los procedimientos
    procedure insertar_categoria_empleado(nombre_cat in varchar2);

    procedure actualizar_categoria_empleado(id_categoria_empleado number, nombreCat varchar2);

    procedure eliminar_categoria_empleado(id_categoria_empleado number);

end mmto_categoria_empleado;
/
create or replace package body mmto_categoria_empleado is

    procedure insertar_categoria_empleado(nombre_cat in varchar2) is
        contador int;
    begin
        select count(*) into contador from CATEGORIA_EMPLEADO where upper(TIPO) = upper(nombre_cat) and ESTADO = 'A';
        if (contador >= 1) then
            DBMS_OUTPUT.PUT_LINE('YA EXISTE');
        else
            insert into CATEGORIA_EMPLEADO(CATEGORIA_EMPLEADO_ID, CREATE_DATE, ESTADO, USER_CREATE, TIPO)
            values (CATEGORIA_EMP_SEQ.nextval, sysdate, 'A', user, nombre_cat);
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO INGRESADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_categoria_empleado;

    procedure actualizar_categoria_empleado(id_categoria_empleado number, nombreCat varchar2) is
        contador int;
        EXISTE   INT;
        yaexiste int;
    begin
        select count(*) into contador from CATEGORIA_EMPLEADO where ESTADO = 'A' and CATEGORIA_EMPLEADO_ID = id_categoria_empleado;
        select count(*)
        into EXISTE
        from CATEGORIA_EMPLEADO
        where upper(TIPO) = upper(nombreCat)
          and ESTADO = 'A'
          AND CATEGORIA_EMPLEADO_ID = id_categoria_empleado;
        select count(*) into yaexiste from CATEGORIA_EMPLEADO where upper(TIPO) = upper(nombreCat) and ESTADO = 'A';
        if (contador = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsIF (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE TIPO DE IDENTIFICACION YA EXISTE');
        ELSE
            update CATEGORIA_EMPLEADO set TIPO=nombreCat, update_date=sysdate, user_update=user where CATEGORIA_EMPLEADO_ID = id_categoria_empleado;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_categoria_empleado;

    procedure  eliminar_categoria_empleado(id_categoria_empleado number) is
        contador          int;
        contadorRelacion int;
    begin
        select count(*) into contadorRelacion from EMPLEADO where ESTADO = 'A' and CAT_EMPLEADO_ID  = id_categoria_empleado;
        select count(*) into contador from CATEGORIA_EMPLEADO where ESTADO = 'A' and CATEGORIA_EMPLEADO_ID = id_categoria_empleado;
        if (contadorRelacion > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN EMPLEADOS ASOCIADOS');
        ELSif (contador >= 1) then
            update CATEGORIA_EMPLEADO set estado='N', update_date=sysdate, user_update=user where CATEGORIA_EMPLEADO_ID = id_categoria_empleado;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_categoria_empleado;

end mmto_categoria_empleado;
/

--Procedimiento para dar mantenimiento a la tabla PUESTO
--Declaracion del paquete
create or replace package mmto_puesto is

    --Declaracion de los procedimientos
    procedure insertar_puesto(nombre_puesto in varchar2, salario in number);

    procedure actualizar_puesto(id_puesto number, nombre_puesto in varchar2, salario in number);

    procedure eliminar_puesto(id_puesto number);

end mmto_puesto;
/
create or replace package body mmto_puesto is

    procedure insertar_puesto(nombre_puesto in varchar2, salario in number) is
        v_contador int;
    begin
        select count(*) into v_contador from PUESTO where upper(NOMBRE) = upper(nombre_puesto) and ESTADO = 'A';
        if (v_contador >= 1) then
            DBMS_OUTPUT.PUT_LINE('YA EXISTE');
        else
            insert into PUESTO(PUESTO_ID, CREATE_DATE, ESTADO, USER_CREATE, NOMBRE, SALARY)
            values (PUESTO_SEQ.nextval, sysdate, 'A', user, nombre_puesto,salario);
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO INGRESADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_puesto;

    procedure actualizar_puesto(id_puesto number, nombre_puesto in varchar2, salario in number) is
        v_contador int;
        v_EXISTE   INT;
        V_yaexiste int;
    begin
        select count(*) into v_contador from PUESTO where ESTADO = 'A' and PUESTO_ID = id_puesto;
        select count(*)
        into v_EXISTE
        from PUESTO
        where upper(NOMBRE) = upper(nombre_puesto)
          and ESTADO = 'A'
          and SALARY = salario
          AND PUESTO_ID = id_puesto;
        select count(*) into V_yaexiste from PUESTO where upper(NOMBRE) = upper(nombre_puesto) and ESTADO = 'A';
        if (v_contador = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (v_EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsIF (V_yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE TIPO DE IDENTIFICACION YA EXISTE');
        ELSE
            update PUESTO set NOMBRE=nombre_puesto, update_date=sysdate, user_update=user, SALARY=salario where PUESTO_ID = id_puesto;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_puesto;

     procedure eliminar_puesto(id_puesto number) is
        v_contador          int;
        v_contadorRelacion int;
    begin
        select count(*) into v_contadorRelacion from EMPLEADO where ESTADO = 'A' and PUESTO_ID  = id_puesto;
        select count(*) into v_contador from PUESTO where ESTADO = 'A' and PUESTO_ID = id_puesto;
        if (v_contadorRelacion > 0) then
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN EMPLEADOS ASOCIADOS');
        ELSif (v_contador >= 1) then
            update PUESTO set estado='N', update_date=sysdate, user_update=user where PUESTO_ID = id_puesto;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_puesto;

end mmto_puesto;
/

--Procedimiento para dar mantenimiento a la tabla HORARIO.
-- FORMATO DE FECHA - '10-Sep-02 14:10:10.1234'
--Declaracion del paquete
create or replace package mmto_horario is

    --Declaracion de los procedimientos
    procedure insertar_horario(hor_entrada in varchar2, hor_salida in varchar2, id_tipo_horario number);

    procedure actualizar_horario(id_horario in number, hor_entrada in varchar2, hor_salida in varchar2, id_tipo_horario number);

    procedure eliminar_horario(id_horario number);


end mmto_horario;
/
create or replace package body mmto_horario is

    procedure insertar_horario(hor_entrada in varchar2, hor_salida in varchar2, id_tipo_horario number) is
        --contador para ver si existe la relacion
        v_contadorRelacion int;
        --contador para verificar si ya existe verificar si ya existe
        v_contadorRegistro int;
    begin
        select count(*)
        into v_contadorRegistro
        from HORARIO
        where HORARIO.TIPO_HORARIO_ID = id_tipo_horario
          and ESTADO = 'A';
        select count(*)
        into v_contadorRelacion
        from TIPO_HORARIO
        where TIPO_HORARIO_ID = id_tipo_horario
          and ESTADO = 'A';
        if (v_contadorRegistro >= 1) then
            DBMS_OUTPUT.PUT_LINE('HORARIO YA EXISTE');
        elsif (v_contadorRelacion >= 1) then
            INSERT INTO HORARIO(HORARIO_ID, CREATE_DATE, ESTADO, HORA_ENTRADA, HORA_SALIDA, USER_CREATE,
                                TIPO_HORARIO_ID)
            values (HORARIO_SEQ.nextval, sysdate, 'A', TO_TIMESTAMP(hor_entrada, 'DD-Mon-RR HH24:MI:SS.FF'),
                    TO_TIMESTAMP(hor_salida, 'DD-Mon-RR HH24:MI:SS.FF') ,user,  id_tipo_horario);
            commit;
            DBMS_OUTPUT.PUT_LINE('UBICACION INGRESADO');
        else
            DBMS_OUTPUT.PUT_LINE('TIPO de HORARIO NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    END insertar_horario;

    procedure actualizar_horario(id_horario in number, hor_entrada in varchar2, hor_salida in varchar2, id_tipo_horario number) is
        v_contador_Registro int;
        V_contador_Relacion int;
        v_existe           int;
        v_yaexiste         int;
    begin
        select count(*)
        into V_contador_Relacion
        from TIPO_HORARIO
        where ESTADO = 'A'
          and TIPO_HORARIO_ID = id_tipo_horario;
        select count(*)
        into v_contador_Registro
        from HORARIO
        where ESTADO = 'A'
          and HORARIO_ID = id_horario;
        select count(*)
        into v_existe
        from HORARIO
        where HORA_ENTRADA = TO_TIMESTAMP(hor_entrada, 'DD-Mon-RR HH24:MI:SS.FF')
          and HORA_SALIDA =  TO_TIMESTAMP(hor_salida, 'DD-Mon-RR HH24:MI:SS.FF')
          and ESTADO = 'A'
          AND HORARIO_ID = id_horario
          and TIPO_HORARIO_ID = id_tipo_horario;
        select count(*)
        into v_yaexiste
        from HORARIO
        where TIPO_HORARIO_ID = id_tipo_horario
          and ESTADO = 'A'
          AND HORARIO.HORARIO_ID != id_horario;

        if (v_contador_Registro = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (V_contador_Relacion = 0) then
            DBMS_OUTPUT.PUT_LINE('EL TIPO DE HORARIO NO ES VALIDO');
        elsif (v_existe = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (v_yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('HORARIO YA EXISTE');
        eLSE
            update HORARIO
            set HORA_ENTRADA=TO_TIMESTAMP(hor_entrada, 'DD-Mon-RR HH24:MI:SS.FF'),
                HORA_SALIDA=TO_TIMESTAMP(hor_salida, 'DD-Mon-RR HH24:MI:SS.FF'),
                update_date=sysdate,
                user_update=user,
                TIPO_HORARIO_ID=id_tipo_horario
            where HORARIO_ID = id_horario;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_horario;

         procedure eliminar_horario(id_horario number)
        is
        V_contador_Registro int;
        V_contador_Relacion_1 int;
        V_contador_Relacion_2 int;
    begin
        select count(*) into V_contador_Registro from HORARIO where ESTADO = 'A' and HORARIO_ID = id_horario;
        select count(*) into V_contador_Relacion_1 from SUCURSAL where ESTADO = 'A' and HORARIO_ID = id_horario;
          select count(*) into V_contador_Relacion_2 from EMPLEADO where ESTADO = 'A' and HORARIO_ID = id_horario;
        IF (V_contador_Relacion_1 > 0) THEN
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN SUCURSALES ASOCIADAS');
        ELSIF(V_contador_Relacion_2 > 0) THEN
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN EMPLEADOS ASOCIADAS');
        ELSIF (V_contador_Registro >= 1) THEN
            UPDATE HORARIO SET ESTADO='N', update_date=sysdate, user_update=user where HORARIO_ID = id_horario;
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ELIMINADO');
        ELSE
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL HORARIO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_horario;

end mmto_horario;
/


--Procedimiento para dar mantenimiento a la tabla SUCURSAL.
--Declaracion del paquete
create or replace package mmto_sucursal is

    --Declaracion de los procedimientos
    procedure insertar_sucursal(nombre_sucursal in varchar2, id_horario number, id_ubicacion number);

    procedure actualizar_sucursal(id_sucursal number, nombre_sucursal in varchar2, id_horario number,
                                  id_ubicacion number);

    procedure eliminar_SUCURSAL(id_sucursal number);


end mmto_sucursal;
/
create or replace package body mmto_sucursal is

    procedure insertar_sucursal(nombre_sucursal in varchar2, id_horario number, id_ubicacion number) is
        --contador para ver si existe la relacion
        v_contadorRelacion_1 number;
        --contador para ver si existe la relacion
        v_contadorRelacion_2 number;
        --contador para verificar si ya existe verificar si ya existe
        v_contadorRegistro   number;
    begin
        select count(*)
        into v_contadorRegistro
        from SUCURSAL
        where HORARIO_ID = id_horario
          and NOMBRE = nombre_sucursal
          and UBICACION_ID = id_ubicacion
          and ESTADO = 'A';
        select count(*)
        into v_contadorRelacion_1
        from HORARIO
        where HORARIO_ID = id_horario
          and ESTADO = 'A';
        select count(*)
        into v_contadorRelacion_2
        from UBICACION
        where UBICACION_ID = id_ubicacion
          and ESTADO = 'A';
        if (v_contadorRelacion_2 = 0) then
            DBMS_OUTPUT.PUT_LINE('UBICACION NO EXISTE');
        elsif (v_contadorRegistro >= 1) then
            DBMS_OUTPUT.PUT_LINE('SUCURSAL YA EXISTE');
        elsif (v_contadorRelacion_1 >= 1) then
            insert into SUCURSAL(SUCURSAL_ID, CREATE_DATE, ESTADO, NOMBRE, USER_CREATE,
                                 HORARIO_ID, UBICACION_ID)
            VALUES (SUCURSAL_SEQ.nextval, sysdate, 'A', nombre_sucursal, user, id_horario, id_ubicacion);
            commit;
            DBMS_OUTPUT.PUT_LINE('SUCURSAL INGRESADO');
        else
            DBMS_OUTPUT.PUT_LINE('HORARIO NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    END insertar_sucursal;

    procedure actualizar_sucursal(id_sucursal number, nombre_sucursal in varchar2, id_horario number,
                                  id_ubicacion number) is
        v_contador_Registro   number;
        V_contador_Relacion_1 number;
        V_contador_Relacion_2 number;
        v_existe              number;
        v_yaexiste            number;
    begin
        select count(*)
        into V_contador_Relacion_1
        from HORARIO
        where ESTADO = 'A'
          and HORARIO_ID = id_horario;
        select count(*)
        into V_contador_Relacion_2
        from UBICACION
        where ESTADO = 'A'
          and UBICACION_ID = id_ubicacion;
        select count(*)
        into v_contador_Registro
        from SUCURSAL
        where ESTADO = 'A'
          and SUCURSAL_ID = id_sucursal;
        select count(*)
        into v_existe
        from SUCURSAL
        where UPPER(NOMBRE) = UPPER(nombre_sucursal)
          and UBICACION_ID =id_ubicacion
          and ESTADO = 'A'
          AND HORARIO_ID = id_horario
          and SUCURSAL_ID = id_sucursal;
        select count(*)
        into v_yaexiste
        from SUCURSAL
        where UBICACION_ID = id_ubicacion
          and ESTADO = 'A'
          AND upper(NOMBRE) =upper(nombre_sucursal)
          AND HORARIO_ID = id_horario
          AND SUCURSAL_ID != id_sucursal;

        if (v_contador_Registro = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (V_contador_Relacion_1 = 0) then
            DBMS_OUTPUT.PUT_LINE('EL HORARIO NO ES VALIDO');
        elsif (V_contador_Relacion_2 = 0) then
            DBMS_OUTPUT.PUT_LINE('lA UBICACION NO ES VALIDO');
        elsif (v_existe = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (v_yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('HORARIO YA EXISTE');
        eLSE
            update SUCURSAL
            set NOMBRE=nombre_sucursal,
                UBICACION_ID=id_ubicacion,
                update_date=sysdate,
                user_update=user,
                HORARIO_ID=id_horario
            where SUCURSAL_ID = id_sucursal;
            commit;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_sucursal;

    procedure eliminar_SUCURSAL(id_sucursal number)
        is
        V_contador_Registro number;
        V_contador_Relacion_1 number;

    begin
        select count(*) into V_contador_Registro from SUCURSAL where ESTADO = 'A' and SUCURSAL_ID = id_sucursal;
        select count(*) into V_contador_Relacion_1 from EMPLEADO where ESTADO = 'A' and SUCURSAL_ID = id_sucursal;

        IF (V_contador_Relacion_1 > 0) THEN
            DBMS_OUTPUT.PUT_LINE('NO SE PUEDE ELIMINAR EXISTEN EMPLEADOS ASOCIADOS');
        ELSIF (V_contador_Registro >= 1) THEN
            UPDATE SUCURSAL SET ESTADO='N', update_date=sysdate, user_update=user where SUCURSAL_ID = id_sucursal;
            COMMIT;
            DBMS_OUTPUT.PUT_LINE('REGISTRO ELIMINADO');
        ELSE
            DBMS_OUTPUT.PUT_LINE('NO EXISTE SUCURSAL');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_SUCURSAL;


end mmto_sucursal;
/

--Procedimiento para dar mantenimiento a la tabla PROVEEDOR.
--Declaracion del paquete
create or replace package mmto_proveedor is

    --Declaracion de los procedimientos
    procedure insertar_proveerdor(nombre_proveerdor in varchar2, id_tipo_proveedor number);

    procedure actualizar_proveerdor(id_proveedor number, nombre_proveerdor in varchar2, id_tipo_proveedor number);

    PROCEDURE eliminar_proveerdor(id_proveedor number);

end mmto_proveedor;
/
create or replace package body mmto_proveedor is

   procedure insertar_proveerdor(nombre_proveerdor in varchar2, id_tipo_proveedor number) is
        contador_registro   int;
        contador_relacion int;
    begin
        select count(*) into contador_registro from PROVEEDOR where upper(NOMBRE) = upper(nombre_proveerdor) and ESTADO = 'A';
        select count(*) into contador_relacion from TIPO_PROVEEDOR where TIPO_PROV_ID = id_tipo_proveedor and ESTADO = 'A';
        if (contador_registro >= 1) then
            DBMS_OUTPUT.PUT_LINE('PROVEEDOR YA EXISTE');
        elsif (contador_relacion >= 1) then
            insert into PROVEEDOR(PROVEEDOR_ID, create_date, estado, user_create, nombre, TIPO_PROV_ID)
            values (PROVE_SEQ.nextval, sysdate, 'A', user, nombre_proveerdor, id_tipo_proveedor);
            commit;
        else
            DBMS_OUTPUT.PUT_LINE('REGION NO EXISTE');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_proveerdor;

    --Procedimiento para actualizar nombre del pais y region.
    procedure actualizar_proveerdor(id_proveedor number, nombre_proveerdor in varchar2, id_tipo_proveedor number) is
        contador_registro   int;
        contador_relacion int;
        EXISTE         INT;
        yaexiste       int;
    begin
        select count(*) into contador_relacion from TIPO_PROVEEDOR where ESTADO = 'A' and TIPO_PROV_ID = id_tipo_proveedor;
        select count(*) into contador_registro from PROVEEDOR where ESTADO = 'A' and PROVEEDOR_ID = id_proveedor;
        select count(*)
        into EXISTE
        from PROVEEDOR
        where upper(NOMBRE) = upper(nombre_proveerdor)
          and ESTADO = 'A'
          AND PROVEEDOR_ID = id_proveedor
          AND TIPO_PROV_ID = id_tipo_proveedor;
        select count(*)
        into yaexiste
        from PROVEEDOR
        where upper(NOMBRE) = upper(nombre_proveerdor) and ESTADO = 'A' AND PROVEEDOR_ID != id_proveedor;
        if (contador_registro = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contador_relacion = 0) then
            DBMS_OUTPUT.PUT_LINE('TIPO PROVEEDOR NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE PAIS YA EXISTE');

        ELSE
            update PROVEEDOR
            set NOMBRE=nombre_proveerdor,
                update_date=sysdate,
                user_update=user,
                TIPO_PROV_ID=id_tipo_proveedor
            where PROVEEDOR_ID = id_proveedor;
            commit;
            DBMS_OUTPUT.PUT_LINE('RGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_proveerdor;

    PROCEDURE eliminar_proveerdor(id_proveedor number) is
        contador_registro   int;

    begin
        select count(*) into contador_registro from PROVEEDOR where ESTADO = 'A' and PROVEEDOR_ID = id_proveedor;
        if (contador_registro >= 1) then
            update PROVEEDOR set estado='N', update_date=sysdate, user_update=user where PROVEEDOR_ID = id_proveedor;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL PROVEEDOR');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_proveerdor;


end mmto_proveedor;
/

--Procedimiento para dar mantenimiento a la tabla PRODUCTO.
--Declaracion del paquete
create or replace package mmto_producto is

    --Declaracion de los procedimientos
    procedure insertar_producto(nombre_producto in varchar2, id_tipo_producto number,id_categoria_producto number);

    procedure actualizar_provducto(id_producto number, nombre_producto in varchar2, id_tipo_producto number,id_categoria_producto number);

    PROCEDURE eliminar_producto(id_producto number);

end mmto_producto;
/
create or replace package body mmto_producto is

   procedure insertar_producto(nombre_producto in varchar2, id_tipo_producto number,id_categoria_producto number) is
        contador_registro   int;
        contador_relacion_1 int;
        contador_relacion_2 int;
    begin
        select count(*) into contador_registro from PRODUCTO where upper(NOMBRE) = upper(nombre_producto) and ESTADO = 'A';
        select count(*) into contador_relacion_1 from TIPO_PRODUCTO where TIPO_PROD_ID = id_tipo_producto and ESTADO = 'A';
        select count(*) into contador_relacion_2 from CATEGORIA_PRODUCTO where CAT_PROD_ID = id_categoria_producto and ESTADO = 'A';
        if (contador_registro >= 1) then
            DBMS_OUTPUT.PUT_LINE('PROVEEDOR YA EXISTE');
        elsif (contador_relacion_1 >= 1 AND contador_relacion_2  >= 1) then
            insert into PRODUCTO(PRODUCTO_ID, create_date, estado, user_create, nombre, CAT_PROD_ID,TIPO_PROD_ID)
            values (PROVE_SEQ.nextval, sysdate, 'A', user, nombre_producto, id_categoria_producto,id_tipo_producto);
            commit;
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE LAS RELACIONES');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_producto;

    --Procedimiento para actualizar nombre del pais y region.
    procedure actualizar_provducto(id_producto number, nombre_producto in varchar2, id_tipo_producto number,id_categoria_producto number) is
        contador_registro   int;
        contador_relacion_1 int;
        contador_relacion_2 int;
        EXISTE         INT;
        yaexiste       int;
    begin
        select count(*) into contador_relacion_1 from TIPO_PRODUCTO where ESTADO = 'A' and TIPO_PROD_ID = id_tipo_producto;
        select count(*) into contador_relacion_2 from CATEGORIA_PRODUCTO where ESTADO = 'A' and CAT_PROD_ID = id_categoria_producto;
        select count(*) into contador_registro from PRODUCTO where ESTADO = 'A' and PRODUCTO_ID = id_producto;
        select count(*)
        into EXISTE
        from PRODUCTO
        where upper(NOMBRE) = upper(nombre_producto)
          and ESTADO = 'A'
          AND TIPO_PROD_ID = id_producto
          AND CAT_PROD_ID = id_categoria_producto
          AND PRODUCTO_ID = id_producto;
        select count(*)
        into yaexiste
        from PRODUCTO
        where upper(NOMBRE) = upper(nombre_producto) and ESTADO = 'A' AND PRODUCTO_ID != id_producto;
        if (contador_registro = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contador_relacion_1 = 0) then
            DBMS_OUTPUT.PUT_LINE('TIPO PRODUCTO NO ES VALIDO');
        elsif (contador_relacion_2 = 0) then
            DBMS_OUTPUT.PUT_LINE('CATEGORIA_PORODCUTO NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE PAIS YA EXISTE');

        ELSE
            update PRODUCTO
            set NOMBRE=nombre_producto,
                update_date=sysdate,
                user_update=user,
                TIPO_PROD_ID=id_tipo_producto,
            CAT_PROD_ID=id_categoria_producto
            where PRODUCTO_ID = id_producto;
            commit;
            DBMS_OUTPUT.PUT_LINE('RGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_provducto;

     PROCEDURE eliminar_producto(id_producto number) is
        contador_registro   int;

    begin
        select count(*) into contador_registro from PRODUCTO where ESTADO = 'A' and PRODUCTO_ID = id_producto;
        if (contador_registro >= 1) then
            update PRODUCTO set estado='N', update_date=sysdate, user_update=user where PRODUCTO_ID = id_producto;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL PRODUCTO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_producto;


end mmto_producto;
/


--Procedimiento para dar mantenimiento a la tabla EXISTENCIA.
--Declaracion del paquete
create or replace package mmto_existencia is

    --Declaracion de los procedimientos
    procedure insertar_existencia(cantidad_exis in number, id_producto in number);

    procedure actualizar_existencia(id_existencia in number, cantidad_exis in number, id_producto in number);

    PROCEDURE eliminar_existencia(id_existencia number);

end mmto_existencia;
/
create or replace package body mmto_existencia is

    procedure insertar_existencia(cantidad_exis in number, id_producto in number) is
        contador_registro   int;
        contador_relacion_1 int;

    begin
        select count(*) into contador_registro from EXISTENCIA where PRODUCTO_ID = id_producto and ESTADO = 'A';
        select count(*) into contador_relacion_1 from PRODUCTO where PRODUCTO_ID = id_producto and ESTADO = 'A';

        if (contador_registro >= 1) then
            DBMS_OUTPUT.PUT_LINE('PRODCUTO YA EXISTE');
        elsif (contador_relacion_1 >= 1) then
            insert into EXISTENCIA(EXISTENCIA_ID, create_date, estado, user_create, CANTIDAD, PRODUCTO_ID)
            values (EXISTENCIA_SEQ.nextval, sysdate, 'A', user, cantidad_exis, id_producto);
            commit;
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE LAS RELACIONES');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_existencia;

    --Procedimiento para actualizar nombre del pais y region.
    procedure actualizar_existencia(id_existencia in number, cantidad_exis in number, id_producto in number) is
        contador_registro   int;
        contador_relacion_1 int;
        EXISTE              INT;
        yaexiste            int;
        cantidad_existencia number;
    begin
        select CANTIDAD into cantidad_existencia from EXISTENCIA where EXISTENCIA_ID=id_existencia;

        select count(*)
        into contador_relacion_1
        from PRODUCTO
        where ESTADO = 'A' and PRODUCTO_ID = id_producto;
        select count(*)
        into contador_registro
        from EXISTENCIA
        where ESTADO = 'A'
          and EXISTENCIA_ID = id_existencia
          and CANTIDAD = cantidad_exis
          and PRODUCTO_ID = id_producto;
        select count(*)
        into EXISTE
        from EXISTENCIA
        where  ESTADO = 'A'
          AND CANTIDAD = cantidad_exis
          AND EXISTENCIA_ID = id_existencia
          AND PRODUCTO_ID = id_producto;
        select count(*)
        into yaexiste
        from EXISTENCIA
        where PRODUCTO_ID=id_producto
          AND EXISTENCIA_ID != id_existencia;
        if (contador_registro = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contador_relacion_1 = 0) then
            DBMS_OUTPUT.PUT_LINE('TIPO PRODUCTO NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE PAIS YA EXISTE');

        ELSE
            cantidad_existencia:= cantidad_existencia+cantidad_exis;
            update EXISTENCIA
            set CANTIDAD=cantidad_existencia,
                update_date=sysdate,
                user_update=user,
                PRODUCTO_ID = id_producto
            where EXISTENCIA_ID = id_existencia;
            commit;
            DBMS_OUTPUT.PUT_LINE('RGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_existencia;

    PROCEDURE eliminar_existencia(id_existencia number) is
        contador_registro int;

    begin
        select count(*) into contador_registro from EXISTENCIA where ESTADO = 'A' and EXISTENCIA_ID = id_existencia;
        if (contador_registro >= 1) then
            update EXISTENCIA set estado='N', update_date=sysdate, user_update=user,CANTIDAD=0 where EXISTENCIA_ID = id_existencia;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL PRODUCTO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_existencia;


end mmto_existencia;
/

--Procedimiento para dar mantenimiento a la tabla VENCIMIENTO_PRODUCTO.
-- FORMATO DE FECHA - '10-Sep-02 14:10:10.1234'
--Declaracion del paquete
create or replace package mmto_vencimiento_producto is

    --Declaracion de los procedimientos
    procedure insertar_vencimiento_producto(fec_venc in varchar2, id_producto in number);

    procedure actualizar_vencimiento_producto(id_vecn_prod in number, fec_venc in varchar2, id_producto in number);

    PROCEDURE eliminar_vencimiento_producto(id_vecn_prod number);

end mmto_vencimiento_producto;
/
create or replace package body mmto_vencimiento_producto is

    procedure insertar_vencimiento_producto(fec_venc in varchar2, id_producto in number) is
        contador_registro   int;
        contador_relacion_1 int;

    begin
        select count(*) into contador_registro from VENCIMIENTO_PRODUCTO where PRODUCTO_ID = id_producto and ESTADO = 'A';
        select count(*) into contador_relacion_1 from PRODUCTO where PRODUCTO_ID = id_producto and ESTADO = 'A';

        if (contador_registro >= 1) then
            DBMS_OUTPUT.PUT_LINE('PRODCUTO YA EXISTE');
        elsif (contador_relacion_1 >= 1) then
            insert into VENCIMIENTO_PRODUCTO(VENC_PROD_ID, create_date, estado, user_create, FECHA_VENCIMIENTO, PRODUCTO_ID)
            values (VENC_PROD_SEQ.nextval, sysdate, 'A', user, TO_TIMESTAMP(fec_venc, 'DD-Mon-RR HH24:MI:SS.FF'), id_producto);
            commit;
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE LAS RELACIONES');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_vencimiento_producto;

    --Procedimiento para actualizar nombre del pais y region.
     procedure actualizar_vencimiento_producto(id_vecn_prod in number,fec_venc in varchar2, id_producto in number) is
        contador_registro   int;
        contador_relacion_1 int;
        EXISTE              INT;
        yaexiste            int;

    begin

        select count(*)
        into contador_relacion_1
        from PRODUCTO
        where ESTADO = 'A' and PRODUCTO_ID = id_producto;
        select count(*)
        into contador_registro
        from VENCIMIENTO_PRODUCTO
        where ESTADO = 'A'
          and VENC_PROD_ID = id_vecn_prod
          and FECHA_VENCIMIENTO = TO_TIMESTAMP(fec_venc, 'DD-Mon-RR HH24:MI:SS.FF')
          and PRODUCTO_ID = id_producto;
        select count(*)
        into EXISTE
        from VENCIMIENTO_PRODUCTO
        where  ESTADO = 'A'
           and FECHA_VENCIMIENTO = TO_TIMESTAMP(fec_venc, 'DD-Mon-RR HH24:MI:SS.FF')
          and VENC_PROD_ID = id_vecn_prod
          AND PRODUCTO_ID = id_producto;
        select count(*)
        into yaexiste
        from VENCIMIENTO_PRODUCTO
        where PRODUCTO_ID=id_producto
          AND VENC_PROD_ID != id_vecn_prod;
        if (contador_registro = 0) then
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL REGISTRO');
        elsif (contador_relacion_1 = 0) then
            DBMS_OUTPUT.PUT_LINE('TIPO PRODUCTO NO ES VALIDO');
        elsif (EXISTE = 1) then
            DBMS_OUTPUT.PUT_LINE('NO SE ENCONTRO NINGUN CAMBIO EN LA ACTUALIZACION');
        elsif (yaexiste >= 1) THEN
            DBMS_OUTPUT.PUT_LINE('NOMBRE DE PAIS YA EXISTE');

        ELSE

            update VENCIMIENTO_PRODUCTO
            set FECHA_VENCIMIENTO = TO_TIMESTAMP(fec_venc, 'DD-Mon-RR HH24:MI:SS.FF'),
                update_date=sysdate,
                user_update=user,
                PRODUCTO_ID = id_producto
            where VENC_PROD_ID = id_vecn_prod;
            commit;
            DBMS_OUTPUT.PUT_LINE('RGISTRO ACTUALIZADO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end actualizar_vencimiento_producto;

   PROCEDURE eliminar_vencimiento_producto(id_vecn_prod number) is
        contador_registro int;

    begin
        select count(*) into contador_registro from VENCIMIENTO_PRODUCTO where ESTADO = 'A' and VENC_PROD_ID = id_vecn_prod;
        if (contador_registro >= 1) then
            update VENCIMIENTO_PRODUCTO set estado='N', update_date=sysdate, user_update=user where VENC_PROD_ID = id_vecn_prod;
            commit;
            DBMS_OUTPUT.PUT_LINE('SE ELIMINO EL REGISTRO');
        else
            DBMS_OUTPUT.PUT_LINE('NO EXISTE EL PRODUCTO');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;
    end eliminar_vencimiento_producto;


end mmto_vencimiento_producto;
/

--Procedimiento para dar mantenimiento a la tabla EMPLEADO.
-- FORMATO DE FECHA - '1992/05/03'
--Declaracion del paquete
create or replace package mmto_empleado is

    --Declaracion de los procedimientos
    procedure insertar_empleado(prim_nombre in varchar2, seg_nombre in varchar2, prim_apell in varchar2,
                                seg_apell in varchar2, num_tele in varchar2, fecha_nac in varchar2,
                                fecha_contr in varchar2, fecha_fin_contr in varchar2, correo in varchar2,
                                id_cate_emp in number, id_horario in number, id_nacionalidad in number,
                                id_puesto in number, id_sucursal in number, id_user in number);


end mmto_empleado;
/
create or replace package body mmto_empleado is

    procedure insertar_empleado(prim_nombre in varchar2, seg_nombre in varchar2, prim_apell in varchar2,
                                seg_apell in varchar2, num_tele in varchar2, fecha_nac in varchar2,
                                fecha_contr in varchar2, fecha_fin_contr in varchar2, correo in varchar2,
                                id_cate_emp in number, id_horario in number, id_nacionalidad in number,
                                id_puesto in number, id_sucursal in number, id_user in number) is
        contador_registro     int;
        contador_categoria    int;
        contador_horario      int;
        contador_nacionalidad int;
        contador_puesto       int;
        contador_sucursal     int;
        contador_user         int;
    begin
        select count(*) into contador_nacionalidad from NACIONALIDAD where NACIONALIDAD_ID = id_nacionalidad;
        select count(*) into contador_puesto from PUESTO where PUESTO_ID = id_puesto;
        select count(*) into contador_user from APP_USER where ID = id_user;
        select count(*) into contador_sucursal from SUCURSAL where SUCURSAL_ID = id_sucursal;
        select count(*) into contador_categoria from CATEGORIA_EMPLEADO where CATEGORIA_EMPLEADO_ID = id_cate_emp;
        select count(*) into contador_horario from HORARIO where HORARIO_ID = id_horario;
        select count(*) into contador_registro from EMPLEADO where USER_ID = id_user;
        if (contador_registro >= 1) then
            DBMS_OUTPUT.PUT_LINE('USUARIO YA EXISTE');
        elsif (contador_categoria = 0) then
            DBMS_OUTPUT.PUT_LINE('CATGORIA EMPLEADO NO EXISTE');
        elsif (contador_horario = 0) then
            DBMS_OUTPUT.PUT_LINE('HORARIO NO EXISTE');
        elsif (contador_nacionalidad = 0) then
            DBMS_OUTPUT.PUT_LINE('NACIONALIDAD NO EXISTE');
        elsif (contador_puesto = 0) then
            DBMS_OUTPUT.PUT_LINE('PUESTO NO EXISTE');
        elsif (contador_sucursal = 0) then
            DBMS_OUTPUT.PUT_LINE('PUESTO NO EXISTE');
        elsif (contador_user = 0) then
            DBMS_OUTPUT.PUT_LINE('USUARIO NO EXISTE');
        else

            insert into empleado(empleado_id, user_id, activo, horario_id, cat_empleado_id, nacionalidad_id, puesto_id,
                                 sucursal_id, primer_nombre, SEGUNDO_NOMBRE, primer_apellido,SEGUNDO_APELLIDO, user_create, create_date,
                                 estado,
                                 fecha_contratacion, fecha_nacimiento,FECHA_FIN_CONTRATO)
            values (empleado_seq.nextval, id_user, 1, id_horario, id_cate_emp, id_nacionalidad, id_puesto, id_sucursal,
                    prim_nombre, seg_nombre, prim_apell,seg_apell, user,
                   sysdate, 'A', to_date(fecha_contr, 'yyyy/mm/dd hh24:mi:ss'),
                   to_date(fecha_nac, 'yyyy/mm/dd hh24:mi:ss'),to_date(fecha_fin_contr, 'yyyy/mm/dd hh24:mi:ss'));
            commit;
            DBMS_OUTPUT.PUT_LINE('Usuario Ingresando');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_empleado;


end mmto_empleado;
/

create or replace package mmto_usuario is

    --Declaracion de los procedimientos
    procedure insertar_usuario(nombre_usuario in varchar2);


end mmto_usuario;
/


create or replace package  body mmto_usuario is

    procedure insertar_usuario(nombre_usuario in varchar2) is
        contador_registro int;
    begin
        select count(*) into contador_registro from app_user where username = nombre_usuario;
        if (contador_registro >= 1) then
            DBMS_OUTPUT.PUT_LINE('USUARIO YA EXISTE');
        else
            insert into app_user(id, username, password)
            values (app_user_seq.nextval,nombre_usuario,
                    '$2a$10$GpSnnc99hTZ7tUzkdRhBrunIsHacrMpJy9KUjxPSCoQeEvn9V/Mzi');
            insert into user_role(user_id,role_id) (select a.id,b.id from app_user a,role b where a.username=nombre_usuario and b.name='USER');
            commit;
            DBMS_OUTPUT.PUT_LINE('Usuario Ingresando');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_usuario;


end mmto_usuario;
/

--Procedimiento para dar mantenimiento a la tabla DOCUMENTO.
-- FORMATO DE FECHA - '1992/05/03'
--Declaracion del paquete
create or replace package mmto_documento is

    --Declaracion de los procedimientos
    procedure insertar_documento(num_doc in varchar2, fecha_exp in varchar2,
                                 fecha_venc in varchar2, id_empleado in number, id_tipo_identicacion in number);


end mmto_documento;
/
create or replace package body mmto_documento is

    procedure insertar_documento(num_doc in varchar2, fecha_exp in varchar2,
                                 fecha_venc in varchar2, id_empleado in number, id_tipo_identicacion in number) is
        contador_registro       int;
        contador_empleado       int;
        contador_identificacion int;

    begin
        select count(*) into contador_empleado from EMPLEADO where EMPLEADO_ID = id_empleado;
        select count(*) into contador_identificacion from TIPO_IDENTIFICACION where TIPO_IDEN_ID = id_tipo_identicacion;

        select count(*)
        into contador_registro
        from DOCUMENTO
        where TIPO_IDENTIFICACION_ID = id_tipo_identicacion
          and EMPLEADO_ID = id_empleado;
        if (contador_registro >= 1) then
            DBMS_OUTPUT.PUT_LINE('USUARIO YA EXISTE');
        elsif (contador_empleado = 0) then
            DBMS_OUTPUT.PUT_LINE('EMPLEADO NO EXISTE');
        elsif (contador_identificacion = 0) then
            DBMS_OUTPUT.PUT_LINE('TIPO DE IDENTIFICACION NO EXISTE');
        else
            INSERT INTO DOCUMENTO(DOCUMENTO_ID, CREATE_DATE, ESTADO, FECHA_EXPEDICION, FECHA_VENCIMIENTO, NUMERO,
                                   USER_CREATE,  EMPLEADO_ID, TIPO_IDENTIFICACION_ID)
            VALUES (DOCUMENTO_SEQ.nextval, SYSDATE, 'A', to_date(fecha_exp, 'yyyy/mm/dd hh24:mi:ss'),
                    to_date(fecha_venc, 'yyyy/mm/dd hh24:mi:ss'),num_doc,USER,id_empleado,id_tipo_identicacion);
            commit;
            DBMS_OUTPUT.PUT_LINE('Documento Ingresado');
        end if;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            ROLLBACK;
        WHEN OTHERS THEN
            ROLLBACK;

    end insertar_documento;


end mmto_documento;
/