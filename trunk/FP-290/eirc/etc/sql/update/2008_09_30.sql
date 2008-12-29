alter table eirc_services_tbl
	add column measure_unit_id bigint comment 'Measure unit reference';

alter table eirc_services_tbl
	add index FK_eirc_services_tbl_measure_unit_id (measure_unit_id),
	add constraint FK_eirc_services_tbl_measure_unit_id
	foreign key (measure_unit_id)
	references common_measure_units_tbl (id);

update common_version_tbl set last_modified_date='2008-09-30', date_version=0;
