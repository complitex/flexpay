create table bti_sewer_type_translations_tbl (
    id bigint not null auto_increment comment 'Primary key',
    name varchar(255) not null comment 'Sewer type name translation',
    description varchar(255) comment 'Optional description translation',
    sewer_type_id bigint not null comment 'Sewer type reference',
    language_id bigint not null comment 'Language reference',
    primary key (id),
    unique (sewer_type_id, language_id)
) comment='Sewer type translations';

create table bti_sewer_types_tbl (
    id bigint not null auto_increment comment 'Primary key',
    status integer not null comment 'Enabled/Disabled status',
    primary key (id)
) comment='Table, where store information about sewer types';

alter table bti_sewer_type_translations_tbl
    add index bti_sewer_type_translations_tbl_sewer_type_id (sewer_type_id),
    add constraint bti_sewer_type_translations_tbl_sewer_type_id
    foreign key (sewer_type_id)
    references bti_sewer_types_tbl (id);

alter table bti_sewer_type_translations_tbl 
    add index lang_sewer_type_pair_language_id (language_id),
    add constraint lang_sewer_type_pair_language_id
    foreign key (language_id)
    references common_languages_tbl (id);

update common_version_tbl set last_modified_date='2009-01-15', date_version=0;
