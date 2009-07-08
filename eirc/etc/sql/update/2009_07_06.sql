create table eirc_consumer_attribute_type_enum_values_tbl (
	id bigint not null auto_increment,
	order_value integer not null comment 'Relational order value',
	date_value datetime comment 'Optional date value',
	int_value integer comment 'Optional int value',
	bool_value bit comment 'Optional boolean value',
	long_value bigint comment 'Optional long value',
	string_value varchar(255) comment 'Optional string value',
	double_value double precision comment 'Optional double value',
	decimal_value decimal(19,5) comment 'Optional double value',
	value_type integer not null comment 'Value type discriminator',
	attribute_type_enum_id bigint not null comment 'Attribute type enum reference',
	primary key (id)
) comment='Values for enumeration attribute types';

create table eirc_consumer_attribute_type_names_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	attribute_type_id bigint not null comment 'Consumer attribute type reference',
	primary key (id),
	unique (language_id, attribute_type_id)
) comment='Consumer attribute type translations';

create table eirc_consumer_attribute_types_tbl (
	id bigint not null auto_increment comment 'Primary key',
	discriminator varchar(255) not null comment 'Class hierarchy discriminator',
	status integer not null comment 'Enabled-disabled status',
	version integer not null comment 'Optimistic lock version',
	unique_code varchar(255) comment 'Internal unique code',
	is_temporal integer not null comment 'Temporal flag',
	measure_unit_id bigint comment 'Optional measure unit reference',
	primary key (id)
) comment='Consumer attribute types';

create table eirc_consumer_attributes_tbl (
	id bigint not null auto_increment comment 'Primary key',
	date_value datetime comment 'Optional date value',
	int_value integer comment 'Optional int value',
	bool_value bit comment 'Optional boolean value',
	long_value bigint comment 'Optional long value',
	string_value varchar(255) comment 'Optional string value',
	double_value double precision comment 'Optional double value',
	decimal_value decimal(19,5) comment 'Optional double value',
	value_type integer not null comment 'Value type discriminator',
	begin_date date comment 'Attribute value begin date',
	end_date date comment 'Attribute value end date',
	temporal_flag integer not null comment 'Temporal attribute flag',
	consumer_id bigint not null comment 'Consumer reference',
	type_id bigint not null comment 'Attribute type reference',
	primary key (id)
) comment='Consumer attribute types';

alter table eirc_consumer_attribute_type_enum_values_tbl
	add index eirc_consumer_attribute_type_enum_values_tbl_enum_id (attribute_type_enum_id),
	add constraint eirc_consumer_attribute_type_enum_values_tbl_enum_id
	foreign key (attribute_type_enum_id)
	references eirc_consumer_attribute_types_tbl (id);

alter table eirc_consumer_attribute_type_names_tbl
	add index FK_eirc_consumer_attribute_type_names_tbl_type_id (attribute_type_id),
	add constraint FK_eirc_consumer_attribute_type_names_tbl_type_id
	foreign key (attribute_type_id)
	references eirc_consumer_attribute_types_tbl (id);

alter table eirc_consumer_attribute_type_names_tbl
	add index FK_eirc_consumer_attribute_type_names_tbl_language_id (language_id),
	add constraint FK_eirc_consumer_attribute_type_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table eirc_consumer_attribute_types_tbl
	add index FK_eirc_consumer_attribute_types_tbl_unit_id (measure_unit_id),
	add constraint FK_eirc_consumer_attribute_types_tbl_unit_id
	foreign key (measure_unit_id)
	references common_measure_units_tbl (id);

alter table eirc_consumer_attributes_tbl
	add index FK_eirc_consumer_attributes_tbl_consumer_id (consumer_id),
	add constraint FK_eirc_consumer_attributes_tbl_consumer_id
	foreign key (consumer_id)
	references eirc_consumers_tbl (id);

alter table eirc_consumer_attributes_tbl
	add index FK_eirc_consumer_attributes_tbl_type_id (type_id),
	add constraint FK_eirc_consumer_attributes_tbl_type_id
	foreign key (type_id)
	references eirc_consumer_attribute_types_tbl (id);

update common_version_tbl set last_modified_date='2009-07-06', date_version=0;
