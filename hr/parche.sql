INSERT INTO TSERVER_PERMISSION(permission_name) VALUES('com.ontimize.hr.api.core.service.IClientService/upsertClient');
insert into trole_server_permission(id_rolename,id_server_permission)
values(0,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/upsertClient'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/upsertClient'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/upsertClient'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/upsertClient'));
ALTER TABLE client
ALTER COLUMN cli_email_subscription SET NOT NULL;