
-- Init Languages table
INSERT INTO languages_tbl (is_default, status, langIsoCode) values (1, 1, 'ru');
SELECT @ru_id:=last_insert_id();

INSERT INTO languages_tbl (is_default, status, langIsoCode) values (0, 1, 'en');
SELECT @en_id:=last_insert_id();

INSERT INTO language_names_tbl (translation, translation_from_language_id, language_id)
 	values ('Русский', @ru_id, @ru_id);
INSERT INTO language_names_tbl (translation, translation_from_language_id, language_id)
 	values ('Английский', @ru_id, @en_id);
INSERT INTO language_names_tbl (translation, translation_from_language_id, language_id)
 	values ('Russia', @en_id, @ru_id);
INSERT INTO language_names_tbl (translation, translation_from_language_id, language_id)
 	values ('English', @en_id, @en_id);

