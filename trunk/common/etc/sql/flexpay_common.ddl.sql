
    create table common_certificates_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        alias varchar(255) not null comment 'Alias',
        begin_date datetime comment 'Certificate validity begin date',
        end_date datetime comment 'Certificate validity end date',
        description varchar(255) not null comment 'Description',
        primary key (id)
    ) comment='Security certificate';

    create table common_currency_infos_tbl (
        id bigint not null auto_increment comment 'Primary key',
        iso_code varchar(255) not null comment 'ISO 4217 code of a currency',
        gender integer not null comment 'Gender (0-masculine, 1-feminine, 2-neuter)',
        primary key (id)
    ) comment='Currency infos';

    create table common_currency_names_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Full currency name translation',
        short_name varchar(255) not null comment 'Short currency name translation',
        fraction_name varchar(255) not null comment 'Full currency fraction name translation',
        fraction_short_name varchar(255) not null comment 'Short currency fraction name translation',
        language_id bigint not null comment 'Language reference',
        currency_info_id bigint not null comment 'Currency info reference',
        primary key (id),
        unique (language_id, currency_info_id)
    ) comment='Currency name translation';

    create table common_data_corrections_tbl (
        id bigint not null auto_increment comment 'Primary key',
        internal_object_id bigint not null comment 'Flexpay object id (ref)',
        object_type integer not null comment 'Object type code',
        external_object_id varchar(255) not null comment 'External object identifier',
        data_source_description_id bigint comment 'Optional data source description reference',
        primary key (id)
    ) comment='Various data sources objects mapping';

    create table common_data_source_descriptions_tbl (
        id bigint not null auto_increment,
        description varchar(255) not null,
        primary key (id)
    );

    create table common_diffs_tbl (
        id bigint not null auto_increment,
        operation_time datetime not null comment 'Operation timestamp',
        object_type integer not null comment 'Object type',
        object_type_name varchar(255) comment 'Object type class name',
        object_id bigint not null comment 'Internal object reference',
        operation_type integer not null comment 'Operation type (create-update-delete)',
        user_name varchar(255) not null comment 'User name performed operation',
        processing_status integer default 0 not null comment 'Processing status',
        master_index varchar(255) not null comment 'Unique among several installations object id',
        instance_id varchar(255) not null comment 'Source application installation identifier',
        error_message varchar(255) comment 'Processing error message',
        primary key (id)
    ) comment='Set of history records for single object';

    create table common_dual_tbl (
        id bigint not null auto_increment,
        primary key (id)
    );

    create table common_external_history_packs_tbl (
        id bigint not null auto_increment,
        receive_date datetime not null comment 'Packet receive time',
        unpuck_tries integer not null comment 'Number of attempts to unpack the pack',
        source_instance_id varchar(255) not null comment 'Source instance id',
        consumption_group_id bigint not null comment 'Consumption group id',
        file_id bigint not null comment 'File containing records reference',
        primary key (id),
        unique (source_instance_id, consumption_group_id)
    ) comment='Received history records pack';

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
        out_transport_config_id bigint not null comment 'Out transport config reference',
        primary key (id)
    ) comment='Some abstract history records consumer';

    create table common_history_consumption_groups_tbl (
        id bigint not null auto_increment,
        consumer_id bigint not null comment 'History consumer reference',
        creation_date datetime not null comment 'Group creation date',
        postpone_time datetime comment 'Last postpone timestamp',
        user_name varchar(255) not null comment 'User name created group',
        send_tries integer not null comment 'Number of tries group file was sent',
        group_status integer not null comment 'Number of tries group file was sent',
        file_id bigint comment 'History group data file reference',
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
        old_decimal_value decimal(19,5) comment 'Optional old decimal value',
        new_decimal_value decimal(19,5) comment 'Optional new decimal value',
        field_type integer not null comment 'Field type value is modified for',
        operation_order integer not null comment 'Object modification operation order',
        language_code varchar(255) comment 'Optional language iso code for multilang fields',
        begin_date date comment 'Optional begin date for temporal fields',
        end_date date comment 'Optional end date for temporal fields',
        field_key varchar(255) comment 'Optional key for field value',
        field_key2 varchar(255) comment 'Optional second key for field value',
        field_key3 varchar(255) comment 'Optional third key for field value',
        processing_status integer default 0 not null comment 'Processing status',
        diff_id bigint not null comment 'Diff (set of records) reference',
        primary key (id)
    ) comment='Single field update record';

    create table common_history_unpack_data_tbl (
        id bigint not null auto_increment,
        source_instance_id varchar(255) not null unique comment 'Source instance id',
        last_pack_id bigint not null comment 'Reference to last history pack',
        primary key (id)
    ) comment='Data for unpacking process';

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

    create table common_out_transport_configs_tbl (
        id bigint not null auto_increment,
        config_type varchar(255) not null comment 'Class hierarchy descriminator',
        version integer not null comment 'Optimistic lock version',
        ws_url varchar(255) comment 'Web service url',
        primary key (id)
    ) comment='Out transport configs';

    create table common_registries_tbl (
        id bigint not null auto_increment,
        version integer not null,
        registry_number bigint,
        records_number bigint,
        errors_number integer default -1 not null comment 'Cached errors number value, -1 is not init',
        creation_date datetime,
        from_date datetime,
        till_date datetime,
        sender_code bigint,
        recipient_code bigint,
        amount decimal(19,2),
        registry_type_id bigint not null comment 'Registry type reference',
        registry_status_id bigint not null comment 'Registry status reference',
        archive_status_id bigint not null comment 'Registry archive status reference',
        import_error_id bigint comment 'Import error reference',
        module_id bigint not null comment 'Module reference',
        primary key (id)
    );

    create table common_registry_archive_statuses_tbl (
        id bigint not null auto_increment,
        code integer not null unique,
        primary key (id)
    );

    create table common_registry_containers_tbl (
        id bigint not null auto_increment,
        data varchar(2048) not null comment 'Registry container data',
        order_weight integer not null comment 'Order of the container in a registry',
        registry_id bigint not null comment 'Registry reference',
        primary key (id)
    );

    create table common_registry_fpfile_types_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic locking version',
        code integer not null unique comment 'FP file registry type code',
        primary key (id)
    );

    create table common_registry_fpfiles_tbl (
        registry_id bigint not null,
        fpfile_id bigint not null,
        registry_fpfile_type_id bigint not null,
        primary key (registry_id, registry_fpfile_type_id)
    );

    create table common_registry_properties_tbl (
        id bigint not null auto_increment comment 'Primary key',
        props_type varchar(255) not null comment 'Hierarchy discriminator, all entities should have the same value',
        version integer not null comment 'Optimistic lock version',
        registry_id bigint comment 'Registry reference',
        primary key (id)
    );

    create table common_registry_record_containers_tbl (
        id bigint not null auto_increment,
        data varchar(2048) not null comment 'Container data',
        order_weight integer not null comment 'Order of the container in a registry record',
        record_id bigint not null comment 'Registry record reference',
        primary key (id)
    );

    create table common_registry_record_properties_tbl (
        id bigint not null auto_increment comment 'Primary key',
        props_type varchar(255) not null comment 'Hierarchy discriminator, all entities should have the same value',
        version integer not null comment 'Optimistic lock version',
        record_id bigint comment 'Registry record reference',
        primary key (id)
    );

    create table common_registry_record_statuses_tbl (
        id bigint not null auto_increment,
        code integer not null unique comment 'Registry record status code',
        primary key (id)
    );

    create table common_registry_records_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        service_code varchar(255) not null,
        personal_account_ext varchar(255) not null,
        town_type varchar(255),
        town_name varchar(255),
        street_type varchar(255),
        street_name varchar(255),
        building_number varchar(255),
        bulk_number varchar(255),
        apartment_number varchar(255),
        first_name varchar(255),
        middle_name varchar(255),
        last_name varchar(255),
        operation_date datetime not null,
        unique_operation_number bigint,
        amount decimal(19,2),
        registry_id bigint not null comment 'Registry reference',
        record_status_id bigint not null comment 'Record status reference',
        import_error_id bigint comment 'Import error reference',
        import_error_type integer comment 'Import error type from import error',
        primary key (id)
    );

    create table common_registry_statuses_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic locking version',
        code integer not null unique comment 'Registry status code',
        primary key (id)
    );

    create table common_registry_types_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic locking version',
        code integer not null unique comment 'Registry type code',
        primary key (id)
    );

    create table common_report_print_history_record_tbl (
        id bigint not null auto_increment,
        version integer not null,
        user_name varchar(255) not null comment 'Name of user who printed report',
        print_date datetime not null comment 'Printing date',
        report_type integer not null comment 'Report type',
        primary key (id)
    );

    create table common_sequences_tbl (
        id bigint not null auto_increment,
        counter bigint not null,
        description varchar(255),
        primary key (id)
    );

    create table common_user_role_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        user_role_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (user_role_id, language_id)
    );

    create table common_user_roles_tbl (
        id bigint not null auto_increment,
        status integer not null,
        external_id varchar(255) not null unique,
        primary key (id)
    );

    create table common_users_tbl (
        id bigint not null auto_increment comment 'Primary key',
        discriminator varchar(255) not null comment 'Class hierarchy discriminator',
        full_name varchar(255) not null comment 'Full user name',
        last_name varchar(255) not null comment 'Last user name',
        first_name varchar(255) comment 'First user name',
        user_name varchar(255) not null unique comment 'User login name',
        language_code varchar(255) not null comment 'Preferred language ISO code',
        page_size integer comment 'Preferred listing page size',
        user_role_id bigint comment 'Optional user role reference',
        primary key (id)
    ) comment='User details';

    alter table common_currency_names_tbl 
        add index FK_common_currency_names_tbl_currency_info_id (currency_info_id), 
        add constraint FK_common_currency_names_tbl_currency_info_id 
        foreign key (currency_info_id) 
        references common_currency_infos_tbl (id);

    alter table common_currency_names_tbl 
        add index common_currency_names_tbl_language_id (language_id), 
        add constraint common_currency_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table common_data_corrections_tbl 
        add index common_data_corrections_tbl_dsd_id (data_source_description_id), 
        add constraint common_data_corrections_tbl_dsd_id 
        foreign key (data_source_description_id) 
        references common_data_source_descriptions_tbl (id);

    create index I_objectid_object_type on common_diffs_tbl (object_type, object_id);

    alter table common_external_history_packs_tbl 
        add index FK_common_external_history_packs_tbl (file_id), 
        add constraint FK_common_external_history_packs_tbl 
        foreign key (file_id) 
        references common_files_tbl (id);

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

    alter table common_history_consumers_tbl 
        add index FK_common_history_consumers_tbl_out_transport_config_id (out_transport_config_id), 
        add constraint FK_common_history_consumers_tbl_out_transport_config_id 
        foreign key (out_transport_config_id) 
        references common_out_transport_configs_tbl (id);

    alter table common_history_consumption_groups_tbl 
        add index FK_common_history_consumption_groups_tbl_file_id (file_id), 
        add constraint FK_common_history_consumption_groups_tbl_file_id 
        foreign key (file_id) 
        references common_files_tbl (id);

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

    alter table common_history_unpack_data_tbl 
        add index FK_common_history_unpack_data_tbl_last_pack_id (last_pack_id), 
        add constraint FK_common_history_unpack_data_tbl_last_pack_id 
        foreign key (last_pack_id) 
        references common_external_history_packs_tbl (id);

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

    alter table common_registries_tbl 
        add index FK_common_registries_tbl_archive_status_id (archive_status_id), 
        add constraint FK_common_registries_tbl_archive_status_id 
        foreign key (archive_status_id) 
        references common_registry_archive_statuses_tbl (id);

    alter table common_registries_tbl 
        add index FK_common_registries_tbl_module_id (module_id), 
        add constraint FK_common_registries_tbl_module_id 
        foreign key (module_id) 
        references common_flexpay_modules_tbl (id);

    alter table common_registries_tbl 
        add index FK_common_registries_tbl_status_id (registry_status_id), 
        add constraint FK_common_registries_tbl_status_id 
        foreign key (registry_status_id) 
        references common_registry_statuses_tbl (id);

    alter table common_registries_tbl 
        add index FK_common_registries_tbl_registry_type_id (registry_type_id), 
        add constraint FK_common_registries_tbl_registry_type_id 
        foreign key (registry_type_id) 
        references common_registry_types_tbl (id);

    alter table common_registries_tbl 
        add index FK_common_registries_tbl_import_error_id (import_error_id), 
        add constraint FK_common_registries_tbl_import_error_id 
        foreign key (import_error_id) 
        references common_import_errors_tbl (id);

    alter table common_registry_containers_tbl 
        add index FK_common_registry_containers_tbl_registry_id (registry_id), 
        add constraint FK_common_registry_containers_tbl_registry_id 
        foreign key (registry_id) 
        references common_registries_tbl (id);

    alter table common_registry_fpfiles_tbl 
        add index FK_common_registry_fpfiles_tbl_fpfile_id (fpfile_id), 
        add constraint FK_common_registry_fpfiles_tbl_fpfile_id 
        foreign key (fpfile_id) 
        references common_files_tbl (id);

    alter table common_registry_fpfiles_tbl 
        add index FK_common_registry_fpfiles_tbl_registry_fpfile_type_id (registry_fpfile_type_id), 
        add constraint FK_common_registry_fpfiles_tbl_registry_fpfile_type_id 
        foreign key (registry_fpfile_type_id) 
        references common_registry_fpfile_types_tbl (id);

    alter table common_registry_fpfiles_tbl 
        add index FK_common_registry_fpfiles_tbl_registry_id (registry_id), 
        add constraint FK_common_registry_fpfiles_tbl_registry_id 
        foreign key (registry_id) 
        references common_registries_tbl (id);

    alter table common_registry_properties_tbl 
        add index FK_common_registry_properties_tbl_registry_id (registry_id), 
        add constraint FK_common_registry_properties_tbl_registry_id 
        foreign key (registry_id) 
        references common_registries_tbl (id);

    alter table common_registry_record_containers_tbl 
        add index FK_common_registry_record_containers_tbl_record_id (record_id), 
        add constraint FK_common_registry_record_containers_tbl_record_id 
        foreign key (record_id) 
        references common_registry_records_tbl (id);

    alter table common_registry_record_properties_tbl 
        add index FK_common_registry_properties_tbl_record_id (record_id), 
        add constraint FK_common_registry_properties_tbl_record_id 
        foreign key (record_id) 
        references common_registry_records_tbl (id);

    create index I_registry_errortype on common_registry_records_tbl (registry_id, import_error_type);

    create index I_registry_status on common_registry_records_tbl (registry_id, record_status_id);

    alter table common_registry_records_tbl 
        add index FK_common_registry_records_tbl_registry_id (registry_id), 
        add constraint FK_common_registry_records_tbl_registry_id 
        foreign key (registry_id) 
        references common_registries_tbl (id);

    alter table common_registry_records_tbl 
        add index FK_common_registry_records_tbl_import_error_id (import_error_id), 
        add constraint FK_common_registry_records_tbl_import_error_id 
        foreign key (import_error_id) 
        references common_import_errors_tbl (id);

    alter table common_registry_records_tbl 
        add index FK_common_registry_records_tbl_record_status_id (record_status_id), 
        add constraint FK_common_registry_records_tbl_record_status_id 
        foreign key (record_status_id) 
        references common_registry_record_statuses_tbl (id);

    alter table common_user_role_name_translations_tbl 
        add index FKB85A9CACA555113A (user_role_id), 
        add constraint FKB85A9CACA555113A 
        foreign key (user_role_id) 
        references common_user_roles_tbl (id);

    alter table common_user_role_name_translations_tbl 
        add index FKB85A9CAC61F37403 (language_id), 
        add constraint FKB85A9CAC61F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table common_users_tbl 
        add index common_user_role_tbl_user_role_id (user_role_id), 
        add constraint common_user_role_tbl_user_role_id 
        foreign key (user_role_id) 
        references common_user_roles_tbl (id);
