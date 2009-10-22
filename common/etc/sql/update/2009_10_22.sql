alter table common_registry_records_tbl
		add column import_error_type integer comment 'Import error type from import error',
		modify record_status_id bigint not null comment 'Record status reference';

create index I_registry_errortype on common_registry_records_tbl (registry_id, import_error_type);

create index I_registry_status on common_registry_records_tbl (registry_id, record_status_id);


update common_registry_records_tbl rr left outer join common_import_errors_tbl e on rr.import_error_id=e.id
	set rr.import_error_type=e.object_type
where e.status=0;

update common_version_tbl set last_modified_date='2009-10-22', date_version=0;
