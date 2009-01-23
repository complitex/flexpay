
create table tc_tariff_calc_rules_file_translations_tbl (
    id bigint not null auto_increment comment 'Primary key',
    name varchar(255) not null comment 'Tariff calculation rules file name translation',
    description varchar(255) comment 'Optional description translation',
    tariff_calc_rules_file_id bigint not null comment 'Tariff calculation rules file reference',
    language_id bigint not null comment 'Language reference',
    primary key (id),
    unique (tariff_calc_rules_file_id, language_id)
) comment='Tariff calculation rules file translations';

create table tc_tariff_calc_rules_files_tbl (
    id bigint not null auto_increment comment 'Primary key',
    status integer not null comment 'Enabled/Disabled status',
    creation_date datetime not null comment 'File creation date',
    user_name varchar(255) not null comment 'User name, which created this file',
    file_id bigint unique,
    type_id bigint comment 'Tariff calculation rules file type reference',
    status_id bigint comment 'Tariff calculation rules file status reference',
    primary key (id)
) comment='Tariff calculation rules files information';

alter table tc_tariff_calc_rules_file_translations_tbl
    add index tc_calc_rules_file_translations_tbl_calc_rules_file_id (tariff_calc_rules_file_id),
    add constraint tc_calc_rules_file_translations_tbl_calc_rules_file_id
    foreign key (tariff_calc_rules_file_id)
    references tc_tariff_calc_rules_files_tbl (id);

alter table tc_tariff_calc_rules_file_translations_tbl
    add index lang_calc_rules_file_pair_language_id (language_id),
    add constraint lang_calc_rules_file_pair_language_id
    foreign key (language_id)
    references common_languages_tbl (id);

alter table tc_tariff_calc_rules_files_tbl
    add index FKDA48352F7D816B8D (file_id),
    add constraint FKDA48352F7D816B8D
    foreign key (file_id)
    references common_files_tbl (id);

alter table tc_tariff_calc_rules_files_tbl
    add index FKDA48352F62EB9A29 (status_id),
    add constraint FKDA48352F62EB9A29
    foreign key (status_id)
    references common_file_statuses_tbl (id);

alter table tc_tariff_calc_rules_files_tbl
    add index FKDA48352F25D394E9 (type_id),
    add constraint FKDA48352F25D394E9
    foreign key (type_id)
    references common_file_types_tbl (id);

INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('tc.file_type.tariff_rules', '.{8}\\u002E(d|D)(r|R)(l|L)', 1, @module_tc);

-- Init TC file statuses
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('tc.file_status.importing', 0, @module_tc);
SELECT @tc_importing_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('tc.file_status.imported', 1, @module_tc);
SELECT @tc_imported_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('tc.file_status.deleting', 2, @module_tc);
SELECT @tc_deleting_status_id:=last_insert_id();

update common_version_tbl set last_modified_date='2009-01-22', date_version=1;
