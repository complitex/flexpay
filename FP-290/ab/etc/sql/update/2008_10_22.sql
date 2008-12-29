alter table ab_building_attribute_type_translations_tbl
	drop index FKCD4187B651C7D5CC,
	drop foreign key FKCD4187B651C7D5CC;

alter table ab_building_attribute_type_translations_tbl
	drop index FKCD4187B661F37403,
	drop foreign key FKCD4187B661F37403;

alter table ab_building_attributes_tbl
	drop index FKDD2E2FA3ECDA1F67,
	drop foreign key FKDD2E2FA3ECDA1F67;

alter table ab_building_attributes_tbl
	drop index FKDD2E2FA351C7D5CC,
	drop index indx_value,
	drop foreign key FKDD2E2FA351C7D5CC;

alter table ab_building_statuses_tbl
	drop index FK68EDF12CF71F858D,
	drop foreign key FK68EDF12CF71F858D;

alter table ab_buildings_tbl
	drop index FK99FC8C201AE9F4D,
	drop foreign key FK99FC8C201AE9F4D;

alter table ab_buildingses_tbl
	drop index FK1737CD8E311847ED,
	drop foreign key FK1737CD8E311847ED;

alter table ab_buildingses_tbl
	drop index FK1737CD8EF71F858D,
	drop foreign key FK1737CD8EF71F858D;

alter table ab_building_attribute_type_translations_tbl
	modify column name varchar(255) not null comment 'Type translation',
	modify column short_name varchar(255) comment 'Optional short translation',
	modify column attribute_type_id bigint not null comment 'Building attribute type reference',
	modify column language_id bigint not null comment 'Language reference',
	comment='Building attribute type translations';

alter table ab_building_attribute_types_tbl
	add column status integer not null comment 'Enabled/Disabled status';

alter table ab_building_attributes_tbl
	add column status integer not null comment 'Enabled/Disabled status',
	modify column attribute_type_id bigint not null comment 'Attribute type reference',
	modify column buildings_id bigint not null comment 'Building address reference',
	comment='Building address attributes';

alter table ab_building_statuses_tbl
	modify column begin_date date not null comment 'Status begin date',
	modify column end_date date not null comment 'Status end date',
	modify column value varchar(255) not null comment 'Status value',
	modify column building_id bigint not null comment 'Building reference status belongs to',
	comment='Buildings statuses, for example building started or rebuilding';

alter table ab_buildingses_tbl
	modify column status integer not null comment 'Enabled/Disabled status',
	modify column primary_status bit not null comment 'Flag of primary building address',
	modify column street_id bigint not null comment 'Street reference',
	modify column building_id bigint not null comment 'Building reference this address belongs to',
	comment='Building addresses';

alter table ab_building_attribute_type_translations_tbl
	add index ab_building_attribute_type_translations_tbl_attribute_type_id (attribute_type_id),
	add constraint ab_building_attribute_type_translations_tbl_attribute_type_id
	foreign key (attribute_type_id)
	references ab_building_attribute_types_tbl (id);

alter table ab_building_attribute_type_translations_tbl
	add index lang_building_attribute_type_pair_language_id (language_id),
	add constraint lang_building_attribute_type_pair_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

create index indx_value on ab_building_attributes_tbl (value);

alter table ab_building_attributes_tbl
	add index ab_building_attributes_tbl_buildings_id (buildings_id),
	add constraint ab_building_attributes_tbl_buildings_id
	foreign key (buildings_id)
	references ab_buildingses_tbl (id);

alter table ab_building_attributes_tbl
	add index ab_building_attributes_tbl_attribute_type_id (attribute_type_id),
	add constraint ab_building_attributes_tbl_attribute_type_id
	foreign key (attribute_type_id)
	references ab_building_attribute_types_tbl (id);

alter table ab_building_statuses_tbl
	add index ab_building_statuses_tbl_building_id (building_id),
	add constraint ab_building_statuses_tbl_building_id
	foreign key (building_id)
	references ab_buildings_tbl (id);

alter table ab_buildings_tbl
	add index ab_buildings_tbl_district_id (district_id),
	add constraint ab_buildings_tbl_district_id
	foreign key (district_id)
	references ab_districts_tbl (id);

alter table ab_buildingses_tbl
	add index ab_buildingses_tbl_street_id (street_id),
	add constraint ab_buildingses_tbl_street_id
	foreign key (street_id)
	references ab_streets_tbl (id);

alter table ab_buildingses_tbl
	add index ab_buildingses_tbl_building_id (building_id),
	add constraint ab_buildingses_tbl_building_id
	foreign key (building_id)
	references ab_buildings_tbl (id);


update common_version_tbl set last_modified_date='2008-10-20', date_version=0;
