    create table common_import_errors_tbl (
        id bigint not null auto_increment,
        status integer not null,
        source_description_id bigint not null,
        object_type integer not null,
        ext_object_id varchar(255) not null,
        handler_object_name varchar(255) not null,
        primary key (id)
    );

    alter table common_import_errors_tbl
        add index FKBAEED8705355D490 (source_description_id),
        add constraint FKBAEED8705355D490
        foreign key (source_description_id)
        references common_data_source_descriptions_tbl (id);

alter table sequences_tbl rename to common_sequences_tbl ;
alter table SEMAPHORE rename to common_semaphores_tbl ;

