
    alter table common_data_corrections_tbl 
        drop 
        foreign key FKF86BDC935BA789BB;

    alter table common_import_errors_tbl 
        drop 
        foreign key FKBAEED8705355D490;

    alter table common_language_names_tbl 
        drop 
        foreign key FK85F168F48626C2BC;

    alter table common_language_names_tbl 
        drop 
        foreign key FK85F168F461F37403;

    drop table if exists common_data_corrections_tbl;

    drop table if exists common_data_source_descriptions_tbl;

    drop table if exists common_import_errors_tbl;

    drop table if exists common_language_names_tbl;

    drop table if exists common_languages_tbl;

    drop table if exists common_sequences_tbl;

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
