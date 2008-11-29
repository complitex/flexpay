    alter table eirc_sp_registries_tbl
        drop
        foreign key FK8F6F49524B0FEDC6;

    alter table eirc_sp_registries_tbl
        drop
        foreign key FK8F6F495212E06FB1;

    alter table eirc_sp_registries_tbl
        drop
        foreign key FK8F6F495212902C71;

    alter table eirc_sp_registry_records_tbl
        drop
        foreign key FKD41D677728C54FB6;

    alter table eirc_sp_registry_records_tbl
        drop
        foreign key FKD41D677728C54FB6;

alter table eirc_sp_registries_tbl
       modify column registry_status_id bigint not null comment 'Registry status reference',
       modify column archive_status_id bigint not null comment 'Registry archive status reference',
       add column sender_id bigint comment 'Sender organization reference',
       add column recipient_id bigint comment 'Recipient organization reference';

    alter table eirc_sp_registries_tbl
        add index FK_archive_status (archive_status_id),
        add constraint FK_archive_status
        foreign key (archive_status_id)
        references eirc_sp_registry_archive_statuses_tbl (id);

    alter table eirc_sp_registries_tbl
        add index FK_sender (sender_id),
        add constraint FK_sender
        foreign key (sender_id)
        references eirc_organizations_tbl (id);

    alter table eirc_sp_registries_tbl
        add index FK_status (registry_status_id),
        add constraint FK_status
        foreign key (registry_status_id)
        references eirc_sp_registry_statuses_tbl (id);

    alter table eirc_sp_registries_tbl
        add index FK_recipient (recipient_id),
        add constraint FK_recipient
        foreign key (recipient_id)
        references eirc_organizations_tbl (id);

drop table if exists eirc_sp_registry_record_statuses_tbl;

    create table eirc_sp_registry_record_statuses_tbl (
        id bigint not null auto_increment,
        code integer not null unique,
        primary key (id)
    );

ALTER TABLE eirc_sp_registry_records_tbl ADD COLUMN record_status_id BIGINT(20) COMMENT 'Record status reference';
ALTER TABLE eirc_sp_registry_records_tbl ADD COLUMN service_type_id bigint comment 'Service type reference';
ALTER TABLE eirc_sp_registry_records_tbl ADD COLUMN import_error_id bigint comment 'Import error reference';

    alter table eirc_sp_registry_records_tbl
        add index FK_registry (registry_id),
        add constraint FK_registry
        foreign key (registry_id)
        references eirc_sp_registries_tbl (id);

    alter table eirc_sp_registry_records_tbl
        add index FK_record_status (record_status_id),
        add constraint FK_record_status
        foreign key (record_status_id)
        references eirc_sp_registry_record_statuses_tbl (id);

    alter table eirc_sp_registry_records_tbl
        add index FK_service_type (service_type_id),
        add constraint FK_service_type
        foreign key (service_type_id)
        references eirc_service_types_tbl (id);

    alter table eirc_sp_registry_records_tbl
        add index FK_import_error (import_error_id),
        add constraint FK_import_error
        foreign key (import_error_id)
        references common_import_errors_tbl (id);
