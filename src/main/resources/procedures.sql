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

--PROCEDIMIENTO PARA APROBAR EL PEDIDO Y REALIZA LA LOGICA DEL INGRESO DEL PEDIDO
create or replace PROCEDURE APROBAR_PEDIDO(Id_Pedido IN NUMBER, usuario IN VARCHAR2)
    IS
    --Declaracion de variables
    estado_pedido       number;
    orden_compra        int;
    --declaracion del cursor y variables para el ingreso del pedido al proveedor

     CURSOR c_proveedor_pedido is (
        SELECT PRODUCTO_ID
        FROM PEDIDO_PRODUCTO
        WHERE PEDIDO_ID = Id_Pedido
    );
    resultado                c_proveedor_pedido%ROWTYPE;
    ordenes_compra_id        number;
    precio1                  float;
    contador_orden_proveedor int;
    prove_id                 number;
    pedido_producto_proveedor int;
    orden_provee number;

BEGIN

    select pro_pedi_id into estado_pedido from Progreso_pedido where Estado_pedido = 'Aprobado' and estado = 'A';

    --Realiza la actualizacion del pedido para que quede en estado Aprobado
    if (estado_pedido > 0) then
        update pedido set pro_pedi_id=estado_pedido, update_date=sysdate, user_update=user where pedido_id = Id_Pedido;

    else
        insert into progreso_pedido(pro_pedi_id, estado_pedido, user_create, create_date, estado)
        values (prog_pedi_seq.nextval, 'Aprobado', user, sysdate, 'A');

        select pro_pedi_id into estado_pedido from Progreso_pedido where Estado_pedido = 'Aprobado' and estado = 'A';
        update pedido set pro_pedi_id=estado_pedido, update_date=sysdate, user_update=user where pedido_id = Id_Pedido;

    end if;

    --ingresamos la orden de compra
    --verificamos que no exista la orden de compra
    select count(*) into orden_compra from INGRESO_ORDEN_COMPRA where PEDIDO_ID = Id_Pedido;
    if (orden_compra != 1) then
        insert into INGRESO_ORDEN_COMPRA(ORDEN_COMPRA_ID, create_date, estado, user_create, pedido_id)
        values (ING_ORDEN_COMPRA_SEQ.nextval, sysdate, 'A', user, Id_Pedido);


    end if;

    --INGRESAMOS EL PEDIDO AL PROVEEDOR.
    open c_proveedor_pedido;

    LOOP
        fetch c_proveedor_pedido into resultado;
        exit when c_proveedor_pedido%NOTFOUND;
        SELECT min(PRECIO) into precio1 FROM PRODUCTO_PROVEEDOR WHERE PRODUCTO_ID = resultado.PRODUCTO_ID;
        select Orden_compra_id into ordenes_compra_id from INGRESO_ORDEN_COMPRA where pedido_id = Id_Pedido;
        select PROVEEDOR_ID
        into prove_id
        from PRODUCTO_PROVEEDOR
        where PRECIO = precio1
          and PRODUCTO_ID = resultado.PRODUCTO_ID and ROWNUM <= 1;
        select count(*)
        into contador_orden_proveedor
        from pedido_proveedor
        where ORDEN_COMPRA_ID = ordenes_compra_id
          and PROVEEDOR_ID = prove_id;
        if (contador_orden_proveedor = 0) then
            insert into PEDIDO_PROVEEDOR(PEDI_PROV_ID, create_date, estado, user_create, ORDEN_COMPRA_ID, proveedor_id)
            values (Pedi_Prove_seq.nextval, sysdate, 'A', user, ordenes_compra_id, prove_id);

            select PEDI_PROV_ID
            into orden_provee
            from pedido_proveedor
            where ORDEN_COMPRA_ID = ordenes_compra_id
              and PROVEEDOR_ID = prove_id;
            select count(*) into pedido_producto_proveedor from proveedor_pedido_producto where PRODUCTO_ID=resultado.PRODUCTO_ID and PEDIDO_ID=Id_Pedido and PEDI_PROV_ID = orden_provee;
            if(pedido_producto_proveedor=0) then
                DBMS_OUTPUT.PUT_LINE('si entro');
                insert into proveedor_pedido_producto(PEDIDO_ID,PRODUCTO_ID,PEDI_PROV_ID) values (Id_Pedido,resultado.PRODUCTO_ID,orden_provee);

            end if;
        end if;
        select PEDI_PROV_ID
            into orden_provee
            from pedido_proveedor
            where ORDEN_COMPRA_ID = ordenes_compra_id
              and PROVEEDOR_ID = prove_id;
            select count(*) into pedido_producto_proveedor from proveedor_pedido_producto where PRODUCTO_ID=resultado.PRODUCTO_ID and PEDIDO_ID=Id_Pedido and PEDI_PROV_ID = orden_provee;
            if(pedido_producto_proveedor=0) then
                DBMS_OUTPUT.PUT_LINE('si entro');
                insert into proveedor_pedido_producto(PEDIDO_ID,PRODUCTO_ID,PEDI_PROV_ID) values (Id_Pedido,resultado.PRODUCTO_ID,orden_provee);

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


