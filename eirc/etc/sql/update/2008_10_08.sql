SELECT @service_provider_cn:=1;
select @ru_id:=id from common_languages_tbl where lang_iso_code='ru';

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 220);
SELECT @service_type_220:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Гараж', '', @ru_id, @service_type_220);

INSERT INTO eirc_service_types_tbl (status, code) VALUES (0, 230);
SELECT @service_type_230:=last_insert_id();
INSERT INTO eirc_service_type_name_translations_tbl (name, description, language_id, service_type_id)
	VALUES ('Сарай', '', @ru_id, @service_type_230);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '220', null, @service_type_220, '1900-01-01', '2100-12-31');
SELECT @service_220:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Гараж', @ru_id, @service_220);

INSERT INTO eirc_services_tbl (provider_id, external_code, measure_unit_id, type_id, begin_date, end_date)
	VALUES (@service_provider_cn, '230', null, @service_type_230, '1900-01-01', '2100-12-31');
SELECT @service_230:=last_insert_id();
INSERT INTO eirc_service_descriptions_tbl (name, language_id, service_id)
	VALUES ('Сарай', @ru_id, @service_230);

