alter table orgs_payments_collectors_tbl rename to orgs_payment_collectors_tbl;

alter table orgs_payment_collectors_tbl	drop foreign key FK_orgs_payments_collectors_tbl_organization_id;
drop index FK_orgs_payments_collectors_tbl_organization_id on orgs_payment_collectors_tbl;

alter table orgs_payment_collectors_tbl
	add index FK_orgs_payment_collectors_tbl_organization_id (organization_id),
	add constraint FK_orgs_payment_collectors_tbl_organization_id
	foreign key (organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_payments_collectors_descriptions_tbl rename to orgs_payment_collectors_descriptions_tbl;

alter table orgs_payment_collectors_descriptions_tbl drop foreign key FK_orgs_payments_collector_descriptions_tbl_collector_id;
drop index FK_orgs_payments_collector_descriptions_tbl_collector_id on orgs_payment_collectors_descriptions_tbl;

alter table orgs_payment_collectors_descriptions_tbl
	add index FK_orgs_payment_collector_descriptions_tbl_collector_id (collector_id),
	add constraint FK_orgs_payment_collector_descriptions_tbl_collector_id
	foreign key (collector_id)
	references orgs_payment_collectors_tbl (id);

alter table orgs_payment_collectors_descriptions_tbl drop foreign key FK_orgs_payments_collector_descriptions_tbl_language_id;

drop index FK_orgs_payments_collector_descriptions_tbl_language_id on orgs_payment_collectors_descriptions_tbl;

alter table orgs_payment_collectors_descriptions_tbl
	add index FK_orgs_payment_collector_descriptions_tbl_language_id (language_id),
	add constraint FK_orgs_payment_collector_descriptions_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

update common_version_tbl set last_modified_date='2009-09-17', date_version=0;