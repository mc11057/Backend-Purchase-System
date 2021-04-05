--This  script is the seed of the application

insert into role (id,name) values (role_seq.nextval,'ADMIN');
insert into role (id,name) values (role_seq.nextval,'USER');

--inserting user of type ADMIN  --password is adminUser
insert into app_user(id,username,password) values (app_user_seq.nextval,'adminUser','$2a$10$My3yDzChxDQLVFIIRsp5leJAy3MC6/5doJ93IZL5saO2IAUB/jquK'); 

--inserting user of type USER --password is customerUserr 
insert into app_user(id,username,password) values (app_user_seq.nextval,'customerUser','$2a$10$4myBvPzw.5.y.8FBZ/5JLOYozDIpZBlCoAaAjKgX5xuGVcVmbZ9xK'); 

--insert user roles relation
insert into user_role(user_id,role_id) values (1,1); 
insert into user_role(user_id,role_id) values (1,2);
insert into user_role(user_id,role_id) values (2,2); 

-- insert into region

insert into region (id, nombre, user_create,create_date) values (reguion_seq.nextval,'Centro America', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));
insert into region  (id, nombre, user_create,create_date) values (reguion_seq.nextval,'Sur America', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));
insert into region  (id, nombre, user_create,create_date) values (reguion_seq.nextval,'Norte America', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));
insert into region  (id, nombre, user_create,create_date) values (reguion_seq.nextval,'Europa', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));
insert into region  (id, nombre, user_create,create_date) values (reguion_seq.nextval,'Asia', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));

--insert into pais
insert into pais(id, nombre, user_create,create_date,region_id) values (pais_seq.nextval,'El Salvador', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1);
insert into pais(id, nombre, user_create,create_date,region_id) values (pais_seq.nextval,'Guatemala', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1);
insert into pais(id, nombre, user_create,create_date,region_id) values (pais_seq.nextval,'Alemania', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),4);
insert into pais(id, nombre, user_create,create_date,region_id) values (pais_seq.nextval,'Argentina', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),2);
insert into pais(id, nombre, user_create,create_date,region_id) values (pais_seq.nextval,'Mexico', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),3);
insert into pais(id, nombre, user_create,create_date,region_id) values (pais_seq.nextval,'Japon', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),5);


--- insert into estado 


insert into estado(id, nombre, user_create,create_date,pais_id) values (pais_seq.nextval,'Zona Oriental', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1);
insert into estado(id, nombre, user_create,create_date,pais_id) values (pais_seq.nextval,'Zona Central', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1);
insert into estado(id, nombre, user_create,create_date,pais_id) values (pais_seq.nextval,'Zona Occidental', 'ADMIN', to_date('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'),1);