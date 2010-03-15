
    create table ab_apartment_numbers_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        value varchar(255) not null comment 'Apartment number value',
        apartment_id bigint not null comment 'Apartment reference',
        primary key (id)
    );

    create table ab_apartments_tbl (
        id bigint not null auto_increment,
        apartment_type varchar(255) not null comment 'Class hierarchy descriminator, all apartments should have the same value',
        status integer not null comment 'Enabled/Disabled status',
        building_id bigint not null comment 'Building reference',
        primary key (id)
    ) comment='Apartments';

    create table ab_building_address_attribute_type_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Type translation',
        short_name varchar(255) comment 'Optional short translation',
        attribute_type_id bigint not null comment 'Building attribute type reference',
        language_id bigint not null comment 'Language reference',
        primary key (id),
        unique (attribute_type_id, language_id)
    ) comment='Building attribute type translations';

    create table ab_building_address_attribute_types_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled/Disabled status',
        primary key (id)
    ) comment='Building attribute type (number, bulk, etc.)';

    create table ab_building_address_attributes_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled/Disabled status',
        value varchar(255) not null comment 'Building attribute value',
        attribute_type_id bigint not null comment 'Attribute type reference',
        buildings_id bigint not null comment 'Building address reference',
        primary key (id)
    ) comment='Building address attributes';

    create table ab_building_addresses_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled/Disabled status',
        primary_status bit not null comment 'Flag of primary building address',
        street_id bigint not null comment 'Street reference',
        building_id bigint not null comment 'Building reference this address belongs to',
        primary key (id)
    ) comment='Building addresses';

    create table ab_building_statuses_tbl (
        id bigint not null auto_increment,
        begin_date date not null comment 'Status begin date',
        end_date date not null comment 'Status end date',
        value varchar(255) not null comment 'Status value',
        building_id bigint not null comment 'Building reference status belongs to',
        primary key (id)
    ) comment='Building status, for example building started or rebuilding';

    create table ab_buildings_tbl (
        id bigint not null auto_increment,
        building_type varchar(255) not null comment 'Class hierarchy descriminator, all buildings should have the same value',
        status integer not null comment 'Enabled/Disabled status',
        district_id bigint not null comment 'District reference',
        eirc_service_organization_id bigint comment 'Service organization reference',
        primary key (id)
    ) comment='Buildings';

    create table ab_countries_tbl (
        id bigint not null auto_increment,
        status integer not null,
        primary key (id)
    );

    create table ab_country_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        short_name varchar(5),
        country_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (country_id, language_id)
    );

    create table ab_district_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        district_name_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (district_name_id, language_id)
    );

    create table ab_district_names_tbl (
        id bigint not null auto_increment,
        district_id bigint not null,
        primary key (id)
    );

    create table ab_district_names_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        district_id bigint not null,
        district_name_id bigint,
        primary key (id)
    );

    create table ab_districts_tbl (
        id bigint not null auto_increment,
        town_id bigint not null,
        status integer not null,
        primary key (id)
    );

    create table ab_identity_type_translations_tbl (
        id bigint not null auto_increment,
        name varchar(150) not null comment 'Identity type translation',
        language_id bigint not null comment 'Language reference',
        identity_type_id bigint not null comment 'Identity type reference',
        primary key (id),
        unique (language_id, identity_type_id)
    ) comment='Person documents type translation';

    create table ab_identity_types_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled-disabled status',
        type_enum integer not null comment 'Identity type code',
        primary key (id)
    ) comment='Person documents type';

    create table ab_person_attributes_tbl (
        id bigint not null auto_increment,
        name varchar(50) not null comment 'Attribute name',
        value varchar(255) comment 'Attribute value',
        language_id bigint not null comment 'Language reference',
        person_id bigint not null comment 'Person reference',
        primary key (id)
    ) comment='Person attributes';

    create table ab_person_identities_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled-Disabled status',
        begin_date date not null comment 'Begin of document valid interval',
        end_date date not null comment 'End of document valid interval',
        birth_date datetime not null comment 'Person birth date',
        serial_number varchar(10) not null comment 'Document serial number',
        document_number varchar(20) not null comment 'Document number',
        first_name varchar(255) not null comment 'Person first name',
        middle_name varchar(255) not null comment 'Person middle name',
        last_name varchar(255) not null comment 'Person last name',
        organization varchar(4000) not null comment 'Organization gave document',
        is_default bit not null comment 'Default document flag',
        sex smallint not null comment 'Person sex type',
        identity_type_id bigint not null comment 'Identity document type reference',
        person_id bigint not null comment 'Person reference',
        primary key (id)
    ) comment='Person documents';

    create table ab_person_identity_attributes_tbl (
        id bigint not null auto_increment,
        name varchar(50) not null comment 'Attribute name',
        value varchar(255) comment 'Attribute value',
        language_id bigint not null comment 'Language reference',
        person_identity_id bigint not null comment 'Person identity reference',
        primary key (id)
    ) comment='Person document additional attributes';

    create table ab_person_registrations_tbl (
        id bigint not null auto_increment,
        begin_date date not null comment 'Registration begin date',
        end_date date not null comment 'Registration end date',
        person_id bigint not null comment 'Registered person reference',
        apartment_id bigint not null comment 'Registered to apartment reference',
        primary key (id)
    ) comment='Person registrations';

    create table ab_persons_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled-Disabled status',
        primary key (id)
    ) comment='Natural persons';

    create table ab_region_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        region_name_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (region_name_id, language_id)
    );

    create table ab_region_names_tbl (
        id bigint not null auto_increment,
        region_id bigint not null,
        primary key (id)
    );

    create table ab_region_names_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        region_id bigint not null,
        region_name_id bigint,
        primary key (id)
    );

    create table ab_regions_tbl (
        id bigint not null auto_increment,
        country_id bigint not null,
        status integer not null,
        primary key (id)
    );

    create table ab_street_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        street_name_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (street_name_id, language_id)
    );

    create table ab_street_names_tbl (
        id bigint not null auto_increment,
        street_id bigint not null,
        primary key (id)
    );

    create table ab_street_names_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        street_id bigint not null comment 'Street reference',
        street_name_id bigint comment 'Street name reference',
        primary key (id)
    ) comment='Street name temporals';

    create table ab_street_type_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        short_name varchar(255),
        language_id bigint not null,
        street_type_id bigint not null,
        primary key (id),
        unique (language_id, street_type_id)
    );

    create table ab_street_types_tbl (
        id bigint not null auto_increment,
        status integer not null,
        primary key (id)
    );

    create table ab_street_types_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        street_id bigint not null comment 'Street reference',
        street_type_id bigint comment 'Street type reference',
        primary key (id)
    ) comment='Street type temporals';

    create table ab_streets_districts_tbl (
        id bigint not null auto_increment,
        street_id bigint not null,
        district_id bigint not null,
        primary key (id),
        unique (street_id, district_id)
    );

    create table ab_streets_tbl (
        id bigint not null auto_increment,
        town_id bigint not null,
        status integer not null,
        primary key (id)
    );

    create table ab_town_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        town_name_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (town_name_id, language_id)
    );

    create table ab_town_names_tbl (
        id bigint not null auto_increment,
        town_id bigint not null,
        primary key (id)
    );

    create table ab_town_names_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        town_id bigint not null,
        town_name_id bigint,
        primary key (id)
    );

    create table ab_town_type_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        short_name varchar(255),
        language_id bigint,
        town_type_id bigint,
        primary key (id),
        unique (language_id, town_type_id)
    );

    create table ab_town_types_tbl (
        id bigint not null auto_increment,
        status integer not null,
        primary key (id)
    );

    create table ab_town_types_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        town_id bigint not null,
        town_type_id bigint,
        primary key (id)
    );

    create table ab_towns_tbl (
        id bigint not null auto_increment,
        region_id bigint not null,
        status integer not null,
        primary key (id)
    );

    create table bti_apartment_attribute_type_enum_values_tbl (
        id bigint not null auto_increment,
        order_value integer not null comment 'Relatiove order value',
        value varchar(255) not null comment 'Enum value',
        attribute_type_enum_id bigint not null comment 'Attribute type enum reference',
        primary key (id)
    ) comment='Values for enumeration attribute types';

    create table bti_apartment_attribute_type_group_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        group_id bigint not null comment 'Apartment attribute type group reference',
        primary key (id),
        unique (language_id, group_id)
    ) comment='Apartment attribute type translations';

    create table bti_apartment_attribute_type_groups_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/disabled status',
        primary key (id)
    ) comment='Apartment attribute type groups';

    create table bti_apartment_attribute_type_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        attribute_type_id bigint not null comment 'Apartment attribute type reference',
        primary key (id),
        unique (language_id, attribute_type_id)
    ) comment='Apartment attribute type translations';

    create table bti_apartment_attribute_types_tbl (
        id bigint not null auto_increment,
        discriminator varchar(255) not null comment 'Class hierarchy descriminator',
        status integer not null comment 'Enabled/Disabled status',
        unique_code varchar(255) comment 'Internal unique code',
        is_temporal integer not null comment 'Temporal flag',
        group_id bigint not null comment 'Attribute group reference',
        primary key (id)
    ) comment='Apartment attribute types';

    create table bti_apartment_attributes_tbl (
        id bigint not null auto_increment,
        apartment_id bigint not null comment 'Apartment reference',
        attribute_type_id bigint not null comment 'Attribute type reference',
        date_value datetime comment 'Optional date value',
        int_value integer comment 'Optional int value',
        bool_value bit comment 'Optional boolean value',
        long_value bigint comment 'Optional long value',
        string_value varchar(255) comment 'Optional string value',
        double_value double precision comment 'Optional double value',
        decimal_value decimal(19,5) comment 'Optional double value',
        value_type integer not null comment 'Value type discriminator',
        begin_date date comment 'Attribute value begin date',
        end_date date comment 'Attribute value end date',
        temporal_flag integer not null comment 'Temporal attribute flag',
        primary key (id)
    ) comment='Apartment attributes';

    create table bti_building_attribute_type_enum_values_tbl (
        id bigint not null auto_increment,
        order_value integer not null comment 'Relatiove order value',
        value varchar(255) not null comment 'Enum value',
        attribute_type_enum_id bigint not null comment 'Attribute type enum reference',
        primary key (id)
    ) comment='Values for enumeration attribute types';

    create table bti_building_attribute_type_group_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        group_id bigint not null comment 'Building attribute type group reference',
        primary key (id),
        unique (language_id, group_id)
    ) comment='Building attribute type translations';

    create table bti_building_attribute_type_groups_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/disabled status',
        primary key (id)
    ) comment='Building attribute type groups';

    create table bti_building_attribute_type_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        attribute_type_id bigint not null comment 'Building attribute type reference',
        primary key (id),
        unique (language_id, attribute_type_id)
    ) comment='Building attribute type translations';

    create table bti_building_attribute_types_tbl (
        id bigint not null auto_increment,
        discriminator varchar(255) not null comment 'Class hierarchy descriminator',
        status integer not null comment 'Enabled/Disabled status',
        unique_code varchar(255) comment 'Internal unique code',
        is_temporal integer not null comment 'Temporal flag',
        group_id bigint not null comment 'Attribute group reference',
        primary key (id)
    ) comment='Building attribute types';

    create table bti_building_attributes_tbl (
        id bigint not null auto_increment,
        building_id bigint not null comment 'Building reference',
        attribute_type_id bigint not null comment 'Attribute type reference',
        date_value datetime comment 'Optional date value',
        int_value integer comment 'Optional int value',
        bool_value bit comment 'Optional boolean value',
        long_value bigint comment 'Optional long value',
        string_value varchar(255) comment 'Optional string value',
        double_value double precision comment 'Optional double value',
        decimal_value decimal(19,5) comment 'Optional double value',
        value_type integer not null comment 'Value type discriminator',
        begin_date date comment 'Attribute value begin date',
        end_date date comment 'Attribute value end date',
        temporal_flag integer not null comment 'Temporal attribute flag',
        primary key (id)
    ) comment='Building attributes';

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
        service_provider_id bigint comment 'Service provider reference',
        sender_organisation_id bigint comment 'Sender organization reference',
        recipient_organisation_id bigint comment 'Recipient organization reference',
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
        consumer_id bigint comment 'Consumer reference',
        person_id bigint comment 'Person reference',
        apartment_id bigint comment 'Apartment reference',
        service_id bigint comment 'Service reference',
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
        city varchar(255),
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

    create table common_users_tbl (
        id bigint not null auto_increment comment 'Primary key',
        discriminator varchar(255) not null comment 'Class hierarchy discriminator',
        full_name varchar(255) not null comment 'Full user name',
        last_name varchar(255) not null comment 'Last user name',
        first_name varchar(255) comment 'First user name',
        user_name varchar(255) not null unique comment 'User login name',
        language_code varchar(255) not null comment 'Preferred language ISO code',
        page_size integer comment 'Preferred listing page size',
        ab_country_filter bigint comment 'Country filter',
        ab_region_filter bigint comment 'Region filter',
        ab_town_filter bigint comment 'Town filter',
        payments_payment_point_id bigint comment 'User payment point',
        payment_collector_id bigint comment 'User payment collector id',
        primary key (id)
    ) comment='User details';

    create table config_payments_mbservices_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        mb_service_code varchar(255) not null unique comment 'MegaBank service code',
        mb_service_name varchar(255) not null unique comment 'MegaBank service name',
        service_type_id bigint not null unique comment 'Internal service type reference',
        primary key (id)
    ) comment='Mapping of MegaBank services to internal types';

    create table eirc_consumer_attribute_type_enum_values_tbl (
        id bigint not null auto_increment,
        order_value integer not null comment 'Relational order value',
        date_value datetime comment 'Optional date value',
        int_value integer comment 'Optional int value',
        bool_value bit comment 'Optional boolean value',
        long_value bigint comment 'Optional long value',
        string_value varchar(255) comment 'Optional string value',
        double_value double precision comment 'Optional double value',
        decimal_value decimal(19,5) comment 'Optional double value',
        value_type integer not null comment 'Value type discriminator',
        attribute_type_enum_id bigint not null comment 'Attribute type enum reference',
        primary key (id)
    ) comment='Values for enumeration attribute types';

    create table eirc_consumer_attribute_type_names_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        attribute_type_id bigint not null comment 'Consumer attribute type reference',
        primary key (id),
        unique (language_id, attribute_type_id)
    ) comment='Consumer attribute type translations';

    create table eirc_consumer_attribute_types_tbl (
        id bigint not null auto_increment comment 'Primary key',
        discriminator varchar(255) not null comment 'Class hierarchy discriminator',
        status integer not null comment 'Enabled-disabled status',
        version integer not null comment 'Optimistic lock version',
        unique_code varchar(255) comment 'Internal unique code',
        is_temporal integer not null comment 'Temporal flag',
        measure_unit_id bigint comment 'Optional measure unit reference',
        primary key (id)
    ) comment='Consumer attribute types';

    create table eirc_consumer_attributes_tbl (
        id bigint not null auto_increment comment 'Primary key',
        date_value datetime comment 'Optional date value',
        int_value integer comment 'Optional int value',
        bool_value bit comment 'Optional boolean value',
        long_value bigint comment 'Optional long value',
        string_value varchar(255) comment 'Optional string value',
        double_value double precision comment 'Optional double value',
        decimal_value decimal(19,5) comment 'Optional double value',
        value_type integer not null comment 'Value type discriminator',
        begin_date date comment 'Attribute value begin date',
        end_date date comment 'Attribute value end date',
        temporal_flag integer not null comment 'Temporal attribute flag',
        consumer_id bigint not null comment 'Consumer reference',
        type_id bigint not null comment 'Attribute type reference',
        primary key (id)
    ) comment='Consumer attribute types';

    create table eirc_consumer_infos_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'ConsumerInfo status',
        first_name varchar(255) comment 'Prividers consumer first name',
        middle_name varchar(255) comment 'Prividers consumer middle name',
        last_name varchar(255) comment 'Prividers consumer last name',
        city_name varchar(255) comment 'Prividers consumer city name',
        street_type_name varchar(255) comment 'Prividers consumer street type name',
        street_name varchar(255) comment 'Prividers consumer street name',
        building_number varchar(255) comment 'Prividers consumer building number',
        building_bulk varchar(255) comment 'Prividers consumer building bulk',
        apartment_number varchar(255) comment 'Prividers consumer apartment number',
        primary key (id)
    );

    create table eirc_consumers_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled-Disabled status',
        external_account_number varchar(255) not null comment 'Service providers internal account number',
        service_id bigint not null comment 'Service reference',
        person_id bigint comment 'Responsible person reference',
        apartment_id bigint not null comment 'Apartment reference',
        eirc_account_id bigint not null comment 'EIRC account reference',
        begin_date datetime not null comment 'Consumer begin date',
        end_date datetime not null comment 'Consumer end date',
        consumer_info_id bigint comment 'Service providers consumer details',
        primary key (id)
    ) comment='Consumer is a person that gets some service';

    create table eirc_eirc_accounts_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled-Disabled status',
        account_number varchar(255) not null comment 'EIRC account number',
        apartment_id bigint not null comment 'Apartment reference',
        person_id bigint comment 'Responsible person reference',
        consumer_info_id bigint comment 'Consumer info used to create account',
        primary key (id)
    ) comment='EIRC Personal accounts table';

    create table eirc_quittance_details_payments_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        payment_id bigint not null comment 'Quittances payment reference',
        payment_status_id bigint not null comment 'Payment status reference',
        details_id bigint not null comment 'Quittance details reference',
        amount decimal(19,2) not null comment 'Amount payed for quittance',
        primary key (id)
    ) comment='Quittance details payments';

    create table eirc_quittance_details_quittances_tbl (
        id bigint not null auto_increment,
        quittance_details_id bigint not null comment 'QuittanceDetails reference',
        quittance_id bigint not null comment 'Quittance reference',
        primary key (id)
    );

    create table eirc_quittance_details_tbl (
        id bigint not null auto_increment,
        consumer_id bigint not null comment 'Consumer reference',
        registry_record_id bigint not null comment 'Source registry record reference',
        incoming_balance decimal(19,2) comment 'incoming balance',
        outgoing_balance decimal(19,2) comment 'Outgoing balance',
        amount decimal(19,5) comment 'Amount',
        expence decimal(19,5) comment 'Expence',
        rate decimal(19,5) comment 'Rate',
        recalculation decimal(19,5) comment 'Recalculation',
        benefit decimal(19,5) comment 'Benefits amount',
        subsidy decimal(19,5) comment 'Subsidy amount',
        payment decimal(19,5) comment 'Payments amount for previous period',
        month datetime not null comment 'Quittance month',
        primary key (id)
    ) comment='Service provider quittance details';

    create table eirc_quittance_packets_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled-disabled status',
        packet_number bigint not null comment 'Packet number',
        creation_date datetime not null comment 'Creation date',
        begin_date datetime comment 'First quittance added date',
        close_date datetime comment 'Packet close date',
        payment_point_id bigint not null comment 'Payment point reference',
        control_quittances_number integer not null comment 'Control quittances number',
        control_overall_summ decimal(19,2) not null comment 'Control overall summ',
        quittances_number integer not null comment 'Inputed quittances number',
        overall_summ decimal(19,2) not null comment 'Inputed overall summ',
        creator_user_name varchar(255) not null comment 'User name that created packet',
        closer_user_name varchar(255) not null comment 'User name that closed packet',
        primary key (id)
    ) comment='Quittance payment packets';

    create table eirc_quittance_payment_statuses_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        code integer not null comment 'System known code',
        i18n_name varchar(255) not null comment 'Translation code',
        primary key (id)
    ) comment='Statuses of quittance payments';

    create table eirc_quittance_payments_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        packet_id bigint comment 'Optional quittances packet reference',
        payment_status_id bigint not null comment 'Payment status reference',
        quittance_id bigint not null comment 'Quittance reference',
        amount decimal(19,2) not null comment 'Amount payed for quittance',
        primary key (id)
    ) comment='Quittance payments';

    create table eirc_quittances_tbl (
        id bigint not null auto_increment,
        service_organization_id bigint not null comment 'Service organization reference',
        eirc_account_id bigint not null comment 'Eirc account reference',
        order_number integer not null comment 'quittance order number for date till',
        date_from datetime not null comment 'Quittance date from',
        date_till datetime not null comment 'Quittance date till',
        creation_date datetime not null comment 'Quittance creation date',
        primary key (id)
    ) comment='Quittance';

    create table orgs_bank_accounts_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optiomistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        account_number varchar(255) not null comment 'Bank account number',
        is_default bit not null comment 'Juridical person default account flag',
        bank_id bigint not null comment 'Bank reference',
        organization_id bigint not null comment 'Juridical person (organization) reference',
        primary key (id)
    ) comment='Bank accounts';

    create table orgs_bank_descriptions_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        bank_id bigint not null comment 'Bank reference',
        primary key (id),
        unique (language_id, bank_id)
    ) comment='Bank desriptions';

    create table orgs_banks_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        bank_identifier_code varchar(255) not null comment 'Bank identifier code (BIK)',
        corresponding_account varchar(255) not null comment 'Corresponding Central Bank account',
        organization_id bigint not null comment 'Organization reference',
        primary key (id)
    ) comment='Banks';

    create table orgs_cashbox_name_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        name varchar(255) not null comment 'Name',
        language_id bigint not null comment 'Language reference',
        cashbox_id bigint not null comment 'Cashbox reference',
        primary key (id),
        unique (language_id, cashbox_id)
    );

    create table orgs_cashboxes_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Cashbox status',
        payment_point_id bigint not null comment 'Payment point reference',
        primary key (id)
    ) comment='Cashboxes table';

    create table orgs_organization_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        organization_id bigint not null comment 'Organization reference',
        primary key (id),
        unique (language_id, organization_id)
    );

    create table orgs_organization_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Name value',
        language_id bigint not null comment 'Language reference',
        organization_id bigint not null comment 'Organization reference',
        primary key (id),
        unique (language_id, organization_id)
    );

    create table orgs_organizations_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        individual_tax_number varchar(255) not null,
        kpp varchar(255) not null,
        juridical_address varchar(255) not null comment 'Juridical address',
        postal_address varchar(255) not null comment 'Postal address',
        data_source_description_id bigint not null comment 'Data source description reference',
        primary key (id)
    );

    create table orgs_payment_collectors_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        collector_id bigint not null comment 'Payment collector reference',
        primary key (id),
        unique (language_id, collector_id)
    ) comment='Payment collector desriptions';

    create table orgs_payment_collectors_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        email varchar(255) comment 'Collector email address',
        organization_id bigint not null comment 'Organization reference',
        primary key (id)
    ) comment='Payment collectors';

    create table orgs_payment_point_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Name value',
        language_id bigint not null comment 'Language reference',
        payment_point_id bigint not null comment 'Payment point reference',
        primary key (id),
        unique (language_id, payment_point_id)
    );

    create table orgs_payment_points_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled-disabled status',
        address varchar(255) not null comment 'Address',
        tradingDayProcessInstance_Id bigint comment 'Trading date process instance id',
        collector_id bigint not null comment 'Payments collector reference',
        primary key (id)
    ) comment='Payment points';

    create table orgs_service_organization_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        service_organization_id bigint not null comment 'Organization reference',
        primary key (id),
        unique (language_id, service_organization_id)
    ) comment='Service organization descriptions';

    create table orgs_service_organizations_tbl (
        id bigint not null auto_increment,
        org_type varchar(255) not null comment 'Class hierarchy descriminator, all entities should have the same value',
        status integer not null comment 'Enabled/Disabled status',
        organization_id bigint not null comment 'Organization reference',
        primary key (id)
    ) comment='Service organizations';

    create table orgs_service_provider_descriptions_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        service_provider_id bigint not null comment 'Service provider reference',
        primary key (id),
        unique (language_id, service_provider_id)
    );

    create table orgs_service_providers_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled-disabled status',
        email varchar(255) comment 'E-mail',
        organization_id bigint not null comment 'Organization reference',
        primary key (id)
    );

    create table orgs_subdivision_descriptions_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        subdivision_id bigint not null comment 'Subdivision reference',
        primary key (id),
        unique (language_id, subdivision_id)
    );

    create table orgs_subdivision_names_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        name varchar(255) not null comment 'Name value',
        language_id bigint not null comment 'Language reference',
        subdivision_id bigint not null comment 'Subdivision reference',
        primary key (id),
        unique (language_id, subdivision_id)
    );

    create table orgs_subdivisions_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        real_address varchar(255) not null comment 'Subdivision real address',
        tree_path varchar(255) not null comment 'Subdivisions tree branch path',
        parent_subdivision_id bigint comment 'Parent subdivision reference if any',
        head_organization_id bigint not null comment 'Head organization reference',
        juridical_person_id bigint comment 'Juridical person (organization) reference if any',
        primary key (id)
    ) comment='Organization subdivisions';

    create table payments_document_addition_type_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        type_id bigint not null comment 'Operation document addition type reference',
        primary key (id),
        unique (language_id, type_id)
    ) comment='Operation document addition type translations';

    create table payments_document_addition_types_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        code integer comment 'Document addition type code',
        primary key (id)
    ) comment='Operation document addition types';

    create table payments_document_additions_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        date_value datetime comment 'Optional date value',
        int_value integer comment 'Optional int value',
        bool_value bit comment 'Optional boolean value',
        long_value bigint comment 'Optional long value',
        string_value varchar(255) comment 'Optional string value',
        double_value double precision comment 'Optional double value',
        decimal_value decimal(19,5) comment 'Optional decimal value',
        value_type integer not null comment 'Value type discriminator',
        addition_type_id bigint not null comment 'Addition type reference',
        document_id bigint not null comment 'Document reference',
        primary key (id)
    ) comment='Operation document additions';

    create table payments_document_status_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        status_id bigint not null comment 'Operation document status reference',
        primary key (id),
        unique (language_id, status_id)
    ) comment='Operation document status translations';

    create table payments_document_statuses_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        code integer not null unique comment 'Status unique code',
        primary key (id)
    ) comment='Operation document statuses';

    create table payments_document_type_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        type_id bigint not null comment 'Operation document type reference',
        primary key (id),
        unique (language_id, type_id)
    ) comment='Operation document type translations';

    create table payments_document_types_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        code integer not null unique comment 'Type unique code',
        primary key (id)
    ) comment='Operation document types';

    create table payments_documents_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        summ decimal(19,2) not null comment 'Summ',
        debt decimal(19,2) comment 'Debt summ',
        address varchar(255) comment 'Payer address',
        payer_fio varchar(255) comment 'Payer first-middle-last names',
        creditor_id varchar(255) comment 'Creditor key',
        debtor_id varchar(255) comment 'Debtor key',
        first_name varchar(255) comment 'Prividers consumer first name',
        middle_name varchar(255) comment 'Prividers consumer middle name',
        last_name varchar(255) comment 'Prividers consumer last name',
        country varchar(255) comment 'Prividers consumer country name',
        region varchar(255) comment 'Prividers consumer region name',
        town varchar(255) comment 'Prividers consumer town name',
        street_type varchar(255) comment 'Prividers consumer street type name',
        street_name varchar(255) comment 'Prividers consumer street name',
        building_number varchar(255) comment 'Prividers consumer building number',
        building_bulk varchar(255) comment 'Prividers consumer building bulk',
        apartment_number varchar(255) comment 'Prividers consumer apartment number',
        creditor_organization_id bigint not null comment 'Creditor organization reference',
        debtor_organization_id bigint not null comment 'Debtor organization reference',
        operation_id bigint not null comment 'Operation reference',
        reference_document_id bigint comment 'Reference document reference',
        registry_record_id bigint comment 'Optional registry record reference',
        type_id bigint not null comment 'Document type reference',
        status_id bigint not null comment 'Document type reference',
        service_id bigint not null comment 'Service reference',
        primary key (id)
    ) comment='Operation document';

    create table payments_operation_addition_type_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        type_id bigint not null comment 'Operation addition type reference',
        primary key (id),
        unique (language_id, type_id)
    ) comment='Operation addition type translations';

    create table payments_operation_addition_types_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        primary key (id)
    ) comment='Operation addition types';

    create table payments_operation_additions_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        date_value datetime comment 'Optional date value',
        int_value integer comment 'Optional int value',
        bool_value bit comment 'Optional boolean value',
        long_value bigint comment 'Optional long value',
        string_value varchar(255) comment 'Optional string value',
        double_value double precision comment 'Optional double value',
        value_type integer not null comment 'Value type discriminator',
        decimal_value decimal(19,5) comment 'Optional decimal value',
        addition_type_id bigint not null comment 'Addition type reference',
        operation_id bigint not null comment 'Operation reference',
        primary key (id)
    ) comment='Operation additions';

    create table payments_operation_level_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        level_id bigint not null comment 'Operation level reference',
        primary key (id),
        unique (language_id, level_id)
    ) comment='Operation level translations';

    create table payments_operation_levels_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        code integer not null unique comment 'Level code',
        primary key (id)
    ) comment='Operation levels';

    create table payments_operation_status_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        status_id bigint not null comment 'Operation status reference',
        primary key (id),
        unique (language_id, status_id)
    ) comment='Operation status translations';

    create table payments_operation_statuses_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        code integer not null unique comment 'Status code',
        primary key (id)
    ) comment='Operation statuses';

    create table payments_operation_type_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        type_id bigint not null comment 'Operation type reference',
        primary key (id),
        unique (language_id, type_id)
    ) comment='Operation type translations';

    create table payments_operation_types_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        code integer not null unique comment 'Type code',
        primary key (id)
    ) comment='Operation types';

    create table payments_operations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        operation_summ decimal(19,2) comment 'Operation summ',
        operation_input_summ decimal(19,2) comment 'Operation input summ',
        change_summ decimal(19,2) comment 'Change',
        creator varchar(255) comment 'Creator username',
        creation_date datetime comment 'Creation date',
        register_user varchar(255) comment 'Register username',
        register_date datetime comment 'Operation registration date',
        address varchar(255) comment 'Payer address',
        payer_fio varchar(255) comment 'Payer first-middle-last names',
        cashier_fio varchar(255) comment 'Cashier full name',
        level_id bigint not null comment 'Operation level reference',
        status_id bigint not null comment 'Operation status reference',
        type_id bigint not null comment 'Operation type reference (operation code)',
        creator_organization_id bigint comment 'Organization operation created in',
        payment_point_id bigint comment 'Payment point operation created in',
        register_organization_id bigint comment 'Organization operation registered in',
        registry_record_id bigint comment 'Registry record',
        reference_operation_id bigint comment 'Optional operation reference',
        cashbox_id bigint comment 'Cash box',
        primary key (id)
    ) comment='Operations';

    create table payments_registry_delivery_history_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        registry_id bigint not null comment 'Registry reference',
        delivery_date datetime not null comment 'Delivery date',
        email varchar(255) not null comment 'E-mail',
        file_id bigint not null comment 'File reference',
        primary key (id)
    ) comment='Registry delivery history';

    create table payments_service_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        language_id bigint not null comment 'Language reference',
        service_id bigint not null comment 'Service reference',
        primary key (id),
        unique (language_id, service_id)
    );

    create table payments_service_provider_attribute_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        name varchar(50) not null comment 'Attribute name',
        service_provider_id bigint not null comment 'Service provider reference',
        value varchar(255) comment 'Attribute value',
        primary key (id),
        unique (name, service_provider_id)
    ) comment='ServiceProviderAttribute';

    create table payments_service_type_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null,
        description varchar(255) not null,
        language_id bigint not null comment 'Language reference',
        service_type_id bigint not null comment 'Service type reference',
        primary key (id),
        unique (language_id, service_type_id)
    );

    create table payments_service_types_tbl (
        id bigint not null auto_increment,
        status integer not null,
        code integer not null,
        primary key (id)
    );

    create table payments_services_tbl (
        id bigint not null auto_increment comment 'Primary key',
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled-disabled status',
        external_code varchar(255) comment 'Service providers internal service code',
        begin_date date not null comment 'The Date service is valid from',
        end_date date not null comment 'The Date service is valid till',
        provider_id bigint not null comment 'Service provider reference',
        type_id bigint not null comment 'Service type reference',
        measure_unit_id bigint comment 'Measure unit reference',
        parent_service_id bigint comment 'If parent service reference present service is a subservice',
        primary key (id)
    ) comment='Services';

    create index indx_value on ab_apartment_numbers_tbl (value);

    alter table ab_apartment_numbers_tbl 
        add index FK_ab_apartment_numbers_tbl_apartment_id (apartment_id), 
        add constraint FK_ab_apartment_numbers_tbl_apartment_id 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table ab_apartments_tbl 
        add index ab_apartments_tbl_building_id (building_id), 
        add constraint ab_apartments_tbl_building_id 
        foreign key (building_id) 
        references ab_buildings_tbl (id);

    alter table ab_building_address_attribute_type_translations_tbl 
        add index ab_building_attribute_type_translations_tbl_attribute_type_id (attribute_type_id), 
        add constraint ab_building_attribute_type_translations_tbl_attribute_type_id 
        foreign key (attribute_type_id) 
        references ab_building_address_attribute_types_tbl (id);

    alter table ab_building_address_attribute_type_translations_tbl 
        add index lang_building_attribute_type_pair_language_id (language_id), 
        add constraint lang_building_attribute_type_pair_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    create index indx_value on ab_building_address_attributes_tbl (value);

    alter table ab_building_address_attributes_tbl 
        add index ab_building_attributes_tbl_buildings_id (buildings_id), 
        add constraint ab_building_attributes_tbl_buildings_id 
        foreign key (buildings_id) 
        references ab_building_addresses_tbl (id);

    alter table ab_building_address_attributes_tbl 
        add index ab_building_attributes_tbl_attribute_type_id (attribute_type_id), 
        add constraint ab_building_attributes_tbl_attribute_type_id 
        foreign key (attribute_type_id) 
        references ab_building_address_attribute_types_tbl (id);

    alter table ab_building_addresses_tbl 
        add index ab_buildingses_tbl_street_id (street_id), 
        add constraint ab_buildingses_tbl_street_id 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table ab_building_addresses_tbl 
        add index ab_buildingses_tbl_building_id (building_id), 
        add constraint ab_buildingses_tbl_building_id 
        foreign key (building_id) 
        references ab_buildings_tbl (id);

    alter table ab_building_statuses_tbl 
        add index ab_building_statuses_tbl_building_id (building_id), 
        add constraint ab_building_statuses_tbl_building_id 
        foreign key (building_id) 
        references ab_buildings_tbl (id);

    alter table ab_buildings_tbl 
        add index FK_eirc_building_service_organization (eirc_service_organization_id), 
        add constraint FK_eirc_building_service_organization 
        foreign key (eirc_service_organization_id) 
        references orgs_service_organizations_tbl (id);

    alter table ab_buildings_tbl 
        add index ab_buildings_tbl_district_id (district_id), 
        add constraint ab_buildings_tbl_district_id 
        foreign key (district_id) 
        references ab_districts_tbl (id);

    alter table ab_country_name_translations_tbl 
        add index FK31EC318E9E89EB47 (country_id), 
        add constraint FK31EC318E9E89EB47 
        foreign key (country_id) 
        references ab_countries_tbl (id);

    alter table ab_country_name_translations_tbl 
        add index FK31EC318E61F37403 (language_id), 
        add constraint FK31EC318E61F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_district_name_translations_tbl 
        add index FKD194B702398B1DAA (district_name_id), 
        add constraint FKD194B702398B1DAA 
        foreign key (district_name_id) 
        references ab_district_names_tbl (id);

    alter table ab_district_name_translations_tbl 
        add index FKD194B70261F37403 (language_id), 
        add constraint FKD194B70261F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_district_names_tbl 
        add index FK7A70D3B41AE9F4D (district_id), 
        add constraint FK7A70D3B41AE9F4D 
        foreign key (district_id) 
        references ab_districts_tbl (id);

    alter table ab_district_names_temporal_tbl 
        add index FK6525F5EB1AE9F4D (district_id), 
        add constraint FK6525F5EB1AE9F4D 
        foreign key (district_id) 
        references ab_districts_tbl (id);

    alter table ab_district_names_temporal_tbl 
        add index FK6525F5EB398B1DAA (district_name_id), 
        add constraint FK6525F5EB398B1DAA 
        foreign key (district_name_id) 
        references ab_district_names_tbl (id);

    alter table ab_districts_tbl 
        add index FK79F1E386712C324D (town_id), 
        add constraint FK79F1E386712C324D 
        foreign key (town_id) 
        references ab_towns_tbl (id);

    alter table ab_identity_type_translations_tbl 
        add index ab_identity_type_translations_tbl_identity_type_id (identity_type_id), 
        add constraint ab_identity_type_translations_tbl_identity_type_id 
        foreign key (identity_type_id) 
        references ab_identity_types_tbl (id);

    alter table ab_identity_type_translations_tbl 
        add index ab_identity_type_translations_tbl_language_id (language_id), 
        add constraint ab_identity_type_translations_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_person_attributes_tbl 
        add index ab_person_attributes_tbl_person_id (person_id), 
        add constraint ab_person_attributes_tbl_person_id 
        foreign key (person_id) 
        references ab_persons_tbl (id);

    alter table ab_person_attributes_tbl 
        add index ab_person_attributes_tbl_language_id (language_id), 
        add constraint ab_person_attributes_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    create index data_index on ab_person_identities_tbl (first_name, middle_name, last_name);

    alter table ab_person_identities_tbl 
        add index ab_person_identities_tbl_identity_type_id (identity_type_id), 
        add constraint ab_person_identities_tbl_identity_type_id 
        foreign key (identity_type_id) 
        references ab_identity_types_tbl (id);

    alter table ab_person_identities_tbl 
        add index ab_person_identities_tbl_person_id (person_id), 
        add constraint ab_person_identities_tbl_person_id 
        foreign key (person_id) 
        references ab_persons_tbl (id);

    alter table ab_person_identity_attributes_tbl 
        add index ab_person_identity_attributes_tbl_person_identity_id (person_identity_id), 
        add constraint ab_person_identity_attributes_tbl_person_identity_id 
        foreign key (person_identity_id) 
        references ab_person_identities_tbl (id);

    alter table ab_person_identity_attributes_tbl 
        add index ab_person_identity_attributes_tbl_language_id (language_id), 
        add constraint ab_person_identity_attributes_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_person_registrations_tbl 
        add index FP_ab_person_registrations_person (person_id), 
        add constraint FP_ab_person_registrations_person 
        foreign key (person_id) 
        references ab_persons_tbl (id);

    alter table ab_person_registrations_tbl 
        add index FP_ab_person_registrations_apartment (apartment_id), 
        add constraint FP_ab_person_registrations_apartment 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table ab_region_name_translations_tbl 
        add index FK3DB8D968D605B436 (region_name_id), 
        add constraint FK3DB8D968D605B436 
        foreign key (region_name_id) 
        references ab_region_names_tbl (id);

    alter table ab_region_name_translations_tbl 
        add index FK3DB8D96861F37403 (language_id), 
        add constraint FK3DB8D96861F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_region_names_tbl 
        add index FKF918DF1A458E164D (region_id), 
        add constraint FKF918DF1A458E164D 
        foreign key (region_id) 
        references ab_regions_tbl (id);

    alter table ab_region_names_temporal_tbl 
        add index FK609D5D45D605B436 (region_name_id), 
        add constraint FK609D5D45D605B436 
        foreign key (region_name_id) 
        references ab_region_names_tbl (id);

    alter table ab_region_names_temporal_tbl 
        add index FK609D5D45458E164D (region_id), 
        add constraint FK609D5D45458E164D 
        foreign key (region_id) 
        references ab_regions_tbl (id);

    alter table ab_regions_tbl 
        add index FK61DDD0609E89EB47 (country_id), 
        add constraint FK61DDD0609E89EB47 
        foreign key (country_id) 
        references ab_countries_tbl (id);

    alter table ab_street_name_translations_tbl 
        add index FK72F93D37D80067D4 (street_name_id), 
        add constraint FK72F93D37D80067D4 
        foreign key (street_name_id) 
        references ab_street_names_tbl (id);

    alter table ab_street_name_translations_tbl 
        add index FK72F93D3761F37403 (language_id), 
        add constraint FK72F93D3761F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_street_names_tbl 
        add index FK496D4169311847ED (street_id), 
        add constraint FK496D4169311847ED 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table ab_street_names_temporal_tbl 
        add index FK_ab_street_names_temporal_tbl_street_id (street_id), 
        add constraint FK_ab_street_names_temporal_tbl_street_id 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table ab_street_names_temporal_tbl 
        add index FK_ab_street_names_temporal_tbl_street_name_id (street_name_id), 
        add constraint FK_ab_street_names_temporal_tbl_street_name_id 
        foreign key (street_name_id) 
        references ab_street_names_tbl (id);

    alter table ab_street_type_translations_tbl 
        add index FKDEBA3C683E877574 (street_type_id), 
        add constraint FKDEBA3C683E877574 
        foreign key (street_type_id) 
        references ab_street_types_tbl (id);

    alter table ab_street_type_translations_tbl 
        add index FKDEBA3C6861F37403 (language_id), 
        add constraint FKDEBA3C6861F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_street_types_temporal_tbl 
        add index FK_ab_street_types_temporal_tbl_street_id (street_id), 
        add constraint FK_ab_street_types_temporal_tbl_street_id 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table ab_street_types_temporal_tbl 
        add index FK_ab_street_types_temporal_tbl_street_type_id (street_type_id), 
        add constraint FK_ab_street_types_temporal_tbl_street_type_id 
        foreign key (street_type_id) 
        references ab_street_types_tbl (id);

    alter table ab_streets_districts_tbl 
        add index FK_ab_streets_districts_tbl_street_id (street_id), 
        add constraint FK_ab_streets_districts_tbl_street_id 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table ab_streets_districts_tbl 
        add index FK_ab_streets_districts_tbl_district_id (district_id), 
        add constraint FK_ab_streets_districts_tbl_district_id 
        foreign key (district_id) 
        references ab_districts_tbl (id);

    alter table ab_streets_tbl 
        add index FKFFBAF8B1712C324D (town_id), 
        add constraint FKFFBAF8B1712C324D 
        foreign key (town_id) 
        references ab_towns_tbl (id);

    alter table ab_town_name_translations_tbl 
        add index FKE4BB206B6638732 (town_name_id), 
        add constraint FKE4BB206B6638732 
        foreign key (town_name_id) 
        references ab_town_names_tbl (id);

    alter table ab_town_name_translations_tbl 
        add index FKE4BB20661F37403 (language_id), 
        add constraint FKE4BB20661F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_town_names_tbl 
        add index FK85A534B8712C324D (town_id), 
        add constraint FK85A534B8712C324D 
        foreign key (town_id) 
        references ab_towns_tbl (id);

    alter table ab_town_names_temporal_tbl 
        add index FK59747967B6638732 (town_name_id), 
        add constraint FK59747967B6638732 
        foreign key (town_name_id) 
        references ab_town_names_tbl (id);

    alter table ab_town_names_temporal_tbl 
        add index FK59747967712C324D (town_id), 
        add constraint FK59747967712C324D 
        foreign key (town_id) 
        references ab_towns_tbl (id);

    alter table ab_town_type_translations_tbl 
        add index FK7A0CB1371CEA94D2 (town_type_id), 
        add constraint FK7A0CB1371CEA94D2 
        foreign key (town_type_id) 
        references ab_town_types_tbl (id);

    alter table ab_town_type_translations_tbl 
        add index FK7A0CB13761F37403 (language_id), 
        add constraint FK7A0CB13761F37403 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table ab_town_types_temporal_tbl 
        add index FK29822FD61CEA94D2 (town_type_id), 
        add constraint FK29822FD61CEA94D2 
        foreign key (town_type_id) 
        references ab_town_types_tbl (id);

    alter table ab_town_types_temporal_tbl 
        add index FK29822FD6712C324D (town_id), 
        add constraint FK29822FD6712C324D 
        foreign key (town_id) 
        references ab_towns_tbl (id);

    alter table ab_towns_tbl 
        add index FK23FDF002458E164D (region_id), 
        add constraint FK23FDF002458E164D 
        foreign key (region_id) 
        references ab_regions_tbl (id);

    alter table bti_apartment_attribute_type_enum_values_tbl 
        add index bti_apartment_attribute_type_enum_values_tbl_enum_id (attribute_type_enum_id), 
        add constraint bti_apartment_attribute_type_enum_values_tbl_enum_id 
        foreign key (attribute_type_enum_id) 
        references bti_apartment_attribute_types_tbl (id);

    alter table bti_apartment_attribute_type_group_names_tbl 
        add index FK_bti_apartment_attribute_type_group_names_tbl_group_id (group_id), 
        add constraint FK_bti_apartment_attribute_type_group_names_tbl_group_id 
        foreign key (group_id) 
        references bti_apartment_attribute_type_groups_tbl (id);

    alter table bti_apartment_attribute_type_group_names_tbl 
        add index FK_bti_apartment_attribute_type_names_tbl_language_id (language_id), 
        add constraint FK_bti_apartment_attribute_type_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table bti_apartment_attribute_type_names_tbl 
        add index bti_apartment_attribute_type_names_tbl_attribute_type_id (attribute_type_id), 
        add constraint bti_apartment_attribute_type_names_tbl_attribute_type_id 
        foreign key (attribute_type_id) 
        references bti_apartment_attribute_types_tbl (id);

    alter table bti_apartment_attribute_type_names_tbl 
        add index bti_apartment_attribute_type_names_tbl_language_id (language_id), 
        add constraint bti_apartment_attribute_type_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table bti_apartment_attribute_types_tbl 
        add index bti_apartment_attribute_types_tbl (group_id), 
        add constraint bti_apartment_attribute_types_tbl 
        foreign key (group_id) 
        references bti_apartment_attribute_type_groups_tbl (id);

    alter table bti_apartment_attributes_tbl 
        add index bti_apartment_attributes_tbl_attribute_type_id (attribute_type_id), 
        add constraint bti_apartment_attributes_tbl_attribute_type_id 
        foreign key (attribute_type_id) 
        references bti_apartment_attribute_types_tbl (id);

    alter table bti_apartment_attributes_tbl 
        add index FK_bti_apartment_attributes_tbl_apartment_id (apartment_id), 
        add constraint FK_bti_apartment_attributes_tbl_apartment_id 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table bti_building_attribute_type_enum_values_tbl 
        add index bti_building_attribute_type_enum_values_tbl_enum_id (attribute_type_enum_id), 
        add constraint bti_building_attribute_type_enum_values_tbl_enum_id 
        foreign key (attribute_type_enum_id) 
        references bti_building_attribute_types_tbl (id);

    alter table bti_building_attribute_type_group_names_tbl 
        add index FK_bti_building_attribute_type_group_names_tbl_group_id (group_id), 
        add constraint FK_bti_building_attribute_type_group_names_tbl_group_id 
        foreign key (group_id) 
        references bti_building_attribute_type_groups_tbl (id);

    alter table bti_building_attribute_type_group_names_tbl 
        add index FK_bti_building_attribute_type_names_tbl_language_id (language_id), 
        add constraint FK_bti_building_attribute_type_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table bti_building_attribute_type_names_tbl 
        add index bti_building_attribute_type_names_tbl_attribute_type_id (attribute_type_id), 
        add constraint bti_building_attribute_type_names_tbl_attribute_type_id 
        foreign key (attribute_type_id) 
        references bti_building_attribute_types_tbl (id);

    alter table bti_building_attribute_type_names_tbl 
        add index bti_building_attribute_type_names_tbl_language_id (language_id), 
        add constraint bti_building_attribute_type_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table bti_building_attribute_types_tbl 
        add index bti_building_attribute_types_tbl (group_id), 
        add constraint bti_building_attribute_types_tbl 
        foreign key (group_id) 
        references bti_building_attribute_type_groups_tbl (id);

    alter table bti_building_attributes_tbl 
        add index bti_building_attributes_tbl_attribute_type_id (attribute_type_id), 
        add constraint bti_building_attributes_tbl_attribute_type_id 
        foreign key (attribute_type_id) 
        references bti_building_attribute_types_tbl (id);

    alter table bti_building_attributes_tbl 
        add index FK_bti_building_attributes_tbl_building_id (building_id), 
        add constraint FK_bti_building_attributes_tbl_building_id 
        foreign key (building_id) 
        references ab_buildings_tbl (id);

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
        add index FK_common_registry_properties_tbl_service_provider_id (service_provider_id), 
        add constraint FK_common_registry_properties_tbl_service_provider_id 
        foreign key (service_provider_id) 
        references orgs_service_providers_tbl (id);

    alter table common_registry_properties_tbl 
        add index FK_common_registry_properties_tbl_registry_id (registry_id), 
        add constraint FK_common_registry_properties_tbl_registry_id 
        foreign key (registry_id) 
        references common_registries_tbl (id);

    alter table common_registry_properties_tbl 
        add index FK_common_registry_properties_tbl_recipient_organisation_id (recipient_organisation_id), 
        add constraint FK_common_registry_properties_tbl_recipient_organisation_id 
        foreign key (recipient_organisation_id) 
        references orgs_organizations_tbl (id);

    alter table common_registry_properties_tbl 
        add index FK_common_registry_properties_tbl_sender_organisation_id (sender_organisation_id), 
        add constraint FK_common_registry_properties_tbl_sender_organisation_id 
        foreign key (sender_organisation_id) 
        references orgs_organizations_tbl (id);

    alter table common_registry_record_containers_tbl 
        add index FK_common_registry_record_containers_tbl_record_id (record_id), 
        add constraint FK_common_registry_record_containers_tbl_record_id 
        foreign key (record_id) 
        references common_registry_records_tbl (id);

    alter table common_registry_record_properties_tbl 
        add index FK_common_registry_record_properties_tbl_person_id (person_id), 
        add constraint FK_common_registry_record_properties_tbl_person_id 
        foreign key (person_id) 
        references ab_persons_tbl (id);

    alter table common_registry_record_properties_tbl 
        add index FK_common_registry_record_properties_tbl_apartment_id (apartment_id), 
        add constraint FK_common_registry_record_properties_tbl_apartment_id 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table common_registry_record_properties_tbl 
        add index FK_common_registry_record_properties_tbl_consumer_id (consumer_id), 
        add constraint FK_common_registry_record_properties_tbl_consumer_id 
        foreign key (consumer_id) 
        references eirc_consumers_tbl (id);

    alter table common_registry_record_properties_tbl 
        add index FK_common_registry_record_properties_tbl_service_id (service_id), 
        add constraint FK_common_registry_record_properties_tbl_service_id 
        foreign key (service_id) 
        references payments_services_tbl (id);

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

    alter table config_payments_mbservices_tbl 
        add index FK_config_payments_mbservices_tbl_type_id (service_type_id), 
        add constraint FK_config_payments_mbservices_tbl_type_id 
        foreign key (service_type_id) 
        references payments_service_types_tbl (id);

    alter table eirc_consumer_attribute_type_enum_values_tbl 
        add index eirc_consumer_attribute_type_enum_values_tbl_enum_id (attribute_type_enum_id), 
        add constraint eirc_consumer_attribute_type_enum_values_tbl_enum_id 
        foreign key (attribute_type_enum_id) 
        references eirc_consumer_attribute_types_tbl (id);

    alter table eirc_consumer_attribute_type_names_tbl 
        add index FK_eirc_consumer_attribute_type_names_tbl_type_id (attribute_type_id), 
        add constraint FK_eirc_consumer_attribute_type_names_tbl_type_id 
        foreign key (attribute_type_id) 
        references eirc_consumer_attribute_types_tbl (id);

    alter table eirc_consumer_attribute_type_names_tbl 
        add index FK_eirc_consumer_attribute_type_names_tbl_language_id (language_id), 
        add constraint FK_eirc_consumer_attribute_type_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_consumer_attribute_types_tbl 
        add index FK_eirc_consumer_attribute_types_tbl_unit_id (measure_unit_id), 
        add constraint FK_eirc_consumer_attribute_types_tbl_unit_id 
        foreign key (measure_unit_id) 
        references common_measure_units_tbl (id);

    alter table eirc_consumer_attributes_tbl 
        add index FK_eirc_consumer_attributes_tbl_consumer_id (consumer_id), 
        add constraint FK_eirc_consumer_attributes_tbl_consumer_id 
        foreign key (consumer_id) 
        references eirc_consumers_tbl (id);

    alter table eirc_consumer_attributes_tbl 
        add index FK_eirc_consumer_attributes_tbl_type_id (type_id), 
        add constraint FK_eirc_consumer_attributes_tbl_type_id 
        foreign key (type_id) 
        references eirc_consumer_attribute_types_tbl (id);

    create index I_external_account_number on eirc_consumers_tbl (external_account_number);

    alter table eirc_consumers_tbl 
        add index FK_eirc_consumer_eirc_account (eirc_account_id), 
        add constraint FK_eirc_consumer_eirc_account 
        foreign key (eirc_account_id) 
        references eirc_eirc_accounts_tbl (id);

    alter table eirc_consumers_tbl 
        add index FK_eirc_consumer_responsible_person (person_id), 
        add constraint FK_eirc_consumer_responsible_person 
        foreign key (person_id) 
        references ab_persons_tbl (id);

    alter table eirc_consumers_tbl 
        add index FK_eirc_consumer_apartment (apartment_id), 
        add constraint FK_eirc_consumer_apartment 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table eirc_consumers_tbl 
        add index FK_eirc_consumers_tbl_consumer_info_id (consumer_info_id), 
        add constraint FK_eirc_consumers_tbl_consumer_info_id 
        foreign key (consumer_info_id) 
        references eirc_consumer_infos_tbl (id);

    alter table eirc_consumers_tbl 
        add index FK_eirc_consumer_service (service_id), 
        add constraint FK_eirc_consumer_service 
        foreign key (service_id) 
        references payments_services_tbl (id);

    alter table eirc_eirc_accounts_tbl 
        add index FK_eirc_eirc_accounts_person_id (person_id), 
        add constraint FK_eirc_eirc_accounts_person_id 
        foreign key (person_id) 
        references ab_persons_tbl (id);

    alter table eirc_eirc_accounts_tbl 
        add index FK_eirc_eirc_accounts_apartment_id (apartment_id), 
        add constraint FK_eirc_eirc_accounts_apartment_id 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table eirc_eirc_accounts_tbl 
        add index FK_eirc_eirc_accounts_consumer_info_id (consumer_info_id), 
        add constraint FK_eirc_eirc_accounts_consumer_info_id 
        foreign key (consumer_info_id) 
        references eirc_consumer_infos_tbl (id);

    alter table eirc_quittance_details_payments_tbl 
        add index FK_eirc_quittance_details_payments_tbl_payment_status_id (payment_status_id), 
        add constraint FK_eirc_quittance_details_payments_tbl_payment_status_id 
        foreign key (payment_status_id) 
        references eirc_quittance_payment_statuses_tbl (id);

    alter table eirc_quittance_details_payments_tbl 
        add index FK_eirc_quittance_details_payments_tbl_payment_id (payment_id), 
        add constraint FK_eirc_quittance_details_payments_tbl_payment_id 
        foreign key (payment_id) 
        references eirc_quittance_payments_tbl (id);

    alter table eirc_quittance_details_payments_tbl 
        add index FK_eirc_quittance_details_payments_tbl_details_id (details_id), 
        add constraint FK_eirc_quittance_details_payments_tbl_details_id 
        foreign key (details_id) 
        references eirc_quittance_details_tbl (id);

    alter table eirc_quittance_details_quittances_tbl 
        add index FP_eirc_quittance_details_quittances_quittance (quittance_id), 
        add constraint FP_eirc_quittance_details_quittances_quittance 
        foreign key (quittance_id) 
        references eirc_quittances_tbl (id);

    alter table eirc_quittance_details_quittances_tbl 
        add index FP_eirc_quittance_details_quittances_quittance_details (quittance_details_id), 
        add constraint FP_eirc_quittance_details_quittances_quittance_details 
        foreign key (quittance_details_id) 
        references eirc_quittance_details_tbl (id);

    alter table eirc_quittance_details_tbl 
        add index FK_eirc_quittance_details_tbl_registry_record_id (registry_record_id), 
        add constraint FK_eirc_quittance_details_tbl_registry_record_id 
        foreign key (registry_record_id) 
        references common_registry_records_tbl (id);

    alter table eirc_quittance_details_tbl 
        add index FK_eirc_quittance_details_tbl_consumer_id (consumer_id), 
        add constraint FK_eirc_quittance_details_tbl_consumer_id 
        foreign key (consumer_id) 
        references eirc_consumers_tbl (id);

    alter table eirc_quittance_packets_tbl 
        add index eirc_quittance_packets_tbl_payment_id (payment_point_id), 
        add constraint eirc_quittance_packets_tbl_payment_id 
        foreign key (payment_point_id) 
        references orgs_payment_points_tbl (id);

    alter table eirc_quittance_payments_tbl 
        add index FK_eirc_quittance_payments_tbl_payment_status_id (payment_status_id), 
        add constraint FK_eirc_quittance_payments_tbl_payment_status_id 
        foreign key (payment_status_id) 
        references eirc_quittance_payment_statuses_tbl (id);

    alter table eirc_quittance_payments_tbl 
        add index FK_eirc_quittance_payments_tbl_quittance_id (quittance_id), 
        add constraint FK_eirc_quittance_payments_tbl_quittance_id 
        foreign key (quittance_id) 
        references eirc_quittances_tbl (id);

    alter table eirc_quittance_payments_tbl 
        add index FK_eirc_quittance_payments_tbl_packet_id (packet_id), 
        add constraint FK_eirc_quittance_payments_tbl_packet_id 
        foreign key (packet_id) 
        references eirc_quittance_packets_tbl (id);

    alter table eirc_quittances_tbl 
        add index FK_eirc_quittance_services_eirc_account (eirc_account_id), 
        add constraint FK_eirc_quittance_services_eirc_account 
        foreign key (eirc_account_id) 
        references eirc_eirc_accounts_tbl (id);

    alter table eirc_quittances_tbl 
        add index FK_eirc_quittances_service_organization (service_organization_id), 
        add constraint FK_eirc_quittances_service_organization 
        foreign key (service_organization_id) 
        references orgs_service_organizations_tbl (id);

    alter table orgs_bank_accounts_tbl 
        add index FK_orgs_bank_accounts_tbl_organization_id (organization_id), 
        add constraint FK_orgs_bank_accounts_tbl_organization_id 
        foreign key (organization_id) 
        references orgs_organizations_tbl (id);

    alter table orgs_bank_accounts_tbl 
        add index FK_orgs_bank_accounts_tbl_bank_id (bank_id), 
        add constraint FK_orgs_bank_accounts_tbl_bank_id 
        foreign key (bank_id) 
        references orgs_banks_tbl (id);

    alter table orgs_bank_descriptions_tbl 
        add index FK_orgs_bank_descriptions_tbl_bank_id (bank_id), 
        add constraint FK_orgs_bank_descriptions_tbl_bank_id 
        foreign key (bank_id) 
        references orgs_banks_tbl (id);

    alter table orgs_bank_descriptions_tbl 
        add index FK_orgs_bank_descriptions_tbl_language_id (language_id), 
        add constraint FK_orgs_bank_descriptions_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_banks_tbl 
        add index FK_orgs_banks_tbl_organization_id (organization_id), 
        add constraint FK_orgs_banks_tbl_organization_id 
        foreign key (organization_id) 
        references orgs_organizations_tbl (id);

    alter table orgs_cashbox_name_translations_tbl 
        add index FK_orgs_cashbox_name_translation_cashbox (cashbox_id), 
        add constraint FK_orgs_cashbox_name_translation_cashbox 
        foreign key (cashbox_id) 
        references orgs_cashboxes_tbl (id);

    alter table orgs_cashbox_name_translations_tbl 
        add index FK_orgs_cashbox_name_translation_language (language_id), 
        add constraint FK_orgs_cashbox_name_translation_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_cashboxes_tbl 
        add index FK_orgs_cashboxes_tbl_payment_point_id (payment_point_id), 
        add constraint FK_orgs_cashboxes_tbl_payment_point_id 
        foreign key (payment_point_id) 
        references orgs_payment_points_tbl (id);

    alter table orgs_organization_descriptions_tbl 
        add index FK_orgs_organization_description_organization (organization_id), 
        add constraint FK_orgs_organization_description_organization 
        foreign key (organization_id) 
        references orgs_organizations_tbl (id);

    alter table orgs_organization_descriptions_tbl 
        add index FK_orgs_organization_description_language (language_id), 
        add constraint FK_orgs_organization_description_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_organization_names_tbl 
        add index FK_orgs_organization_name_organization (organization_id), 
        add constraint FK_orgs_organization_name_organization 
        foreign key (organization_id) 
        references orgs_organizations_tbl (id);

    alter table orgs_organization_names_tbl 
        add index FK_orgs_organization_name_language (language_id), 
        add constraint FK_orgs_organization_name_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_organizations_tbl 
        add index FK_orgs_organization_data_source_description (data_source_description_id), 
        add constraint FK_orgs_organization_data_source_description 
        foreign key (data_source_description_id) 
        references common_data_source_descriptions_tbl (id);

    alter table orgs_payment_collectors_descriptions_tbl 
        add index FK_orgs_payment_collector_descriptions_tbl_collector_id (collector_id), 
        add constraint FK_orgs_payment_collector_descriptions_tbl_collector_id 
        foreign key (collector_id) 
        references orgs_payment_collectors_tbl (id);

    alter table orgs_payment_collectors_descriptions_tbl 
        add index FK_orgs_payment_collector_descriptions_tbl_language_id (language_id), 
        add constraint FK_orgs_payment_collector_descriptions_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_payment_collectors_tbl 
        add index FK_orgs_payment_collectors_tbl_organization_id (organization_id), 
        add constraint FK_orgs_payment_collectors_tbl_organization_id 
        foreign key (organization_id) 
        references orgs_organizations_tbl (id);

    alter table orgs_payment_point_names_tbl 
        add index FK_orgs_payment_point_names_tbl_point (payment_point_id), 
        add constraint FK_orgs_payment_point_names_tbl_point 
        foreign key (payment_point_id) 
        references orgs_payment_points_tbl (id);

    alter table orgs_payment_point_names_tbl 
        add index FK_orgs_payment_point_names_tbl_name_language (language_id), 
        add constraint FK_orgs_payment_point_names_tbl_name_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_payment_points_tbl 
        add index FK_orgs_payment_points_tbl_collector_id (collector_id), 
        add constraint FK_orgs_payment_points_tbl_collector_id 
        foreign key (collector_id) 
        references orgs_payment_collectors_tbl (id);

    alter table orgs_service_organization_descriptions_tbl 
        add index FK_orgs_service_organization_description_service_organization (service_organization_id), 
        add constraint FK_orgs_service_organization_description_service_organization 
        foreign key (service_organization_id) 
        references orgs_service_organizations_tbl (id);

    alter table orgs_service_organization_descriptions_tbl 
        add index FK_orgs_service_organization_description_language (language_id), 
        add constraint FK_orgs_service_organization_description_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_service_organizations_tbl 
        add index FK_orgs_service_organization_organization (organization_id), 
        add constraint FK_orgs_service_organization_organization 
        foreign key (organization_id) 
        references orgs_organizations_tbl (id);

    alter table orgs_service_provider_descriptions_tbl 
        add index FK_orgs_service_provider_description_service_provider (service_provider_id), 
        add constraint FK_orgs_service_provider_description_service_provider 
        foreign key (service_provider_id) 
        references orgs_service_providers_tbl (id);

    alter table orgs_service_provider_descriptions_tbl 
        add index FK_orgs_service_provider_description_language (language_id), 
        add constraint FK_orgs_service_provider_description_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_service_providers_tbl 
        add index FK_orgs_service_provider_organization (organization_id), 
        add constraint FK_orgs_service_provider_organization 
        foreign key (organization_id) 
        references orgs_organizations_tbl (id);

    alter table orgs_subdivision_descriptions_tbl 
        add index FK_orgs_subdivision_descriptions_tbl_subdivision_id (subdivision_id), 
        add constraint FK_orgs_subdivision_descriptions_tbl_subdivision_id 
        foreign key (subdivision_id) 
        references orgs_subdivisions_tbl (id);

    alter table orgs_subdivision_descriptions_tbl 
        add index FK_orgs_subdivision_descriptions_tbl_language_id (language_id), 
        add constraint FK_orgs_subdivision_descriptions_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table orgs_subdivision_names_tbl 
        add index FK_orgs_subdivision_names_tbl_subdivision_id (subdivision_id), 
        add constraint FK_orgs_subdivision_names_tbl_subdivision_id 
        foreign key (subdivision_id) 
        references orgs_subdivisions_tbl (id);

    alter table orgs_subdivision_names_tbl 
        add index FK_orgs_subdivision_names_tbl_language_id (language_id), 
        add constraint FK_orgs_subdivision_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    create index INDX_tree_path on orgs_subdivisions_tbl (tree_path);

    alter table orgs_subdivisions_tbl 
        add index FK_eirc_subdivisions_tbl_parent_subdivision_id (parent_subdivision_id), 
        add constraint FK_eirc_subdivisions_tbl_parent_subdivision_id 
        foreign key (parent_subdivision_id) 
        references orgs_subdivisions_tbl (id);

    alter table orgs_subdivisions_tbl 
        add index FK_eirc_subdivisions_tbl_head_organization_id (head_organization_id), 
        add constraint FK_eirc_subdivisions_tbl_head_organization_id 
        foreign key (head_organization_id) 
        references orgs_organizations_tbl (id);

    alter table orgs_subdivisions_tbl 
        add index FK_eirc_subdivisions_tbl_juridical_person_id (juridical_person_id), 
        add constraint FK_eirc_subdivisions_tbl_juridical_person_id 
        foreign key (juridical_person_id) 
        references orgs_organizations_tbl (id);

    alter table payments_document_addition_type_translations_tbl 
        add index FK_payments_document_add_type_translations_tbl_type_id (type_id), 
        add constraint FK_payments_document_add_type_translations_tbl_type_id 
        foreign key (type_id) 
        references payments_document_addition_types_tbl (id);

    alter table payments_document_addition_type_translations_tbl 
        add index FK_payments_document_add_type_translations_tbl_lang_id (language_id), 
        add constraint FK_payments_document_add_type_translations_tbl_lang_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table payments_document_additions_tbl 
        add index FK_payments_document_additions_tbl_document_id (document_id), 
        add constraint FK_payments_document_additions_tbl_document_id 
        foreign key (document_id) 
        references payments_documents_tbl (id);

    alter table payments_document_additions_tbl 
        add index FK_payments_document_additions_tbl_addition_type_id (addition_type_id), 
        add constraint FK_payments_document_additions_tbl_addition_type_id 
        foreign key (addition_type_id) 
        references payments_document_addition_types_tbl (id);

    alter table payments_document_status_translations_tbl 
        add index FK_payments_document_status_translations_tbl_status_id (status_id), 
        add constraint FK_payments_document_status_translations_tbl_status_id 
        foreign key (status_id) 
        references payments_document_statuses_tbl (id);

    alter table payments_document_status_translations_tbl 
        add index FK_payments_document_status_translations_tbl_lang_id (language_id), 
        add constraint FK_payments_document_status_translations_tbl_lang_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table payments_document_type_translations_tbl 
        add index FK_payments_document_type_translations_tbl_type_id (type_id), 
        add constraint FK_payments_document_type_translations_tbl_type_id 
        foreign key (type_id) 
        references payments_document_types_tbl (id);

    alter table payments_document_type_translations_tbl 
        add index FK_payments_document_type_translations_tbl_lang_id (language_id), 
        add constraint FK_payments_document_type_translations_tbl_lang_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table payments_documents_tbl 
        add index FK_payments_documents_tbl_debt_org_id (debtor_organization_id), 
        add constraint FK_payments_documents_tbl_debt_org_id 
        foreign key (debtor_organization_id) 
        references orgs_organizations_tbl (id);

    alter table payments_documents_tbl 
        add index FK_payments_documents_tbl_credit_org_id (creditor_organization_id), 
        add constraint FK_payments_documents_tbl_credit_org_id 
        foreign key (creditor_organization_id) 
        references orgs_organizations_tbl (id);

    alter table payments_documents_tbl 
        add index FK_payments_documents_tbl_registry_record_id (registry_record_id), 
        add constraint FK_payments_documents_tbl_registry_record_id 
        foreign key (registry_record_id) 
        references common_registry_records_tbl (id);

    alter table payments_documents_tbl 
        add index FK_payments_documents_tbl_ref_doc_id (reference_document_id), 
        add constraint FK_payments_documents_tbl_ref_doc_id 
        foreign key (reference_document_id) 
        references payments_documents_tbl (id);

    alter table payments_documents_tbl 
        add index FK_payments_documents_tbl_status_id (status_id), 
        add constraint FK_payments_documents_tbl_status_id 
        foreign key (status_id) 
        references payments_document_statuses_tbl (id);

    alter table payments_documents_tbl 
        add index FK_payments_documents_tbl_document_type_id (type_id), 
        add constraint FK_payments_documents_tbl_document_type_id 
        foreign key (type_id) 
        references payments_document_types_tbl (id);

    alter table payments_documents_tbl 
        add index FK_payments_documents_tbl_payments_operation_id (operation_id), 
        add constraint FK_payments_documents_tbl_payments_operation_id 
        foreign key (operation_id) 
        references payments_operations_tbl (id);

    alter table payments_documents_tbl 
        add index FK_payments_documents_tbl_service_id (service_id), 
        add constraint FK_payments_documents_tbl_service_id 
        foreign key (service_id) 
        references payments_services_tbl (id);

    alter table payments_operation_addition_type_translations_tbl 
        add index FK_payments_operation_add_type_translations_tbl_type_id (type_id), 
        add constraint FK_payments_operation_add_type_translations_tbl_type_id 
        foreign key (type_id) 
        references payments_operation_addition_types_tbl (id);

    alter table payments_operation_addition_type_translations_tbl 
        add index FK_payments_operation_add_type_translations_tbl_lang_id (language_id), 
        add constraint FK_payments_operation_add_type_translations_tbl_lang_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table payments_operation_additions_tbl 
        add index FK_payments_operation_additions_tbl_addition_type_id (addition_type_id), 
        add constraint FK_payments_operation_additions_tbl_addition_type_id 
        foreign key (addition_type_id) 
        references payments_operation_addition_types_tbl (id);

    alter table payments_operation_additions_tbl 
        add index FK_payments_operation_additions_tbl_operation (operation_id), 
        add constraint FK_payments_operation_additions_tbl_operation 
        foreign key (operation_id) 
        references payments_operations_tbl (id);

    alter table payments_operation_level_translations_tbl 
        add index FK_payments_operation_level_translations_tbl_type_id (level_id), 
        add constraint FK_payments_operation_level_translations_tbl_type_id 
        foreign key (level_id) 
        references payments_operation_levels_tbl (id);

    alter table payments_operation_level_translations_tbl 
        add index FK_payments_operation_level_translations_tbl_lang_id (language_id), 
        add constraint FK_payments_operation_level_translations_tbl_lang_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table payments_operation_status_translations_tbl 
        add index FK_payments_operation_status_translations_tbl_type_id (status_id), 
        add constraint FK_payments_operation_status_translations_tbl_type_id 
        foreign key (status_id) 
        references payments_operation_statuses_tbl (id);

    alter table payments_operation_status_translations_tbl 
        add index FK_payments_operation_status_translations_tbl_lang_id (language_id), 
        add constraint FK_payments_operation_status_translations_tbl_lang_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table payments_operation_type_translations_tbl 
        add index FK_payments_operation_type_translations_tbl_type_id (type_id), 
        add constraint FK_payments_operation_type_translations_tbl_type_id 
        foreign key (type_id) 
        references payments_operation_types_tbl (id);

    alter table payments_operation_type_translations_tbl 
        add index FK_payments_operation_type_translations_tbl_lang_id (language_id), 
        add constraint FK_payments_operation_type_translations_tbl_lang_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table payments_operations_tbl 
        add index FK_orgs_cashboxes_tbl_cashbox_id (cashbox_id), 
        add constraint FK_orgs_cashboxes_tbl_cashbox_id 
        foreign key (cashbox_id) 
        references orgs_cashboxes_tbl (id);

    alter table payments_operations_tbl 
        add index FK_payments_operations_tbl_payment_point_id (payment_point_id), 
        add constraint FK_payments_operations_tbl_payment_point_id 
        foreign key (payment_point_id) 
        references orgs_payment_points_tbl (id);

    alter table payments_operations_tbl 
        add index FK_payments_operations_tbl_registry_record_id (registry_record_id), 
        add constraint FK_payments_operations_tbl_registry_record_id 
        foreign key (registry_record_id) 
        references common_registry_records_tbl (id);

    alter table payments_operations_tbl 
        add index FK_payments_operations_tbl_register_organization_id (register_organization_id), 
        add constraint FK_payments_operations_tbl_register_organization_id 
        foreign key (register_organization_id) 
        references orgs_organizations_tbl (id);

    alter table payments_operations_tbl 
        add index FK_payments_operations_tbl_reference_id (reference_operation_id), 
        add constraint FK_payments_operations_tbl_reference_id 
        foreign key (reference_operation_id) 
        references payments_operations_tbl (id);

    alter table payments_operations_tbl 
        add index FK_payments_operations_tbl_status_id (status_id), 
        add constraint FK_payments_operations_tbl_status_id 
        foreign key (status_id) 
        references payments_operation_statuses_tbl (id);

    alter table payments_operations_tbl 
        add index FK_payments_operations_tbl_level_id (level_id), 
        add constraint FK_payments_operations_tbl_level_id 
        foreign key (level_id) 
        references payments_operation_levels_tbl (id);

    alter table payments_operations_tbl 
        add index FK_payments_operations_tbl_type_id (type_id), 
        add constraint FK_payments_operations_tbl_type_id 
        foreign key (type_id) 
        references payments_operation_types_tbl (id);

    alter table payments_operations_tbl 
        add index FK_payments_operations_tbl_creator_organization_id (creator_organization_id), 
        add constraint FK_payments_operations_tbl_creator_organization_id 
        foreign key (creator_organization_id) 
        references orgs_organizations_tbl (id);

    alter table payments_registry_delivery_history_tbl 
        add index FK_payments_registry_delivery_history_tbl_file_id (file_id), 
        add constraint FK_payments_registry_delivery_history_tbl_file_id 
        foreign key (file_id) 
        references common_files_tbl (id);

    alter table payments_registry_delivery_history_tbl 
        add index FK_payments_registry_delivery_history_tbl_registry_id (registry_id), 
        add constraint FK_payments_registry_delivery_history_tbl_registry_id 
        foreign key (registry_id) 
        references common_registries_tbl (id);

    alter table payments_service_descriptions_tbl 
        add index FK_payments_service_description_service (service_id), 
        add constraint FK_payments_service_description_service 
        foreign key (service_id) 
        references payments_services_tbl (id);

    alter table payments_service_descriptions_tbl 
        add index FK_payments_service_desciption_language (language_id), 
        add constraint FK_payments_service_desciption_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table payments_service_provider_attribute_tbl 
        add index FK_payments_service_provider_attribute_tbl_service_provider_id (service_provider_id), 
        add constraint FK_payments_service_provider_attribute_tbl_service_provider_id 
        foreign key (service_provider_id) 
        references orgs_service_providers_tbl (id);

    alter table payments_service_type_name_translations_tbl 
        add index FK_payments_service_type_name_translation_service_type (service_type_id), 
        add constraint FK_payments_service_type_name_translation_service_type 
        foreign key (service_type_id) 
        references payments_service_types_tbl (id);

    alter table payments_service_type_name_translations_tbl 
        add index FK_payments_service_type_name_translation_language (language_id), 
        add constraint FK_payments_service_type_name_translation_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    create index INDX_payments_service_external_code on payments_services_tbl (external_code);

    alter table payments_services_tbl 
        add index FK_payments_services_tbl_measure_unit_id (measure_unit_id), 
        add constraint FK_payments_services_tbl_measure_unit_id 
        foreign key (measure_unit_id) 
        references common_measure_units_tbl (id);

    alter table payments_services_tbl 
        add index FK_payments_service_parent_service_id (parent_service_id), 
        add constraint FK_payments_service_parent_service_id 
        foreign key (parent_service_id) 
        references payments_services_tbl (id);

    alter table payments_services_tbl 
        add index FK_payments_service_service_provider (provider_id), 
        add constraint FK_payments_service_service_provider 
        foreign key (provider_id) 
        references orgs_service_providers_tbl (id);

    alter table payments_services_tbl 
        add index FK_payments_service_service_type (type_id), 
        add constraint FK_payments_service_service_type 
        foreign key (type_id) 
        references payments_service_types_tbl (id);
