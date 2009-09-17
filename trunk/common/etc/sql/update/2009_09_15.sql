alter table common_registries_tbl
		add column import_error_id bigint comment 'Import error reference';

alter table common_registries_tbl
	add index FK_common_registries_tbl_import_error_id (import_error_id),
	add constraint FK_common_registries_tbl_import_error_id
	foreign key (import_error_id)
	references common_import_errors_tbl (id);

update common_version_tbl set last_modified_date='2009-09-15', date_version=0;
