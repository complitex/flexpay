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

-- Init Town Types table
INSERT INTO ab_town_types_tbl (id, status) VALUES (1, 0);
SELECT @town_type_town_id:=1;
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Город', 'г', @ru_id, @town_type_town_id);
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Town', 'twn', @en_id, @town_type_town_id);

INSERT INTO ab_town_types_tbl (id, status) VALUES (2, 0);
SELECT @town_type_village_id:=2;
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Село', 'с', @ru_id, @town_type_village_id);
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Village', 'vil', @en_id, @town_type_village_id);

INSERT INTO ab_town_types_tbl (id, status) VALUES (3, 0);
SELECT @town_type_settlement_id:=3;
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Поселок', 'пос', @ru_id, @town_type_settlement_id);
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Settlement', 'stl', @en_id, @town_type_settlement_id);

INSERT INTO ab_town_types_tbl (id, status) VALUES (4, 0);
SELECT @town_type_urban_settlement_id:=4;
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Поселок городского типа', 'пгт', @ru_id, @town_type_urban_settlement_id);
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Urban settlement', 'ustl', @en_id, @town_type_urban_settlement_id);

INSERT INTO ab_town_types_tbl (status) VALUES (0);
SELECT @town_type_industrial_settlement_id:=last_insert_id();
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Рабочий поселок', 'р.пос', @ru_id, @town_type_industrial_settlement_id);
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Industrial settlement', 'i.stl', @en_id, @town_type_industrial_settlement_id);

INSERT INTO ab_town_types_tbl (status) VALUES (0);
SELECT @town_type_aul_id:=last_insert_id();
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Аул', 'аул', @ru_id, @town_type_aul_id);
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Aul', 'aul', @en_id, @town_type_aul_id);

INSERT INTO ab_town_types_tbl (status) VALUES (0);
SELECT @town_type_stanitsa_id:=last_insert_id();
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Станица', 'стц', @ru_id, @town_type_stanitsa_id);
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Stanitsa', 'stn', @en_id, @town_type_stanitsa_id);

INSERT INTO ab_town_types_tbl (status) VALUES (0);
SELECT @town_type_isolated_farmstead_id:=last_insert_id();
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Хутор', 'хут', @ru_id, @town_type_isolated_farmstead_id);
INSERT INTO ab_town_type_translations_tbl (name, short_name, language_id, town_type_id)
	VALUES ('Isolated farmstead', 'is.fr', @en_id, @town_type_isolated_farmstead_id);

-- Init street types
INSERT INTO ab_street_types_tbl (id, status) VALUES (1, 0);
SELECT @street_type_id:=1;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Бульвар', 'б-р', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Boulevard', 'blv', @en_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (2, 0);
SELECT @street_type_id:=2;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Виадук', 'вдк', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Viaduct', 'vdt', @en_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (3, 0);
SELECT @street_type_id:=3;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Въезд', 'в-д', @ru_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (4, 0);
SELECT @street_type_id:=4;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Микрорайон', 'м-н', @ru_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (5, 0);
SELECT @street_type_id:=5;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Набережная', 'наб', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Embankment', 'emb', @en_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (6, 0);
SELECT @street_type_id:=6;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Переулок', 'пер', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Lane', 'ln', @en_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (7, 0);
SELECT @street_type_id:=7;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Площадь', 'пл', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Square', 'sq', @en_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (8, 0);
SELECT @street_type_id:=8;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Поселок', 'пос', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Settlement', 'stl', @en_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (9, 0);
SELECT @street_type_id:=9;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Проезд', 'пр-д', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Passage', 'pas', @en_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (10, 0);
SELECT @street_type_id:=10;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Проспект', 'пр', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Avenue', 'av', @en_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x1004, 'просп', NULL);

INSERT INTO ab_street_types_tbl (id, status) VALUES (11, 0);
SELECT @street_type_id:=11;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Спуск', 'сп', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Slope', 'sl', @en_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (12, 0);
SELECT @street_type_id:=12;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Станция', 'ст', @ru_id, @street_type_id);

INSERT INTO ab_street_types_tbl (id, status) VALUES (13, 0);
SELECT @street_type_id:=13;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Тупик', 'туп', @ru_id, @street_type_id);
INSERT INTO common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	VALUES (@street_type_id, 0x1004, 'т', NULL);

INSERT INTO ab_street_types_tbl (id, status) VALUES (14, 0);
SELECT @street_type_street:=14;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Улица', 'ул', @ru_id, @street_type_street);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Street', 'st',  @en_id, @street_type_street);

INSERT INTO ab_street_types_tbl (id, status) VALUES (15, 0);
SELECT @street_type_id:=15;
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Шоссе', 'шос', @ru_id, @street_type_id);
INSERT INTO ab_street_type_translations_tbl (name, short_name, language_id, street_type_id)
	VALUES ('Highway', 'hig', @en_id, @street_type_id);

-- init regions
INSERT INTO ab_regions_tbl (id, status, country_id) VALUES (1000, 0, @russia_id);
SELECT @region_novosibirskaya_obl_id:=1000;
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_novosibirskaya_obl_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Томская губерния', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_novosibirskaya_obl_id, @region_name_id, '1900-01-01', '1920-12-31', '2008-01-01', '2100-12-31');
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_novosibirskaya_obl_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Новониколаевская губерния', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_novosibirskaya_obl_id, @region_name_id, '1921-01-01', '1925-05-31', '2008-01-01', '2100-12-31');
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_novosibirskaya_obl_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Сибирский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_novosibirskaya_obl_id, @region_name_id, '1925-06-01', '1930-05-31', '2008-01-01', '2100-12-31');
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_novosibirskaya_obl_id);
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_novosibirskaya_obl_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Западно-Сибирский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_novosibirskaya_obl_id, @region_name_id, '1930-06-01', '1937-05-31', '2008-01-01', '2100-12-31');
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_novosibirskaya_obl_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Новосибирская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_novosibirskaya_obl_id, @region_name_id, '1937-06-01', '2100-12-31', '2008-01-01', '2100-12-31');
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_novosibirskaya_obl_id);

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_adygeya_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_adygeya_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Адыгея', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
VALUES (@region_adygeya_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_bashkortostan_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_bashkortostan_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Башкортостан', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_bashkortostan_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Бурятия', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Алтай', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Дагестан', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Ингушетия', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Кабардино-Балкария', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Калмыкия', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Карачаево-Черкесия', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Карелия', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Коми', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Марий Эл', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Мордовия', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Саха(Якутия)', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Северная Осетия', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Татарстан', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Тыва (Тува)', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Удмуртская Республика', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Республика Хакасия', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Чеченская Республика', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Чувашская Республика', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('алтайский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Краснодарский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Красноярский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Приморский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ставропольский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Хабаровский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('амурская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('архангельская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Астраханская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Белгородская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Брянская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Владимирская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Волгоградская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Вологодская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Воронежская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ивановская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Иркутская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Калининградская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Калужская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Камчатская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Кемеровская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Кировская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Костромская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Курганская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Курская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ленинградская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Липецкая область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Магаданская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Московская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Мурманская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Нижегородская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Новгородская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Омская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id) VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Оренбургская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Орловская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Пензенская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Пермский край', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Псковская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ростовская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Рязанская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Самарская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Саратовская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Сахалинская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Свердловская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Смоленская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Тамбовская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Тверская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Томская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Тульская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Тюменская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ульяновская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Челябинская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Читинская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ярославская область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Москва', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Санкт-Петербург', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Еврейская автономная область', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Агинский Бурятский АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Коми-Пермяцкий АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Корякский АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ненецкий АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Таймырский (Долгано-Ненецкий) АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Усть-Ордынский Бурятский АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ханты-Мансийский АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Чукотский АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Эвенкийский АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

INSERT INTO ab_regions_tbl (status, country_id) VALUES (0, @russia_id);
SELECT @region_id:=last_insert_id();
INSERT INTO ab_region_names_tbl (region_id)
	VALUES (@region_id);
SELECT @region_name_id:=last_insert_id();
INSERT INTO ab_region_name_translations_tbl (name, region_name_id, language_id)
	VALUES ('Ямало-Ненецкий АО', @region_name_id, @ru_id);
INSERT INTO ab_region_names_temporal_tbl (region_id, region_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@region_id, @region_name_id, '1900-01-01', '2100-12-31', '2008-01-01', '2100-12-31');

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

-- init Novosibirsk
INSERT INTO ab_towns_tbl (id, status, region_id) VALUES (2, 0, @region_novosibirskaya_obl_id);
SELECT @town_novosibirsk_id:=2;
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_novosibirsk_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Новониколаевский', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_novosibirsk_id, @town_name_id, '1900-01-01', '1902-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_novosibirsk_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Новониколаевск', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_novosibirsk_id, @town_name_id, '1903-01-01', '1925-05-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_novosibirsk_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Новосибирск', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_novosibirsk_id, @town_name_id, '1925-06-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_novosibirsk_id, @town_type_settlement_id, '1900-01-01', '1902-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_novosibirsk_id, @town_type_town_id, '1903-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

-- Init Adygeya republic towns
INSERT INTO ab_towns_tbl (id, status, region_id) VALUES (3, 0, @region_adygeya_id);
SELECT @town_id:=3;
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Майкоп', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '1950-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1951-01-01', '2009-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '2010-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Ханская', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Адыгейск', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Гиагинская', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Дондуковская', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Кошехабль', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Блечепсин', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Вольное', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Натырбово', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Ходзь', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Красногвардейское', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Белое', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_village_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Хатукай', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Тульский', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Каменномостский', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Абадзехская', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Краснооктябрьский', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Кужорская', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_stanitsa_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Северо-Восточные Сады', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_isolated_farmstead_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Тахтамукай', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Энем', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Яблоновский', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Понежукай', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Тлюстенхабль', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_adygeya_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Хакуринохабль', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_aul_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Бердск', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Искитим', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Кольцово', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_industrial_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Обь', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id)
	VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Маслянино', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Черепаново', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Сузун', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_novosibirskaya_obl_id);
SELECT @town_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Ордынск', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_id, @town_type_urban_settlement_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_towns_tbl (status, region_id) VALUES (0, @region_bashkortostan_id);
SELECT @town_ufa_id:=last_insert_id();
INSERT INTO ab_town_names_tbl (town_id) VALUES (@town_ufa_id);
SELECT @town_name_id:=last_insert_id();
INSERT INTO ab_town_name_translations_tbl (name, town_name_id, language_id)
	VALUES ('Уфа', @town_name_id, @ru_id);
INSERT INTO ab_town_names_temporal_tbl (town_id, town_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_ufa_id, @town_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_town_types_temporal_tbl (town_id, town_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@town_ufa_id, @town_type_town_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

-- Init Districts
INSERT INTO ab_districts_tbl (id, status, town_id) VALUES (1, 0, @town_novosibirsk_id);
SELECT @district_id_nsk_sovetskiy:=1;
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id_nsk_sovetskiy);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Советский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id_nsk_sovetskiy, @district_name_id, '1958-04-06', '2100-12-31', '2008-01-17', '2100-12-31');

-- add master correction for the first district
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@district_id_nsk_sovetskiy, 0x1000 + 0x05, concat('COMMON_INSTANCE-', @district_id_nsk_sovetskiy), @source_description_master_index);

INSERT INTO ab_districts_tbl (id, status, town_id) VALUES (2, 0, @town_novosibirsk_id);
SELECT @district_id_nsk_zaelcovskiy:=2;
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id_nsk_zaelcovskiy);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Заельцовский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id_nsk_zaelcovskiy, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (id, status, town_id) VALUES (3, 0, @town_novosibirsk_id);
SELECT @district_id:=3;
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Дзержинский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (id, status, town_id) VALUES (4, 0, @town_novosibirsk_id);
SELECT @district_id:=4;
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Вокзальная часть', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '1932-05-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Кагановический', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1932-08-01', '1957-05-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Железнодорожный', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1957-06-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Калининский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Кировский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Лениниский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Октябрьский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Первомайский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_novosibirsk_id);
SELECT @district_id_nsk_centralniy:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id_nsk_centralniy);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Центральный', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id_nsk_centralniy, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Дёмский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Калиниский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Кировский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Лениниский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Октябрьский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Орджоникидзевский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_ufa_id);
SELECT @district_id:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Советский', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

-- Init Streets
INSERT INTO ab_streets_tbl (id, status, town_id) values (1, 0, @town_novosibirsk_id);
SELECT @street_id_demakova:=1;
INSERT INTO ab_street_names_tbl (street_id) VALUES (@street_id_demakova);
SELECT @street_name_id:=last_insert_id();
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Демакова', @street_name_id, @ru_id);
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Demakova', @street_name_id, @en_id);
INSERT INTO ab_street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_demakova, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_demakova, @street_type_street, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_streets_districts_tbl (street_id, district_id) VALUES (@street_id_demakova, @district_id_nsk_sovetskiy);

INSERT INTO ab_streets_tbl (id, status, town_id) values (2, 0, @town_novosibirsk_id);
SELECT @street_id_ivanova:=2;
INSERT INTO ab_street_names_tbl (street_id) VALUES (@street_id_ivanova);
SELECT @street_name_id:=last_insert_id();
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Иванова', @street_name_id, @ru_id);
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Ivanova', @street_name_id, @en_id);
INSERT INTO ab_street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_ivanova, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_ivanova, @street_type_street, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_streets_districts_tbl (street_id, district_id) VALUES (@street_id_ivanova, @district_id_nsk_sovetskiy);

INSERT INTO ab_streets_tbl (id, status, town_id) values (3, 0, @town_novosibirsk_id);
SELECT @street_id_rossiiskaya:=3;
INSERT INTO ab_street_names_tbl (street_id) VALUES (@street_id_rossiiskaya);
SELECT @street_name_id:=last_insert_id();
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Российская', @street_name_id, @ru_id);
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Rossiiskaya', @street_name_id, @en_id);
INSERT INTO ab_street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_rossiiskaya, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_rossiiskaya, @street_type_street, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_streets_districts_tbl (street_id, district_id) VALUES (@street_id_rossiiskaya, @district_id_nsk_sovetskiy);

INSERT INTO ab_streets_tbl (id, status, town_id) values (4, 0, @town_novosibirsk_id);
SELECT @street_id_krasniy:=4;
INSERT INTO ab_street_names_tbl (street_id) VALUES (@street_id_krasniy);
SELECT @street_name_id:=last_insert_id();
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Новониколаевский проспект', @street_name_id, @ru_id);
INSERT INTO ab_street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_krasniy, @street_name_id, '1900-01-01', '1917-10-07', '2008-01-17', '2100-12-31');
INSERT INTO ab_street_names_tbl (street_id) VALUES (@street_id_krasniy);
SELECT @street_name_id:=last_insert_id();
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('красный проспект', @street_name_id, @ru_id);
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Krasnii prospekt', @street_name_id, @en_id);
INSERT INTO ab_street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_krasniy, @street_name_id, '1917-10-08', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_krasniy, @street_type_street, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_streets_districts_tbl (street_id, district_id) VALUES (@street_id_krasniy, @district_id_nsk_centralniy);
INSERT INTO ab_streets_districts_tbl (street_id, district_id) VALUES (@street_id_krasniy, @district_id_nsk_zaelcovskiy);

-- add master correction for the streets
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@street_id_demakova, 0x1000 + 0x06, concat('COMMON_INSTANCE-', @street_id_demakova), @source_description_master_index);
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@street_id_ivanova, 0x1000 + 0x06, concat('COMMON_INSTANCE-', @street_id_ivanova), @source_description_master_index);
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@street_id_rossiiskaya, 0x1000 + 0x06, concat('COMMON_INSTANCE-', @street_id_rossiiskaya), @source_description_master_index);
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@street_id_krasniy, 0x1000 + 0x06, concat('COMMON_INSTANCE-', @street_id_krasniy), @source_description_master_index);

-- Init Persons
INSERT INTO ab_persons_tbl (id, status) VALUES (1, 0);
SELECT @person_id:=1;

INSERT INTO ab_person_attributes_tbl (name, value, language_id, person_id)
	VALUES('Кол-во детей', '12', @ru_id, @person_id);
INSERT INTO ab_person_attributes_tbl (name, value, language_id, person_id)
	VALUES('Атрибут', 'значение', @ru_id, @person_id);
INSERT INTO ab_person_attributes_tbl (name, value, language_id, person_id)
	VALUES('Children number', '13', @en_id, @person_id);

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 1, '1983-01-25', '2100-12-31', '1983-01-25', '',
	'', 'Михаил', 'Анатольевич', 'Федько', '',
	0, @identity_type_fio_id, @person_id);
SELECT @person_identity_id:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 1, '2003-06-09', '2100-12-31', '1983-01-25', 5003,
	1231231, 'Михаил', 'Анатольевич', 'Федько', 'ОВД Советского района города Новосибирска',
	1, @identity_type_passport_id, @person_id);
INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 1, '2004-10-22', '2009-10-22', '1983-01-25', 60,
	123123123, 'Mikhail', '', 'Fedko', 'ГУВД 316',
	0, @identity_type_foreign_passport_id, @person_id);

INSERT INTO ab_person_identity_attributes_tbl (name, value, language_id, person_identity_id)
	VALUES ('ИНН', 'НЕТ', @ru_id, @person_identity_id);

INSERT INTO ab_person_identity_attributes_tbl (name, value, language_id, person_identity_id)
	VALUES ('INN', 'NONE', @en_id, @person_identity_id);

INSERT INTO ab_person_identity_attributes_tbl (name, value, language_id, person_identity_id)
	VALUES ('Атрибут', 'Значение', @ru_id, @person_identity_id);

-- Init Buildings attribute types
INSERT INTO ab_building_address_attribute_types_tbl (id, status) VALUES (3, 0);
SELECT @attr_type_part_id:=3;
INSERT INTO ab_building_address_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Часть', 'ч', @attr_type_part_id, @ru_id);
INSERT INTO ab_building_address_attribute_type_translations_tbl (name, short_name, attribute_type_id, language_id)
	VALUES ('Part', 'p', @attr_type_part_id, @en_id);

-- add master correction for the address attribute types
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@attr_type_home_number_id, 0x1000 + 0x0B, concat('COMMON_INSTANCE-', @attr_type_home_number_id), @source_description_master_index);
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@attr_type_bulk_id, 0x1000 + 0x0B, concat('COMMON_INSTANCE-', @attr_type_bulk_id), @source_description_master_index);
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@attr_type_part_id, 0x1000 + 0x0B, concat('COMMON_INSTANCE-', @attr_type_part_id), @source_description_master_index);



-- Init Buildings
INSERT INTO ab_buildings_tbl (id, status, building_type, district_id) VALUES (1, 0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_ivanova_27_id:=1;
INSERT INTO ab_building_addresses_tbl (id, status, primary_status, street_id, building_id)
	VALUES (1001, 0, b'1', @street_id_ivanova, @building_ivanova_27_id);
SELECT @buildings_ivanova_27:=1001;
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('27', 0, @attr_type_home_number_id, @buildings_ivanova_27);

-- add master correction for the first building
insert into common_data_corrections_tbl (internal_object_id, object_type, external_object_id, data_source_description_id)
	values (@building_ivanova_27_id, 0x1000 + 0x0A, concat('COMMON_INSTANCE-', @building_ivanova_27_id), @source_description_master_index);

INSERT INTO ab_buildings_tbl (id, status, building_type, district_id) VALUES (2, 0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=2;
INSERT INTO ab_building_addresses_tbl (id, status, primary_status, street_id, building_id)
	VALUES (1002, 0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=1002;
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('2', 0, @attr_type_home_number_id, @buildings_id);
INSERT INTO ab_building_addresses_tbl (id, status, primary_status, street_id, building_id)
	VALUES (3, 0, b'0', @street_id_demakova, @building_id);
SELECT @buildings_id:=3;
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('220D', 0, @attr_type_home_number_id, @buildings_id);
INSERT INTO ab_building_addresses_tbl (id, status, primary_status, street_id, building_id)
	VALUES (4, 0, b'0', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=4;
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('220R', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (id, status, building_type,  district_id) VALUES (3, 0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=3;
INSERT INTO ab_building_addresses_tbl (id, status, primary_status, street_id, building_id)
	VALUES (1003, 0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=1003;
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('3', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('4', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('4а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('4б', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('5', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('6', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('7', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('8', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('9', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('10', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_1:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('11', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_2:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('11а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_3:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('13', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_4:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('14', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('14а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_5:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('15', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('16', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('17', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('18', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_6:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('20', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_7:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('22', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('24', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('26', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_8:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('26а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id_9:=last_insert_id();
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('27а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('28', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('28а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('29', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('30', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('30а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('31', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('31', 0, @attr_type_home_number_id, @buildings_id);
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('2', 0, @attr_type_bulk_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('31', 0, @attr_type_home_number_id, @buildings_id);
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('5', 0, @attr_type_bulk_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('31/3', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('31/4', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('31/5', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('32', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('32а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('33', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('33а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('33б', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('34', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('35', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('35а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('36', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('37', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('38', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('39', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('40', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('41', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('42', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('43', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('43', 0, @attr_type_home_number_id, @buildings_id);
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('2', 0, @attr_type_bulk_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('44', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('45', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('47', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('49', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('49а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_ivanova, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('53', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('13', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('13а', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('14', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('15', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('16', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('17', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('18', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_buildings_tbl (status, building_type,  district_id) VALUES (0, 'ab', @district_id_nsk_sovetskiy);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_rossiiskaya, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('19', 0, @attr_type_home_number_id, @buildings_id);

-- Init Apartments for Novosibirsk, Ivanova st., 27
INSERT INTO ab_apartments_tbl (id, status, apartment_type, building_id) VALUES (1, 0, 'ab', @building_ivanova_27_id);
SELECT @apartment_ivanova_329_id:=1;
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1960-01-01', '2100-12-31', '329', @apartment_ivanova_329_id);

INSERT INTO ab_apartments_tbl (id, status, apartment_type, building_id) VALUES (2, 0, 'ab', @building_ivanova_27_id);
SELECT @apartment_ivanova_330_id:=2;
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '330', @apartment_ivanova_330_id);

INSERT INTO ab_apartments_tbl (id, status, apartment_type, building_id) VALUES (3, 0, 'ab', @building_ivanova_27_id);
SELECT @apartment_ivanova_1_id:=3;
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '1', @apartment_ivanova_1_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '2', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '3', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '4', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '5', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '6', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '7', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '8', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '9', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '10', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '11', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '12', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '13', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '14', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '15', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '16', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '17', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '18', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '19', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '20', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '21', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '22', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '23', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '24', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '25', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '26', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '27', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '28', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '29', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '30', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '31', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '32', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '33', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '34', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '35', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '36', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '37', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '38', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '39', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '40', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '41', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '42', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '43', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '44', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '45', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '46', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '47', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '48', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '49', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '50', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '51', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '52', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '53', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '54', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '55', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '56', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '57', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '58', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '59', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '60', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '61', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '62', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '63', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '64', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '65', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '66', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '67', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '68', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '69', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '70', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '71', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '72', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '73', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '74', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '75', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '76', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '77', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '78', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '79', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '80', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '81', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '82', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '83', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '84', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '85', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '86', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '87', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '88', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '89', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '90', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '91', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '92', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '93', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '94', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '95', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '96', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '97', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '98', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '99', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '100', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '101', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '102', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '103', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '104', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '105', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '106', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '107', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '108', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '109', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '110', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '111', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '112', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '113', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '114', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '115', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '116', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '117', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '118', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '119', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '120', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '121', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '122', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '123', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '124', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '125', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '126', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '127', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '128', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '129', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '130', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '131', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '132', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '133', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '134', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '135', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '136', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '137', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '138', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '139', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '140', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '141', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '142', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '143', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '144', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '145', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '146', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '147', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '148', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '149', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '150', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '151', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '152', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '153', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '154', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '155', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '156', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '157', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '158', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '159', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '160', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '161', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '162', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '163', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '164', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '165', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '166', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '167', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '168', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '169', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '170', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '171', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '172', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '173', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '174', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '175', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '176', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '177', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '178', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '179', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '180', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '181', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '182', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '183', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '184', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '185', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '186', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '187', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '188', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '189', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '190', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '191', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '192', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '193', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '194', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '195', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '196', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '197', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '198', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '199', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '200', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '201', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '202', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '203', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '204', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '205', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '206', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '207', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '208', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '209', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '210', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '211', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '212', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '213', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '214', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '215', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '216', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '217', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '218', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '219', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '220', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '221', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '222', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '223', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '224', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '225', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '226', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '227', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '228', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '229', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '230', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '231', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '232', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '233', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '234', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '235', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '236', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '237', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '238', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '239', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '240', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '241', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '242', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '243', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '244', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '245', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '246', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '247', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '248', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '249', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '250', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '251', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '252', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '253', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '254', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '255', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '256', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '257', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '258', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '259', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '260', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '261', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '262', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '263', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '264', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '265', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '266', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '267', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '268', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '269', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '270', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '271', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '272', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '273', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '274', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '275', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '276', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '277', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '278', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '279', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '280', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '281', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '282', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '283', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '284', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '285', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '286', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '287', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '288', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '289', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '290', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '291', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '292', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '293', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '294', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '295', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '296', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '297', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '298', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '299', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '300', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '301', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '302', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '303', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '304', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '305', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '306', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '307', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '308', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '309', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '310', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '311', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '312', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '313', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '314', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '315', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '316', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '317', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '318', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '319', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '320', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '321', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '322', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '323', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '324', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '325', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '326', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '327', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_ivanova_27_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '328', @apartment_id);

-- Init person registrations
INSERT INTO ab_person_registrations_tbl (begin_date, end_date, person_id, apartment_id)
	VALUES ('2000-01-01', '2007-01-11', @person_id, @apartment_ivanova_330_id);

INSERT INTO ab_person_registrations_tbl (begin_date, end_date, person_id, apartment_id)
	VALUES ('2007-01-12', '2008-01-13', @person_id, @apartment_ivanova_1_id);

INSERT INTO ab_person_registrations_tbl (begin_date, end_date, person_id, apartment_id)
	VALUES ('2008-04-12', '2100-12-31', @person_id, @apartment_ivanova_330_id);

update common_users_tbl set
		ab_country_filter=1, ab_region_filter=1000, ab_town_filter=2
where id=@user_test;

-- master index data
select @ds:=id from common_data_source_descriptions_tbl where description='Master-index';
select @instId:='090-';
select @ab_base:=0x1000 + 0;

insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x09, @ds from ab_persons_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x08, @ds from ab_apartments_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x07, @ds from ab_building_addresses_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x0A, @ds from ab_buildings_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x0B, @ds from ab_building_address_attribute_types_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x06, @ds from ab_streets_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x05, @ds from ab_districts_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x04, @ds from ab_street_types_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x03, @ds from ab_towns_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x10, @ds from ab_town_types_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x02, @ds from ab_regions_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x01, @ds from ab_countries_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x0C, @ds from ab_identity_types_tbl);