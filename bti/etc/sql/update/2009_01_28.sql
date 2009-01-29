create table bti_building_attribute_temp_values_tbl (
	id bigint not null auto_increment,
	attribute_value varchar(255) comment 'Attribute value',
	begin_date datetime not null comment 'Value begin date',
	end_date datetime not null comment 'Value end date',
	attribute_id bigint not null comment 'Temporal attribute reference',
	primary key (id)
) comment='Temporal values for building attributes';

alter table bti_building_attributes_tbl
	change column attribute_value normal_attribute_value varchar(255) comment 'Attribute value',
	drop column begin_date,
	drop column end_date;

alter table bti_building_attribute_temp_values_tbl
	add index FK_bti_building_attribute_temp_values_tbl_attr_id (attribute_id),
	add constraint FK_bti_building_attribute_temp_values_tbl_attr_id
	foreign key (attribute_id)
	references bti_building_attributes_tbl (id);

update common_version_tbl set last_modified_date='2009-01-28', date_version=0;
