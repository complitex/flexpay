-- fields modification
alter table eirc_bank_descriptions_tbl
		add column version integer not null comment 'Optimistic lock version';

alter table eirc_organization_descriptions_tbl
		modify column name varchar(255) not null comment 'Description value';
alter table eirc_organization_names_tbl
		modify column name varchar(255) not null comment 'Name value';
alter table eirc_organizations_tbl
		modify column version integer not null comment 'Optimistic lock version',
		modify column status integer not null comment 'Enabled/Disabled status';

alter table eirc_service_provider_descriptions_tbl
		add column version integer not null comment 'Optimistic lock version',
		modify column name varchar(255) not null comment 'Description value';
alter table eirc_service_providers_tbl
		add column version integer not null comment 'Optimistic lock version',
		modify column status integer not null comment 'Enabled-disabled status';

alter table eirc_service_organizations_tbl
		add column org_type varchar(255) not null comment 'Class hierarchy descriminator, all entities should have the same value';
update eirc_service_organizations_tbl set org_type='orgs';

alter table eirc_subdivision_descriptions_tbl
		add column version integer not null comment 'Optimistic lock version';
alter table eirc_subdivision_names_tbl
		add column version integer not null comment 'Optimistic lock version';
alter table eirc_subdivisions_tbl
		modify column version integer not null comment 'Optimistic lock version';

-- drop foreign keys and indexes
alter table eirc_bank_accounts_tbl
		drop index FK_eirc_bank_accounts_tbl_organization_id,
		drop foreign key FK_eirc_bank_accounts_tbl_organization_id;
alter table eirc_bank_accounts_tbl
		drop index FK_eirc_bank_accounts_tbl_bank_id,
		drop foreign key FK_eirc_bank_accounts_tbl_bank_id;
alter table eirc_bank_descriptions_tbl
		drop index FK_eirc_bank_descriptions_tbl_bank_id,
		drop foreign key FK_eirc_bank_descriptions_tbl_bank_id;
alter table eirc_bank_descriptions_tbl
		drop index FK_eirc_bank_descriptions_tbl_language_id,
		drop foreign key FK_eirc_bank_descriptions_tbl_language_id;
alter table eirc_banks_tbl
		drop index FK_eirc_banks_tbl_organization_id,
		drop foreign key FK_eirc_banks_tbl_organization_id;

alter table eirc_organization_descriptions_tbl
		drop index FK_eirc_organization_description_organization,
		drop foreign key FK_eirc_organization_description_organization;
alter table eirc_organization_descriptions_tbl
		drop index FK_eirc_organization_description_language,
		drop foreign key FK_eirc_organization_description_language;
alter table eirc_organization_names_tbl
		drop index FK_eirc_organization_name_organization,
		drop foreign key FK_eirc_organization_name_organization;
alter table eirc_organization_names_tbl
		drop index FK_eirc_organization_name_language,
		drop foreign key FK_eirc_organization_name_language;

alter table eirc_payment_points_tbl
		drop index FK_eirc_payment_points_tbl_collector_id,
		drop foreign key FK_eirc_payment_points_tbl_collector_id;

alter table eirc_payments_collectors_descriptions_tbl
		drop index FK_eirc_payments_collector_descriptions_tbl_collector_id,
		drop foreign key FK_eirc_payments_collector_descriptions_tbl_collector_id;
alter table eirc_payments_collectors_descriptions_tbl
		drop index FK_eirc_payments_collector_descriptions_tbl_language_id,
		drop foreign key FK_eirc_payments_collector_descriptions_tbl_language_id;
alter table eirc_payments_collectors_tbl
		drop index FK_eirc_payments_collectors_tbl_organization_id,
		drop foreign key FK_eirc_payments_collectors_tbl_organization_id;

alter table eirc_service_organization_descriptions_tbl
		drop index FK_eirc_service_organization_description_service_organization,
		drop foreign key FK_eirc_service_organization_description_service_organization;
alter table eirc_service_organization_descriptions_tbl
		drop index FK_eirc_service_organization_description_language,
		drop foreign key FK_eirc_service_organization_description_language;
alter table eirc_service_organizations_tbl
		drop index FK_eirc_service_organization_organization,
		drop foreign key FK_eirc_service_organization_organization;

alter table eirc_service_provider_descriptions_tbl
		drop index FK_eirc_service_provider_description_service_provider,
		drop foreign key FK_eirc_service_provider_description_service_provider;
alter table eirc_service_provider_descriptions_tbl
		drop index FK_eirc_service_provider_description_language,
		drop foreign key FK_eirc_service_provider_description_language;
alter table eirc_service_providers_tbl
		drop index FK_eirc_service_provider_organization,
		drop foreign key FK_eirc_service_provider_organization;
alter table eirc_service_providers_tbl
		drop index FK_eirc_service_provider_data_source_description,
		drop foreign key FK_eirc_service_provider_data_source_description;

alter table eirc_subdivision_descriptions_tbl
		drop index FK_eirc_subdivision_descriptions_tbl_subdivision_id,
		drop foreign key FK_eirc_subdivision_descriptions_tbl_subdivision_id;
alter table eirc_subdivision_descriptions_tbl
		drop index FK_eirc_subdivision_descriptions_tbl_language_id,
		drop foreign key FK_eirc_subdivision_descriptions_tbl_language_id;
alter table eirc_subdivision_names_tbl
		drop index FK_eirc_subdivision_names_tbl_subdivision_id,
		drop foreign key FK_eirc_subdivision_names_tbl_subdivision_id;
alter table eirc_subdivision_names_tbl
		drop index FK_eirc_subdivision_names_tbl_language_id,
		drop foreign key FK_eirc_subdivision_names_tbl_language_id;
alter table eirc_subdivisions_tbl
		drop index FK_eirc_subdivisions_tbl_parent_subdivision_id,
		drop foreign key FK_eirc_subdivisions_tbl_parent_subdivision_id;
alter table eirc_subdivisions_tbl
		drop index FK_eirc_subdivisions_tbl_head_organization_id,
		drop foreign key FK_eirc_subdivisions_tbl_head_organization_id;
alter table eirc_subdivisions_tbl
		drop index FK_eirc_subdivisions_tbl_juridical_person_id,
		drop foreign key FK_eirc_subdivisions_tbl_juridical_person_id;

-- tables rename
rename table eirc_bank_accounts_tbl to orgs_bank_accounts_tbl;
rename table eirc_bank_descriptions_tbl  to orgs_bank_descriptions_tbl ;
rename table eirc_banks_tbl to orgs_banks_tbl;

rename table eirc_organization_descriptions_tbl to orgs_organization_descriptions_tbl;
rename table eirc_organization_names_tbl to orgs_organization_names_tbl;
rename table eirc_organizations_tbl to orgs_organizations_tbl;

rename table eirc_payment_points_tbl to orgs_payment_points_tbl;
rename table eirc_payments_collectors_descriptions_tbl to orgs_payments_collectors_descriptions_tbl;
rename table eirc_payments_collectors_tbl to orgs_payments_collectors_tbl;

rename table eirc_service_organization_descriptions_tbl to orgs_service_organization_descriptions_tbl;
rename table eirc_service_organizations_tbl to orgs_service_organizations_tbl;

rename table eirc_service_provider_descriptions_tbl to orgs_service_provider_descriptions_tbl;
rename table eirc_service_providers_tbl to orgs_service_providers_tbl;

rename table eirc_subdivision_descriptions_tbl to orgs_subdivision_descriptions_tbl;
rename table eirc_subdivision_names_tbl to orgs_subdivision_names_tbl;
rename table eirc_subdivisions_tbl to orgs_subdivisions_tbl;

-- add foreign keys and indexes
alter table orgs_bank_accounts_tbl
	add index FK_orgs_bank_accounts_tbl_organization_id (organization_id),
	add constraint FK_orgs_bank_accounts_tbl_organization_id
	foreign key (organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_bank_accounts_tbl
	add index FK_orgs_bank_accounts_tbl_bank_id (bank_id),
	add constraint FK_orgs_bank_accounts_tbl_bank_id
	foreign key (bank_id)
	references orgs_banks_tbl (id);

alter table orgs_bank_descriptions_tbl
	add index FK_orgs_bank_descriptions_tbl_bank_id (bank_id),
	add constraint FK_orgs_bank_descriptions_tbl_bank_id
	foreign key (bank_id)
	references orgs_banks_tbl (id);

alter table orgs_bank_descriptions_tbl
	add index FK_orgs_bank_descriptions_tbl_language_id (language_id),
	add constraint FK_orgs_bank_descriptions_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_banks_tbl
	add index FK_orgs_banks_tbl_organization_id (organization_id),
	add constraint FK_orgs_banks_tbl_organization_id
	foreign key (organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_organization_descriptions_tbl
	add index FK_orgs_organization_description_organization (organization_id),
	add constraint FK_orgs_organization_description_organization
	foreign key (organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_organization_descriptions_tbl
	add index FK_orgs_organization_description_language (language_id),
	add constraint FK_orgs_organization_description_language
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_organization_names_tbl
	add index FK_orgs_organization_name_organization (organization_id),
	add constraint FK_orgs_organization_name_organization
	foreign key (organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_organization_names_tbl
	add index FK_orgs_organization_name_language (language_id),
	add constraint FK_orgs_organization_name_language
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_payment_points_tbl
	add index FK_orgs_payment_points_tbl_collector_id (collector_id),
	add constraint FK_orgs_payment_points_tbl_collector_id
	foreign key (collector_id)
	references orgs_payments_collectors_tbl (id);

alter table orgs_payments_collectors_descriptions_tbl
	add index FK_orgs_payments_collector_descriptions_tbl_collector_id (collector_id),
	add constraint FK_orgs_payments_collector_descriptions_tbl_collector_id
	foreign key (collector_id)
	references orgs_payments_collectors_tbl (id);

alter table orgs_payments_collectors_descriptions_tbl
	add index FK_orgs_payments_collector_descriptions_tbl_language_id (language_id),
	add constraint FK_orgs_payments_collector_descriptions_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_payments_collectors_tbl
	add index FK_orgs_payments_collectors_tbl_organization_id (organization_id),
	add constraint FK_orgs_payments_collectors_tbl_organization_id
	foreign key (organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_service_organization_descriptions_tbl
	add index FK_orgs_service_organization_description_service_organization (service_organization_id),
	add constraint FK_orgs_service_organization_description_service_organization
	foreign key (service_organization_id)
	references orgs_service_organizations_tbl (id);

alter table orgs_service_organization_descriptions_tbl
	add index FK_orgs_service_organization_description_language (language_id),
	add constraint FK_orgs_service_organization_description_language
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_service_organizations_tbl
	add index FK_orgs_service_organization_organization (organization_id),
	add constraint FK_orgs_service_organization_organization
	foreign key (organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_service_provider_descriptions_tbl
	add index FK_orgs_service_provider_description_service_provider (service_provider_id),
	add constraint FK_orgs_service_provider_description_service_provider
	foreign key (service_provider_id)
	references orgs_service_providers_tbl (id);

alter table orgs_service_provider_descriptions_tbl
	add index FK_orgs_service_provider_description_language (language_id),
	add constraint FK_orgs_service_provider_description_language
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_service_providers_tbl
	add index FK_orgs_service_provider_organization (organization_id),
	add constraint FK_orgs_service_provider_organization
	foreign key (organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_service_providers_tbl
	add index FK_orgs_service_provider_data_source_description (data_source_description_id),
	add constraint FK_orgs_service_provider_data_source_description
	foreign key (data_source_description_id)
	references common_data_source_descriptions_tbl (id);

alter table orgs_subdivision_descriptions_tbl
	add index FK_orgs_subdivision_descriptions_tbl_subdivision_id (subdivision_id),
	add constraint FK_orgs_subdivision_descriptions_tbl_subdivision_id
	foreign key (subdivision_id)
	references orgs_subdivisions_tbl (id);

alter table orgs_subdivision_descriptions_tbl
	add index FK_orgs_subdivision_descriptions_tbl_language_id (language_id),
	add constraint FK_orgs_subdivision_descriptions_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_subdivision_names_tbl
	add index FK_orgs_subdivision_names_tbl_subdivision_id (subdivision_id),
	add constraint FK_orgs_subdivision_names_tbl_subdivision_id
	foreign key (subdivision_id)
	references orgs_subdivisions_tbl (id);

alter table orgs_subdivision_names_tbl
	add index FK_orgs_subdivision_names_tbl_language_id (language_id),
	add constraint FK_orgs_subdivision_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_subdivisions_tbl 
	add index FK_eirc_subdivisions_tbl_parent_subdivision_id (parent_subdivision_id),
	add constraint FK_eirc_subdivisions_tbl_parent_subdivision_id
	foreign key (parent_subdivision_id)
	references orgs_subdivisions_tbl (id);

alter table orgs_subdivisions_tbl
	add index FK_eirc_subdivisions_tbl_head_organization_id (head_organization_id),
	add constraint FK_eirc_subdivisions_tbl_head_organization_id
	foreign key (head_organization_id)
	references orgs_organizations_tbl (id);

alter table orgs_subdivisions_tbl
	add index FK_eirc_subdivisions_tbl_juridical_person_id (juridical_person_id),
	add constraint FK_eirc_subdivisions_tbl_juridical_person_id
	foreign key (juridical_person_id)
	references orgs_organizations_tbl (id);

update common_version_tbl set last_modified_date='2009-04-02', date_version=0;
