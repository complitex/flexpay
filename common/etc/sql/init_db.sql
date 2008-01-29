
-- Init Languages table
INSERT INTO languages_tbl (is_default, status, lang_iso_code) values (1, 0, 'ru');
SELECT @ru_id:=last_insert_id();

INSERT INTO languages_tbl (is_default, status, lang_iso_code) values (0, 0, 'en');
SELECT @en_id:=last_insert_id();

INSERT INTO language_names_tbl (translation, translation_from_language_id, language_id)
 	VALUES ('Русский', @ru_id, @ru_id);
INSERT INTO language_names_tbl (translation, translation_from_language_id, language_id)
 	VALUES ('Английский', @ru_id, @en_id);
INSERT INTO language_names_tbl (translation, translation_from_language_id, language_id)
 	VALUES ('Russia', @en_id, @ru_id);
INSERT INTO language_names_tbl (translation, translation_from_language_id, language_id)
 	VALUES ('English', @en_id, @en_id);

-- Init Countries table
INSERT INTO countries_tbl (status) values (0);
SELECT @russia_id:=last_insert_id();

INSERT INTO countries_tbl (status) values (0);
SELECT @usa_id:=last_insert_id();

INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Соединеные Штаты Америки', 'США', @usa_id, @ru_id);
INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('United States of America', 'USA', @usa_id, @en_id);
INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Россия', 'РФ', @russia_id, @ru_id);
INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Russia', 'RU', @russia_id, @en_id);

-- Init Town Types table
INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_town_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Город', @ru_id, @town_type_town_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Town', @en_id, @town_type_town_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_village_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Село', @ru_id, @town_type_village_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Village', @en_id, @town_type_village_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_settlement_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Поселок', @ru_id, @town_type_settlement_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Settlement', @en_id, @town_type_settlement_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_urban_settlement_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Поселок городского типа', @ru_id, @town_type_urban_settlement_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Urban settlement', @en_id, @town_type_urban_settlement_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_industrial_settlement_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Рабочий поселок', @ru_id, @town_type_industrial_settlement_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Industrial settlement', @en_id, @town_type_industrial_settlement_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_aul_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Аул', @ru_id, @town_type_aul_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Aul', @en_id, @town_type_aul_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_stanitsa_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Станица', @ru_id, @town_type_stanitsa_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Stanitsa', @en_id, @town_type_stanitsa_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_isolated_farmstead_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Хутор', @ru_id, @town_type_isolated_farmstead_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Isolated farmstead', @en_id, @town_type_isolated_farmstead_id);

-- Init Regions table
INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_adygeya_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id) VALUES (@region_adygeya_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Адыгея', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
VALUES (@region_adygeya_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_bashkortostan_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id) VALUES (@region_bashkortostan_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Башкортостан', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_bashkortostan_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id) VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Бурятия', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Алтай', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Дагестан', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Ингушетия', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Кабардино-Балкария', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Калмыкия', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Карачаево-Черкесия', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Карелия', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Коми', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Марий Эл', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Мордовия', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Саха(Якутия)', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Северная Осетия', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Татарстан', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Тыва (Тува)', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Удмуртская Республика', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Хакасия', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Чеченская Республика', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Чувашская Республика', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Алтайский край', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Краснодарский край', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Красноярский край', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Приморский край', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ставропольский край', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Хабаровский край', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Амурская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Архангельская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Астраханская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Белгородская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Брянская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Владимирская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Волгоградская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Вологодская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Воронежская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ивановская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Иркутская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Калининградская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Калужская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Камчатская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Кемеровская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Кировская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Костромская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Курганская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Курская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ленинградская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Липецкая область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Магаданская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Московская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Мурманская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Нижегородская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Новгородская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_novosibirskaya_obl_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id) VALUES (@region_novosibirskaya_obl_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Новосибирская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_novosibirskaya_obl_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id) VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Омская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id) VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Оренбургская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Орловская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Пензенская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Пермский край', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Псковская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ростовская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Рязанская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Самарская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Саратовская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Сахалинская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Свердловская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Смоленская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Тамбовская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Тверская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Томская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Тульская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Тюменская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ульяновская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Челябинская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Читинская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ярославская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Москва', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Санкт-Петербург', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Еврейская автономная область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Агинский Бурятский АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Коми-Пермяцкий АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Корякский АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ненецкий АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Таймырский (Долгано-Ненецкий) АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Усть-Ордынский Бурятский АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ханты-Мансийский АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Чукотский АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Эвенкийский АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ямало-Ненецкий АО', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

-- Adygeya republic towns
INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Майкоп', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Ханская', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Адыгейск', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Гиагинская', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Дондуковская', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Кошехабль', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Блечепсин', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Вольное', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Натырбово', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Ходзь', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Красногвардейское', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Белое', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Хатукай', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Тульский', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Каменномостский', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Абадзехская', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Краснооктябрьский', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Кужорская', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Северо-Восточные Сады', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_isolated_farmstead_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Тахтамукай', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Энем', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Яблоновский', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Понежукай', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Тлюстенхабль', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Хакуринохабль', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_novosibirsk_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_novosibirsk_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Новосибирск', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_novosibirsk_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Бердск', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Искитим', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Кольцово', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_industrial_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Обь', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Маслянино', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Черепаново', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Сузун', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Ордынск', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_bashkortostan_id);
SELECT @town_ufa_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_ufa_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Уфа', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_ufa_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

-- Districts
INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id_nsk_zaelcovskiy:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id_nsk_zaelcovskiy);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Заельцовский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id_nsk_zaelcovskiy, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Дзержинский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Железнодорожный', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Калининский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Кировский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Лениниский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Октябрьский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Первомайский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id_nsk_sovetskiy:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Советский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id_nsk_sovetskiy, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id_nsk_centralniy:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id_nsk_centralniy);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Центральный', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id_nsk_centralniy, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Дёмский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Калиниский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Кировский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Лениниский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Октябрьский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Орджоникидзевский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Советский', @district_name_id, @ru_id);
INSERT INTO district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

-- Streets
INSERT INTO streets_tbl (status, town_id) values (0, @town_novosibirsk_id);
SELECT @street_id_demakova:=last_insert_id();
INSERT INTO street_names_tbl (street_id) VALUES (@street_id_demakova);
SELECT @street_name_id:=last_insert_id();
INSERT INTO street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Демакова', @street_name_id, @ru_id);
INSERT INTO street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_demakova, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_demakova, @district_id_nsk_sovetskiy);

INSERT INTO streets_tbl (status, town_id) values (0, @town_novosibirsk_id);
SELECT @street_id_ivanova:=last_insert_id();
INSERT INTO street_names_tbl (street_id) VALUES (@street_id_ivanova);
SELECT @street_name_id:=last_insert_id();
INSERT INTO street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Иванова', @street_name_id, @ru_id);
INSERT INTO street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_ivanova, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_ivanova, @district_id_nsk_sovetskiy);

INSERT INTO streets_tbl (status, town_id) values (0, @town_novosibirsk_id);
SELECT @street_id_rossiiskaya:=last_insert_id();
INSERT INTO street_names_tbl (street_id) VALUES (@street_id_rossiiskaya);
SELECT @street_name_id:=last_insert_id();
INSERT INTO street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Российская', @street_name_id, @ru_id);
INSERT INTO street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_rossiiskaya, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_rossiiskaya, @district_id_nsk_sovetskiy);

INSERT INTO streets_tbl (status, town_id) values (0, @town_novosibirsk_id);
SELECT @street_id_krasniy:=last_insert_id();
INSERT INTO street_names_tbl (street_id) VALUES (@street_id_krasniy);
SELECT @street_name_id:=last_insert_id();
INSERT INTO street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Красный проспект', @street_name_id, @ru_id);
INSERT INTO street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_krasniy, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_krasniy, @district_id_nsk_centralniy);
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_krasniy, @district_id_nsk_zaelcovskiy);

-- Identity types
INSERT INTO identity_types_tbl (status) VALUES (0);
SELECT @identity_type_passport_id:=last_insert_id();
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Паспорт', @ru_id, @identity_type_passport_id);
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Pasport', @en_id, @identity_type_passport_id);

INSERT INTO identity_types_tbl (status) VALUES (0);
SELECT @identity_type_foreign_passport_id:=last_insert_id();
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Заграничный паспорт', @ru_id, @identity_type_foreign_passport_id);
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('ForeignPasport', @en_id, @identity_type_foreign_passport_id);

-- Persons
INSERT INTO persons_tbl (status) VALUES (0);
SELECT @person_id:=last_insert_id();
INSERT INTO person_identities_tbl (begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES ('2003-06-09', '2100-12-31', '1983-01-25', 5003,
	1231231, 'Михаил', 'Анатольевич', 'Федько', 'ОВД Советского района города Новосибирска',
	1, @identity_type_passport_id, @person_id);
INSERT INTO person_identities_tbl (begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES ('2004-10-22', '2009-10-22', '1983-01-25', 60,
	123123123, 'Mikhail', '', 'Fedko', 'ГУВД 316',
	0, @identity_type_foreign_passport_id, @person_id);

-- Buildings attribute types
INSERT INTO building_attribute_types_tbl () VALUES ();
SELECT @attr_type_home_number_id:=last_insert_id();
INSERT INTO building_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Номер дома', 'д', @attr_type_home_number_id, @ru_id);
INSERT INTO building_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Home number', '', @attr_type_home_number_id, @en_id);

INSERT INTO building_attribute_types_tbl () VALUES ();
SELECT @attr_type_bulk_id:=last_insert_id();
INSERT INTO building_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Корпус', 'к', @attr_type_bulk_id, @ru_id);
INSERT INTO building_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Bulk', '', @attr_type_bulk_id, @en_id);

-- Buildings
INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('2', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('3', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('4', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('4а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('4б', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('5', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('6', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('7', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('8', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('9', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('10', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('11', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('11а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('13', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('14', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('14а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('15', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('16', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('17', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('18', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('20', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('22', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('24', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('26', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('26а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('27', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('27а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('28', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('28а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('29', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('30', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('30а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31', @attr_type_home_number_id, @buildings_id);
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('2', @attr_type_bulk_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31', @attr_type_home_number_id, @buildings_id);
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('5', @attr_type_bulk_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31/3', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31/4', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31/5', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('32', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('32а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('33', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('33а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('33б', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('34', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('35', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('35а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('36', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('37', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('38', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('39', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('40', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('41', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('42', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('43', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('43', @attr_type_home_number_id, @buildings_id);
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('2', @attr_type_bulk_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('44', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('45', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('47', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('49', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('49а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('53', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @district_id_nsk_sovetskiy);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('13', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @district_id_nsk_sovetskiy);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('13а', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @district_id_nsk_sovetskiy);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('14', @attr_type_home_number_id, @buildings_id);


INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @district_id_nsk_sovetskiy);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('15', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @district_id_nsk_sovetskiy);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('16', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @district_id_nsk_sovetskiy);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('17', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @district_id_nsk_sovetskiy);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('18', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @district_id_nsk_sovetskiy);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('19', @attr_type_home_number_id, @buildings_id);

