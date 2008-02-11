alter table inpacc drop column district_id,
	drop index index_2,
	drop index index_3,
	drop index index_4,
	drop index index_5;

alter table inpacc add column district_id integer,
	add index index_2 (District),
	add index index_3 (StreetName, StreetType),
	add index index_4 (HouseNum, PartNum, StreetId),
	add index index_5 (Appartment, BuildingId);

-- extract districts
DROP TABLE IF EXISTS districts;
CREATE TABLE districts(
	id int(10) unsigned NOT NULL auto_increment, 
    District varchar(45) NOT NULL,  
    PRIMARY KEY  (id));
alter table districts add index i_district (District);

insert into districts (District) select distinct district from inpacc;
update inpacc inp set district_id = (select id from districts d where d.district=inp.district);

-- extract streets
DROP TABLE IF EXISTS streets;
CREATE TABLE streets(
	id int(10) unsigned NOT NULL auto_increment, 
    StreetName varchar(45) NOT NULL,  
    StreetType varchar(10) NOT NULL,
    StreetTypeId int(10),
    PRIMARY KEY  (id));
alter table streets add index i_street (StreetName, StreetType);

insert into streets (StreetName, StreetType) select distinct StreetName, StreetType from inpacc;
update inpacc inp set StreetId = (select id from streets st where st.StreetName=inp.StreetName AND st.StreetType=inp.StreetType);

DROP TABLE IF EXISTS street_types;
CREATE TABLE street_types(
	id int(10) unsigned NOT NULL auto_increment, 
    StreetType varchar(10) NOT NULL,
    PRIMARY KEY  (id));

insert into street_types (StreetType) select distinct StreetType from streets;
update streets st set StreetTypeId = (select id from street_types stt where stt.StreetType=st.StreetType);

-- extract buildings
DROP TABLE IF EXISTS buildings;
create table buildings(
    id int(10) unsigned NOT NULL auto_increment,
    houseNum varchar(100) NOT NULL,
    partNum varchar(100),
    StreetID int(10) NOT NULL,
    PRIMARY KEY (id));
alter table buildings add index i_house_part (houseNum, partNum, StreetId);
insert into buildings (houseNum, partNum, StreetID) select distinct houseNum, partNum, StreetID from inpacc;
update inpacc inp set BuildingID = (select id from buildings b where b.houseNum=inp.houseNum and b.partNum=inp.partNum and b.StreetID=inp.StreetID);

-- extract apartments
DROP TABLE IF EXISTS apartments;
create table apartments(
	id int(10) unsigned NOT NULL auto_increment,
	Apartment varchar(100),
	BuildingId int(10) NOT NULL,
	PRIMARY KEY (id));
alter table apartments add index i_apartment_building (Apartment, BuildingId);
insert into apartments (Apartment, BuildingId) select distinct Appartment, BuildingId from inpacc;
update inpacc inp set AppartmentID = (select id from apartments a where a.Apartment=inp.Appartment and a.BuildingId=inp.BuildingId);

-- alter table inpacc drop column District;
-- alter table inpacc drop column StreetName, drop column StreetType;
