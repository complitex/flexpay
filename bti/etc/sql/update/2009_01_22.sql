create table bti_building_temp_attributes_tbl (
	id bigint not null auto_increment,
	begin_date datetime not null comment 'Value begin date',
	end_date datetime not null comment 'Value end date',
	attribute_name varchar(255) not null comment 'Attribute name',
	attribute_value varchar(255) not null comment 'Attribute value',
	building_id bigint not null comment 'Building reference',
	primary key (id)
) comment='Building time-dependent attributes';


alter table bti_building_temp_attributes_tbl
	add index FK_bti_building_attributes_tbl_building_id (building_id),
	add constraint FK_bti_building_attributes_tbl_building_id
	foreign key (building_id)
	references ab_buildings_tbl (id);


update common_version_tbl set last_modified_date='2009-01-22', date_version=0;
