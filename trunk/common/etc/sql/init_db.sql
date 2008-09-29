
-- Init Languages table
INSERT INTO common_languages_tbl (is_default, status, lang_iso_code) values (1, 0, 'ru');
SELECT @ru_id:=last_insert_id();

INSERT INTO common_languages_tbl (is_default, status, lang_iso_code) values (0, 0, 'en');
SELECT @en_id:=last_insert_id();

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
