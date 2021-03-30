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