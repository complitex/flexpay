INSERT INTO common_flexpay_modules_tbl (name) VALUES ('ab');
SELECT @module_ab:=last_insert_id();

-- Init Countries table
INSERT INTO ab_countries_tbl (status) values (0);
SELECT @russia_id:=last_insert_id();

INSERT INTO ab_countries_tbl (status) values (0);
SELECT @usa_id:=last_insert_id();

INSERT INTO ab_countries_tbl (status) values (0);
SELECT @ukraine_id:=last_insert_id();

INSERT INTO ab_country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Соединеные Штаты Америки', 'США', @usa_id, @ru_id);
INSERT INTO ab_country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('United States of America', 'USA', @usa_id, @en_id);
INSERT INTO ab_country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Россия', 'РФ', @russia_id, @ru_id);
INSERT INTO ab_country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Russia', 'RU', @russia_id, @en_id);
INSERT INTO ab_country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Украина', 'УК', @ukraine_id, @ru_id);
INSERT INTO ab_country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Ukraine', 'RU', @ukraine_id, @en_id);

-- Init Regions table
INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @ukraine_id);
SELECT @region_harkovschina_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_harkovschina_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Харьковская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
VALUES (@region_harkovschina_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

-- Init Harkov region towns
INSERT INTO ab_towns_tbl (id, status, region_id) VALUES (1, 0, @region_harkovschina_id);
SELECT @town_id:=1;
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Харьков', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

-- Init Identity types
INSERT INTO ab_identity_types_tbl (status, type_enum) VALUES (0, 1);
SELECT @identity_type_fio_id:=last_insert_id();
INSERT INTO ab_identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('ФИО', @ru_id, @identity_type_fio_id);
INSERT INTO ab_identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('FIO', @en_id, @identity_type_fio_id);

INSERT INTO ab_identity_types_tbl (status, type_enum) VALUES (0, 2);
SELECT @identity_type_passport_id:=last_insert_id();
INSERT INTO ab_identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Паспорт', @ru_id, @identity_type_passport_id);
INSERT INTO ab_identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Passport', @en_id, @identity_type_passport_id);

INSERT INTO ab_identity_types_tbl (status, type_enum) VALUES (0, 3);
SELECT @identity_type_foreign_passport_id:=last_insert_id();
INSERT INTO ab_identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Заграничный паспорт', @ru_id, @identity_type_foreign_passport_id);
INSERT INTO ab_identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('ForeignPassport', @en_id, @identity_type_foreign_passport_id);
