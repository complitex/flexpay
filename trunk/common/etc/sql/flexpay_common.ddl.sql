
    alter table common_data_corrections_tbl 
        drop 
        foreign key FKF86BDC935BA789BB;

    alter table language_names_tbl 
        drop 
        foreign key FKF47122208626C2BC;

    alter table language_names_tbl 
        drop 
        foreign key FKF471222061F37403;

    drop table if exists common_data_corrections_tbl;

    drop table if exists common_data_source_descriptions_tbl;

    drop table if exists language_names_tbl;

    drop table if exists languages_tbl;

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

    alter table common_data_corrections_tbl 
        add index FKF86BDC935BA789BB (data_source_description_id), 
        add constraint FKF86BDC935BA789BB 
        foreign key (data_source_description_id) 
        references common_data_source_descriptions_tbl (id);

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
