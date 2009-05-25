alter table payments_service_provider_attribute_tbl
	add constraint FK_payments_service_provider_attribute_tbl_service_provider_id
	foreign key (service_provider_id)
	references orgs_service_providers_tbl (id);

update common_version_tbl set last_modified_date='2009-05-25', date_version=7;
