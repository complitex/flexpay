alter table accounting_operations_tbl
	modify column confirmator varchar(255) comment 'Confirmator username',
	modify column confirmator_organization_id bigint comment 'Organization operation confirmed in',
	modify column registry_record_id bigint comment 'Registry record',
	modify column confirmation_date datetime comment 'Confirmation date';


update common_version_tbl set last_modified_date='2009-03-27', date_version=2;
