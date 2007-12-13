
-- Init Languages table
INSERT INTO languages_tbl (is_default, status, langIsoCode) values (1, 1, 'ru');
SELECT @ru_id:=last_insert_id();

INSERT INTO languages_tbl (is_default, status, langIsoCode) values (0, 1, 'en');
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
SELECT @town_type_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Город', @ru_id, @town_type_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Town', @en_id, @town_type_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Село', @ru_id, @town_type_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Village', @en_id, @town_type_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Поселок', @ru_id, @town_type_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Settlement', @en_id, @town_type_id);

INSERT INTO town_types_tbl (status) VALUES (0);
SELECT @town_type_id:=last_insert_id();
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Поселок городского типа', @ru_id, @town_type_id);
INSERT INTO town_type_translations_tbl (name, language_id, town_type_id)
	VALUES ('Urban settlement', @en_id, @town_type_id);
