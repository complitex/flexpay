
-- Init Languages table
INSERT INTO common_languages_tbl (id, is_default, status, lang_iso_code) values (1, 1, 0, 'ru');
SELECT @ru_id:=1;

INSERT INTO common_languages_tbl (id, is_default, status, lang_iso_code) values (2, 0, 0, 'en');
SELECT @en_id:=2;

INSERT INTO common_language_names_tbl (translation, translation_from_language_id, language_id)
 	VALUES ('Русский', @ru_id, @ru_id);
INSERT INTO common_language_names_tbl (translation, translation_from_language_id, language_id)
 	VALUES ('Английский', @ru_id, @en_id);
INSERT INTO common_language_names_tbl (translation, translation_from_language_id, language_id)
 	VALUES ('Russia', @en_id, @ru_id);
INSERT INTO common_language_names_tbl (translation, translation_from_language_id, language_id)
 	VALUES ('English', @en_id, @en_id);

INSERT INTO common_dual_tbl (id) VALUES (1);

-- init measure units
insert into common_measure_units_tbl (id, status) values (1, 0);
select @unit_cubometr:=1;
insert into common_mesuare_unit_names_tbl(name, measure_unit_id, language_id)
	values ('кб.м.', @unit_cubometr, @ru_id);

insert into common_measure_units_tbl (id, status) values (2, 0);
select @unit_square_meter:=2;
insert into common_mesuare_unit_names_tbl(name, measure_unit_id, language_id)
	values ('кв.м.', @unit_square_meter, @ru_id);

insert into common_measure_units_tbl (id, status) values (3, 0);
select @unit_gcalories:=3;
insert into common_mesuare_unit_names_tbl(name, measure_unit_id, language_id)
	values ('Гкал.', @unit_gcalories, @ru_id);

insert into common_measure_units_tbl (id, status) values (4, 0);
select @unit_days:=4;
insert into common_mesuare_unit_names_tbl(name, measure_unit_id, language_id)
	values ('дн.', @unit_days, @ru_id);

insert into common_measure_units_tbl (id, status) values (5, 0);
select @unit_grn_m2:=5;
insert into common_mesuare_unit_names_tbl(name, measure_unit_id, language_id)
	values ('грн/м2', @unit_grn_m2, @ru_id);

INSERT INTO common_data_source_descriptions_tbl (id, description) VALUES (1, 'Источник - Тестовые данные ПУ из ЦН');
SELECT @source_description_test_data:=1;

-- Init modules
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (1, 'common');
SELECT @module_common:=1;
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (2, 'ab');
SELECT @module_ab:=2;
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (3, 'bti');
SELECT @module_bti:=3;
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (4, 'eirc');
SELECT @module_eirc:=4;
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (5, 'sz');
SELECT @module_sz:=5;
