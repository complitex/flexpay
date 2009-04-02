alter table accounting_operations_tbl
		change registry_rec_ord_id registry_record_id bigint comment 'Registry record';

update common_version_tbl set last_modified_date='2009-04-02', date_version=1;
