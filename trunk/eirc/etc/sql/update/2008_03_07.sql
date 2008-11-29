alter table eirc_organizations_tbl add column individual_tax_number varchar(255) not null;

    alter table eirc_personal_account_records_tbl
        drop
        foreign key FK4883F2DAD0BDDFB;

    alter table eirc_personal_account_records_tbl
        drop
        foreign key FK4883F2DA58F3985B;

    create table eirc_consumers_tbl (
        id bigint not null auto_increment,
        status integer not null,
        external_account_number varchar(255) not null,
        service_id bigint not null,
        account_id bigint not null,
        primary key (id)
    );

    create table eirc_personal_account_record_types_tbl (
        id bigint not null auto_increment,
        description integer not null,
        type_enum_id integer not null,
        primary key (id)
    );

ALTER TABLE eirc_personal_account_records_tbl
 DROP FOREIGN KEY FK4883F2DA58F3985B,
 DROP FOREIGN KEY FK4883F2DAD0BDDFB;

ALTER TABLE eirc_personal_account_records_tbl
 DROP COLUMN account_id,
 DROP COLUMN service_id,
 DROP COLUMN bill_begin_date,
 DROP COLUMN bill_end_date,
 ADD COLUMN consumer_id bigint not null,
 ADD COLUMN operation_date datetime not null,
 ADD COLUMN record_type_id bigint not null;

ALTER TABLE eirc_service_providers_tbl
 ADD COLUMN data_source_description_id bigint not null,
 ADD COLUMN provider_number bigint not null unique;

ALTER TABLE eirc_service_providers_tbl
 ADD COLUMN status integer not null,
 ADD COLUMN code integer not null;

    create table eirc_sp_registries_tbl (
        id bigint not null auto_increment,
        registry_number bigint,
        records_number bigint,
        creation_date datetime,
        from_date datetime,
        till_date datetime,
        sender_code bigint,
        recipient_code bigint,
        amount decimal(19,2),
        containers varchar(255),
        registry_type_id bigint not null,
        sp_file_id bigint not null,
        service_provider_id bigint,
        primary key (id)
    );

    create table eirc_sp_registry_records_tbl (
        id bigint not null auto_increment,
        service_code bigint,
        personal_account_ext varchar(255),
        city varchar(255),
        street_type varchar(255),
        street_name varchar(255),
        building_number varchar(255),
        bulk_number varchar(255),
        apartment_number varchar(255),
        first_name varchar(255),
        middle_name varchar(255),
        last_name varchar(255),
        operation_date datetime,
        unique_operation_number bigint,
        amount decimal(19,2),
        containers varchar(255),
        personal_account_id bigint,
        registry_id bigint not null,
        primary key (id)
    );

    create table eirc_sp_registry_types_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null,
        direction varchar(255) not null,
        primary key (id)
    );


    alter table eirc_consumers_tbl
        add index FK9751FED2D0BDDFB (account_id),
        add constraint FK9751FED2D0BDDFB
        foreign key (account_id)
        references eirc_personal_accounts_tbl (id);

    alter table eirc_consumers_tbl
        add index FK9751FED258F3985B (service_id),
        add constraint FK9751FED258F3985B
        foreign key (service_id)
        references eirc_services_tbl (id);

    alter table eirc_personal_account_records_tbl
        add index FK4883F2DA91349F59 (consumer_id),
        add constraint FK4883F2DA91349F59
        foreign key (consumer_id)
        references eirc_consumers_tbl (id);

    alter table eirc_personal_account_records_tbl
        add index FK4883F2DAA16375AB (record_type_id),
        add constraint FK4883F2DAA16375AB
        foreign key (record_type_id)
        references eirc_personal_account_record_types_tbl (id);

    alter table eirc_service_providers_tbl
        add index FK960743AD5BA789BB (data_source_description_id),
        add constraint FK960743AD5BA789BB
        foreign key (data_source_description_id)
        references common_data_source_descriptions_tbl (id);


    alter table eirc_sp_registries_tbl
        add index FK8F6F49528819126 (service_provider_id),
        add constraint FK8F6F49528819126
        foreign key (service_provider_id)
        references eirc_service_providers_tbl (id);

    alter table eirc_sp_registries_tbl
        add index FK8F6F495212902C71 (registry_type_id),
        add constraint FK8F6F495212902C71
        foreign key (registry_type_id)
        references eirc_sp_registry_types_tbl (id);

    alter table eirc_sp_registries_tbl
        add index FK8F6F4952D1F3C974 (sp_file_id),
        add constraint FK8F6F4952D1F3C974
        foreign key (sp_file_id)
        references eirc_sp_files_tbl (id);

    alter table eirc_sp_registry_records_tbl
        add index FKD41D6777B835A55A (personal_account_id),
        add constraint FKD41D6777B835A55A
        foreign key (personal_account_id)
        references eirc_personal_accounts_tbl (id);

    alter table eirc_sp_registry_records_tbl
        add index FKD41D677728C54FB6 (registry_id),
        add constraint FKD41D677728C54FB6
        foreign key (registry_id)
        references eirc_sp_registries_tbl (id);

-- Init personal account record types
INSERT INTO eirc_personal_account_record_types_tbl (type_id, description)
	VALUES (0, 'Ошибочный');
INSERT INTO eirc_personal_account_record_types_tbl (type_id, description)
	VALUES (1, 'Входящая оплата');
INSERT INTO eirc_personal_account_record_types_tbl (type_id, description)
	VALUES (2, 'Исходящая оплата');
INSERT INTO eirc_personal_account_record_types_tbl (type_id, description)
	VALUES (3, 'Сальдо');

UPDATE common_version_tbl SET last_modified_date='2007-03-07', date_version=1;
