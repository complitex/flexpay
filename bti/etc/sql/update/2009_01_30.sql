create table bti_building_attribute_type_group_names_tbl (
	id bigint not null auto_increment,
	name varchar(255) not null comment 'Translation value',
	language_id bigint not null comment 'Language reference',
	group_id bigint not null comment 'Building attribute type group reference',
	primary key (id),
	unique (language_id, group_id)
) comment='Building attribute type translations';

create table bti_building_attribute_type_groups_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	status integer not null comment 'Enabled/disabled status',
	primary key (id)
) comment='Building attribute type groups';

alter table bti_building_attribute_type_group_names_tbl
	add index FK_bti_building_attribute_type_group_names_tbl_group_id (group_id),
	add constraint FK_bti_building_attribute_type_group_names_tbl_group_id
	foreign key (group_id)
	references bti_building_attribute_type_groups_tbl (id);

alter table bti_building_attribute_type_group_names_tbl
	add index FK_bti_building_attribute_type_names_tbl_language_id (language_id),
	add constraint FK_bti_building_attribute_type_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

-- init building attribute group
SELECT @ru_id:=id FROM common_languages_tbl WHERE lang_iso_code='ru';
SELECT @en_id:=id FROM common_languages_tbl WHERE lang_iso_code='en';
insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (1, 0, 0);
select @attribute_group_1:=1;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Другие', @ru_id, @attribute_group_1);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Other', @en_id, @attribute_group_1);

alter table bti_building_attribute_types_tbl
	add column group_id bigint not null comment 'Attribute group reference';

-- put all attribute types to other by default
update bti_building_attribute_types_tbl set group_id=@attribute_group_1;

alter table bti_building_attribute_types_tbl 
	add index bti_building_attribute_types_tbl (group_id),
	add constraint bti_building_attribute_types_tbl
	foreign key (group_id)
	references bti_building_attribute_type_groups_tbl (id);


update common_version_tbl set last_modified_date='2009-01-30', date_version=0;
