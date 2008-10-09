SELECT @service_provider_cn:=1;
select @ru_id:=id from common_languages_tbl where lang_iso_code='ru';

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 240);
SELECT @service_type_240:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Погреба', '', @ru_id, @service_type_240);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 250);
SELECT @service_type_250:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Содержание животных', '', @ru_id, @service_type_250);


INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '240', null, @service_type_240, '1900-01-01', '2100-12-31');
SELECT @service_240:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Погреба', @ru_id, @service_240);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '250', null, @service_type_250, '1900-01-01', '2100-12-31');
SELECT @service_250:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Содержание животных', @ru_id, @service_250);
