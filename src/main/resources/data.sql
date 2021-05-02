--This  script is the seed of the application

insert into role (id,name) values (role_seq.nextval,'ADMIN');
insert into role (id,name) values (role_seq.nextval,'USER');

--inserting user of type ADMIN  --password is adminUser
insert into app_user(id,username,password) values (app_user_seq.nextval,'adminUser','$2a$10$My3yDzChxDQLVFIIRsp5leJAy3MC6/5doJ93IZL5saO2IAUB/jquK'); 

--inserting user of type USER --password is customerUserr 
insert into app_user(id,username,password) values (app_user_seq.nextval,'customerUser','$2a$10$4myBvPzw.5.y.8FBZ/5JLOYozDIpZBlCoAaAjKgX5xuGVcVmbZ9xK'); 

--insert user roles relation
insert into user_role(user_id,role_id) (select a.id,b.id from app_user a,role b where a.username='adminUser' and b.name='ADMIN');
insert into user_role(user_id,role_id) (select a.id,b.id from app_user a,role b where a.username='adminUser' and b.name='USER');
insert into user_role(user_id,role_id) (select a.id,b.id from app_user a,role b where a.username='customerUser' and b.name='USER');
-- insert into region

insert into region (region_id, nombre, user_create,create_date,estado) values (reguion_seq.nextval,'Centro America', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into region  (region_id, nombre, user_create,create_date,estado) values (reguion_seq.nextval,'Sur America', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into region  (region_id, nombre, user_create,create_date,estado) values (reguion_seq.nextval,'Norte America', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into region  (region_id, nombre, user_create,create_date,estado) values (reguion_seq.nextval,'Europa', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into region  (region_id, nombre, user_create,create_date,estado) values (reguion_seq.nextval,'Asia', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');

--insert into pais
insert into pais(pais_id, nombre, user_create,create_date,region_id,estado) values (pais_seq.nextval,'El Salvador', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');
insert into pais(pais_id, nombre, user_create,create_date,region_id,estado) values (pais_seq.nextval,'Guatemala', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');
insert into pais(pais_id, nombre, user_create,create_date,region_id,estado) values (pais_seq.nextval,'Alemania', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),4,'A');
insert into pais(pais_id, nombre, user_create,create_date,region_id,estado) values (pais_seq.nextval,'Argentina', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,'A');
insert into pais(pais_id, nombre, user_create,create_date,region_id,estado) values (pais_seq.nextval,'Mexico', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),3,'A');
insert into pais(pais_id, nombre, user_create,create_date,region_id,estado) values (pais_seq.nextval,'Japon', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),5,'A');


--- insert into estado 
insert into estado(estado_id, nombre, user_create,create_date,pais_id,estado) values (estado_seq.nextval,'Zona Oriental', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');
insert into estado(estado_id, nombre, user_create,create_date,pais_id,estado) values (estado_seq.nextval,'Zona Central', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');
insert into estado(estado_id, nombre, user_create,create_date,pais_id,estado) values (estado_seq.nextval,'Zona Occidental', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');

--- insert into departamento
insert into departamento(departamento_id, nombre, user_create,create_date,estado_id,estado) values (departamento_seq.nextval,'San Miguel', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');
insert into departamento(departamento_id, nombre, user_create,create_date,estado_id,estado) values (departamento_seq.nextval,'Usualutan', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');
insert into departamento(departamento_id, nombre, user_create,create_date,estado_id,estado) values (departamento_seq.nextval,'San Salvador', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,'A');

--- insert into ciudad
insert into ciudad(ciudad_id, nombre, user_create,create_date,departamento_id,estado) values (ciudad_seq.nextval,'San Salvador', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),3,'A');
insert into ciudad(ciudad_id, nombre, user_create,create_date,departamento_id,estado) values (ciudad_seq.nextval,'Soyapango', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),3,'A');
insert into ciudad(ciudad_id, nombre, user_create,create_date,departamento_id,estado) values (ciudad_seq.nextval,'Ilopango', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),3,'A');


--- insert into barrio
insert into barrio(barrio_id, nombre, user_create,create_date,ciudad_id,estado) values (barrio_seq.nextval,'Miralvalle', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');
insert into barrio(barrio_id, nombre, user_create,create_date,ciudad_id,estado) values (barrio_seq.nextval,'Colonia Guayacan', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,'A');
insert into barrio(barrio_id, nombre, user_create,create_date,ciudad_id,estado) values (barrio_seq.nextval,'Colonia San Jose', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,'A');


--- insert into Ubicacion
insert into ubicacion(ubicacion_id, direccion, user_create,create_date,barrio_id,estado) values (ubicacion_seq.nextval,'Pasaje Florida #54-2A', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,'A');
insert into ubicacion(ubicacion_id, direccion, user_create,create_date,barrio_id,estado) values (ubicacion_seq.nextval,'Pasaje Eztanzuela #64-2A', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,'A');
insert into ubicacion(ubicacion_id, direccion, user_create,create_date,barrio_id,estado) values (ubicacion_seq.nextval,'calle Princiapal', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,'A');


--- insert into Tipo Horario
insert into tipo_horario(tipo_horario_id, tipo, user_create,create_date,estado) values (tipo_horario_seq.nextval,'TIPO 1', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into tipo_horario(tipo_horario_id, tipo, user_create,create_date,estado) values (tipo_horario_seq.nextval,'TIPO 2', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into tipo_horario(tipo_horario_id, tipo, user_create,create_date,estado) values (tipo_horario_seq.nextval,'TIPO 3', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');


--- insert into horario
insert into horario(horario_id,hora_entrada,hora_salida, user_create,create_date,tipo_horario_id,estado) values (horario_seq.nextval, to_date('2021/05/03 08:30:44', 'yyyy/mm/dd hh24:mi:ss'),to_date('2021/05/03 15:00:44', 'yyyy/mm/dd hh24:mi:ss'),'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,'A');
insert into horario(horario_id,hora_entrada,hora_salida, user_create,create_date,tipo_horario_id,estado) values (horario_seq.nextval, to_date('2021/05/03 12:30:44', 'yyyy/mm/dd hh24:mi:ss'),to_date('2021/05/03 19:00:44', 'yyyy/mm/dd hh24:mi:ss'),'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,'A');
insert into horario(horario_id,hora_entrada,hora_salida, user_create,create_date,tipo_horario_id,estado) values (horario_seq.nextval, to_date('2021/05/03 14:30:44', 'yyyy/mm/dd hh24:mi:ss'),to_date('2021/05/03 21:00:44', 'yyyy/mm/dd hh24:mi:ss'),'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),3,'A');


--- insert into sucursal
insert into sucursal(sucursal_id, nombre, user_create,create_date,horario_id,ubicacion_id,estado) values (sucursal_seq.nextval,'SUCURSAL 1', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1,1,'A');
insert into sucursal(sucursal_id, nombre, user_create,create_date,horario_id,ubicacion_id,estado) values (sucursal_seq.nextval,'SUCURSAL 2', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2,2,'A');
insert into sucursal(sucursal_id, nombre, user_create,create_date,horario_id,ubicacion_id,estado) values (sucursal_seq.nextval,'SUCURSAL 3', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),3,3,'A');

--- insert progreso_pedido
insert into progreso_pedido(pro_pedi_id, estado_pedido, user_create,create_date,estado) values (prog_pedi_seq.nextval,'Activo', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into progreso_pedido(pro_pedi_id, estado_pedido, user_create,create_date,estado) values (prog_pedi_seq.nextval,'Activo', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into progreso_pedido(pro_pedi_id, estado_pedido, user_create,create_date,estado) values (prog_pedi_seq.nextval,'Cancelado', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'N');

--- insert tipo_identificacion
insert into tipo_identificacion(tipo_iden_id, tipo, user_create,create_date,estado) values (tipo_iden_seq.nextval,'DUI', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into tipo_identificacion(tipo_iden_id, tipo, user_create,create_date,estado) values (tipo_iden_seq.nextval,'NIT', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into tipo_identificacion(tipo_iden_id, tipo, user_create,create_date,estado) values (tipo_iden_seq.nextval,'PASA', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'N');

--- insert nacionalidad
insert into nacionalidad(nacionalidad_id, nombre, user_create,create_date,estado) values (nacionalidad_seq.nextval,'El Salvador', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into nacionalidad(nacionalidad_id, nombre, user_create,create_date,estado) values (nacionalidad_seq.nextval,'Guatemala', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into nacionalidad(nacionalidad_id, nombre, user_create,create_date,estado) values (nacionalidad_seq.nextval,'Honduras', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'N');

--- insert categoria_empleado
insert into categoria_empleado(categoria_empleado_id, tipo, user_create,create_date,estado) values (categoria_emp_seq.nextval,'Tipo A', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into categoria_empleado(categoria_empleado_id, tipo, user_create,create_date,estado) values (categoria_emp_seq.nextval,'Tipo B', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A');
insert into categoria_empleado(categoria_empleado_id, tipo, user_create,create_date,estado) values (categoria_emp_seq.nextval,'Tipo C', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'N');

--insert into puesto
insert into puesto (puesto_id,create_date,estado,nombre,salary,user_create) values (puesto_seq.nextval,sysdate,'A','JEFE DE SUCURSAL',200.00,'ADMIN');

--insert into empleado
insert into empleado(empleado_id,activo,horario_id,cat_empleado_id,nacionalidad_id,puesto_id,sucursal_id,primer_nombre,primer_apellido, user_create,create_date,estado,fecha_contratacion,fecha_nacimiento)values (empleado_seq.nextval,1,1,1,1,1,1,'Walter' ,'Quintanilla', 'ADMIN',to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),'A',sysdate,to_date('1992/05/03','yyyy/mm/dd'));

--insert into categoria_producto
insert into categoria_producto (cat_prod_id,create_date,estado,nombre,user_create) values(cat_prod_seq.nextval,sysdate,'A','PERECEDERO','ADMIN');

--insert into tipo_producto
insert into tipo_producto (tipo_prod_id,create_date,estado,tipo,user_create) values (tipo_prod_seq.nextval,sysdate,'A','COMESTIBLE','ADMIN');

--insert into producto
insert into producto(producto_id,create_date,estado,nombre,user_create,cat_prod_id,tipo_prod_id)values (producto_seq.nextval,sysdate,'A','MANZANA','ADMIN',1,1);

--insert into existencia
insert into existencia(existencia_id,cantidad,create_date,estado,user_create,producto_id) values (existencia_seq.nextval,100,sysdate,'A','ADMIN',1);
--insert into vencimiento_producto
insert into vencimiento_producto(venc_prod_id,create_date,estado,fecha_vencimiento,user_create,producto_id) values (venc_prod_seq.nextval,sysdate,'A', to_date('2022/05/03', 'yyyy/mm/dd'),'ADMIN',1);
--update vencimiento and existencia in product
update producto set existencia_id='1',venc_prod_id='1' where producto_id='1';
--insert into pedido
insert into pedido(pedido_id,create_date,estado,user_create,empleado_id,pro_pedi_id) values (pedido_seq.nextval,sysdate,'A','ADMIN',1,1);
--insert into pedido_producto
insert into pedido_producto(pedido_id,producto_id,cantidad) values (1,1,5);
