
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

INSERT INTO countries_tbl (status) values (0);
SELECT @ukraine_id:=last_insert_id();

INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Соединеные Штаты Америки', 'США', @usa_id, @ru_id);
INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('United States of America', 'USA', @usa_id, @en_id);
INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Россия', 'РФ', @russia_id, @ru_id);
INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Russia', 'RU', @russia_id, @en_id);
INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Украина', 'УК', @ukraine_id, @ru_id);
INSERT INTO country_name_translations_tbl (name, short_name, country_id, language_id)
	VALUES ('Ukraine', 'RU', @ukraine_id, @en_id);

-- Init Town Types table
INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_town_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Город', 'г', @ru_id, @town_type_town_id);
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Town', 'twn', @en_id, @town_type_town_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_village_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Село', 'с', @ru_id, @town_type_village_id);
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Village', 'vil', @en_id, @town_type_village_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_settlement_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Поселок', 'пос', @ru_id, @town_type_settlement_id);
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Settlement', 'stl', @en_id, @town_type_settlement_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_urban_settlement_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Поселок городского типа', 'пгт', @ru_id, @town_type_urban_settlement_id);
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Urban settlement', 'ustl', @en_id, @town_type_urban_settlement_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_industrial_settlement_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Рабочий поселок', 'р.пос', @ru_id, @town_type_industrial_settlement_id);
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Industrial settlement', 'i.stl', @en_id, @town_type_industrial_settlement_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_aul_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Аул', 'аул', @ru_id, @town_type_aul_id);
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Aul', 'aul', @en_id, @town_type_aul_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_stanitsa_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Станица', 'стц', @ru_id, @town_type_stanitsa_id);
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Stanitsa', 'stn', @en_id, @town_type_stanitsa_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_isolated_farmstead_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Хутор', 'хут', @ru_id, @town_type_isolated_farmstead_id);
INSERT INTO town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Isolated farmstead', 'is.fr', @en_id, @town_type_isolated_farmstead_id);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_street:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Улица', 'ул', @ru_id, @street_type_street);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Street', 'st',  @en_id, @street_type_street);

INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_street, 0x04, 'ул', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Проспект', 'пр', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Avenue', 'av', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'просп', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Виадук', 'вдк', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Viaduct', 'vdt', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'в-д', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Переулок', 'пер', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Lane', 'ln', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'пер', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Набережная', 'наб', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Embankment', 'emb', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'наб', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Проезд', 'пр', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Passage', 'pas', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'пр-д', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Площадь', 'пл', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Square', 'sq', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'пл', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Шоссе', 'шос', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Highway', 'hig' @en_id, @street_type_id);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Бульвар', 'б-р', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Boulevard', 'blv', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'б-р', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Спуск', 'сп', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Slope', 'sl', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'сп', NULL);

INSERT INTO street_types_tbl (status) VALUES (0);
SELECT @street_type_id:=LAST_INSERT_ID();
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Поселок', 'пос', @ru_id, @street_type_id);
INSERT INTO street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Settlement', 'stl', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x04, 'пос', NULL);

-- Init Regions table
INSERT INTO regions_tbl (status, country_id) VALUES (0, @ukraine_id);
SELECT @region_harkovschina_id:=last_insert_id();
INSERT INTO region_names_tbl (region_id) VALUES (@region_harkovschina_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Харьковская область', @region_name_id, @ru_id);
INSERT INTO region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
VALUES (@region_harkovschina_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

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

-- Init Harkov region towns
INSERT INTO towns_tbl (status, region_id) VALUES (0, @region_harkovschina_id);
SELECT @town_id:=last_insert_id();
INSERT INTO town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Харьков', @town_name_id, @ru_id);
INSERT INTO town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

-- Init Adygeya republic towns
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

-- Init Districts
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

-- Init Streets
INSERT INTO streets_tbl (status, town_id) values (0, @town_novosibirsk_id);
SELECT @street_id_demakova:=last_insert_id();
INSERT INTO street_names_tbl (street_id) VALUES (@street_id_demakova);
SELECT @street_name_id:=last_insert_id();
INSERT INTO street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Демакова', @street_name_id, @ru_id);
INSERT INTO street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_demakova, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_demakova, @street_type_street, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_demakova, @district_id_nsk_sovetskiy);

INSERT INTO streets_tbl (status, town_id) values (0, @town_novosibirsk_id);
SELECT @street_id_ivanova:=last_insert_id();
INSERT INTO street_names_tbl (street_id) VALUES (@street_id_ivanova);
SELECT @street_name_id:=last_insert_id();
INSERT INTO street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Иванова', @street_name_id, @ru_id);
INSERT INTO street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_ivanova, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_ivanova, @street_type_street, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_ivanova, @district_id_nsk_sovetskiy);

INSERT INTO streets_tbl (status, town_id) values (0, @town_novosibirsk_id);
SELECT @street_id_rossiiskaya:=last_insert_id();
INSERT INTO street_names_tbl (street_id) VALUES (@street_id_rossiiskaya);
SELECT @street_name_id:=last_insert_id();
INSERT INTO street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Российская', @street_name_id, @ru_id);
INSERT INTO street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_rossiiskaya, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_rossiiskaya, @street_type_street, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_rossiiskaya, @district_id_nsk_sovetskiy);

INSERT INTO streets_tbl (status, town_id) values (0, @town_novosibirsk_id);
SELECT @street_id_krasniy:=last_insert_id();
INSERT INTO street_names_tbl (street_id) VALUES (@street_id_krasniy);
SELECT @street_name_id:=last_insert_id();
INSERT INTO street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Красный проспект', @street_name_id, @ru_id);
INSERT INTO street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_krasniy, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_krasniy, @street_type_street, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_krasniy, @district_id_nsk_centralniy);
INSERT INTO streets_districts_tbl (street_id, district_id) VALUES (@street_id_krasniy, @district_id_nsk_zaelcovskiy);

-- Init Identity types
INSERT INTO identity_types_tbl (status, type_enum) VALUES (0, 1);
SELECT @identity_type_passport_id:=last_insert_id();
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Паспорт', @ru_id, @identity_type_passport_id);
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Passport', @en_id, @identity_type_passport_id);

INSERT INTO identity_types_tbl (status, type_enum) VALUES (0, 2);
SELECT @identity_type_foreign_passport_id:=last_insert_id();
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Заграничный паспорт', @ru_id, @identity_type_foreign_passport_id);
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('ForeignPassport', @en_id, @identity_type_foreign_passport_id);

-- Init Persons
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

-- Init Buildings attribute types
INSERT INTO building_attribute_types_tbl (type) VALUES (1);
SELECT @attr_type_home_number_id:=last_insert_id();
INSERT INTO building_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Номер дома', 'д', @attr_type_home_number_id, @ru_id);
INSERT INTO building_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Home number', '', @attr_type_home_number_id, @en_id);

INSERT INTO building_attribute_types_tbl (type) VALUES (2);
SELECT @attr_type_bulk_id:=last_insert_id();
INSERT INTO building_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Корпус', 'к', @attr_type_bulk_id, @ru_id);
INSERT INTO building_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Bulk', '', @attr_type_bulk_id, @en_id);

-- Init Buildings
INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('2', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('3', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('4', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('4а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('4б', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('5', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('6', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('7', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('8', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('9', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('10', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('11', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('11а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('13', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('14', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('14а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('15', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('16', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('17', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('18', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('20', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('22', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('24', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('26', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('26а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_ivanova_27_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_ivanova_27_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('27', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('27а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('28', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('28а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('29', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('30', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('30а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31', @attr_type_home_number_id, @buildings_id);
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('2', @attr_type_bulk_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31', @attr_type_home_number_id, @buildings_id);
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('5', @attr_type_bulk_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31/3', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31/4', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('31/5', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('32', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('32а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('33', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('33а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('33б', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('34', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('35', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('35а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('36', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('37', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('38', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('39', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('40', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('41', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('42', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('43', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('43', @attr_type_home_number_id, @buildings_id);
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('2', @attr_type_bulk_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('44', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('45', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('47', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('49', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('49а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('53', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('13', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('13а', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('14', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('15', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('16', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('17', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('18', @attr_type_home_number_id, @buildings_id);

INSERT INTO buildings_tbl (building_type, district_id) VALUES ('ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO buildingses_tbl (status, street_id, building_id)
	VALUES (0, @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO building_attributes_tbl (value, attribute_type_id, buildings_id)
	VALUES ('19', @attr_type_home_number_id, @buildings_id);

-- Init Apartments for Novosibirsk, Ivanova st., 27
INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '1', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '2', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '3', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '4', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '5', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '6', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '7', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '8', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '9', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '10', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '11', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '12', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '13', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '14', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '15', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '16', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '17', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '18', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '19', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '20', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '21', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '22', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '23', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '24', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '25', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '26', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '27', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '28', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '29', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '30', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '31', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '32', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '33', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '34', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '35', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '36', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '37', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '38', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '39', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '40', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '41', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '42', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '43', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '44', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '45', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '46', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '47', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '48', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '49', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '50', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '51', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '52', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '53', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '54', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '55', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '56', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '57', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '58', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '59', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '60', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '61', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '62', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '63', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '64', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '65', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '66', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '67', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '68', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '69', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '70', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '71', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '72', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '73', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '74', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '75', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '76', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '77', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '78', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '79', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '80', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '81', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '82', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '83', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '84', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '85', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '86', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '87', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '88', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '89', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '90', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '91', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '92', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '93', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '94', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '95', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '96', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '97', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '98', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '99', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '100', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '101', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '102', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '103', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '104', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '105', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '106', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '107', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '108', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '109', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '110', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '111', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '112', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '113', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '114', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '115', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '116', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '117', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '118', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '119', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '120', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '121', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '122', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '123', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '124', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '125', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '126', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '127', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '128', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '129', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '130', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '131', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '132', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '133', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '134', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '135', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '136', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '137', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '138', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '139', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '140', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '141', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '142', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '143', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '144', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '145', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '146', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '147', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '148', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '149', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '150', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '151', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '152', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '153', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '154', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '155', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '156', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '157', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '158', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '159', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '160', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '161', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '162', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '163', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '164', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '165', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '166', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '167', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '168', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '169', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '170', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '171', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '172', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '173', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '174', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '175', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '176', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '177', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '178', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '179', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '180', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '181', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '182', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '183', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '184', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '185', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '186', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '187', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '188', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '189', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '190', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '191', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '192', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '193', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '194', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '195', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '196', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '197', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '198', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '199', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '200', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '201', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '202', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '203', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '204', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '205', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '206', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '207', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '208', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '209', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '210', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '211', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '212', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '213', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '214', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '215', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '216', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '217', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '218', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '219', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '220', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '221', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '222', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '223', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '224', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '225', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '226', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '227', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '228', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '229', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '230', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '231', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '232', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '233', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '234', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '235', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '236', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '237', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '238', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '239', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '240', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '241', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '242', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '243', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '244', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '245', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '246', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '247', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '248', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '249', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '250', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '251', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '252', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '253', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '254', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '255', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '256', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '257', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '258', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '259', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '260', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '261', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '262', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '263', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '264', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '265', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '266', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '267', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '268', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '269', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '270', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '271', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '272', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '273', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '274', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '275', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '276', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '277', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '278', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '279', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '280', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '281', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '282', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '283', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '284', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '285', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '286', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '287', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '288', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '289', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '290', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '291', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '292', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '293', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '294', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '295', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '296', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '297', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '298', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '299', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '300', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '301', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '302', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '303', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '304', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '305', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '306', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '307', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '308', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '309', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '310', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '311', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '312', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '313', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '314', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '315', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '316', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '317', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '318', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '319', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '320', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '321', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '322', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '323', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '324', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '325', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '326', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '327', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '328', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '329', @apartment_id);

INSERT INTO apartments_tbl (status, building_id) VALUES (0, @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '330', @apartment_id);
	
-- Init Sz file types
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (1, '(t|T)(a|A)(R|R)(i|I)(f|F)\\u002E(d|D)(b|B)(f|F)', 'sz.file_type.tarif');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (2, '\\d{8}\\u002E(a|A)\\d{2}', 'sz.file_type.characteristics');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (3, '\\d{8}\\u002E(b|B)\\d{2}', 'sz.file_type.srv_types');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (4, '\\d{8}\\u002E(e|E)\\d{2}', 'sz.file_type.form2');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (5, '\\d{8}\\u002E(c|C)\\d{2}', 'sz.file_type.characteristics_response');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (6,'\\d{8}\\u002E(d|D)\\d{2}', 'sz.file_type.srv_types_response');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (7,'.*\\d{4}\\u002E(d|D)(b|B)(f|F)', 'sz.file_type.subsidy');
INSERT INTO sz_file_types_tbl (file_mask, description)
	VALUES ('\\d{8}\\u002E(a|A)\\d{2}', 'sz.file_type.subsidy');

-- Init Sz file status
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (1, 'sz.file_status.imported');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (2, 'sz.file_status.marked_for_processing');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (3, 'sz.file_status.processing');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (4, 'sz.file_status.processed');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (5, 'sz.file_status.processed_with_warnings');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (6, 'sz.file_status.marked_as_deleted');

-- Init Sz file actuality status
INSERT INTO sz_file_actuality_status_tbl (id, description)
	VALUES (1, 'sz.file_actuality_status.not_actualy');
INSERT INTO sz_file_actuality_status_tbl (id, description)
	VALUES (2, 'sz.file_actuality_status.actualy');	

-- Init OSZN orgs
insert into oszn_tbl (id, district_id,description) values (1, 1,'Заельцовский');
insert into oszn_tbl (id, district_id,description) values (2, 2,'Дзержинский');
insert into oszn_tbl (id, description, district_id) values (3, 'Железнодорожный', 3);
insert into oszn_tbl (id, description, district_id) values (4, 'Калининский', 4);
insert into oszn_tbl (id, description, district_id) values (5, 'Кировский', 5);
insert into oszn_tbl (id, description, district_id) values (6, 'Лениниский', 6);
insert into oszn_tbl (id, description, district_id) values (7, 'Октябрьский', 7);
insert into oszn_tbl (id, description, district_id) values (8, 'Первомайский', 8);
insert into oszn_tbl (id, description, district_id) values (9, 'Советский', 9);
insert into oszn_tbl (id, description, district_id) values (10, 'Центральный', 10);
insert into oszn_tbl (id, description, district_id) values (11, 'Дёмский', 11);
insert into oszn_tbl (id, description, district_id) values (12, 'Калиниский', 12);
insert into oszn_tbl (id, description, district_id) values (13, 'Кировский', 13);
insert into oszn_tbl (id, description, district_id) values (14, 'Лениниский', 14);
insert into oszn_tbl (id, description, district_id) values (15, 'Октябрьский', 15);
insert into oszn_tbl (id, description, district_id) values (16, 'Орджоникидзевский', 16);
insert into oszn_tbl (id, description, district_id) values (17, 'Советский', 17);

insert into common_data_source_descriptions_tbl (id, description)
	values (1, 'Харьковский центр начислений');

-- Init Sequences table
INSERT INTO common_sequences_tbl (id, counter, description) VALUES (1, 10, 'Последовательность для ЛС модуля ЕИРЦ');	

-- Init personal account record types
INSERT INTO eirc_account_record_types_tbl (type_enum_id, description)
	VALUES (0, 'Ошибочный');
INSERT INTO eirc_account_record_types_tbl (type_enum_id, description)
	VALUES (1, 'Входящая оплата');
INSERT INTO eirc_account_record_types_tbl (type_enum_id, description)
	VALUES (2, 'Исходящая оплата');
INSERT INTO eirc_account_record_types_tbl (type_enum_id, description)
	VALUES (3, 'Сальдо');

-- Init service providers registry types
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (1, 1);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (2, 2);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (3, 3);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (4, 4);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (5, 5);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (6, 6);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (7, 7);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (8, 8);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (9, 9);
INSERT INTO eirc_sp_registry_types_tbl (id, code) VALUES (10, 10);

-- Init organisations
INSERT INTO eirc_organisations_tbl (status, individual_tax_number, kpp, description, name, district_id, unique_id)
	VALUES (0, '-------', '123', 'Eirc itself', 'EIRC', @district_id_nsk_sovetskiy, '1');
SELECT @organisation_eirc:=last_insert_id();
INSERT INTO eirc_organisations_tbl (status, individual_tax_number, kpp, description, name, district_id, unique_id)
	VALUES (0, '123123123', '123', 'Test organisation', 'ЖКО', @district_id_nsk_sovetskiy, '2');
SELECT @organisation_zhko:=last_insert_id();
INSERT INTO eirc_organisations_tbl (status, individual_tax_number, kpp, description, name, district_id, unique_id)
	VALUES (0, '456456456', '56', 'Test organisation 2', 'ТСЖ', @district_id_nsk_sovetskiy, '3');
SELECT @organisation_tszh:=last_insert_id();
INSERT INTO eirc_organisations_tbl (status, individual_tax_number, kpp, description, name, district_id, unique_id)
	VALUES (0, '1111111', '56', 'Calculation center', 'CN', @district_id_nsk_sovetskiy, '10');
SELECT @organisation_cn:=last_insert_id();

-- Init service providers
INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('Источник - ЖКО Советского района');
SELECT @source_description_id:=last_insert_id();
INSERT INTO eirc_service_providers_tbl(organisation_id, data_source_description_id, description, provider_number)
	VALUES (@organisation_zhko, @source_description_id, 'ПУ ЖКО', '1');
SELECT @service_provider_zhko:=last_insert_id();

INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('Источник - ТСЖ Советского района');
SELECT @source_description_id:=last_insert_id();
INSERT INTO eirc_service_providers_tbl(organisation_id, data_source_description_id, description, provider_number)
	VALUES (@organisation_tszh, @source_description_id, 'ПУ тсж', '2');
SELECT @service_provider_tszh:=last_insert_id();

INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('Источник - Тестовые данные ПУ из ЦН');
SELECT @source_description_id:=last_insert_id();
INSERT INTO eirc_service_providers_tbl(organisation_id, data_source_description_id, description, provider_number)
	VALUES (@organisation_cn, @source_description_id, 'ПУ тсж', '10');
SELECT @service_provider_cn:=last_insert_id();

-- Init service types
INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 1);
SELECT @service_vodootvedenie:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Водоотведение', '', @ru_id, @service_vodootvedenie);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 2);
SELECT @service_dogs:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание собак', '', @ru_id, @service_dogs);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 3);
SELECT @service_garage:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', '', @ru_id, @service_garage);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 4);
SELECT @service_heating:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Отопление', '', @ru_id, @service_heating);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 5);
SELECT @service_water_cooling:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Подогрев воды', '', @ru_id, @service_water_cooling);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 6);
SELECT @service_cold_water:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Холодная вода', '', @ru_id, @service_cold_water);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 7);
SELECT @service_hot_water:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Горячая вода', '', @ru_id, @service_hot_water);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 8);
SELECT @service_cleaning:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка территории', '', @ru_id, @service_cleaning);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 9);
SELECT @service_cleaning_ext:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Уборка валов, тех.этажей, крыш', '', @ru_id, @service_cleaning_ext);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 10);
SELECT @service_TBO:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Вывоз и утилизация ТБО', '', @ru_id, @service_TBO);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 11);
SELECT @service_maintainance_water_supply:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем водоснабжения', '', @ru_id, @service_maintainance_water_supply);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 12);
SELECT @service_maintainance_vodootvedenie:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем водоотведения', '', @ru_id, @service_maintainance_vodootvedenie);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 13);
SELECT @service_maintainance:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('ТО систем', '', @ru_id, @service_maintainance);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 14);
SELECT @service_fog_canals:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Обслуживание дымоотв. каналов', '', @ru_id, @service_fog_canals);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 15);
SELECT @service_lighting:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Освещение мест общего пльзования', '', @ru_id, @service_lighting);

-- Init services
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_vodootvedenie, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_vodootvedenie, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_vodootvedenie, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_lighting, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_lighting, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_lighting, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_fog_canals, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_fog_canals, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_fog_canals, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_maintainance, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_maintainance, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_maintainance, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_maintainance_vodootvedenie, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_maintainance_vodootvedenie, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_maintainance_vodootvedenie, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_maintainance_water_supply, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_maintainance_water_supply, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_maintainance_water_supply, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_TBO, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_TBO, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_TBO, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_cleaning_ext, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_cleaning_ext, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_cleaning_ext, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_cleaning, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_cleaning, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_cleaning, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_hot_water, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_hot_water, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_hot_water, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_cold_water, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_cold_water, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_cold_water, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_dogs, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_dogs, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_dogs, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_garage, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_garage, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_garage, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_heating, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_heating, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_heating, '');

INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_zhko, @service_water_cooling, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_tszh, @service_water_cooling, '');
INSERT INTO eirc_services_tbl (provider_id, type_id, description) VALUES (@service_provider_cn, @service_water_cooling, '');

-- Init SpRegistryStatuses
INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (0);
SELECT @sp_registry_status_loading:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (1);
SELECT @sp_registry_status_loaded:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (2);
SELECT @sp_registry_status_loading_canceled:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (3);
SELECT @sp_registry_status_loaded_with_error:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (4);
SELECT @sp_registry_status_processing:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (5);
SELECT @sp_registry_status_processing_with_error:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (6);
SELECT @sp_registry_status_processed:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (7);
SELECT @sp_registry_status_processed_with_error:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (8);
SELECT @sp_registry_status_processing_canceled:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (9);
SELECT @sp_registry_status_rollbacking:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (10);
SELECT @sp_registry_status_rollbacked:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (11);
SELECT @sp_registry_status_creating:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (12);
SELECT @sp_registry_status_created:=last_insert_id();

INSERT INTO eirc_sp_registry_statuses_tbl (code) VALUES (13);
SELECT @sp_registry_status_creating_canceled:=last_insert_id();

-- Init SpRegistryArchiveStatuses
INSERT INTO eirc_sp_registry_archive_statuses_tbl (code) VALUES (0);
SELECT @sp_registry_archive_status_none:=last_insert_id();

INSERT INTO eirc_sp_registry_archive_statuses_tbl (code) VALUES (1);
SELECT @sp_registry_archive_status_archiving:=last_insert_id();

INSERT INTO eirc_sp_registry_archive_statuses_tbl (code) VALUES (2);
SELECT @sp_registry_archive_status_archived:=last_insert_id();

INSERT INTO eirc_sp_registry_archive_statuses_tbl (code) VALUES (3);
SELECT @sp_registry_archive_status_canceled:=last_insert_id();
