create table
        bti_sewer_material_type_translations_tbl (
    id bigint not null auto_increment comment 'Primary key identifier',
    name varchar(255) not null comment 'Type name translation',
    description varchar(255) comment 'Type description translation',
    sewer_material_type_id bigint not null comment
            'Sewer material type reference',
    language_id bigint not null comment 'Language reference',
    primary key (id),
    unique (sewer_material_type_id, language_id)
)
        comment='Sewer material type translations';

create table bti_sewer_material_types_tbl (
    id bigint not null auto_increment comment 'Primary key identifier',
    status integer not null comment
            'Shows whether entity is active or disabled',
    primary key (id)
)
        comment='Table contains sewer material types information';

alter table
        bti_sewer_material_type_translations_tbl
add index
        bti_sewer_material_type_tbl_sewer_material_type_id (sewer_material_type_id),
add constraint
        bti_sewer_material_type_tbl_sewer_material_type_id
foreign key (sewer_material_type_id)
references bti_sewer_material_types_tbl (id);

alter table
        bti_sewer_material_type_translations_tbl
add index
        lang_sewer_material_type_pair_language_id (language_id),
add constraint
        lang_sewer_material_type_pair_language_id
foreign key (language_id)
references common_languages_tbl (id);