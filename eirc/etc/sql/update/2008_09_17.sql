-- Drop account records tables, they should be replaced with a account plans
alter table eirc_account_records_tbl
	drop index FK_eirc_account_record_consumer,
	drop foreign key FK_eirc_account_record_consumer;

alter table eirc_account_records_tbl
	drop index FK_eirc_account_record_record_type,
	drop foreign key FK_eirc_account_record_record_type;

alter table eirc_account_records_tbl
	drop index FK_eirc_account_record_organisation,
	drop foreign key FK_eirc_account_record_organisation;

alter table eirc_account_records_tbl
	drop index FK_eirc_account_record_registry_record,
	drop foreign key FK_eirc_account_record_registry_record;

drop table eirc_account_record_types_tbl;
drop table eirc_account_records_tbl;

-- Update service organisation for two houses
SELECT @so_grad_n:=e.id FROM flexpay_db.eirc_service_organisations_tbl e, flexpay_db.eirc_service_organisation_descriptions_tbl ed
WHERE ed.service_organisation_id=e.id AND ed.`language_id`=1 AND ed.`name` = 'ГРАД-Н';

update ab_buildings_tbl b
        inner join common_data_corrections_tbl cr on
(b.id=cr.internal_object_id and cr.object_type=0x07 and cr.internal_object_id=b.id and cr.data_source_description_id=1 and cr.external_object_id='598989267')
set b.eirc_service_organisation_id = @so_grad_n;

SELECT @so_uchastok_49:=e.id FROM flexpay_db.eirc_service_organisations_tbl e, flexpay_db.eirc_service_organisation_descriptions_tbl ed
WHERE ed.service_organisation_id=e.id AND ed.`language_id`=1 AND ed.`name` = 'УЧАСТОК № 49';

update ab_buildings_tbl b
        inner join common_data_corrections_tbl cr on
(b.id=cr.internal_object_id and cr.object_type=0x07 and cr.internal_object_id=b.id and cr.data_source_description_id=1 and cr.external_object_id='598478302')
set b.eirc_service_organisation_id = @so_uchastok_49;


update common_version_tbl set last_modified_date='2008-09-17', date_version=0;
