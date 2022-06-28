drop  table if exists booking;
drop  table if exists room;
drop table if exists room_type;
drop table if exists hotel;
drop  table if exists client;

CREATE TABLE if not exists hotel(
    htl_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
    htl_name varchar(100) NOT NULL,
    htl_city varchar(50),
    htl_address varchar(255),
    htl_zip_code varchar(20),
    htl_email varchar(150),
    htl_phone varchar(20),
    htl_stars integer,
    CONSTRAINT CK_hotel_htl_name CHECK (ltrim(rtrim(hotel.htl_name))<>''),
    CONSTRAINT UQ_hotel_htl_email UNIQUE (htl_email)
);


CREATE TABLE if not exists room_type(
    typ_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
    typ_name varchar(20) NOT NULL,
    typ_bed_number integer NOT NULL,
    typ_base_price decimal(10,2) NOT NULL,
    CONSTRAINT CK_room_type_typ_name CHECK (ltrim(rtrim(room_type.typ_name))<>''),
    CONSTRAINT UQ_room_type_typ_name UNIQUE (typ_name)
);


CREATE TABLE if not exists client(
    cli_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
    cli_name varchar(30) NOT NULL,
    cli_surname1 varchar(30) NOT NULL,
    cli_surname2 varchar(30),
    cli_identification varchar(20) not null,
    cli_phone varchar(20),
    cli_email varchar(150),
    CONSTRAINT CK_client_cli_name CHECK (ltrim(rtrim(client.cli_name))<>''),
    CONSTRAINT CK_client_cli_surname1 CHECK (ltrim(rtrim(client.cli_surname1))<>''),
    CONSTRAINT UQ_client_cli_name UNIQUE (cli_name)

);


CREATE TABLE if not exists room(
   	rom_htl_id INTEGER,
	rom_number varchar(15),
    rom_typ_id integer NOT NULL,
    CONSTRAINT CK_room_rom_number CHECK (ltrim(rtrim(room.rom_number))<>''),
    constraint pk_room primary key (rom_htl_id,rom_number)
);

ALTER TABLE room ADD CONSTRAINT fk_type_room FOREIGN KEY(rom_typ_id) REFERENCES room_type(typ_id);
ALTER TABLE room ADD CONSTRAINT fk_hotel_room FOREIGN KEY(rom_htl_id) REFERENCES hotel(htl_id);


CREATE TABLE if not exists booking(
    bok_id INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 1) NOT NULL PRIMARY KEY,
    bok_cli_id integer NOT NULL,
    bok_htl_id INTEGER NOT NULL,
	bok_rom_number varchar(15)NOT NULL,
    bok_entry_date date NOT NULL,
    bok_departure_date date,
    bok_coments varchar(255)
);

ALTER TABLE booking ADD CONSTRAINT fk_client_booking FOREIGN KEY(bok_cli_id) REFERENCES client(cli_id);
ALTER TABLE booking ADD CONSTRAINT fk_room_booking FOREIGN KEY(bok_rom_number,bok_htl_id) REFERENCES room(rom_number,rom_htl_id);


INSERT INTO hotel (htl_name,htl_city,htl_address,htl_zip_code,htl_email,htl_phone,htl_stars) VALUES('Exceptions Hotel Las Vegas','Las vegas','3778 Las Vegas Blvd','NV 89109','lasvegas@exceptionshoteles.com','877.386.5497',5);
INSERT INTO hotel (htl_name,htl_city,htl_address,htl_zip_code,htl_email,htl_phone,htl_stars) VALUES('Exceptions Hotel Vigo','Vigo','Avda Castelao 67','36208','vigo@exceptionshoteles.com','986 50 48 67',2);
INSERT INTO hotel (htl_name,htl_city,htl_address,htl_zip_code,htl_email,htl_phone,htl_stars) VALUES('Exceptions Hotel Coruña','Coruña','C\ Riazor 20','52831','corunha@exceptionshoteles.com','983 96 45 12',3);

INSERT INTO room_type(typ_name,typ_bed_number,typ_base_price) VALUES('doble',2,200);
INSERT INTO room_type(typ_name,typ_bed_number,typ_base_price) VALUES('simple',1,120);
INSERT INTO room_type(typ_name,typ_bed_number,typ_base_price) VALUES('suite',1,350);


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
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1001',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1002',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1003',3);
INSERT INTO room(rom_htl_id,rom_number,rom_typ_id) VALUES (1,'1004',3);



insert  into client (cli_name,cli_surname1,cli_surname2,cli_identification,cli_phone,cli_email) values ('CARLOS', 'LOPEZ','LOPEZ','33445566T','344565789','carlos@micorreo.com');
insert  into client (cli_name,cli_surname1,cli_surname2,cli_identification,cli_phone,cli_email) values ('JOSE', 'PÉREZ','MARTINEZ','76545454Y','667788990','jp@mail.net');
insert  into client (cli_name,cli_surname1,cli_surname2,cli_identification,cli_phone,cli_email) values ('JUAN', 'JUAREZ','LOPEZ','3444455556P','22334433','juan@mail.co.uk');



