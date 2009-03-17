
    create table common_data_corrections_tbl (
        id bigint not null auto_increment,
        internal_object_id bigint not null,
        object_type integer,
        external_object_id varchar(255) not null,
        data_source_description_id bigint,
        primary key (id),
        unique (object_type, external_object_id, data_source_description_id)
    );

    create table common_data_source_descriptions_tbl (
        id bigint not null auto_increment,
        description varchar(255) not null,
        primary key (id)
    );

    create table common_diffs_tbl (
        id bigint not null auto_increment,
        operation_time datetime not null comment 'Operation timestamp',
        object_type integer not null comment 'Object type',
        object_id bigint not null comment 'Internal object reference',
        operation_type integer not null comment 'Operation type (create-update-delete)',
        user_name varchar(255) not null comment 'User name performed operation',
        processing_status integer default 0 not null comment 'Processing status',
        master_index varchar(255) not null comment 'Unique among several installations object id',
        instance_id varchar(255) not null comment 'Source application installation identifier',
        primary key (id)
    ) comment='Set of history records for single object';

    create table common_dual_tbl (
        id bigint not null auto_increment,
        primary key (id)
    );

    create table common_file_statuses_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Filestatus name',
        description varchar(255) comment 'Filestatus description',
        code bigint not null comment 'Unique filestatus code',
        module_id bigint not null comment 'Flexpay module reference',
        primary key (id),
        unique (code, module_id)
    ) comment='Information about file statuses';

    create table common_file_types_tbl (
        id bigint not null auto_increment comment 'Primary key',
        file_mask varchar(255) not null comment 'Mask of files for this type',
        name varchar(255) not null comment 'Filetype name',
        description varchar(255) comment 'Filetype description',
        code bigint not null comment 'Unique filetype code',
        module_id bigint not null comment 'Flexpay module reference',
        primary key (id),
        unique (code, module_id)
    ) comment='Information about known filetypes';

    create table common_files_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name_on_server varchar(255) not null comment 'File name on flexpay server',
        original_name varchar(255) not null comment 'Original file name',
        description varchar(255) comment 'File description',
        creation_date datetime not null comment 'File creation date',
        user_name varchar(255) not null comment 'User name who create this file',
        size bigint comment 'File size',
        module_id bigint not null comment 'Flexpay module reference',
        primary key (id)
    ) comment='Table, where store information about all flexpay files';

    create table common_flexpay_modules_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null unique comment 'Flexpay module name',
        primary key (id)
    ) comment='Information about all flexpay modules';

    create table common_history_consumers_tbl (
        id bigint not null auto_increment,
        active integer not null comment 'Enabled-disabled status',
        name varchar(255) comment 'Consumer name',
        description varchar(255) comment 'Optional consumer description',
        last_diff_id bigint comment 'Last packed diff reference',
        primary key (id)
    ) comment='Some abstract history records consumer';

    create table common_history_consumption_groups_tbl (
        id bigint not null auto_increment,
        consumer_id bigint not null comment 'History consumer reference',
        creation_date datetime not null comment 'Group creation date',
        user_name varchar(255) not null comment 'User name created group',
        primary key (id)
    ) comment='Group of several consumptions';

    create table common_history_consumptions_tbl (
        id bigint not null auto_increment,
        record_id bigint not null comment 'History record reference',
        group_id bigint not null comment 'History consumption group reference',
        primary key (id)
    ) comment='Consumption of single history record';

    create table common_history_records_tbl (
        id bigint not null auto_increment,
        old_date_value datetime comment 'Optional old date value',
        new_date_value datetime comment 'Optional new date value',
        old_int_value integer comment 'Optional old int value',
        new_int_value integer comment 'Optional new int value',
        old_bool_value bit comment 'Optional old boolean value',
        new_bool_value bit comment 'Optional new bool value',
        old_long_value bigint comment 'Optional old long value',
        new_long_value bigint comment 'Optional new long value',
        old_string_value varchar(255) comment 'Optional old string value',
        new_string_value varchar(255) comment 'Optional new string value',
        old_double_value double precision comment 'Optional old double value',
        new_double_value double precision comment 'Optional new double value',
        field_type integer not null comment 'Field type value is modified for',
        operation_order integer not null comment 'Object modification operation order',
        language_code varchar(255) comment 'Optional language iso code for multilang fields',
        begin_date date comment 'Optional begin date for temporal fields',
        end_date date comment 'Optional end date for temporal fields',
        field_key varchar(255) comment 'Optional key for field value',
        processing_status integer default 0 not null comment 'Processing status',
        diff_id bigint not null comment 'Diff (set of records) reference',
        primary key (id)
    ) comment='Single field update record';

    create table common_import_errors_tbl (
        id bigint not null auto_increment,
        status integer not null,
        source_description_id bigint not null,
        object_type integer not null,
        ext_object_id varchar(255) not null,
        handler_object_name varchar(255) not null,
        error_key varchar(255),
        primary key (id)
    );

    create table common_language_names_tbl (
        id bigint not null auto_increment,
        translation varchar(255),
        language_id bigint not null,
        translation_from_language_id bigint not null,
        primary key (id),
        unique (language_id, translation_from_language_id)
    );

    create table common_languages_tbl (
        id bigint not null auto_increment,
        status integer not null,
        is_default bit,
        lang_iso_code varchar(3) not null unique,
        primary key (id)
    );

    create table common_measure_units_tbl (
        id bigint not null auto_increment comment 'Primary key',
        status integer not null comment 'Enabled - disabled status',
        primary key (id)
    ) comment='Measure unit translation';

    create table common_mesuare_unit_names_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation',
        language_id bigint not null comment 'Language reference',
        measure_unit_id bigint not null comment 'Measure unit reference',
        primary key (id),
        unique (language_id, measure_unit_id)
    ) comment='Measure unit translation';

    create table common_sequences_tbl (
        id bigint not null auto_increment,
        counter bigint not null,
        description varchar(255),
        primary key (id)
    );

    alter table common_data_corrections_tbl 
        add index FKF86BDC935BA789BB (data_source_description_id), 
        add constraint FKF86BDC935BA789BB 
        foreign key (data_source_description_id) 
        references common_data_source_descriptions_tbl (id);

    alter table common_file_statuses_tbl 
        add index common_file_statuses_tbl_module_id (module_id), 
        add constraint common_file_statuses_tbl_module_id 
        foreign key (module_id) 
        references common_flexpay_modules_tbl (id);

    alter table common_file_types_tbl 
        add index common_file_types_tbl_module_id (module_id), 
        add constraint common_file_types_tbl_module_id 
        foreign key (module_id) 
        references common_flexpay_modules_tbl (id);

    alter table common_files_tbl 
        add index common_files_tbl_module_id (module_id), 
        add constraint common_files_tbl_module_id 
        foreign key (module_id) 
        references common_flexpay_modules_tbl (id);

    alter table common_history_consumers_tbl 
        add index FK_common_history_consumers_tbl_last_diff_id (last_diff_id), 
        add constraint FK_common_history_consumers_tbl_last_diff_id 
        foreign key (last_diff_id) 
        references common_diffs_tbl (id);

    alter table common_history_consumption_groups_tbl 
        add index FK_common_history_consumption_groups_tbl_consumer_id (consumer_id), 
        add constraint FK_common_history_consumption_groups_tbl_consumer_id 
        foreign key (consumer_id) 
        references common_history_consumers_tbl (id);

    alter table common_history_consumptions_tbl 
        add index FK_common_history_consumptions_tbl_group_id (group_id), 
        add constraint FK_common_history_consumptions_tbl_group_id 
        foreign key (group_id) 
        references common_history_consumption_groups_tbl (id);

    alter table common_history_consumptions_tbl 
        add index FK_common_history_consumptions_tbl_record_id (record_id), 
        add constraint FK_common_history_consumptions_tbl_record_id 
        foreign key (record_id) 
        references common_history_records_tbl (id);

    alter table common_history_records_tbl 
        add index FK_common_history_records_tbl_diff_id (diff_id), 
        add constraint FK_common_history_records_tbl_diff_id 
        foreign key (diff_id) 
        references common_diffs_tbl (id);

    alter table common_import_errors_tbl 
        add index FKBAEED8705355D490 (source_description_id), 
        add constraint FKBAEED8705355D490 
        foreign key (source_description_id) 
        references common_data_source_descriptions_tbl (id);

    alter table common_language_names_tbl 
        add index FK85F168F48626C2BC (translation_from_language_id), 
        add constraint FK85F168F48626C2BC 
        foreign key (translation_from_language_id) 
        references common_languages_tbl (id);

    alter table common_language_names_tbl 
        add index FK85F168F461F37403 (language_id), 
        add constraint FK85F168F461F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table common_mesuare_unit_names_tbl 
        add index common_mesuare_unit_names_tbl_measure_unit_id (measure_unit_id), 
        add constraint common_mesuare_unit_names_tbl_measure_unit_id 
        foreign key (measure_unit_id) 
        references common_measure_units_tbl (id);

    alter table common_mesuare_unit_names_tbl 
        add index common_mesuare_unit_names_tbl_language_id (language_id), 
        add constraint common_mesuare_unit_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);
