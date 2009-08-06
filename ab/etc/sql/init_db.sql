INSERT INTO common_flexpay_modules_tbl (name) VALUES ('ab');
SELECT @module_ab:=last_insert_id();

-- Init Countries table
INSERT INTO ab_countries_tbl (id, status) values (1, 0);
SELECT @russia_id:=1;

INSERT INTO ab_countries_tbl (id, status) values (2, 0);
SELECT @usa_id:=2;

INSERT INTO ab_countries_tbl (id, status) values (3, 0);
SELECT @ukraine_id:=3;

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
	VALUES ('Ukraine', 'UA', @ukraine_id, @en_id);

-- Init Regions table
INSERT INTO ab_regions_tbl (id, status, country_id) VALUES (1, 0, @ukraine_id);
SELECT @region_harkovschina_id:=1;
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_harkovschina_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Харьковская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
VALUES (@region_harkovschina_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');
