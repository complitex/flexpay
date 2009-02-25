
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
        status integer not null,
        building_id bigint not null,
        primary key (id)
    );

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
        district_id bigint not null comment 'District reference',
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
        district_id bigint not null,
        street_id bigint not null,
        primary key (street_id, district_id)
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
        ID bigint not null auto_increment,
        name varchar(255),
        short_name varchar(255),
        language_id bigint,
        town_type_id bigint,
        primary key (ID),
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

    create table bti_building_attribute_temp_values_tbl (
        id bigint not null auto_increment,
        attribute_value varchar(255) comment 'Attribute value',
        begin_date datetime not null comment 'Value begin date',
        end_date datetime not null comment 'Value end date',
        attribute_id bigint not null comment 'Temporal attribute reference',
        primary key (id)
    ) comment='Temporal values for building attributes';

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
        group_id bigint not null comment 'Attribute group reference',
        unique_code varchar(255) comment 'Internal unique code',
        is_temporal integer not null comment 'Temporal flag',
        primary key (id)
    ) comment='Building attribute types';

    create table bti_building_attributes_tbl (
        id bigint not null auto_increment,
        discriminator varchar(255) not null comment 'Class hierarchy descriminator',
        building_id bigint not null comment 'Building reference',
        attribute_type_id bigint not null comment 'Attribute type reference',
        normal_attribute_value varchar(255) comment 'Attribute value',
        primary key (id)
    ) comment='Building attributes';

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
        primary key (id)
    ) comment='Some abstract history records consumer';

    create table common_history_consumptions_tbl (
        id bigint not null auto_increment,
        record_id bigint not null comment 'History record reference',
        consumer_id bigint not null comment 'History consumer reference',
        primary key (id)
    ) comment='Consumption of single history record';

    create table common_history_records_tbl (
        id bigint not null auto_increment,
        old_date_value datetime comment 'Optional old date value',
        new_date_value datetime comment 'Optional new date value',
        old_int_value integer comment 'Optional old int value',
        new_int_value integer comment 'Optional new int value',
        old_long_value bigint comment 'Optional old long value',
        new_long_value bigint comment 'Optional new long value',
        old_string_value varchar(255) comment 'Optional old string value',
        new_string_value varchar(255) comment 'Optional new string value',
        old_double_value double precision comment 'Optional old double value',
        new_double_value double precision comment 'Optional new double value',
        field_type integer not null comment 'Field type value is modified for',
        operation_order integer not null comment 'Object modification operation order',
        language_id bigint comment 'Optional language reference for multilang fields',
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

    create index indx_value on ab_apartment_numbers_tbl (value);

    alter table ab_apartment_numbers_tbl 
        add index FK_ab_apartment_numbers_tbl_apartment_id (apartment_id), 
        add constraint FK_ab_apartment_numbers_tbl_apartment_id 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table ab_apartments_tbl 
        add index FKBEC651DEF71F858D (building_id), 
        add constraint FKBEC651DEF71F858D 
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
        add index FK93093857311847ED (street_id), 
        add constraint FK93093857311847ED 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table ab_streets_districts_tbl 
        add index FK930938571AE9F4D (district_id), 
        add constraint FK930938571AE9F4D 
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

    alter table bti_building_attribute_temp_values_tbl 
        add index FK_bti_building_attribute_temp_values_tbl_attr_id (attribute_id), 
        add constraint FK_bti_building_attribute_temp_values_tbl_attr_id 
        foreign key (attribute_id) 
        references bti_building_attributes_tbl (id);

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

    alter table common_history_consumptions_tbl 
        add index FK_common_history_consumptions_tbl_consumer_id (consumer_id), 
        add constraint FK_common_history_consumptions_tbl_consumer_id 
        foreign key (consumer_id) 
        references common_history_consumers_tbl (id);

    alter table common_history_consumptions_tbl 
        add index FK_common_history_consumptions_tbl_record_id (record_id), 
        add constraint FK_common_history_consumptions_tbl_record_id 
        foreign key (record_id) 
        references common_history_records_tbl (id);

    alter table common_history_records_tbl 
        add index FK_common_history_records_tbl_language_id (language_id), 
        add constraint FK_common_history_records_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

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
