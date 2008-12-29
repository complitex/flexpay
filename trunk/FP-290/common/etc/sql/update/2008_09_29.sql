create table common_measure_units_tbl (
	id bigint not null auto_increment comment 'Primary key',
	status integer not null comment 'Enabled - disabled status',
	primary key (id)
) comment='Measure unit translation';

create table common_mesuare_unit_names_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Translation',
	language_id bigint not null comment 'Language reference',
	measure_unit_id bigint not null comment 'Measure unit reference',
	primary key (id),
	unique (language_id, measure_unit_id)
) comment='Measure unit translation';

alter table common_mesuare_unit_names_tbl
	add index common_mesuare_unit_names_tbl_measure_unit_id (measure_unit_id),
	add constraint common_mesuare_unit_names_tbl_measure_unit_id
	foreign key (measure_unit_id)
	references common_measure_units_tbl (id);

alter table common_mesuare_unit_names_tbl
	add index common_mesuare_unit_names_tbl_language_id (language_id),
	add constraint common_mesuare_unit_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

update common_version_tbl set last_modified_date='2008-09-29', date_version=0;
