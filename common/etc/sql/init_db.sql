
-- Init Languages table
INSERT INTO common_languages_tbl (is_default, status, lang_iso_code) values (1, 0, 'ru');
SELECT @ru_id:=last_insert_id();
INSERT INTO common_languages_tbl (is_default, status, lang_iso_code) values (0, 0, 'en');
SELECT @en_id:=last_insert_id();

INSERT INTO common_language_names_tbl (translation_from_language_id, language_id, translation) VALUES
    (@ru_id, @ru_id, 'Русский'),
    (@ru_id, @en_id, 'Английский'),
    (@en_id, @ru_id, 'Russian'),
    (@en_id, @en_id, 'English');

INSERT INTO common_dual_tbl (id) VALUES (1);

-- init measure units
insert into common_measure_units_tbl (status) values (0);
select @unit_cubometr:=last_insert_id();
insert into common_measure_units_tbl (status) values (0);
select @unit_square_meter:=last_insert_id();
insert into common_measure_units_tbl (status) values (0);
select @unit_gcalories:=last_insert_id();
insert into common_measure_units_tbl (status) values (0);
select @unit_days:=last_insert_id();
insert into common_measure_units_tbl (status) values (0);
select @unit_grn_m2:=last_insert_id();

insert into common_mesuare_unit_names_tbl(measure_unit_id, language_id, name) values
	(@unit_cubometr, @ru_id, 'кб.м.'),
	(@unit_square_meter, @ru_id, 'кв.м.'),
	(@unit_gcalories, @ru_id, 'Гкал.'),
	(@unit_days, @ru_id, 'дн.'),
	(@unit_grn_m2, @ru_id, 'грн/м2');

INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('Источник - Тестовые данные ПУ из ЦН');
SELECT @source_description_test_data:=last_insert_id();

INSERT INTO common_data_source_descriptions_tbl (id, description) VALUES (2002, 'Master-Index');
SELECT @source_description_master_index:=2002;

-- Init modules
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('common');
SELECT @module_common:=last_insert_id();

insert into common_history_consumers_tbl (id, active, name, description)
	values (1, 0, "Test-History-Consumer", "Sample history consumer");
select @history_consumer_1:=1;

