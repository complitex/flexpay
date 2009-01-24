create table bti_building_attribute_type_enum_values_tbl (
	id bigint not null auto_increment,
	order_value integer not null comment 'Relatiove order value',
	value varchar(255) not null comment 'Enum value',
	attribute_type_enum_id bigint not null comment 'Attribute type enum reference',
	primary key (id)
) comment='Values for enumeration attribute types';

create table bti_building_attribute_type_names_tbl (
	id bigint not null auto_increment,
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	attribute_type_id bigint not null comment 'Building attribute type reference',
	primary key (id),
	unique (language_id, attribute_type_id)
) comment='Building attribute type translations';

create table bti_building_attribute_types_tbl (
	id bigint not null auto_increment,
	discriminator varchar(255) not null comment 'Class hierarchy descriminator',
	primary key (id)
);

create table bti_building_attributes_tbl (
	id bigint not null auto_increment,
	discriminator varchar(255) not null comment 'Class hierarchy descriminator',
	attribute_value varchar(255) not null comment 'Attribute value',
	building_id bigint not null comment 'Building reference',
	attribute_type_id bigint not null comment 'Attribute type reference',
	begin_date datetime comment 'Value begin date',
	end_date datetime comment 'Value end date',
	primary key (id)
) comment='Building attributes';

drop table bti_building_temp_attributes_tbl;

alter table bti_building_attribute_type_enum_values_tbl
	add index bti_building_attribute_type_enum_values_tbl_enum_id (attribute_type_enum_id),
	add constraint bti_building_attribute_type_enum_values_tbl_enum_id
	foreign key (attribute_type_enum_id)
	references bti_building_attribute_types_tbl (id);

alter table bti_building_attribute_type_names_tbl
	add index bti_building_attribute_type_names_tbl_attribute_type_id (attribute_type_id),
	add constraint bti_building_attribute_type_names_tbl_attribute_type_id
	foreign key (attribute_type_id)
	references bti_building_attribute_types_tbl (id);

alter table bti_building_attribute_type_names_tbl
	add index bti_building_attribute_type_names_tbl_language_id (language_id),
	add constraint bti_building_attribute_type_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table bti_building_attributes_tbl
	add index bti_building_attributes_tbl_attribute_type_id (attribute_type_id),
	add constraint bti_building_attributes_tbl_attribute_type_id
	foreign key (attribute_type_id)
	references bti_building_attribute_types_tbl (id);

alter table bti_building_attributes_tbl
	add index FK_bti_building_attributes_tbl_building_id (building_id),
	add constraint FK_bti_building_attributes_tbl_building_id
	foreign key (building_id)
	references ab_buildings_tbl (id);

update common_version_tbl set last_modified_date='2009-01-23', date_version=1;
