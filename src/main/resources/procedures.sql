create or replace PROCEDURE denegar_pedido(ID_Pedido IN NUMBER, usuario IN VARCHAR2)
IS
estado_pedido number;
BEGIN

   select pro_pedi_id into estado_pedido from Progreso_pedido where Estado_pedido ='Cancelado' and estado ='A';

   if(estado_pedido>0)then
    update pedido set pro_pedi_id=estado_pedido,update_date=sysdate,user_update=user,estado='N' where pedido_id=ID_Pedido;
   else
   insert into progreso_pedido(pro_pedi_id, estado_pedido, user_create,create_date,estado) values (prog_pedi_seq.nextval,'Cancelado', user, sysdate,'A');
   begin
   select pro_pedi_id into estado_pedido from Progreso_pedido where Estado_pedido ='Cancelado' and estado ='A';
    end;
     update pedido set pro_pedi_id=estado_pedido,update_date=sysdate,user_update=user,estado='N' where pedido_id=ID_Pedido;
     commit;
   end if;
    EXCEPTION
    WHEN NO_DATA_FOUND THEN
     DBMS_OUTPUT.PUT_LINE('si entro');
    ROLLBACK;
    WHEN OTHERS THEN
     ROLLBACK;

END;

create or replace NONEDITIONABLE PROCEDURE APROBAR_PEDIDO(Id_Pedido IN NUMBER, usuario IN VARCHAR2)
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
            if (pedido_producto_proveedor = 0) then
                DBMS_OUTPUT.PUT_LINE('si entro');
                insert into proveedor_pedido_producto(PEDIDO_ID, PRODUCTO_ID, PEDI_PROV_ID)
                values (Id_Pedido, resultado.PRODUCTO_ID, orden_provee);

            end if;
            IF (contador_facturas = 0) THEN
                INSERT INTO FACTURA_ORDEN_PAGO(fact_orden_pago_id, create_date, estado, fecha_emision, numero_factura,
                                               user_create, pedi_prov_id)
                VALUES (FACT_ORDEN_PAGO_SEQ.nextval, sysdate, 'A', SYSDATE, FACT_ORDEN_PAGO_SEQ.nextval, usuario,
                        orden_provee);
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
        if (pedido_producto_proveedor = 0) then
            DBMS_OUTPUT.PUT_LINE('si entro');
            insert into proveedor_pedido_producto(PEDIDO_ID, PRODUCTO_ID, PEDI_PROV_ID)
            values (Id_Pedido, resultado.PRODUCTO_ID, orden_provee);

        end if;
        IF (contador_facturas = 0) THEN
            INSERT INTO FACTURA_ORDEN_PAGO(fact_orden_pago_id, create_date, estado, fecha_emision, numero_factura,
                                           user_create, pedi_prov_id)
            VALUES (FACT_ORDEN_PAGO_SEQ.nextval, sysdate, 'A', SYSDATE, FACT_ORDEN_PAGO_SEQ.nextval, usuario,
                    orden_provee);
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


create or replace procedure crear_pago(id_factura_orden_pago IN NUMBER, id_moneda in number, id_forma_pago IN NUMBER, usuario in varchar2)
IS
contador_factura number;
contador_moneda number;
contador_pago number;

BEGIN
    --validamos que existan los registros que vienen de parametros
    select count(*) into contador_factura from factura_orden_pago where fact_orden_pago_id = id_factura_orden_pago and estado='A';
    select count(*) into contador_moneda from moneda where moneda_id = id_moneda and estado='A';
    select count(*) into contador_pago from forma_pago where forma_pago_id = id_forma_pago and estado='A';
    
    if(contador_factura=1) then
        if(contador_moneda=1) then
            if(contador_pago=1) then
            insert into pago(pago_id,create_date,estado,user_create,fact_orden_pago_id,forma_pago_id,moneda_id) 
            values(pago_seq.nextval,sysdate,'A',usuario,id_factura_orden_pago,id_forma_pago,id_moneda);
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