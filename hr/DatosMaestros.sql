/*************************************************************************
*        MASTER TABLES POPULATION                                        *
*************************************************************************/        
-- inserto tipo de gastos
insert into details_type(det_code, det_description) values ('NIGHT','Price room night');
insert into details_type(det_code, det_description) values ('LNDRY','Laundry service');
insert into details_type(det_code, det_description) values ('BRKFS','Breakfast');
insert into details_type(det_code, det_description) values ('MNBAR','Minibar');
insert into details_type(det_code, det_description) values ('PRKNG','Parking');


/**************************************************************************/

INSERT INTO hotel (htl_name,htl_city,htl_address,htl_zip_code,htl_email,htl_phone,htl_stars,htl_latitude,htl_longitude) VALUES('Exceptions Hotel Las Vegas','Las Vegas','3778 Las Vegas Blvd','NV 89109','lasvegas@exceptionshoteles.com','877.386.5497',5,'36.1196804136566','-115.17470388915481');
INSERT INTO hotel (htl_name,htl_city,htl_address,htl_zip_code,htl_email,htl_phone,htl_stars,htl_latitude,htl_longitude) VALUES('Exceptions Hotel Vigo','Vigo','Avda Castelao 67','36208','vigo@exceptionshoteles.com','986 50 48 67',2,'42.21847248367241', '-8.748586559209471');
INSERT INTO hotel (htl_name,htl_city,htl_address,htl_zip_code,htl_email,htl_phone,htl_stars,htl_latitude,htl_longitude) VALUES('Exceptions Hotel A Coruña','A Coruña','Calle Riazor 20','52831','corunha@exceptionshoteles.com','983 96 45 12',3,'43.3685141433047', '-8.409046581369644');

insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(1,1);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(2,1);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(3,1);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(4,1);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(5,1);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(1,2);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(2,2);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(3,2);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(4,2);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(5,2);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(1,3);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(2,3);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(3,3);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(4,3);
insert into details_type_hotel(dhtl_det_id,dhtl_htl_id)values(5,3);

INSERT INTO room_type(rtyp_name,rtyp_bed_number,rtyp_base_price,rtyp_capacity) VALUES('doble',2,200,4);
INSERT INTO room_type(rtyp_name,rtyp_bed_number,rtyp_base_price,rtyp_capacity) VALUES('simple',1,120,1);
INSERT INTO room_type(rtyp_name,rtyp_bed_number,rtyp_base_price,rtyp_capacity) VALUES('suite',1,350,3);

INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'101',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'102',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'103',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'104',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'105',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'106',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'107',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'108',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'109',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'110',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'201',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'202',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'203',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'204',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'205',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'206',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'207',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'208',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'209',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'210',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1001',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1002',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1003',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1004',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1005',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1006',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1007',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1008',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1009',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1010',3);

INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'101',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'102',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'103',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'104',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'105',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'106',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'107',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'108',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'109',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'110',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'201',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'202',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'203',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'204',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'205',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'206',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'207',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'208',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'209',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'210',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1001',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1002',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1003',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1004',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1005',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1006',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1007',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1008',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1009',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (2,'1010',3);

INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'101',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'102',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'103',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'104',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'105',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'106',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'107',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'108',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'109',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'110',2);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'201',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'202',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'203',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'204',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'205',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'206',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'207',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'208',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'209',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'210',1);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1001',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1002',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1003',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1004',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1005',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1006',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1007',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1008',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1009',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (3,'1010',3);

insert into room_status(rsts_status,rsts_description) values ('mantenimiento','habitacion cerrada por mantenimiento');
insert into room_status(rsts_status,rsts_description) values ('reforma','habitacion cerrada por reformas');
insert into room_status(rsts_status,rsts_description) values ('alguiler','habitacion apartada para alquiler');
insert into room_status(rsts_status,rsts_description) values ('sucia','habitacion cerrada por necesidad de limpieza profunda');

--Creamos los roles--
insert into trole(rolename,xmlclientpermission) values('gestor','<?xml version="1.0" encoding="UTF-8"?><security></security>');
insert into trole(rolename,xmlclientpermission) values('recepcionista','<?xml version="1.0" encoding="UTF-8"?><security></security>');
insert into trole(rolename,xmlclientpermission) values('limpieza','<?xml version="1.0" encoding="UTF-8"?><security></security>');
insert into trole(rolename,xmlclientpermission) values('externo','<?xml version="1.0" encoding="UTF-8"?><security></security>');

--Creamos los usuarios--
insert into tuser (user_,password,name,surname,email,nif) values('juan','password','Juan','Perico','juan-perioc@examlpe.com','11223344J');
insert into tuser (user_,password,name,surname,email,nif) values('mario','password','Mario','Perico','mario-perioc@examlpe.com','11223355J');
insert into tuser (user_,password,name,surname,email,nif) values('carlos','password','Carlos','Perico','Carlos-perioc@examlpe.com','11223366J');
insert into tuser (user_,password,name,surname,email,nif) values('miguel','password','Miguel','Perico','miguel-perioc@examlpe.com','11223366J');

--Creamos los puestos de trabajo--
insert into employee_type (etyp_name,etyp_desc,etyp_role_id) values('Gestor','desc',1);
insert into employee_type (etyp_name,etyp_desc,etyp_role_id) values('Recepcionista','desc',2);
insert into employee_type (etyp_name,etyp_desc,etyp_role_id) values('Limpieza','desc',3);

--creamos los empleados--
insert into employee(emp_etyp_id,emp_name,emp_surname1,emp_surname2,emp_bank_account, emp_identification,emp_email,emp_phone,emp_htl_id,emp_usr_user)
values('1','Juan','Perico','Palotes','ES 92 5555 6666 7777 5555','11223344J','juan-perico@Yahoo.es','666555444','1','juan');
insert into employee(emp_etyp_id,emp_name,emp_surname1,emp_surname2,emp_bank_account, emp_identification,emp_email,emp_phone,emp_htl_id,emp_usr_user)
values('2','Mario','Perico','Palotes','ES 92 5555 6666 7777 6666','11223345J','mario-perico@Yahoo.es','666555445','1','mario');
insert into employee(emp_etyp_id,emp_name,emp_surname1,emp_surname2,emp_bank_account, emp_identification,emp_email,emp_phone,emp_htl_id,emp_usr_user)
values('2','Carlos','Perico','Palotes','ES 92 5555 6666 7777 8888','11223347J','carlos-perico@Yahoo.es','666555447','1','carlos');
insert into employee(emp_etyp_id,emp_name,emp_surname1,emp_surname2,emp_bank_account, emp_identification,emp_email,emp_phone,emp_htl_id,emp_usr_user)
values('3','Miguel','Perico','Palotes','ES 92 5555 6666 7777 7777','11223346J','miguel-perico@Yahoo.es','666555446','1','miguel');

--asignamos rol a los usuarios creados--
insert into tuser_role(id_rolename,user_) values(1,'juan');
insert into tuser_role(id_rolename,user_) values(2,'mario');
insert into tuser_role(id_rolename,user_) values(3,'miguel');
insert into tuser_role(id_rolename,user_) values(4,'carlos');

--insertamos temporadas--
insert into season(sea_name, sea_multiplier) values ('LOW',1);
insert into season(sea_name, sea_multiplier) values ('HIGH',1.5);

-- semana santa
insert into dates_season (dts_htl_id, dts_start_date, dts_end_date, dts_sea_id, dts_comments) values
                        (2,'2022-04-10','2022-04-17',2,'Holy Week hotel Vigo');
-- navidades
insert into dates_season(dts_htl_id, dts_start_date, dts_end_date, dts_sea_id, dts_comments) values
                        (2,'2022-12-24','2023-01-6',2,'Christmas hotel Vigo');

-- verano
insert into dates_season(dts_htl_id, dts_start_date, dts_end_date, dts_sea_id, dts_comments) values
                        (2,'2022-07-01','2022-08-31',2,'Summer hotel Vigo');

insert into dates_season(dts_htl_id, dts_start_date, dts_end_date, dts_sea_id, dts_comments)  values
                        (1,'2022-01-15','2022-05-15',2,'Crazy season Las vegas hotel');
                       
--inserto oferta dia aleatorio, 25 de julio, hotel vigo(2) en tipo doble(1), suele valer 200 la pongo a 150
insert into offers (ofe_day, ofe_htl_id, ofe_rom_typ_id, ofe_night_price) values ('2022-07-25',2,1,150);
insert into offers (ofe_day, ofe_htl_id, ofe_rom_typ_id, ofe_night_price) values ('2022-08-15',1,1,150);
insert into offers (ofe_day, ofe_htl_id, ofe_rom_typ_id, ofe_night_price) values ('2022-08-20',1,2,50);

-- INSERTO POLITICAS DE CANCELACION
----------------HOTEL 1-----------------------
-- TEMPORADA BAJA -----
-- todas las cancelaciones gratuitas, hasta un día antes. Sino, se paga la primera noche
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(1,1,1,1,1); --doble
insert into cancellations(can_htl_id,can_rtyp,can_sea_id,can_days_to_bok,can_nights_to_pay) values(1,2,1,1,1); --simple
--suites se exige avisar 7 días antes,o se cobra la primera noche tb
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(1,3,1,7,1); --suite
--TEMPORADA ALTA ----
-- todas las cancelaciones gratuitas, hasta 7 días antes. Después se paga la primera noche, y si se avisa en los 3 últimos días se cobran las dos primeras noches(si las hay)
-- 7 ultimos dias
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(1,1,2,7,1);
insert into cancellations(can_htl_id,can_rtyp,can_sea_id,can_days_to_bok,can_nights_to_pay) values(1,2,2,7,1);
-- 3 ultimos dias
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(1,1,2,3,2);
insert into cancellations(can_htl_id,can_rtyp,can_sea_id,can_days_to_bok,can_nights_to_pay) values(1,2,2,3,2);
-- suites se exige avisar 7 días antes,o se cobran las 3 primeras noches
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(1,3,2,7,3);

----------------HOTEL 2-----------------------
-- TEMPORADA BAJA -----
-- todas las cancelaciones gratuitas, hasta un día antes. Sino, se paga la primera noche
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(2,1,1,1,1); --doble
insert into cancellations(can_htl_id,can_rtyp,can_sea_id,can_days_to_bok,can_nights_to_pay) values(2,2,1,1,1); --simple
--suites se exige avisar 7 días antes,o se cobra la primera noche tb
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(2,3,1,7,1); --suite
--TEMPORADA ALTA ----
-- todas las cancelaciones gratuitas, hasta 3 días antes. Sino se paga la primera noche
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(2,1,2,3,1);
insert into cancellations(can_htl_id,can_rtyp,can_sea_id,can_days_to_bok,can_nights_to_pay) values(2,2,2,3,1);
-- suites se exige avisar 7 días antes,o se cobran las 3 primeras noches
insert into cancellations(can_htl_id, can_rtyp, can_sea_id, can_days_to_bok, can_nights_to_pay) values(2,3,2,7,3);

--asginamos permisos a gerente--
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/hotelQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/hotelInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/hotelUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/hotelDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getHotelByCoordinates'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getAirports'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getRecommendations'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getWeather'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomTypeService/roomTypeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomTypeService/roomTypeInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomTypeService/roomTypeDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomTypeService/roomTypeUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomMarkDirty'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingOcupiedCleanQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingOcupiedQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingByType'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/getPdfReport'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/upsertClient'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeByTypeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientsInDateQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeService/employeeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeService/employeeInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeService/employeeUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeService/employeeDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeService/employeeCreateUser'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISeasonService/seasonQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISeasonService/seasonInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISeasonService/seasonUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISeasonService/seasonDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDatesSeasonService/datesSeasonQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDatesSeasonService/datesSeasonInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDatesSeasonService/datesSeasonUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDatesSeasonService/datesSeasonDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDetailsTypeService/detailsTypeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDetailsTypeService/detailsTypeInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDetailsTypeService/detailsTypeUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDetailsTypeService/detailsTypeDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDetailsTypeHotelService/detailsTypeHotelQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDetailsTypeHotelService/detailsTypeHotelInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDetailsTypeHotelService/detailsTypeHotelUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDetailsTypeHotelService/detailsTypeHotelDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeScheduleService/employeeScheduleQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeScheduleService/employeeScheduleInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeScheduleService/employeeScheduleUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeScheduleService/employeeScheduleDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeScheduleService/employeeScheduleToday'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockIn'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockOut'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomUpdateStatus'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/sendMailClients'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IOffersService/offersByDateRangeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IOffersService/offerQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IOffersService/offerInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IOffersService/offerUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IOffersService/offerDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingDetailsService/bookingDetailsAdd'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/getPdfReport'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ICancellationsService/cancellationsQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ICancellationsService/cancellationsInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ICancellationsService/cancellationsUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ICancellationsService/cancellationsDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ICancellationsService/cancelBooking'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersService/specialOfferCreate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersService/specialOfferListAll'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersService/specialOfferListAlternatives'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersService/specialOfferDisable'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersConditionsService/specialOfferConditionAdd'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersConditionsService/specialOfferConditionModify'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersConditionsService/specialOfferConditionRemove'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersProductsService/specialOfferProductAdd'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersProductsService/specialOfferProductModify'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersProductsService/specialOfferProductRemove'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBirthdayService/setEnabledServiceBirthdayMail'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingGuestService/bookingGuestQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingGuestService/bookingGuestInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingGuestService/bookingGuestDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingGuestService/bookingGuestUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomStatusRecordService/roomStatusRecordQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomStatusRecordService/roomStatusRecordInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomStatusRecordService/roomStatusRecordUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomStatusRecordService/roomStatusRecordDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomStatusRecordService/roomStatusRecordSearch'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingDetailsService/bookingDetailsQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingDetailsService/bookingDetailsInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingDetailsService/bookingDetailsUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingDetailsService/bookingDetailsDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IPictureService/pictureQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IPictureService/pictureInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IPictureService/pictureUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IPictureService/pictureDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IPictureService/getPicture'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IPictureService/postPicture'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IPictureService/getPictureArray'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/checkIn'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/checkOut'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeByCityOrHotel'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(1,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingRoomChange'));

--asignamos permisos a recepcionista--
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/upsertClient'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/clientsInDateQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/hotelQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomTypeService/roomTypeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomMarkDirty'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingOcupiedQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingSearchByClient'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomUpdateStatus'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingByType'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingUpdateById'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockIn'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockOut'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeScheduleService/employeeScheduleQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/getBudget'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingcheckintodayQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/checkIn'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/checkOut'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeByTypeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/getPdfReport'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingGuestService/bookingGuestQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingGuestService/bookingGuestInsert'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingGuestService/bookingGuestDelete'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingGuestService/bookingGuestUpdate'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomStatusRecordService/roomStatusRecordQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingDetailsService/bookingDetailsQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISeasonService/seasonQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IDatesSeasonService/datesSeasonQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getHotelByCoordinates'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getAirports'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getRecommendations'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getWeather'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockIn'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockOut'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeScheduleService/employeeScheduleToday'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomStatusRecordService/roomStatusRecordSearch'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeByCityOrHotel'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingOcupiedCleanQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/getPdfReport'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingRoomChange'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ICancellationsService/cancelBooking'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IOffersService/offersByDateRangeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersService/specialOfferListAll'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersService/specialOfferListAlternatives'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(2,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingDetailsService/bookingDetailsAdd'));

--asignamos permisos a rol limpieza--
insert into trole_server_permission(id_rolename,id_server_permission)
values(3,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingOcupiedCleanQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(3,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockIn'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(3,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockOut'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(3,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeScheduleService/employeeScheduleQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(3,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockIn'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(3,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IEmployeeClockService/employeeClockOut'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(3,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IRoomService/roomMarkDirty'));

--asignamos permisos a agencia de viajes externa--
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getHotelByCoordinates'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getAirports'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getRecommendations'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IHotelService/getWeather'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/getBudget'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeByTypeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingByType'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IClientService/upsertClient'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISeasonService/seasonQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IOffersService/offersByDateRangeQuery'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.IBookingService/bookingFreeByCityOrHotel'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersService/specialOfferListAll'));
insert into trole_server_permission(id_rolename,id_server_permission)
values(4,(select id_server_permission from TSERVER_PERMISSION where permission_name ='com.ontimize.hr.api.core.service.ISpecialOffersService/specialOfferListAlternatives'));

--insertamos clientes--
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Marthe', 'Winsome', 'Ardley', '1981-05-31', '12594006E', '866-528-7505', 'mardley0@squarespace.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Manny', 'Sharpus', 'Scrivens', '1951-04-16', '12780030J', '908-848-5482', 'mscrivens1@reddit.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Goldarina', 'Orrill', 'Standage', '1956-06-09', '02953642D', '895-109-5580', 'gstandage2@usgs.gov');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Vinita', 'Hitcham', 'Jozefowicz', '1957-07-28', '33133995K', '992-339-5868', 'vjozefowicz3@360.cn');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Maria', 'Crat', 'Spendlove', '1977-04-30', '47069261H', '147-469-2343', 'mspendlove4@yellowpages.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Katha', 'Tompkinson', 'Allgood', '1984-12-13', '53171576D', '909-680-9614', 'kallgood5@ucoz.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Dulcy', 'Menier', 'Anderer', '1976-05-26', '28763947S', '104-741-2385', 'danderer6@weebly.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Evyn', 'Vieyra', 'Parade', '1951-10-04', '15542508R', '192-425-5441', 'eparade7@scribd.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Collin', 'MacGorley', 'O''Halligan', '1977-02-25', '72026674W', '529-383-6538', 'cohalligan8@trellian.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Hugibert', 'Hedylstone', 'Dysert', '1963-04-19', '76722525R', '575-945-2975', 'hdysert9@amazonaws.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Jay', 'Jado', 'Leipoldt', '1973-06-18', '89979264B', '551-616-5931', 'jleipoldta@paypal.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Dru', 'Felten', 'Symon', '1992-06-08', '14870999N', '769-124-1368', 'dsymonb@mapquest.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Oralee', 'Merrell', 'Coster', '1994-01-29', '11117067A', '960-448-4618', 'ocosterc@ask.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Nicko', 'Bulcock', 'Lebell', '1986-01-25', '18077608H', '456-696-3174', 'nlebelld@apache.org');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Page', 'Aupol', 'Brachell', '1956-08-12', '01735084J', '295-461-0778', 'pbrachelle@nbcnews.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Nicoline', 'Tilt', 'Ouchterlony', '1973-04-17', '38724046P', '345-536-8809', 'nouchterlonyf@feedburner.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Stuart', 'Faithorn', 'Blackie', '1980-02-28', '37178848L', '812-976-3767', 'sblackieg@nasa.gov');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Maurizio', 'Leaming', 'Mullane', '1957-12-02', '72596545W', '859-107-8457', 'mmullaneh@seesaa.net');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Niki', 'Tullis', 'Blaksley', '1989-08-23', '99814284E', '750-686-1164', 'nblaksleyi@oaic.gov.au');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Tomlin', 'Giacomoni', 'Sedgebeer', '1960-05-12', '85420890O', '684-487-6662', 'tsedgebeerj@washington.edu');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Phylis', 'Mugleston', 'Beaument', '1965-11-06', '17012302D', '752-582-0360', 'pbeaumentk@woothemes.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Kiel', 'Bute', 'Pauling', '1954-06-23', '11687673W', '979-750-0295', 'kpaulingl@vkontakte.ru');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Dyanna', 'Mostin', 'Chettle', '1996-07-09', '11553676S', '102-885-6663', 'dchettlem@hc360.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Christoper', 'Chittenden', 'Witcombe', '1985-11-02', '82352491H', '788-427-3587', 'cwitcomben@jalbum.net');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Mommy', 'Gantz', 'Deplacido', '1992-09-01', '90780410W', '590-903-8640', 'mdeplacidoo@pbs.org');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Garnet', 'Artz', 'Laybourne', '2001-04-24', '18799761I', '476-387-6969', 'glaybournep@de.vu');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Damiano', 'Possa', 'Lattimer', '1993-02-25', '48730026J', '170-877-1727', 'dlattimerq@symantec.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Abey', 'Gildea', 'Tellenbrook', '2001-06-18', '75506760I', '745-763-4923', 'atellenbrookr@usatoday.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Oriana', 'Ropcke', 'Bleyman', '2001-04-13', '56696876C', '938-711-2193', 'obleymans@prweb.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Jarid', 'Fiske', 'Tooke', '1976-05-22', '77905277Q', '563-731-2710', 'jtooket@unicef.org');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Ada', 'Fulger', 'Melpuss', '1972-12-18', '99623310D', '488-987-0374', 'amelpussu@cnbc.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Myranda', 'Sumshon', 'Letson', '1960-06-04', '06781896V', '351-325-5919', 'mletsonv@utexas.edu');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Ivonne', 'Brine', 'Giacobilio', '1962-01-24', '00498002S', '488-696-4966', 'igiacobiliow@smugmug.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Aurilia', 'Baston', 'Dorrington', '1966-09-05', '99119630W', '765-380-8412', 'adorringtonx@opera.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Aymer', 'Acock', 'Eccleston', '1952-09-06', '98416700M', '266-823-8680', 'aecclestony@alibaba.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Enrico', 'Renols', 'Issacof', '1958-12-28', '84454370K', '954-763-5960', 'eissacofz@nsw.gov.au');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Wright', 'Wornham', 'De Ruggiero', '1959-02-20', '98687650S', '240-746-2416', 'wderuggiero10@pen.io');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Halimeda', 'Lanegran', 'Eitter', '1974-12-17', '36525364W', '138-108-5526', 'heitter11@foxnews.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Adlai', 'Spire', 'Elen', '1979-02-27', '14167549E', '548-223-7960', 'aelen12@indiatimes.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Fleurette', 'Trewin', 'Glentz', '1975-11-30', '23167172D', '437-139-8643', 'fglentz13@phoca.cz');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Elmer', 'Gannaway', 'Fleisch', '1963-11-24', '02977152W', '340-492-3329', 'efleisch14@github.io');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Maribelle', 'Kowalski', 'Bernon', '1990-11-28', '16164827O', '221-564-3494', 'mbernon15@wix.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Cameron', 'Wannell', 'Gisbye', '1994-11-21', '79605982D', '933-326-5682', 'cgisbye16@google.es');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Baxie', 'Aulsford', 'Riddiough', '1974-10-17', '17663792G', '250-681-5004', 'briddiough17@themeforest.net');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Tommy', 'Chue', 'Keme', '1999-07-11', '80278466D', '392-448-9175', 'tkeme18@vinaora.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Chery', 'Diggles', 'Sturzaker', '1950-10-29', '26357076W', '322-879-1606', 'csturzaker19@furl.net');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Inge', 'Summerskill', 'McNab', '1999-06-28', '33832454D', '149-969-3145', 'imcnab1a@angelfire.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Zacharia', 'Abrami', 'Despenser', '1956-02-10', '21939127M', '413-651-6468', 'zdespenser1b@ucsd.edu');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Sasha', 'Latour', 'Mariner', '1971-11-09', '82239788T', '299-957-6021', 'smariner1c@reference.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Jillian', 'Stovin', 'Bugler', '1980-05-25', '10115140B', '576-591-3704', 'jbugler1d@sun.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Dall', 'Jobin', 'Eaken', '1951-01-11', '49582025V', '333-225-2053', 'deaken1e@cisco.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Karin', 'Lewzey', 'Pourvoieur', '1975-09-10', '39355290M', '918-259-8268', 'kpourvoieur1f@apache.org');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Meta', 'Adderson', 'Freer', '1972-11-04', '10557487X', '463-634-2118', 'mfreer1g@meetup.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Meade', 'Peres', 'McKerton', '1982-07-29', '23364764Z', '554-125-0910', 'mmckerton1h@prweb.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Melody', 'Niaves', 'Brigstock', '1957-05-25', '11382132L', '254-724-6326', 'mbrigstock1i@tinyurl.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Saree', 'Prati', 'Jackling', '1950-08-19', '77014403Q', '599-757-4196', 'sjackling1j@1688.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Dody', 'Bogies', 'Littledike', '1954-01-12', '45254779A', '616-946-0151', 'dlittledike1k@naver.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Joseito', 'Gaylard', 'Foxley', '1960-08-20', '89880997W', '420-892-8245', 'jfoxley1l@icq.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Leila', 'Bleaden', 'Thurley', '1985-08-19', '88722760D', '374-255-9257', 'lthurley1m@home.pl');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Sayre', 'Franks', 'Whitear', '1976-12-05', '72398505J', '145-465-9899', 'swhitear1n@goodreads.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Marcy', 'Brandin', 'Chipman', '1995-10-17', '17555257Y', '883-518-4471', 'mchipman1o@cloudflare.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Bobbi', 'Putley', 'Jenny', '1998-07-15', '16242679W', '720-842-9458', 'bjenny1p@fda.gov');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Janine', 'Crumley', 'Twelves', '1998-01-12', '85766774D', '125-480-7411', 'jtwelves1q@51.la');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Denney', 'Longfellow', 'Thody', '1985-12-31', '95570045R', '379-874-0869', 'dthody1r@lulu.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Brodie', 'Sartain', 'Audenis', '1986-11-19', '88732861X', '515-158-7354', 'baudenis1s@pagesperso-orange.fr');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Adlai', 'Kares', 'Skilbeck', '1960-09-25', '16431634E', '498-355-5913', 'askilbeck1t@sakura.ne.jp');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Elyse', 'Tawton', 'Towriss', '1990-05-15', '04094269T', '863-216-6502', 'etowriss1u@ow.ly');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Hurlee', 'Mulderrig', 'Pharo', '1972-02-07', '32161829B', '473-260-2960', 'hpharo1v@netscape.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Elias', 'Chong', 'Secker', '1977-08-25', '50842713X', '683-267-0722', 'esecker1w@guardian.co.uk');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Lebbie', 'Trustey', 'Ravillas', '1995-05-06', '78856732S', '910-892-7060', 'lravillas1x@tumblr.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Finn', 'Zolini', 'Wohlers', '1967-04-19', '22744471P', '299-278-0898', 'fwohlers1y@nytimes.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Lyssa', 'Cohen', 'Broadbent', '1983-12-30', '39472577C', '743-406-7973', 'lbroadbent1z@go.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Hallie', 'Scopes', 'Leeming', '1970-01-30', '58984362R', '701-592-6949', 'hleeming20@yellowpages.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Ralina', 'Toft', 'Fluit', '1991-02-19', '76092295X', '198-529-5706', 'rfluit21@hibu.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Conny', 'Kernley', 'Galier', '1963-12-13', '71980169H', '915-433-5782', 'cgalier22@wp.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Charis', 'Cantillon', 'Kesey', '1983-10-25', '43718801S', '542-608-3764', 'ckesey23@java.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Caz', 'Skewes', 'Inglish', '1972-12-14', '53844270U', '802-326-5887', 'cinglish24@webnode.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Nehemiah', 'Coull', 'McCarroll', '1976-07-26', '36199749B', '954-436-0339', 'nmccarroll25@wisc.edu');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Johnette', 'Gossop', 'Luby', '1954-10-04', '11009285D', '134-657-7885', 'jluby26@slideshare.net');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Dina', 'Hatz', 'Kubista', '1971-11-28', '68470601W', '949-801-2462', 'dkubista27@loc.gov');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Anson', 'Kill', 'Petruk', '1961-06-15', '70008637E', '617-274-8289', 'apetruk28@goo.ne.jp');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Maurits', 'Shire', 'Brosius', '1955-05-11', '82796542C', '645-223-8138', 'mbrosius29@springer.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Clare', 'Barkess', 'O'' Reagan', '1983-05-31', '30279999E', '365-669-2951', 'coreagan2a@ifeng.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Janey', 'Conibear', 'Jacobsz', '1993-04-21', '43004494X', '108-123-3121', 'jjacobsz2b@g.co');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Quintina', 'Paler', 'Linder', '1979-04-13', '11870388U', '689-361-0653', 'qlinder2c@flickr.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Patton', 'Ramel', 'Vowles', '1965-08-14', '50189270M', '433-192-7290', 'pvowles2d@bloomberg.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Vite', 'Timewell', 'Beesley', '1973-04-03', '96337747N', '983-271-4351', 'vbeesley2e@wired.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Kissee', 'Doodney', 'Brinsden', '1981-11-27', '71921702Y', '235-891-4387', 'kbrinsden2f@jigsy.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Riobard', 'Ram', 'Kingsford', '1991-01-19', '43237566I', '989-364-5822', 'rkingsford2g@prnewswire.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Arnie', 'Hann', 'Starton', '1951-09-11', '95074745Q', '380-776-8779', 'astarton2h@technorati.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Gawain', 'Swyer-Sexey', 'Ubsdell', '1984-10-22', '79968460K', '188-482-4080', 'gubsdell2i@sakura.ne.jp');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Clementia', 'Pershouse', 'Meckiff', '1975-08-13', '03509496J', '937-427-3878', 'cmeckiff2j@ovh.net');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Loretta', 'Wyson', 'Lourens', '1965-07-31', '30590772K', '769-128-6194', 'llourens2k@so-net.ne.jp');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Perceval', 'Baudic', 'Dorin', '1969-11-16', '96801577L', '692-489-8440', 'pdorin2l@illinois.edu');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Shalna', 'Faragan', 'Nussey', '1998-07-31', '87425419M', '202-458-2050', 'snussey2m@trellian.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Inez', 'Kenealy', 'Dyos', '1977-11-10', '53913442Z', '889-841-7394', 'idyos2n@miibeian.gov.cn');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Bobbye', 'Chapellow', 'Davids', '1992-10-01', '98492979X', '837-303-8585', 'bdavids2o@aol.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Legra', 'Tremathack', 'Struther', '1992-10-05', '48858745N', '408-574-8213', 'lstruther2p@walmart.com');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Madalyn', 'Kimblin', 'Bowgen', '2000-11-20', '39276164Q', '406-984-5037', 'mbowgen2q@altervista.org');
insert into client (cli_name, cli_surname1, cli_surname2, cli_birthday, cli_identification, cli_phone, cli_email) values ('Nanny', 'Le Brun', 'Bonhill', '1987-08-11', '08456667U', '793-612-5622', 'nbonhill2r@stanford.edu');

insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code, bok_comments)
values (1,1,'101','2022-07-25','2022-07-30','F','comments');

insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
values(1,'2022-06-25',1,120,120,true),
      (1,'2022-06-26',1,120,120,true),
      (1,'2022-06-27',1,120,120,true),
      (1,'2022-06-28',1,120,120,true),
      (1,'2022-06-29',1,120,120,true);
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,1,'102','2022-07-27','2022-08-02','F','comments');
--
--insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
--values(2,'2022-06-27',1,120,120,true),
--      (2,'2022-06-28',1,120,120,true),
--      (2,'2022-06-29',1,120,120,true),
--      (2,'2022-06-30',1,120,120,true),
--      (2,'2022-07-01',1,120,120,true),
--      (2,'2022-07-02',1,120,120,true);
--
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,1,'103','2022-07-03','2022-07-04','F','comments');
--
--insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
--values(3,'2022-07-03',1,120,120,true);
--
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,1,'104','2022-07-03','2022-07-07','F','comments');
--
--insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
--values(4,'2022-07-03',1,120,120,true),
--      (4,'2022-07-04',1,120,120,true),
--      (4,'2022-07-05',1,120,120,true),
--      (4,'2022-07-06',1,120,120,true);
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,1,'105','2022-07-03','2022-07-07','F','comments');
--
--insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
--values(5,'2022-06-28',1,120,120,true),
--      (5,'2022-06-29',1,120,120,true),
--      (5,'2022-06-30',1,120,120,true),
--      (5,'2022-07-01',1,120,120,true),
--      (5,'2022-07-02',1,120,120,true),
--      (5,'2022-07-03',1,120,120,true),
--      (5,'2022-07-04',1,120,120,true),
--      (5,'2022-07-05',1,120,120,true),
--      (5,'2022-07-06',1,120,120,true);
--
--
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,1,'106','2022-07-05','2022-07-07','F','comments');
--
--insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
--values(6,'2022-07-05',1,120,120,true),
--      (6,'2022-07-06',1,120,120,true);
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,1,'103','2022-07-19','2022-07-25','F','comments');
--
--insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
--values(7,'2022-07-19',1,120,120,true),
--      (7,'2022-07-20',1,120,120,true),
--      (7,'2022-07-21',1,120,120,true),
--      (7,'2022-07-22',1,120,120,true),
--      (7,'2022-07-23',1,120,120,true),
--      (7,'2022-07-24',1,120,120,true);
--
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,2,'101','2022-06-25','2022-07-28','C','comments');
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,2,'102','2022-06-15','2022-07-15','F','comments');
--
--insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
--values(9,'2022-06-19',1,120,120,true),
--      (9,'2022-06-20',1,120,120,true),
--      (9,'2022-06-21',1,120,120,true),
--      (9,'2022-06-22',1,120,120,true),
--      (9,'2022-06-23',1,120,120,true),
--      (9,'2022-06-24',1,120,120,true),
--      (9,'2022-06-25',1,120,120,true),
--      (9,'2022-06-26',1,120,120,true),
--      (9,'2022-06-27',1,120,120,true),
--      (9,'2022-06-28',1,120,120,true),
--      (9,'2022-06-29',1,120,120,true),
--      (9,'2022-06-30',1,120,120,true),
--      (9,'2022-07-01',1,120,120,true),
--      (9,'2022-07-02',1,120,120,true),
--      (9,'2022-07-02',1,120,120,true),
--      (9,'2022-07-03',1,120,120,true),
--      (9,'2022-07-04',1,120,120,true),
--      (9,'2022-07-05',1,120,120,true),
--      (9,'2022-07-06',1,120,120,true),
--      (9,'2022-07-07',1,120,120,true),
--      (9,'2022-07-08',1,120,120,true),
--      (9,'2022-07-09',1,120,120,true),
--      (9,'2022-07-10',1,120,120,true),
--      (9,'2022-07-11',1,120,120,true),
--      (9,'2022-07-12',1,120,120,true),
--      (9,'2022-07-13',1,120,120,true),
--      (9,'2022-07-14',1,120,120,true);
--
--insert into booking (bok_cli_id,bok_htl_id,bok_rom_number,bok_entry_date,bok_departure_date,bok_status_code,bok_comments)
--values (1,2,'103','2022-06-29','2022-07-02','F','comments');
--
--insert into booking_details(bok_det_bok_id,bok_det_date,bok_det_type,bok_det_price,bok_det_nominal_price,bok_det_paid)
--values(10,'2022-06-29',1,350,350,true),
--      (10,'2022-06-30',1,350,350,true),
--      (10,'2022-07-01',1,350,350,true);
--
--ALTER SEQUENCE booking_bok_id_seq RESTART WITH 12;

--prueba de horario de empleado--
--insert into employee_schedule(emp_sch_emp_id,emp_sch_day,emp_sch_turn_in,emp_sch_turn_out)
--values(1,'2022-08-03','7:00','15:00');
--insert into employee_schedule(emp_sch_emp_id,emp_sch_day,emp_sch_turn_in,emp_sch_turn_out)
--values(2,'2022-08-03','07:00','15:00');
--insert into employee_schedule(emp_sch_emp_id,emp_sch_day,emp_sch_turn_in,emp_sch_turn_out)
--values(3,'2022-08-03','15:00','23:00');
--insert into employee_schedule(emp_sch_emp_id,emp_sch_day,emp_sch_turn_in,emp_sch_turn_out)
--values(4,'2022-08-03','10:00','18:00');





-- inserto ejemplo bok_details
--insert into booking_details(bok_det_bok_id, bok_det_type, bok_det_date ,bok_det_price,bok_det_nominal_price,bok_det_paid) values
--                            (1,2,'2022-06-27',25,25,false);
--insert into booking_details(bok_det_bok_id, bok_det_type, bok_det_date ,bok_det_price,bok_det_nominal_price,bok_det_paid) values
--                            (1,2,'2022-06-27',50,50,false);


