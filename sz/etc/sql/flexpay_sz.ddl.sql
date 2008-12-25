
    create table SubsReqvNach (
        id bigint not null auto_increment,
        sz_file_id bigint not null,
        surName varchar(255),
        firstName varchar(255),
        midName varchar(255),
        indx varchar(255),
        n_name varchar(255),
        n_code varchar(255),
        streetType varchar(255),
        streetName varchar(255),
        extStreetID double precision,
        houseNum varchar(255),
        partNum varchar(255),
        appartment varchar(255),
        account varchar(255),
        app_num varchar(255),
        begin datetime,
        dat_end datetime,
        cm_area double precision,
        totalSq double precision,
        blc_area double precision,
        frog double precision,
        debt double precision,
        living double precision,
        nach double precision,
        tarif double precision,
        p2 double precision,
        n2 double precision,
        p3 double precision,
        n3 double precision,
        n4 double precision,
        p4 double precision,
        n5 double precision,
        p5 double precision,
        n6 double precision,
        p6 double precision,
        n7 double precision,
        p7 double precision,
        n8 double precision,
        p8 double precision,
        orgsID double precision,
        fileID double precision,
        status double precision,
        primary key (id)
    );

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

    create table ab_building_attribute_type_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Type translation',
        short_name varchar(255) comment 'Optional short translation',
        attribute_type_id bigint not null comment 'Building attribute type reference',
        language_id bigint not null comment 'Language reference',
        primary key (id),
        unique (attribute_type_id, language_id)
    ) comment='Building attribute type translations';

    create table ab_building_attribute_types_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled/Disabled status',
        primary key (id)
    ) comment='Building attribute type (number, bulk, etc.)';

    create table ab_building_attributes_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled/Disabled status',
        value varchar(255) not null comment 'Building attribute value',
        attribute_type_id bigint not null comment 'Attribute type reference',
        buildings_id bigint not null comment 'Building address reference',
        primary key (id)
    ) comment='Building address attributes';

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
        eirc_service_organization_id bigint comment 'Service organization reference',
        primary key (id)
    ) comment='Buildings';

    create table ab_buildingses_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled/Disabled status',
        primary_status bit not null comment 'Flag of primary building address',
        street_id bigint not null comment 'Street reference',
        building_id bigint not null comment 'Building reference this address belongs to',
        primary key (id)
    ) comment='Building addresses';

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
    );

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

    create table characteristic_records_tbl (
        id bigint not null auto_increment,
        sz_file_id bigint not null,
        cod double precision,
        cdpr double precision,
        ncard double precision,
        idcode varchar(255),
        pasp varchar(255),
        fio varchar(255),
        idpil varchar(255),
        pasppil varchar(255),
        fiopil varchar(255),
        idx double precision,
        cdul double precision,
        house varchar(255),
        build varchar(255),
        apt varchar(255),
        vl double precision,
        plzag double precision,
        plopal double precision,
        primary key (id)
    );

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

    create table common_dual_tbl (
        id bigint not null auto_increment,
        primary key (id)
    );

    create table common_file_statuses_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Flexpay filestatus title',
        description varchar(255) not null comment 'Flexpay filestatus description',
        module_id bigint not null comment 'Flexpay module reference',
        primary key (id)
    ) comment='Information about file statuses';

    create table common_file_types_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Filetype title',
        description varchar(255) not null comment 'Filetype description',
        file_mask varchar(255) not null comment 'Mask of files for this type',
        module_id bigint not null comment 'Flexpay module reference',
        primary key (id)
    ) comment='Information about known filetypes';

    create table common_files_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name_on_server varchar(255) not null comment 'File name on flexpay server',
        original_name varchar(255) not null comment 'Original file name',
        description varchar(255) comment 'File description',
        creation_date datetime not null comment 'File creation date',
        user_name varchar(255) not null comment 'User name who create this file',
        size bigint comment 'File size',
        type_id bigint comment 'Flexpay file type reference',
        status_id bigint comment 'Flexpay file status reference',
        module_id bigint not null comment 'Flexpay module reference',
        primary key (id)
    ) comment='Table, where store information about all flexpay files';

    create table common_flexpay_modules_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Flexpay module name',
        primary key (id)
    ) comment='Information about all flexpay modules';

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

    create table eirc_bank_accounts_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optiomistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        account_number varchar(255) not null comment 'Bank account number',
        is_default bit not null comment 'Juridical person default account flag',
        bank_id bigint not null comment 'Bank reference',
        organization_id bigint not null comment 'Juridical person (organization) reference',
        primary key (id)
    ) comment='Bank accounts';

    create table eirc_bank_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        bank_id bigint not null comment 'Bank reference',
        primary key (id),
        unique (language_id, bank_id)
    ) comment='Bank desriptions';

    create table eirc_banks_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        bank_identifier_code varchar(255) not null comment 'Bank identifier code (BIK)',
        corresponding_account varchar(255) not null comment 'Corresponding Central Bank account',
        organization_id bigint not null comment 'Organization reference',
        primary key (id)
    ) comment='Banks';

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

    create table eirc_organization_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        language_id bigint not null comment 'Language reference',
        organization_id bigint not null comment 'Organization reference',
        primary key (id),
        unique (language_id, organization_id)
    );

    create table eirc_organization_names_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        language_id bigint not null comment 'Language reference',
        organization_id bigint not null comment 'Organization reference',
        primary key (id),
        unique (language_id, organization_id)
    );

    create table eirc_organizations_tbl (
        id bigint not null auto_increment,
        version integer not null,
        status integer not null,
        individual_tax_number varchar(255) not null,
        kpp varchar(255) not null,
        juridical_address varchar(255) not null comment 'Juridical address',
        postal_address varchar(255) not null comment 'Postal address',
        primary key (id)
    );

    create table eirc_payment_points_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled-disabled status',
        address varchar(255) not null comment 'Address',
        collector_id bigint not null comment 'Payments collector reference',
        primary key (id)
    ) comment='Payment points';

    create table eirc_payments_collectors_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        collector_id bigint not null comment 'Payment collector reference',
        primary key (id),
        unique (language_id, collector_id)
    ) comment='Payment collector desriptions';

    create table eirc_payments_collectors_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        organization_id bigint not null comment 'Organization reference',
        primary key (id)
    ) comment='Payment collectors';

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

    create table eirc_registries_tbl (
        id bigint not null auto_increment,
        version integer not null,
        registry_number bigint,
        records_number bigint,
        creation_date datetime,
        from_date datetime,
        till_date datetime,
        sender_code bigint,
        recipient_code bigint,
        amount decimal(19,2),
        registry_type_id bigint not null comment 'Registry type reference',
        sp_file_id bigint comment 'Registry file reference',
        service_provider_id bigint comment 'Service provider reference',
        registry_status_id bigint not null comment 'Registry status reference',
        archive_status_id bigint not null comment 'Registry archive status reference',
        sender_id bigint comment 'Sender organization reference',
        recipient_id bigint comment 'Recipient organization reference',
        primary key (id)
    );

    create table eirc_registry_archive_statuses_tbl (
        id bigint not null auto_increment,
        code integer not null unique,
        primary key (id)
    );

    create table eirc_registry_containers_tbl (
        id bigint not null auto_increment,
        data varchar(2048) not null comment 'Registry container data',
        order_weight integer not null comment 'Order of the container in a registry',
        registry_id bigint not null comment 'Registry reference',
        primary key (id)
    );

    create table eirc_registry_record_containers_tbl (
        id bigint not null auto_increment,
        data varchar(2048) not null comment 'Container data',
        order_weight integer not null comment 'Order of the container in a registry record',
        record_id bigint not null comment 'Registry record reference',
        primary key (id)
    );

    create table eirc_registry_record_statuses_tbl (
        id bigint not null auto_increment,
        code integer not null unique comment 'Registry record status code',
        primary key (id)
    );

    create table eirc_registry_records_tbl (
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
        consumer_id bigint comment 'Consumer reference',
        registry_id bigint not null comment 'Registry reference',
        record_status_id bigint comment 'Record status reference',
        import_error_id bigint comment 'Import error reference',
        person_id bigint comment 'Person reference',
        apartment_id bigint comment 'Apartment reference',
        service_id bigint comment 'Service reference',
        primary key (id)
    );

    create table eirc_registry_statuses_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic locking version',
        code integer not null unique comment 'Registry status code',
        primary key (id)
    );

    create table eirc_registry_types_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic locking version',
        code integer not null unique comment 'Registry type code',
        primary key (id)
    );

    create table eirc_service_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        language_id bigint not null comment 'Language reference',
        service_id bigint not null comment 'Service reference',
        primary key (id),
        unique (language_id, service_id)
    );

    create table eirc_service_organization_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        service_organization_id bigint not null comment 'Organization reference',
        primary key (id),
        unique (language_id, service_organization_id)
    ) comment='Service organization descriptions';

    create table eirc_service_organizations_tbl (
        id bigint not null auto_increment,
        status integer not null comment 'Enabled/Disabled status',
        organization_id bigint not null comment 'Organization reference',
        primary key (id)
    ) comment='Service organizations';

    create table eirc_service_provider_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        language_id bigint not null comment 'Language reference',
        service_provider_id bigint not null comment 'Service provider reference',
        primary key (id),
        unique (language_id, service_provider_id)
    );

    create table eirc_service_providers_tbl (
        id bigint not null auto_increment,
        status integer not null,
        organization_id bigint not null comment 'Organization reference',
        data_source_description_id bigint not null comment 'Data source description reference',
        primary key (id)
    );

    create table eirc_service_type_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null,
        description varchar(255) not null,
        language_id bigint not null comment 'Language reference',
        service_type_id bigint not null comment 'Service type reference',
        primary key (id),
        unique (language_id, service_type_id)
    );

    create table eirc_service_types_tbl (
        id bigint not null auto_increment,
        status integer not null,
        code integer not null,
        primary key (id)
    );

    create table eirc_services_tbl (
        id bigint not null auto_increment,
        external_code varchar(255) comment 'Service providers internal service code',
        begin_date date not null comment 'The Date service is valid from',
        end_date date not null comment 'The Date service is valid till',
        provider_id bigint not null comment 'Service provider reference',
        type_id bigint not null comment 'Service type reference',
        measure_unit_id bigint comment 'Measure unit reference',
        parent_service_id bigint comment 'If parent service reference present service is a subservice',
        primary key (id)
    );

    create table eirc_subdivision_descriptions_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Description value',
        language_id bigint not null comment 'Language reference',
        subdivision_id bigint not null comment 'Subdivision reference',
        primary key (id),
        unique (language_id, subdivision_id)
    );

    create table eirc_subdivision_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Name value',
        language_id bigint not null comment 'Language reference',
        subdivision_id bigint not null comment 'Subdivision reference',
        primary key (id),
        unique (language_id, subdivision_id)
    );

    create table eirc_subdivisions_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optiomistic lock version',
        status integer not null comment 'Enabled/Disabled status',
        real_address varchar(255) not null comment 'Subdivision real address',
        tree_path varchar(255) not null comment 'Subdivisions tree branch path',
        parent_subdivision_id bigint comment 'Parent subdivision reference if any',
        head_organization_id bigint not null comment 'Head organization reference',
        juridical_person_id bigint comment 'Juridical person (organization) reference if any',
        primary key (id)
    ) comment='Organization subdivisions';

    create table eirc_ticket_service_amounts_tbl (
        id bigint not null auto_increment,
        ticket_id bigint comment 'Ticket reference',
        consumer_id bigint not null comment 'Consumer reference',
        date_from_amount decimal(19,2) not null,
        date_till_amount decimal(19,2) not null,
        primary key (id)
    );

    create table eirc_tickets_tbl (
        id bigint not null auto_increment,
        creation_date datetime not null,
        service_organization_id bigint not null comment 'Service organization reference',
        person_id bigint not null comment 'Person reference',
        ticket_number integer not null,
        date_from datetime not null,
        date_till datetime not null,
        apartment_id bigint not null comment 'Apartment reference',
        primary key (id)
    );

    create table oszn_tbl (
        id bigint not null auto_increment,
        district_id bigint not null,
        description varchar(255) not null,
        primary key (id)
    );

    create table sz_apartment_number_corrections_tbl (
        id bigint not null auto_increment,
        apartment_id bigint not null,
        oszn_id bigint not null,
        external_number varchar(50) not null,
        primary key (id)
    );

    create table sz_building_number_corrections_tbl (
        id bigint not null auto_increment,
        building_id bigint not null,
        oszn_id bigint not null,
        external_number varchar(50) not null,
        external_bulk varchar(50),
        primary key (id)
    );

    create table sz_district_corrections_tbl (
        id bigint not null auto_increment,
        district_id bigint not null,
        oszn_id bigint not null,
        external_id varchar(50) not null,
        primary key (id)
    );

    create table sz_file_actuality_status_tbl (
        id bigint not null auto_increment,
        description varchar(255),
        primary key (id)
    );

    create table sz_file_status_tbl (
        id bigint not null auto_increment,
        description varchar(255),
        primary key (id)
    );

    create table sz_file_types_tbl (
        id bigint not null auto_increment,
        file_mask varchar(255) not null,
        description varchar(255),
        primary key (id)
    );

    create table sz_files_tbl (
        id bigint not null auto_increment,
        oszn_id bigint not null,
        request_file_name varchar(255) not null,
        internal_request_file_name varchar(255) not null,
        internal_response_file_name varchar(255),
        sz_file_type_id bigint not null,
        file_year integer not null,
        file_month integer not null,
        user_name varchar(255) not null,
        import_date datetime not null,
        sz_file_status_id bigint not null,
        sz_file_actuality_status_id bigint not null,
        primary key (id)
    );

    create table sz_service_type_records_tbl (
        id bigint not null auto_increment,
        sz_file_id bigint not null,
        deadhead_id bigint,
        apartmentNumber varchar(255),
        buildingNumber varchar(255),
        bulkNumber varchar(255),
        deadheadCategory integer,
        deadheadName varchar(255),
        deadheadPassport varchar(255),
        deadheadTaxNumber varchar(255),
        dwellingOwnerId integer,
        dwellingOwnerName varchar(255),
        dwellingOwnerPasport varchar(255),
        dwellingOwnerTaxNumber varchar(255),
        extDistrictCode integer,
        extOrganizationCode integer,
        extStreetCode integer,
        personalAccountNumber varchar(255),
        postalCode integer,
        privilegeCode integer,
        privilegeEndMonth integer,
        privilegeEndYear integer,
        privilegeStartMonth integer,
        privilegeStartYear integer,
        serviceType integer,
        tarifCode integer,
        primary key (id)
    );

    create table sz_street_corrections_tbl (
        id bigint not null auto_increment,
        street_id bigint not null,
        oszn_id bigint not null,
        external_id varchar(50) not null,
        primary key (id)
    );

    alter table SubsReqvNach 
        add index FK96669C839D0EDA76 (sz_file_id), 
        add constraint FK96669C839D0EDA76 
        foreign key (sz_file_id) 
        references sz_files_tbl (id);

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

    alter table ab_building_attribute_type_translations_tbl 
        add index ab_building_attribute_type_translations_tbl_attribute_type_id (attribute_type_id), 
        add constraint ab_building_attribute_type_translations_tbl_attribute_type_id 
        foreign key (attribute_type_id) 
        references ab_building_attribute_types_tbl (id);

    alter table ab_building_attribute_type_translations_tbl 
        add index lang_building_attribute_type_pair_language_id (language_id), 
        add constraint lang_building_attribute_type_pair_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    create index indx_value on ab_building_attributes_tbl (value);

    alter table ab_building_attributes_tbl 
        add index ab_building_attributes_tbl_buildings_id (buildings_id), 
        add constraint ab_building_attributes_tbl_buildings_id 
        foreign key (buildings_id) 
        references ab_buildingses_tbl (id);

    alter table ab_building_attributes_tbl 
        add index ab_building_attributes_tbl_attribute_type_id (attribute_type_id), 
        add constraint ab_building_attributes_tbl_attribute_type_id 
        foreign key (attribute_type_id) 
        references ab_building_attribute_types_tbl (id);

    alter table ab_building_statuses_tbl 
        add index ab_building_statuses_tbl_building_id (building_id), 
        add constraint ab_building_statuses_tbl_building_id 
        foreign key (building_id) 
        references ab_buildings_tbl (id);

    alter table ab_buildings_tbl 
        add index FK_eirc_building_service_organization (eirc_service_organization_id), 
        add constraint FK_eirc_building_service_organization 
        foreign key (eirc_service_organization_id) 
        references eirc_service_organizations_tbl (id);

    alter table ab_buildings_tbl 
        add index ab_buildings_tbl_district_id (district_id), 
        add constraint ab_buildings_tbl_district_id 
        foreign key (district_id) 
        references ab_districts_tbl (id);

    alter table ab_buildingses_tbl 
        add index ab_buildingses_tbl_street_id (street_id), 
        add constraint ab_buildingses_tbl_street_id 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table ab_buildingses_tbl 
        add index ab_buildingses_tbl_building_id (building_id), 
        add constraint ab_buildingses_tbl_building_id 
        foreign key (building_id) 
        references ab_buildings_tbl (id);

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
        add index ab_street_names_temporal_tbl_street_id (street_id), 
        add constraint ab_street_names_temporal_tbl_street_id 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table ab_street_names_temporal_tbl 
        add index ab_street_names_temporal_tbl_street_name_id (street_name_id), 
        add constraint ab_street_names_temporal_tbl_street_name_id 
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

    alter table characteristic_records_tbl 
        add index FK98746B9D9D0EDA76 (sz_file_id), 
        add constraint FK98746B9D9D0EDA76 
        foreign key (sz_file_id) 
        references sz_files_tbl (id);

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

    alter table common_files_tbl 
        add index common_files_tbl_status_id (status_id), 
        add constraint common_files_tbl_status_id 
        foreign key (status_id) 
        references common_file_statuses_tbl (id);

    alter table common_files_tbl 
        add index common_files_tbl_type_id (type_id), 
        add constraint common_files_tbl_type_id 
        foreign key (type_id) 
        references common_file_types_tbl (id);

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

    alter table eirc_bank_accounts_tbl 
        add index FK_eirc_bank_accounts_tbl_organization_id (organization_id), 
        add constraint FK_eirc_bank_accounts_tbl_organization_id 
        foreign key (organization_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_bank_accounts_tbl 
        add index FK_eirc_bank_accounts_tbl_bank_id (bank_id), 
        add constraint FK_eirc_bank_accounts_tbl_bank_id 
        foreign key (bank_id) 
        references eirc_banks_tbl (id);

    alter table eirc_bank_descriptions_tbl 
        add index FK_eirc_bank_descriptions_tbl_bank_id (bank_id), 
        add constraint FK_eirc_bank_descriptions_tbl_bank_id 
        foreign key (bank_id) 
        references eirc_banks_tbl (id);

    alter table eirc_bank_descriptions_tbl 
        add index FK_eirc_bank_descriptions_tbl_language_id (language_id), 
        add constraint FK_eirc_bank_descriptions_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_banks_tbl 
        add index FK_eirc_banks_tbl_organization_id (organization_id), 
        add constraint FK_eirc_banks_tbl_organization_id 
        foreign key (organization_id) 
        references eirc_organizations_tbl (id);

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
        references eirc_services_tbl (id);

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

    alter table eirc_organization_descriptions_tbl 
        add index FK_eirc_organization_description_organization (organization_id), 
        add constraint FK_eirc_organization_description_organization 
        foreign key (organization_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_organization_descriptions_tbl 
        add index FK_eirc_organization_description_language (language_id), 
        add constraint FK_eirc_organization_description_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_organization_names_tbl 
        add index FK_eirc_organization_name_organization (organization_id), 
        add constraint FK_eirc_organization_name_organization 
        foreign key (organization_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_organization_names_tbl 
        add index FK_eirc_organization_name_language (language_id), 
        add constraint FK_eirc_organization_name_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_payment_points_tbl 
        add index FK_eirc_payment_points_tbl_collector_id (collector_id), 
        add constraint FK_eirc_payment_points_tbl_collector_id 
        foreign key (collector_id) 
        references eirc_payments_collectors_tbl (id);

    alter table eirc_payments_collectors_descriptions_tbl 
        add index FK_eirc_payments_collector_descriptions_tbl_collector_id (collector_id), 
        add constraint FK_eirc_payments_collector_descriptions_tbl_collector_id 
        foreign key (collector_id) 
        references eirc_payments_collectors_tbl (id);

    alter table eirc_payments_collectors_descriptions_tbl 
        add index FK_eirc_payments_collector_descriptions_tbl_language_id (language_id), 
        add constraint FK_eirc_payments_collector_descriptions_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_payments_collectors_tbl 
        add index FK_eirc_payments_collectors_tbl_organization_id (organization_id), 
        add constraint FK_eirc_payments_collectors_tbl_organization_id 
        foreign key (organization_id) 
        references eirc_organizations_tbl (id);

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
        references eirc_registry_records_tbl (id);

    alter table eirc_quittance_details_tbl 
        add index FK_eirc_quittance_details_tbl_consumer_id (consumer_id), 
        add constraint FK_eirc_quittance_details_tbl_consumer_id 
        foreign key (consumer_id) 
        references eirc_consumers_tbl (id);

    alter table eirc_quittances_tbl 
        add index FK_eirc_quittance_services_eirc_account (eirc_account_id), 
        add constraint FK_eirc_quittance_services_eirc_account 
        foreign key (eirc_account_id) 
        references eirc_eirc_accounts_tbl (id);

    alter table eirc_quittances_tbl 
        add index FK_eirc_quittances_service_organization (service_organization_id), 
        add constraint FK_eirc_quittances_service_organization 
        foreign key (service_organization_id) 
        references eirc_service_organizations_tbl (id);

    alter table eirc_registries_tbl 
        add index FK_eirc_registry_service_provider (service_provider_id), 
        add constraint FK_eirc_registry_service_provider 
        foreign key (service_provider_id) 
        references eirc_service_providers_tbl (id);

    alter table eirc_registries_tbl 
        add index FK_eirc_registry_archive_status (archive_status_id), 
        add constraint FK_eirc_registry_archive_status 
        foreign key (archive_status_id) 
        references eirc_registry_archive_statuses_tbl (id);

    alter table eirc_registries_tbl 
        add index FK_eirc_registry_sender (sender_id), 
        add constraint FK_eirc_registry_sender 
        foreign key (sender_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_registries_tbl 
        add index FK_eirc_registry_status (registry_status_id), 
        add constraint FK_eirc_registry_status 
        foreign key (registry_status_id) 
        references eirc_registry_statuses_tbl (id);

    alter table eirc_registries_tbl 
        add index FK_eirc_registry_recipient (recipient_id), 
        add constraint FK_eirc_registry_recipient 
        foreign key (recipient_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_registries_tbl 
        add index FK_eirc_registry_registry_type (registry_type_id), 
        add constraint FK_eirc_registry_registry_type 
        foreign key (registry_type_id) 
        references eirc_registry_types_tbl (id);

    alter table eirc_registries_tbl 
        add index FK_eirc_registry_file (sp_file_id), 
        add constraint FK_eirc_registry_file 
        foreign key (sp_file_id) 
        references common_files_tbl (id);

    alter table eirc_registry_containers_tbl 
        add index FK_eirc_registry_containers_tbl_registry_id (registry_id), 
        add constraint FK_eirc_registry_containers_tbl_registry_id 
        foreign key (registry_id) 
        references eirc_registries_tbl (id);

    alter table eirc_registry_record_containers_tbl 
        add index FK_eirc_registry_record_containers_tbl_record_id (record_id), 
        add constraint FK_eirc_registry_record_containers_tbl_record_id 
        foreign key (record_id) 
        references eirc_registry_records_tbl (id);

    alter table eirc_registry_records_tbl 
        add index FK_eirc_registry_record_registry (registry_id), 
        add constraint FK_eirc_registry_record_registry 
        foreign key (registry_id) 
        references eirc_registries_tbl (id);

    alter table eirc_registry_records_tbl 
        add index FK_eirc_registry_record_person_id (person_id), 
        add constraint FK_eirc_registry_record_person_id 
        foreign key (person_id) 
        references ab_persons_tbl (id);

    alter table eirc_registry_records_tbl 
        add index FK_eirc_registry_record_apartment_id (apartment_id), 
        add constraint FK_eirc_registry_record_apartment_id 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table eirc_registry_records_tbl 
        add index FK_eirc_registry_record_consumer (consumer_id), 
        add constraint FK_eirc_registry_record_consumer 
        foreign key (consumer_id) 
        references eirc_consumers_tbl (id);

    alter table eirc_registry_records_tbl 
        add index FK_eirc_registry_record_import_error (import_error_id), 
        add constraint FK_eirc_registry_record_import_error 
        foreign key (import_error_id) 
        references common_import_errors_tbl (id);

    alter table eirc_registry_records_tbl 
        add index FK_eirc_registry_record_record_status (record_status_id), 
        add constraint FK_eirc_registry_record_record_status 
        foreign key (record_status_id) 
        references eirc_registry_record_statuses_tbl (id);

    alter table eirc_registry_records_tbl 
        add index FK_eirc_registry_records_tbl_service_id (service_id), 
        add constraint FK_eirc_registry_records_tbl_service_id 
        foreign key (service_id) 
        references eirc_services_tbl (id);

    alter table eirc_service_descriptions_tbl 
        add index FK_eirc_service__description_service (service_id), 
        add constraint FK_eirc_service__description_service 
        foreign key (service_id) 
        references eirc_services_tbl (id);

    alter table eirc_service_descriptions_tbl 
        add index FK_eirc_service_desciption_language (language_id), 
        add constraint FK_eirc_service_desciption_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_service_organization_descriptions_tbl 
        add index FK_eirc_service_organization_description_service_organization (service_organization_id), 
        add constraint FK_eirc_service_organization_description_service_organization 
        foreign key (service_organization_id) 
        references eirc_service_organizations_tbl (id);

    alter table eirc_service_organization_descriptions_tbl 
        add index FK_eirc_service_organization_description_language (language_id), 
        add constraint FK_eirc_service_organization_description_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_service_organizations_tbl 
        add index FK_eirc_service_organization_organization (organization_id), 
        add constraint FK_eirc_service_organization_organization 
        foreign key (organization_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_service_provider_descriptions_tbl 
        add index FK_eirc_service_provider_description_service_provider (service_provider_id), 
        add constraint FK_eirc_service_provider_description_service_provider 
        foreign key (service_provider_id) 
        references eirc_service_providers_tbl (id);

    alter table eirc_service_provider_descriptions_tbl 
        add index FK_eirc_service_provider_description_language (language_id), 
        add constraint FK_eirc_service_provider_description_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_service_providers_tbl 
        add index FK_eirc_service_provider_organization (organization_id), 
        add constraint FK_eirc_service_provider_organization 
        foreign key (organization_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_service_providers_tbl 
        add index FK_eirc_service_provider_data_source_description (data_source_description_id), 
        add constraint FK_eirc_service_provider_data_source_description 
        foreign key (data_source_description_id) 
        references common_data_source_descriptions_tbl (id);

    alter table eirc_service_type_name_translations_tbl 
        add index FK_eirc_service_type_name_translation_service_type (service_type_id), 
        add constraint FK_eirc_service_type_name_translation_service_type 
        foreign key (service_type_id) 
        references eirc_service_types_tbl (id);

    alter table eirc_service_type_name_translations_tbl 
        add index FK_eirc_service_type_name_translation_language (language_id), 
        add constraint FK_eirc_service_type_name_translation_language 
        foreign key (language_id) 
        references common_languages_tbl (id);

    create index INDX_eirc_service_external_code on eirc_services_tbl (external_code);

    alter table eirc_services_tbl 
        add index FK_eirc_services_tbl_measure_unit_id (measure_unit_id), 
        add constraint FK_eirc_services_tbl_measure_unit_id 
        foreign key (measure_unit_id) 
        references common_measure_units_tbl (id);

    alter table eirc_services_tbl 
        add index FK_eirc_service_parent_service_id (parent_service_id), 
        add constraint FK_eirc_service_parent_service_id 
        foreign key (parent_service_id) 
        references eirc_services_tbl (id);

    alter table eirc_services_tbl 
        add index FK_eirc_service_service_provider (provider_id), 
        add constraint FK_eirc_service_service_provider 
        foreign key (provider_id) 
        references eirc_service_providers_tbl (id);

    alter table eirc_services_tbl 
        add index FK_eirc_service_service_type (type_id), 
        add constraint FK_eirc_service_service_type 
        foreign key (type_id) 
        references eirc_service_types_tbl (id);

    alter table eirc_subdivision_descriptions_tbl 
        add index FK_eirc_subdivision_descriptions_tbl_subdivision_id (subdivision_id), 
        add constraint FK_eirc_subdivision_descriptions_tbl_subdivision_id 
        foreign key (subdivision_id) 
        references eirc_subdivisions_tbl (id);

    alter table eirc_subdivision_descriptions_tbl 
        add index FK_eirc_subdivision_descriptions_tbl_language_id (language_id), 
        add constraint FK_eirc_subdivision_descriptions_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    alter table eirc_subdivision_names_tbl 
        add index FK_eirc_subdivision_names_tbl_subdivision_id (subdivision_id), 
        add constraint FK_eirc_subdivision_names_tbl_subdivision_id 
        foreign key (subdivision_id) 
        references eirc_subdivisions_tbl (id);

    alter table eirc_subdivision_names_tbl 
        add index FK_eirc_subdivision_names_tbl_language_id (language_id), 
        add constraint FK_eirc_subdivision_names_tbl_language_id 
        foreign key (language_id) 
        references common_languages_tbl (id);

    create index INDX_tree_path on eirc_subdivisions_tbl (tree_path);

    alter table eirc_subdivisions_tbl 
        add index FK_eirc_subdivisions_tbl_parent_subdivision_id (parent_subdivision_id), 
        add constraint FK_eirc_subdivisions_tbl_parent_subdivision_id 
        foreign key (parent_subdivision_id) 
        references eirc_subdivisions_tbl (id);

    alter table eirc_subdivisions_tbl 
        add index FK_eirc_subdivisions_tbl_head_organization_id (head_organization_id), 
        add constraint FK_eirc_subdivisions_tbl_head_organization_id 
        foreign key (head_organization_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_subdivisions_tbl 
        add index FK_eirc_subdivisions_tbl_juridical_person_id (juridical_person_id), 
        add constraint FK_eirc_subdivisions_tbl_juridical_person_id 
        foreign key (juridical_person_id) 
        references eirc_organizations_tbl (id);

    alter table eirc_ticket_service_amounts_tbl 
        add index FK_eirc_ticket_service_amount_ticket (ticket_id), 
        add constraint FK_eirc_ticket_service_amount_ticket 
        foreign key (ticket_id) 
        references eirc_tickets_tbl (id);

    alter table eirc_ticket_service_amounts_tbl 
        add index FK_eirc_ticket_service_amount_consumer (consumer_id), 
        add constraint FK_eirc_ticket_service_amount_consumer 
        foreign key (consumer_id) 
        references eirc_consumers_tbl (id);

    alter table eirc_tickets_tbl 
        add index FK_eirc_ticket_person (person_id), 
        add constraint FK_eirc_ticket_person 
        foreign key (person_id) 
        references ab_persons_tbl (id);

    alter table eirc_tickets_tbl 
        add index FK_eirc_ticket_service_organization (service_organization_id), 
        add constraint FK_eirc_ticket_service_organization 
        foreign key (service_organization_id) 
        references eirc_service_organizations_tbl (id);

    alter table eirc_tickets_tbl 
        add index FK_eirc_ticket_apartment (apartment_id), 
        add constraint FK_eirc_ticket_apartment 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table oszn_tbl 
        add index FKA47957171AE9F4D (district_id), 
        add constraint FKA47957171AE9F4D 
        foreign key (district_id) 
        references ab_districts_tbl (id);

    alter table sz_apartment_number_corrections_tbl 
        add index FK53DEB6E3DEF75687 (apartment_id), 
        add constraint FK53DEB6E3DEF75687 
        foreign key (apartment_id) 
        references ab_apartments_tbl (id);

    alter table sz_apartment_number_corrections_tbl 
        add index FK53DEB6E353508F07 (oszn_id), 
        add constraint FK53DEB6E353508F07 
        foreign key (oszn_id) 
        references oszn_tbl (id);

    alter table sz_building_number_corrections_tbl 
        add index FKCFE53071F71F858D (building_id), 
        add constraint FKCFE53071F71F858D 
        foreign key (building_id) 
        references ab_buildings_tbl (id);

    alter table sz_building_number_corrections_tbl 
        add index FKCFE5307153508F07 (oszn_id), 
        add constraint FKCFE5307153508F07 
        foreign key (oszn_id) 
        references oszn_tbl (id);

    alter table sz_district_corrections_tbl 
        add index FK7E38AD7B1AE9F4D (district_id), 
        add constraint FK7E38AD7B1AE9F4D 
        foreign key (district_id) 
        references ab_districts_tbl (id);

    alter table sz_district_corrections_tbl 
        add index FK7E38AD7B53508F07 (oszn_id), 
        add constraint FK7E38AD7B53508F07 
        foreign key (oszn_id) 
        references oszn_tbl (id);

    alter table sz_files_tbl 
        add index FKC3A70A1E96D0F83F (sz_file_type_id), 
        add constraint FKC3A70A1E96D0F83F 
        foreign key (sz_file_type_id) 
        references sz_file_types_tbl (id);

    alter table sz_files_tbl 
        add index FKC3A70A1E8A1D7FFF (sz_file_status_id), 
        add constraint FKC3A70A1E8A1D7FFF 
        foreign key (sz_file_status_id) 
        references sz_file_status_tbl (id);

    alter table sz_files_tbl 
        add index FKC3A70A1EFB383BC0 (sz_file_actuality_status_id), 
        add constraint FKC3A70A1EFB383BC0 
        foreign key (sz_file_actuality_status_id) 
        references sz_file_actuality_status_tbl (id);

    alter table sz_files_tbl 
        add index FKC3A70A1E53508F07 (oszn_id), 
        add constraint FKC3A70A1E53508F07 
        foreign key (oszn_id) 
        references oszn_tbl (id);

    alter table sz_service_type_records_tbl 
        add index FKAA22F5DEB955775E (deadhead_id), 
        add constraint FKAA22F5DEB955775E 
        foreign key (deadhead_id) 
        references ab_persons_tbl (id);

    alter table sz_service_type_records_tbl 
        add index FKAA22F5DE9D0EDA76 (sz_file_id), 
        add constraint FKAA22F5DE9D0EDA76 
        foreign key (sz_file_id) 
        references sz_files_tbl (id);

    alter table sz_street_corrections_tbl 
        add index FK45AF15F0311847ED (street_id), 
        add constraint FK45AF15F0311847ED 
        foreign key (street_id) 
        references ab_streets_tbl (id);

    alter table sz_street_corrections_tbl 
        add index FK45AF15F053508F07 (oszn_id), 
        add constraint FK45AF15F053508F07 
        foreign key (oszn_id) 
        references oszn_tbl (id);
