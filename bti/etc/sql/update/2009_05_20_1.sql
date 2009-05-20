
    create table bti_apartment_attribute_temp_values_tbl (
        id bigint not null auto_increment,
        attribute_value varchar(255) comment 'Attribute value',
        begin_date datetime not null comment 'Value begin date',
        end_date datetime not null comment 'Value end date',
        attribute_id bigint not null comment 'Temporal attribute reference',
        primary key (id)
    ) comment='Temporal values for apartment attributes';

    create table bti_apartment_attribute_type_enum_values_tbl (
        id bigint not null auto_increment,
        order_value integer not null comment 'Relatiove order value',
        value varchar(255) not null comment 'Enum value',
        attribute_type_enum_id bigint not null comment 'Attribute type enum reference',
        primary key (id)
    ) comment='Values for enumeration attribute types';

    create table bti_apartment_attribute_type_group_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        group_id bigint not null comment 'Apartment attribute type group reference',
        primary key (id),
        unique (language_id, group_id)
    ) comment='Apartment attribute type translations';

    create table bti_apartment_attribute_type_groups_tbl (
        id bigint not null auto_increment,
        version integer not null comment 'Optimistic lock version',
        status integer not null comment 'Enabled/disabled status',
        primary key (id)
    ) comment='Apartment attribute type groups';

    create table bti_apartment_attribute_type_names_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null comment 'Translation value',
        language_id bigint not null comment 'Language reference',
        attribute_type_id bigint not null comment 'Apartment attribute type reference',
        primary key (id),
        unique (language_id, attribute_type_id)
    ) comment='Apartment attribute type translations';

    create table bti_apartment_attribute_types_tbl (
        id bigint not null auto_increment,
        discriminator varchar(255) not null comment 'Class hierarchy descriminator',
        group_id bigint not null comment 'Attribute group reference',
        unique_code varchar(255) comment 'Internal unique code',
        is_temporal integer not null comment 'Temporal flag',
        primary key (id)
    ) comment='Apartment attribute types';

    create table bti_apartment_attributes_tbl (
        id bigint not null auto_increment,
        discriminator varchar(255) not null comment 'Class hierarchy descriminator',
        apartment_id bigint not null comment 'Apartment reference',
        attribute_type_id bigint not null comment 'Attribute type reference',
        normal_attribute_value varchar(255) comment 'Attribute value',
        building_id bigint not null,
        primary key (id)
    ) comment='Apartment attributes';

    alter table bti_apartment_attribute_temp_values_tbl
        add index FK_bti_apartment_attribute_temp_values_tbl_attr_id (attribute_id),
        add constraint FK_bti_apartment_attribute_temp_values_tbl_attr_id
        foreign key (attribute_id)
        references bti_apartment_attributes_tbl (id);

    alter table bti_apartment_attribute_type_enum_values_tbl
        add index bti_apartment_attribute_type_enum_values_tbl_enum_id (attribute_type_enum_id),
        add constraint bti_apartment_attribute_type_enum_values_tbl_enum_id
        foreign key (attribute_type_enum_id)
        references bti_apartment_attribute_types_tbl (id);

    alter table bti_apartment_attribute_type_group_names_tbl
        add index FK_bti_apartment_attribute_type_group_names_tbl_group_id (group_id),
        add constraint FK_bti_apartment_attribute_type_group_names_tbl_group_id
        foreign key (group_id)
        references bti_apartment_attribute_type_groups_tbl (id);

    alter table bti_apartment_attribute_type_group_names_tbl
        add index FK_bti_apartment_attribute_type_names_tbl_language_id (language_id),
        add constraint FK_bti_apartment_attribute_type_names_tbl_language_id
        foreign key (language_id)
        references common_languages_tbl (id);

    alter table bti_apartment_attribute_type_names_tbl
        add index bti_apartment_attribute_type_names_tbl_attribute_type_id (attribute_type_id),
        add constraint bti_apartment_attribute_type_names_tbl_attribute_type_id
        foreign key (attribute_type_id)
        references bti_apartment_attribute_types_tbl (id);

    alter table bti_apartment_attribute_type_names_tbl
        add index bti_apartment_attribute_type_names_tbl_language_id (language_id),
        add constraint bti_apartment_attribute_type_names_tbl_language_id
        foreign key (language_id)
        references common_languages_tbl (id);

    alter table bti_apartment_attribute_types_tbl
        add index bti_apartment_attribute_types_tbl (group_id),
        add constraint bti_apartment_attribute_types_tbl
        foreign key (group_id)
        references bti_apartment_attribute_type_groups_tbl (id);

    alter table bti_apartment_attributes_tbl
        add index bti_apartment_attributes_tbl_attribute_type_id (attribute_type_id),
        add constraint bti_apartment_attributes_tbl_attribute_type_id
        foreign key (attribute_type_id)
        references bti_apartment_attribute_types_tbl (id);

    alter table bti_apartment_attributes_tbl
        add index FK57FCB66B8A068EAC (building_id),
        add constraint FK57FCB66B8A068EAC
        foreign key (building_id)
        references ab_apartments_tbl (id);

    alter table bti_apartment_attributes_tbl
        add index FK_bti_apartment_attributes_tbl_apartment_id (apartment_id),
        add constraint FK_bti_apartment_attributes_tbl_apartment_id
        foreign key (apartment_id)
        references ab_apartments_tbl (id);
    
update common_version_tbl set last_modified_date='2009-05-20', date_version=1;
