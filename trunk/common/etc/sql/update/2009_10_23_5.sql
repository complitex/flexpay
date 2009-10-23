alter table common_registries_tbl
	add index FK_common_registries_tbl_module_id (module_id),
	add constraint FK_common_registries_tbl_module_id
	foreign key (module_id)
	references common_flexpay_modules_tbl (id);

update common_version_tbl set last_modified_date='2009-10-23', date_version=5;
