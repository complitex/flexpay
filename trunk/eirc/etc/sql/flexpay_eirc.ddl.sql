
    alter table apartment_numbers_tbl 
        drop 
        foreign key FKC790C2BCDEF75687;

    alter table apartments_tbl 
        drop 
        foreign key FK7C25D600F71F858D;

    alter table building_attribute_type_translations_tbl 
        drop 
        foreign key FKEB48455851C7D5CC;

    alter table building_attribute_type_translations_tbl 
        drop 
        foreign key FKEB48455861F37403;

    alter table building_attributes_tbl 
        drop 
        foreign key FKAC431C1ECDA1F67;

    alter table building_attributes_tbl 
        drop 
        foreign key FKAC431C151C7D5CC;

    alter table building_statuses_tbl 
        drop 
        foreign key FK99B9E2CAF71F858D;

    alter table buildings_tbl 
        drop 
        foreign key FKEA6AFBBE1AE9F4D;

    alter table buildingses_tbl 
        drop 
        foreign key FK5C8CDAC311847ED;

    alter table buildingses_tbl 
        drop 
        foreign key FK5C8CDACF71F858D;

    alter table common_data_corrections_tbl 
        drop 
        foreign key FKF86BDC935BA789BB;

    alter table country_name_translations_tbl 
        drop 
        foreign key FK5673A52C9E89EB47;

    alter table country_name_translations_tbl 
        drop 
        foreign key FK5673A52C61F37403;

    alter table district_name_translations_tbl 
        drop 
        foreign key FK3DFBB724398B1DAA;

    alter table district_name_translations_tbl 
        drop 
        foreign key FK3DFBB72461F37403;

    alter table district_names_tbl 
        drop 
        foreign key FKB64D76D61AE9F4D;

    alter table district_names_temporal_tbl 
        drop 
        foreign key FKF591B9091AE9F4D;

    alter table district_names_temporal_tbl 
        drop 
        foreign key FKF591B909398B1DAA;

    alter table districts_tbl 
        drop 
        foreign key FKCA605324712C324D;

    alter table eirc_account_statuses_tbl 
        drop 
        foreign key FKB610981B47DC07F9;

    alter table eirc_organisations_tbl 
        drop 
        foreign key FK9AA6756E1AE9F4D;

    alter table eirc_personal_account_records_tbl 
        drop 
        foreign key FK4883F2DA36052C2B;

    alter table eirc_personal_account_records_tbl 
        drop 
        foreign key FK4883F2DAF6C14BBB;

    alter table eirc_personal_accounts_tbl 
        drop 
        foreign key FK9201389A7095AEAD;

    alter table eirc_personal_accounts_tbl 
        drop 
        foreign key FK9201389ADEF75687;

    alter table eirc_personal_accounts_tbl 
        drop 
        foreign key FK9201389AC2AC3F08;

    alter table eirc_service_providers_tbl 
        drop 
        foreign key FK960743AD7F30FD59;

    alter table eirc_service_type_name_translations_tbl 
        drop 
        foreign key FKA057A0442C648686;

    alter table eirc_service_type_name_translations_tbl 
        drop 
        foreign key FKA057A04461F37403;

    alter table eirc_services_tbl 
        drop 
        foreign key FK4D78EA87A26D3B0;

    alter table eirc_services_tbl 
        drop 
        foreign key FK4D78EA875A549E10;

    alter table identity_type_translations_tbl 
        drop 
        foreign key FK8DFCEF85D8765DAA;

    alter table identity_type_translations_tbl 
        drop 
        foreign key FK8DFCEF8561F37403;

    alter table language_names_tbl 
        drop 
        foreign key FKF47122208626C2BC;

    alter table language_names_tbl 
        drop 
        foreign key FKF471222061F37403;

    alter table person_attributes_tbl 
        drop 
        foreign key FK941633007095AEAD;

    alter table person_attributes_tbl 
        drop 
        foreign key FK9416330061F37403;

    alter table person_identities_tbl 
        drop 
        foreign key FKD319C905D8765DAA;

    alter table person_identities_tbl 
        drop 
        foreign key FKD319C9057095AEAD;

    alter table person_identity_attributes_tbl 
        drop 
        foreign key FKE20EF8D1F110398;

    alter table person_identity_attributes_tbl 
        drop 
        foreign key FKE20EF8D61F37403;

    alter table persons_tbl 
        drop 
        foreign key FKE7B2AFBDDEF75687;

    alter table region_name_translations_tbl 
        drop 
        foreign key FKBAC57A0AD605B436;

    alter table region_name_translations_tbl 
        drop 
        foreign key FKBAC57A0A61F37403;

    alter table region_names_tbl 
        drop 
        foreign key FKDCA7E2BC458E164D;

    alter table region_names_temporal_tbl 
        drop 
        foreign key FK80BB4FE3D605B436;

    alter table region_names_temporal_tbl 
        drop 
        foreign key FK80BB4FE3458E164D;

    alter table regions_tbl 
        drop 
        foreign key FKA3BF8F7E9E89EB47;

    alter table street_name_translations_tbl 
        drop 
        foreign key FKF005DDD9D80067D4;

    alter table street_name_translations_tbl 
        drop 
        foreign key FKF005DDD961F37403;

    alter table street_names_tbl 
        drop 
        foreign key FK2CFC450B311847ED;

    alter table street_names_temporal_tbl 
        drop 
        foreign key FKCEDF1674311847ED;

    alter table street_names_temporal_tbl 
        drop 
        foreign key FKCEDF1674D80067D4;

    alter table street_type_translations_tbl 
        drop 
        foreign key FK5BC6DD0A3E877574;

    alter table street_type_translations_tbl 
        drop 
        foreign key FK5BC6DD0A61F37403;

    alter table street_types_temporal_tbl 
        drop 
        foreign key FK9EECCCE3311847ED;

    alter table street_types_temporal_tbl 
        drop 
        foreign key FK9EECCCE33E877574;

    alter table streets_districts_tbl 
        drop 
        foreign key FKC3D529F5311847ED;

    alter table streets_districts_tbl 
        drop 
        foreign key FKC3D529F51AE9F4D;

    alter table streets_tbl 
        drop 
        foreign key FK419CB7CF712C324D;

    alter table town_name_translations_tbl 
        drop 
        foreign key FKF1EC1328B6638732;

    alter table town_name_translations_tbl 
        drop 
        foreign key FKF1EC132861F37403;

    alter table town_names_tbl 
        drop 
        foreign key FK4304B8DA712C324D;

    alter table town_names_temporal_tbl 
        drop 
        foreign key FK870A7B85B6638732;

    alter table town_names_temporal_tbl 
        drop 
        foreign key FK870A7B85712C324D;

    alter table town_type_translations_tbl 
        drop 
        foreign key FK5DAD12591CEA94D2;

    alter table town_type_translations_tbl 
        drop 
        foreign key FK5DAD125961F37403;

    alter table town_types_temporal_tbl 
        drop 
        foreign key FK571831F41CEA94D2;

    alter table town_types_temporal_tbl 
        drop 
        foreign key FK571831F4712C324D;

    alter table towns_tbl 
        drop 
        foreign key FK92E0DEA0458E164D;

    drop table if exists apartment_numbers_tbl;

    drop table if exists apartments_tbl;

    drop table if exists building_attribute_type_translations_tbl;

    drop table if exists building_attribute_types_tbl;

    drop table if exists building_attributes_tbl;

    drop table if exists building_statuses_tbl;

    drop table if exists buildings_tbl;

    drop table if exists buildingses_tbl;

    drop table if exists common_data_corrections_tbl;

    drop table if exists common_data_source_descriptions_tbl;

    drop table if exists countries_tbl;

    drop table if exists country_name_translations_tbl;

    drop table if exists district_name_translations_tbl;

    drop table if exists district_names_tbl;

    drop table if exists district_names_temporal_tbl;

    drop table if exists districts_tbl;

    drop table if exists eirc_account_statuses_tbl;

    drop table if exists eirc_organisations_tbl;

    drop table if exists eirc_personal_account_records_tbl;

    drop table if exists eirc_personal_accounts_tbl;

    drop table if exists eirc_service_providers_tbl;

    drop table if exists eirc_service_type_name_translations_tbl;

    drop table if exists eirc_service_types_tbl;

    drop table if exists eirc_services_tbl;

    drop table if exists identity_type_translations_tbl;

    drop table if exists identity_types_tbl;

    drop table if exists language_names_tbl;

    drop table if exists languages_tbl;

    drop table if exists person_attributes_tbl;

    drop table if exists person_identities_tbl;

    drop table if exists person_identity_attributes_tbl;

    drop table if exists persons_tbl;

    drop table if exists region_name_translations_tbl;

    drop table if exists region_names_tbl;

    drop table if exists region_names_temporal_tbl;

    drop table if exists regions_tbl;

    drop table if exists street_name_translations_tbl;

    drop table if exists street_names_tbl;

    drop table if exists street_names_temporal_tbl;

    drop table if exists street_type_translations_tbl;

    drop table if exists street_types_tbl;

    drop table if exists street_types_temporal_tbl;

    drop table if exists streets_districts_tbl;

    drop table if exists streets_tbl;

    drop table if exists town_name_translations_tbl;

    drop table if exists town_names_tbl;

    drop table if exists town_names_temporal_tbl;

    drop table if exists town_type_translations_tbl;

    drop table if exists town_types_tbl;

    drop table if exists town_types_temporal_tbl;

    drop table if exists towns_tbl;

    create table apartment_numbers_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        value varchar(255) not null,
        apartment_id bigint,
        primary key (id)
    );

    create table apartments_tbl (
        id bigint not null auto_increment,
        status integer not null,
        building_id bigint not null,
        primary key (id)
    );

    create table building_attribute_type_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null,
        short_name varchar(255),
        attribute_type_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (attribute_type_id, language_id)
    );

    create table building_attribute_types_tbl (
        id bigint not null auto_increment,
        type integer,
        primary key (id)
    );

    create table building_attributes_tbl (
        id bigint not null auto_increment,
        value varchar(255) not null,
        attribute_type_id bigint not null,
        buildings_id bigint not null,
        primary key (id)
    );

    create table building_statuses_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        value varchar(255) not null,
        building_id bigint not null,
        primary key (id)
    );

    create table buildings_tbl (
        id bigint not null auto_increment,
        district_id bigint not null,
        primary key (id)
    );

    create table buildingses_tbl (
        id bigint not null auto_increment,
        status integer not null,
        street_id bigint not null,
        building_id bigint not null,
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

    create table countries_tbl (
        id bigint not null auto_increment,
        status integer not null,
        primary key (id)
    );

    create table country_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        short_name varchar(5),
        country_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (country_id, language_id)
    );

    create table district_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        district_name_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (district_name_id, language_id)
    );

    create table district_names_tbl (
        id bigint not null auto_increment,
        district_id bigint not null,
        primary key (id)
    );

    create table district_names_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        district_id bigint not null,
        district_name_id bigint,
        primary key (id)
    );

    create table districts_tbl (
        id bigint not null auto_increment,
        town_id bigint not null,
        status integer not null,
        primary key (id)
    );

    create table eirc_account_statuses_tbl (
        id bigint not null auto_increment,
        value varchar(255) not null,
        language bigint not null,
        primary key (id)
    );

    create table eirc_organisations_tbl (
        id bigint not null auto_increment,
        status integer not null,
        inn varchar(255) not null,
        kpp varchar(255) not null,
        description varchar(255) not null,
        name varchar(255) not null,
        district_id bigint not null,
        primary key (id)
    );

    create table eirc_personal_account_records_tbl (
        id bigint not null auto_increment,
        account bigint not null,
        service bigint not null,
        billBeginDate date not null,
        billEndDate date not null,
        amount numeric(19,2) not null,
        primary key (id),
        unique (account, service)
    );

    create table eirc_personal_accounts_tbl (
        id bigint not null auto_increment,
        apartment_id bigint not null,
        person_id bigint not null,
        status_id bigint not null,
        creationDate date not null,
        primary key (id),
        unique (apartment_id, person_id, status_id)
    );

    create table eirc_service_providers_tbl (
        id bigint not null auto_increment,
        organisation_id bigint not null,
        description varchar(255) not null,
        primary key (id)
    );

    create table eirc_service_type_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null,
        description varchar(255) not null,
        language_id bigint not null,
        service_type_id bigint not null,
        primary key (id),
        unique (language_id, service_type_id)
    );

    create table eirc_service_types_tbl (
        id bigint not null auto_increment,
        primary key (id)
    );

    create table eirc_services_tbl (
        id bigint not null auto_increment,
        provider_id bigint not null,
        type_id bigint not null,
        description varchar(255) not null,
        primary key (id)
    );

    create table identity_type_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        language_id bigint not null,
        identity_type_id bigint not null,
        primary key (id),
        unique (language_id, identity_type_id)
    );

    create table identity_types_tbl (
        id bigint not null auto_increment,
        status integer not null,
        type_enum integer not null,
        primary key (id)
    );

    create table language_names_tbl (
        id bigint not null auto_increment,
        translation varchar(255),
        language_id bigint not null,
        translation_from_language_id bigint not null,
        primary key (id),
        unique (language_id, translation_from_language_id)
    );

    create table languages_tbl (
        id bigint not null auto_increment,
        status integer not null,
        is_default bit,
        lang_iso_code varchar(3) not null unique,
        primary key (id)
    );

    create table person_attributes_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        value varchar(255),
        language_id bigint not null,
        person_id bigint not null,
        primary key (id),
        unique (language_id, person_id)
    );

    create table person_identities_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        birth_date date not null,
        serial_number varchar(10) not null,
        document_number varchar(20) not null,
        first_name varchar(255) not null,
        middle_name varchar(255) not null,
        last_name varchar(255) not null,
        organization varchar(4000) not null,
        is_default bit not null,
        identity_type_id bigint not null,
        person_id bigint not null,
        primary key (id)
    );

    create table person_identity_attributes_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        value varchar(255),
        language_id bigint not null,
        person_identity_id bigint not null,
        primary key (id),
        unique (language_id, person_identity_id)
    );

    create table persons_tbl (
        id bigint not null auto_increment,
        status integer not null,
        apartment_id bigint,
        primary key (id)
    );

    create table region_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        region_name_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (region_name_id, language_id)
    );

    create table region_names_tbl (
        id bigint not null auto_increment,
        region_id bigint not null,
        primary key (id)
    );

    create table region_names_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        region_id bigint not null,
        region_name_id bigint,
        primary key (id)
    );

    create table regions_tbl (
        id bigint not null auto_increment,
        country_id bigint not null,
        status integer not null,
        primary key (id)
    );

    create table street_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        street_name_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (street_name_id, language_id)
    );

    create table street_names_tbl (
        id bigint not null auto_increment,
        street_id bigint not null,
        primary key (id)
    );

    create table street_names_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        street_id bigint not null,
        street_name_id bigint,
        primary key (id)
    );

    create table street_type_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        language_id bigint not null,
        street_type_id bigint not null,
        primary key (id),
        unique (language_id, street_type_id)
    );

    create table street_types_tbl (
        id bigint not null auto_increment,
        status integer not null,
        primary key (id)
    );

    create table street_types_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        street_id bigint not null,
        street_type_id bigint,
        primary key (id)
    );

    create table streets_districts_tbl (
        district_id bigint not null,
        street_id bigint not null,
        primary key (street_id, district_id)
    );

    create table streets_tbl (
        id bigint not null auto_increment,
        town_id bigint not null,
        status integer not null,
        primary key (id)
    );

    create table town_name_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255),
        town_name_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (town_name_id, language_id)
    );

    create table town_names_tbl (
        id bigint not null auto_increment,
        town_id bigint not null,
        primary key (id)
    );

    create table town_names_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        town_id bigint not null,
        town_name_id bigint,
        primary key (id)
    );

    create table town_type_translations_tbl (
        ID bigint not null auto_increment,
        name varchar(255),
        language_id bigint,
        town_type_id bigint,
        primary key (ID),
        unique (language_id, town_type_id)
    );

    create table town_types_tbl (
        id bigint not null auto_increment,
        status integer not null,
        primary key (id)
    );

    create table town_types_temporal_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        create_date date not null,
        invalid_date date not null,
        town_id bigint not null,
        town_type_id bigint,
        primary key (id)
    );

    create table towns_tbl (
        id bigint not null auto_increment,
        region_id bigint not null,
        status integer not null,
        primary key (id)
    );

    alter table apartment_numbers_tbl 
        add index FKC790C2BCDEF75687 (apartment_id), 
        add constraint FKC790C2BCDEF75687 
        foreign key (apartment_id) 
        references apartments_tbl (id);

    alter table apartments_tbl 
        add index FK7C25D600F71F858D (building_id), 
        add constraint FK7C25D600F71F858D 
        foreign key (building_id) 
        references buildings_tbl (id);

    alter table building_attribute_type_translations_tbl 
        add index FKEB48455851C7D5CC (attribute_type_id), 
        add constraint FKEB48455851C7D5CC 
        foreign key (attribute_type_id) 
        references building_attribute_types_tbl (id);

    alter table building_attribute_type_translations_tbl 
        add index FKEB48455861F37403 (language_id), 
        add constraint FKEB48455861F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table building_attributes_tbl 
        add index FKAC431C1ECDA1F67 (buildings_id), 
        add constraint FKAC431C1ECDA1F67 
        foreign key (buildings_id) 
        references buildingses_tbl (id);

    alter table building_attributes_tbl 
        add index FKAC431C151C7D5CC (attribute_type_id), 
        add constraint FKAC431C151C7D5CC 
        foreign key (attribute_type_id) 
        references building_attribute_types_tbl (id);

    alter table building_statuses_tbl 
        add index FK99B9E2CAF71F858D (building_id), 
        add constraint FK99B9E2CAF71F858D 
        foreign key (building_id) 
        references buildings_tbl (id);

    alter table buildings_tbl 
        add index FKEA6AFBBE1AE9F4D (district_id), 
        add constraint FKEA6AFBBE1AE9F4D 
        foreign key (district_id) 
        references districts_tbl (id);

    alter table buildingses_tbl 
        add index FK5C8CDAC311847ED (street_id), 
        add constraint FK5C8CDAC311847ED 
        foreign key (street_id) 
        references streets_tbl (id);

    alter table buildingses_tbl 
        add index FK5C8CDACF71F858D (building_id), 
        add constraint FK5C8CDACF71F858D 
        foreign key (building_id) 
        references buildings_tbl (id);

    alter table common_data_corrections_tbl 
        add index FKF86BDC935BA789BB (data_source_description_id), 
        add constraint FKF86BDC935BA789BB 
        foreign key (data_source_description_id) 
        references common_data_source_descriptions_tbl (id);

    alter table country_name_translations_tbl 
        add index FK5673A52C9E89EB47 (country_id), 
        add constraint FK5673A52C9E89EB47 
        foreign key (country_id) 
        references countries_tbl (id);

    alter table country_name_translations_tbl 
        add index FK5673A52C61F37403 (language_id), 
        add constraint FK5673A52C61F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table district_name_translations_tbl 
        add index FK3DFBB724398B1DAA (district_name_id), 
        add constraint FK3DFBB724398B1DAA 
        foreign key (district_name_id) 
        references district_names_tbl (id);

    alter table district_name_translations_tbl 
        add index FK3DFBB72461F37403 (language_id), 
        add constraint FK3DFBB72461F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table district_names_tbl 
        add index FKB64D76D61AE9F4D (district_id), 
        add constraint FKB64D76D61AE9F4D 
        foreign key (district_id) 
        references districts_tbl (id);

    alter table district_names_temporal_tbl 
        add index FKF591B9091AE9F4D (district_id), 
        add constraint FKF591B9091AE9F4D 
        foreign key (district_id) 
        references districts_tbl (id);

    alter table district_names_temporal_tbl 
        add index FKF591B909398B1DAA (district_name_id), 
        add constraint FKF591B909398B1DAA 
        foreign key (district_name_id) 
        references district_names_tbl (id);

    alter table districts_tbl 
        add index FKCA605324712C324D (town_id), 
        add constraint FKCA605324712C324D 
        foreign key (town_id) 
        references towns_tbl (id);

    alter table eirc_account_statuses_tbl 
        add index FKB610981B47DC07F9 (language), 
        add constraint FKB610981B47DC07F9 
        foreign key (language) 
        references languages_tbl (id);

    alter table eirc_organisations_tbl 
        add index FK9AA6756E1AE9F4D (district_id), 
        add constraint FK9AA6756E1AE9F4D 
        foreign key (district_id) 
        references districts_tbl (id);

    alter table eirc_personal_account_records_tbl 
        add index FK4883F2DA36052C2B (service), 
        add constraint FK4883F2DA36052C2B 
        foreign key (service) 
        references eirc_services_tbl (id);

    alter table eirc_personal_account_records_tbl 
        add index FK4883F2DAF6C14BBB (account), 
        add constraint FK4883F2DAF6C14BBB 
        foreign key (account) 
        references eirc_personal_accounts_tbl (id);

    alter table eirc_personal_accounts_tbl 
        add index FK9201389A7095AEAD (person_id), 
        add constraint FK9201389A7095AEAD 
        foreign key (person_id) 
        references persons_tbl (id);

    alter table eirc_personal_accounts_tbl 
        add index FK9201389ADEF75687 (apartment_id), 
        add constraint FK9201389ADEF75687 
        foreign key (apartment_id) 
        references apartments_tbl (id);

    alter table eirc_personal_accounts_tbl 
        add index FK9201389AC2AC3F08 (status_id), 
        add constraint FK9201389AC2AC3F08 
        foreign key (status_id) 
        references eirc_account_statuses_tbl (id);

    alter table eirc_service_providers_tbl 
        add index FK960743AD7F30FD59 (organisation_id), 
        add constraint FK960743AD7F30FD59 
        foreign key (organisation_id) 
        references eirc_organisations_tbl (id);

    alter table eirc_service_type_name_translations_tbl 
        add index FKA057A0442C648686 (service_type_id), 
        add constraint FKA057A0442C648686 
        foreign key (service_type_id) 
        references eirc_service_types_tbl (id);

    alter table eirc_service_type_name_translations_tbl 
        add index FKA057A04461F37403 (language_id), 
        add constraint FKA057A04461F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table eirc_services_tbl 
        add index FK4D78EA87A26D3B0 (provider_id), 
        add constraint FK4D78EA87A26D3B0 
        foreign key (provider_id) 
        references eirc_service_providers_tbl (id);

    alter table eirc_services_tbl 
        add index FK4D78EA875A549E10 (type_id), 
        add constraint FK4D78EA875A549E10 
        foreign key (type_id) 
        references eirc_service_types_tbl (id);

    alter table identity_type_translations_tbl 
        add index FK8DFCEF85D8765DAA (identity_type_id), 
        add constraint FK8DFCEF85D8765DAA 
        foreign key (identity_type_id) 
        references identity_types_tbl (id);

    alter table identity_type_translations_tbl 
        add index FK8DFCEF8561F37403 (language_id), 
        add constraint FK8DFCEF8561F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table language_names_tbl 
        add index FKF47122208626C2BC (translation_from_language_id), 
        add constraint FKF47122208626C2BC 
        foreign key (translation_from_language_id) 
        references languages_tbl (id);

    alter table language_names_tbl 
        add index FKF471222061F37403 (language_id), 
        add constraint FKF471222061F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table person_attributes_tbl 
        add index FK941633007095AEAD (person_id), 
        add constraint FK941633007095AEAD 
        foreign key (person_id) 
        references persons_tbl (id);

    alter table person_attributes_tbl 
        add index FK9416330061F37403 (language_id), 
        add constraint FK9416330061F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table person_identities_tbl 
        add index FKD319C905D8765DAA (identity_type_id), 
        add constraint FKD319C905D8765DAA 
        foreign key (identity_type_id) 
        references identity_types_tbl (id);

    alter table person_identities_tbl 
        add index FKD319C9057095AEAD (person_id), 
        add constraint FKD319C9057095AEAD 
        foreign key (person_id) 
        references persons_tbl (id);

    alter table person_identity_attributes_tbl 
        add index FKE20EF8D1F110398 (person_identity_id), 
        add constraint FKE20EF8D1F110398 
        foreign key (person_identity_id) 
        references person_identities_tbl (id);

    alter table person_identity_attributes_tbl 
        add index FKE20EF8D61F37403 (language_id), 
        add constraint FKE20EF8D61F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table persons_tbl 
        add index FKE7B2AFBDDEF75687 (apartment_id), 
        add constraint FKE7B2AFBDDEF75687 
        foreign key (apartment_id) 
        references apartments_tbl (id);

    alter table region_name_translations_tbl 
        add index FKBAC57A0AD605B436 (region_name_id), 
        add constraint FKBAC57A0AD605B436 
        foreign key (region_name_id) 
        references region_names_tbl (id);

    alter table region_name_translations_tbl 
        add index FKBAC57A0A61F37403 (language_id), 
        add constraint FKBAC57A0A61F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table region_names_tbl 
        add index FKDCA7E2BC458E164D (region_id), 
        add constraint FKDCA7E2BC458E164D 
        foreign key (region_id) 
        references regions_tbl (id);

    alter table region_names_temporal_tbl 
        add index FK80BB4FE3D605B436 (region_name_id), 
        add constraint FK80BB4FE3D605B436 
        foreign key (region_name_id) 
        references region_names_tbl (id);

    alter table region_names_temporal_tbl 
        add index FK80BB4FE3458E164D (region_id), 
        add constraint FK80BB4FE3458E164D 
        foreign key (region_id) 
        references regions_tbl (id);

    alter table regions_tbl 
        add index FKA3BF8F7E9E89EB47 (country_id), 
        add constraint FKA3BF8F7E9E89EB47 
        foreign key (country_id) 
        references countries_tbl (id);

    alter table street_name_translations_tbl 
        add index FKF005DDD9D80067D4 (street_name_id), 
        add constraint FKF005DDD9D80067D4 
        foreign key (street_name_id) 
        references street_names_tbl (id);

    alter table street_name_translations_tbl 
        add index FKF005DDD961F37403 (language_id), 
        add constraint FKF005DDD961F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table street_names_tbl 
        add index FK2CFC450B311847ED (street_id), 
        add constraint FK2CFC450B311847ED 
        foreign key (street_id) 
        references streets_tbl (id);

    alter table street_names_temporal_tbl 
        add index FKCEDF1674311847ED (street_id), 
        add constraint FKCEDF1674311847ED 
        foreign key (street_id) 
        references streets_tbl (id);

    alter table street_names_temporal_tbl 
        add index FKCEDF1674D80067D4 (street_name_id), 
        add constraint FKCEDF1674D80067D4 
        foreign key (street_name_id) 
        references street_names_tbl (id);

    alter table street_type_translations_tbl 
        add index FK5BC6DD0A3E877574 (street_type_id), 
        add constraint FK5BC6DD0A3E877574 
        foreign key (street_type_id) 
        references street_types_tbl (id);

    alter table street_type_translations_tbl 
        add index FK5BC6DD0A61F37403 (language_id), 
        add constraint FK5BC6DD0A61F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table street_types_temporal_tbl 
        add index FK9EECCCE3311847ED (street_id), 
        add constraint FK9EECCCE3311847ED 
        foreign key (street_id) 
        references streets_tbl (id);

    alter table street_types_temporal_tbl 
        add index FK9EECCCE33E877574 (street_type_id), 
        add constraint FK9EECCCE33E877574 
        foreign key (street_type_id) 
        references street_types_tbl (id);

    alter table streets_districts_tbl 
        add index FKC3D529F5311847ED (street_id), 
        add constraint FKC3D529F5311847ED 
        foreign key (street_id) 
        references streets_tbl (id);

    alter table streets_districts_tbl 
        add index FKC3D529F51AE9F4D (district_id), 
        add constraint FKC3D529F51AE9F4D 
        foreign key (district_id) 
        references districts_tbl (id);

    alter table streets_tbl 
        add index FK419CB7CF712C324D (town_id), 
        add constraint FK419CB7CF712C324D 
        foreign key (town_id) 
        references towns_tbl (id);

    alter table town_name_translations_tbl 
        add index FKF1EC1328B6638732 (town_name_id), 
        add constraint FKF1EC1328B6638732 
        foreign key (town_name_id) 
        references town_names_tbl (id);

    alter table town_name_translations_tbl 
        add index FKF1EC132861F37403 (language_id), 
        add constraint FKF1EC132861F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table town_names_tbl 
        add index FK4304B8DA712C324D (town_id), 
        add constraint FK4304B8DA712C324D 
        foreign key (town_id) 
        references towns_tbl (id);

    alter table town_names_temporal_tbl 
        add index FK870A7B85B6638732 (town_name_id), 
        add constraint FK870A7B85B6638732 
        foreign key (town_name_id) 
        references town_names_tbl (id);

    alter table town_names_temporal_tbl 
        add index FK870A7B85712C324D (town_id), 
        add constraint FK870A7B85712C324D 
        foreign key (town_id) 
        references towns_tbl (id);

    alter table town_type_translations_tbl 
        add index FK5DAD12591CEA94D2 (town_type_id), 
        add constraint FK5DAD12591CEA94D2 
        foreign key (town_type_id) 
        references town_types_tbl (id);

    alter table town_type_translations_tbl 
        add index FK5DAD125961F37403 (language_id), 
        add constraint FK5DAD125961F37403 
        foreign key (language_id) 
        references languages_tbl (id);

    alter table town_types_temporal_tbl 
        add index FK571831F41CEA94D2 (town_type_id), 
        add constraint FK571831F41CEA94D2 
        foreign key (town_type_id) 
        references town_types_tbl (id);

    alter table town_types_temporal_tbl 
        add index FK571831F4712C324D (town_id), 
        add constraint FK571831F4712C324D 
        foreign key (town_id) 
        references towns_tbl (id);

    alter table towns_tbl 
        add index FK92E0DEA0458E164D (region_id), 
        add constraint FK92E0DEA0458E164D 
        foreign key (region_id) 
        references regions_tbl (id);
