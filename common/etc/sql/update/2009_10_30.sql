
create index I_objectid_object_type on common_diffs_tbl (object_type, object_id);

alter table common_data_corrections_tbl
		drop index object_type,
		drop index FKF86BDC935BA789BB,
		drop foreign key FKF86BDC935BA789BB;

alter table common_data_corrections_tbl
	add index common_data_corrections_tbl_dsd_id (data_source_description_id),
	add constraint common_data_corrections_tbl_dsd_id
	foreign key (data_source_description_id)
	references common_data_source_descriptions_tbl (id);

alter table common_data_corrections_tbl
		add unique index I_externalid_datasource_objecttype (object_type, external_object_id, data_source_description_id),
		add index I_internalid_datasource_objecttype (object_type, internal_object_id, data_source_description_id);


update common_version_tbl set last_modified_date='2009-10-30', date_version=0;
