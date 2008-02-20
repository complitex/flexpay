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
        foreign key FKA057A044A83C068F;

    alter table eirc_service_type_name_translations_tbl
        drop
        foreign key FKA057A0445A549E10;

    alter table eirc_services_tbl
        drop
        foreign key FK4D78EA87A26D3B0;

    alter table eirc_services_tbl
        drop
        foreign key FK4D78EA875A549E10;

    drop table if exists eirc_account_statuses_tbl;

    drop table if exists eirc_organisations_tbl;

    drop table if exists eirc_personal_account_records_tbl;

    drop table if exists eirc_personal_accounts_tbl;

    drop table if exists eirc_service_providers_tbl;

    drop table if exists eirc_service_type_name_translations_tbl;

    drop table if exists eirc_service_types_tbl;

    drop table if exists eirc_services_tbl;

	drop table if exists sz_organisations_tbl;

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
            lang bigint not null,
            type_id bigint not null,
            primary key (id),
            unique (lang, type_id)
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
