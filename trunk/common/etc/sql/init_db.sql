-- add indexes
alter table common_data_corrections_tbl
		add unique index I_externalid_datasource_objecttype (object_type, external_object_id, data_source_description_id),
		add index I_internalid_datasource_objecttype (object_type, internal_object_id, data_source_description_id);

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

select @TYPE_BOOLEAN := 1;
select @TYPE_INT := 2;
select @TYPE_LONG := 3;
select @TYPE_STRING := 4;
select @TYPE_DATE := 5;
select @TYPE_DOUBLE := 6;
select @TYPE_DECIMAL := 7;

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

insert into common_data_source_descriptions_tbl (id, description)
	values (2, 'Харьковский центр начислений');
SELECT @source_description_cn:=2;

-- Init modules
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('common');
SELECT @module_common:=last_insert_id();

-- Init registry types
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (1, 0, 1);
select @registry_type:=1;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (2, 0, 2);
select @registry_type:=2;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (3, 0, 3);
select @registry_type:=3;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (4, 0, 4);
select @registry_type:=4;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (5, 0, 5);
select @registry_type:=5;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (6, 0, 6);
select @registry_type:=6;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (7, 0, 7);
select @registry_type:=7;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (8, 0, 8);
select @registry_type:=8;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (9, 0, 9);
select @registry_type:=9;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (10, 0, 10);
select @registry_type:=10;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (11, 0, 11);
select @registry_type_info:=11;
INSERT INTO common_registry_types_tbl (id, version, code) VALUES (12, 0, 12);
select @registry_type_bank_payments:=12;

-- Init RegistryStatuses
INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 0);
SELECT @registry_status_loading:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 1);
SELECT @registry_status_loaded:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 2);
SELECT @registry_status_loading_canceled:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 3);
SELECT @registry_status_loaded_with_error:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 4);
SELECT @registry_status_processing:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 5);
SELECT @registry_status_processing_with_error:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 6);
SELECT @registry_status_processed:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 7);
SELECT @registry_status_processed_with_error:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 8);
SELECT @registry_status_processing_canceled:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 9);
SELECT @registry_status_rollbacking:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 10);
SELECT @registry_status_rollbacked:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 11);
SELECT @registry_status_creating:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 12);
SELECT @registry_status_created:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 13);
SELECT @registry_status_creating_canceled:=last_insert_id();

-- Init RegistryArchiveStatuses
INSERT INTO common_registry_archive_statuses_tbl (code) VALUES (0);
SELECT @sp_registry_archive_status_none:=last_insert_id();

INSERT INTO common_registry_archive_statuses_tbl (code) VALUES (1);
SELECT @sp_registry_archive_status_archiving:=last_insert_id();

INSERT INTO common_registry_archive_statuses_tbl (code) VALUES (2);
SELECT @sp_registry_archive_status_archived:=last_insert_id();

INSERT INTO common_registry_archive_statuses_tbl (code) VALUES (3);
SELECT @sp_registry_archive_status_canceled:=last_insert_id();

-- Init FPFileRegistryTypes
INSERT INTO common_registry_fpfile_types_tbl (version, code) VALUES (0, 0);
SELECT @sp_registry_file_fp_format:=last_insert_id();

INSERT INTO common_registry_fpfile_types_tbl (version, code) VALUES (0, 1);
SELECT @sp_registry_file_mb_format:=last_insert_id();

-- Init RegistryRecordStatus
INSERT INTO common_registry_record_statuses_tbl (id, code) VALUES (1, 1);
select @record_status_loaded:=1;
INSERT INTO common_registry_record_statuses_tbl (id, code) VALUES (2, 2);
select @record_status_error:=2;
INSERT INTO common_registry_record_statuses_tbl (id, code) VALUES (3, 3);
select @record_status_error_fixed:=3;
INSERT INTO common_registry_record_statuses_tbl (id, code) VALUES (4, 4);
select @record_status_processed:=4;

-- Init CurrencyInfo
insert into common_currency_infos_tbl(id, iso_code, gender) values (1, 'UAH', 1);
select @currency_grivna:=1;
insert into common_currency_names_tbl(language_id, currency_info_id, name, short_name, fraction_name, fraction_short_name)
	values (@ru_id, @currency_grivna, 'Гривна', 'грн', 'Копейка', 'коп');

-- Init User Roles table
INSERT INTO admin_user_roles_tbl (id, status, external_id) values (1, 0, 'buhgalter');
SELECT @buhgalter_id:=1;
INSERT INTO admin_user_roles_tbl (id, status, external_id) values (2, 0, 'cashier');
SELECT @cashier_id:=2;

INSERT INTO admin_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Бухгалтер', @buhgalter_id, @ru_id);
INSERT INTO admin_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Кассир', @cashier_id, @ru_id);
INSERT INTO admin_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Accountant', @buhgalter_id, @en_id);
INSERT INTO admin_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Cashier', @cashier_id, @en_id);
