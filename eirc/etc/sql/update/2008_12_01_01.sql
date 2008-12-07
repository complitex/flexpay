alter table ab_buildings_tbl
    drop index FK_eirc_building_service_organisation,
    drop foreign key FK_eirc_building_service_organisation;

alter table eirc_bank_accounts_tbl
    drop index FK_eirc_bank_accounts_tbl_organisation_id,
    drop foreign key FK_eirc_bank_accounts_tbl_organisation_id;

alter table eirc_banks_tbl
    drop index FK_eirc_banks_tbl_organisation_id,
    drop foreign key FK_eirc_banks_tbl_organisation_id;

alter table eirc_organisation_descriptions_tbl
    drop index FK_eirc_organisation_description_organisation,
    drop foreign key FK_eirc_organisation_description_organisation,
    drop index FK_eirc_organisation_description_language,
    drop foreign key FK_eirc_organisation_description_language;

alter table eirc_organisation_names_tbl
    drop index FK_eirc_organisation_name_organisation,
    drop foreign key FK_eirc_organisation_name_organisation,
    drop index FK_eirc_organisation_name_language,
    drop foreign key FK_eirc_organisation_name_language;

alter table eirc_quittances_tbl
    drop index FK_eirc_quittances_service_organisation,
    drop foreign key FK_eirc_quittances_service_organisation;

alter table eirc_registries_tbl
    drop index FK_eirc_registry_sender,
    drop foreign key FK_eirc_registry_sender,
    drop index FK_eirc_registry_recipient,
    drop foreign key FK_eirc_registry_recipient;

alter table eirc_service_organisation_descriptions_tbl
    drop index FK_eirc_service_organisation_description_service_organisation,
    drop foreign key FK_eirc_service_organisation_description_service_organisation,
    drop index FK_eirc_service_organisation_description_language,
    drop foreign key FK_eirc_service_organisation_description_language;

alter table eirc_service_organisations_tbl
    drop index FK_eirc_service_organisation_organisation,
    drop foreign key FK_eirc_service_organisation_organisation;

alter table eirc_service_providers_tbl
    drop index FK_eirc_service_provider_organisation,
    drop foreign key FK_eirc_service_provider_organisation;

alter table eirc_subdivisions_tbl
    drop index FK_eirc_subdivisions_tbl_head_organisation_id,
    drop foreign key FK_eirc_subdivisions_tbl_head_organisation_id,
    drop index FK_eirc_subdivisions_tbl_juridical_person_id,
    drop foreign key FK_eirc_subdivisions_tbl_juridical_person_id;

alter table eirc_tickets_tbl
    drop index FK_eirc_ticket_service_organisation,
    drop foreign key FK_eirc_ticket_service_organisation;

alter table ab_buildings_tbl
	change column eirc_service_organisation_id eirc_service_organization_id bigint comment 'Service organization reference';

alter table ab_person_identities_tbl
    modify column organization varchar(4000) not null comment 'Organization gave document';

alter table eirc_bank_accounts_tbl
    change column organisation_id organization_id bigint not null comment 'Juridical person (organization) reference';

alter table eirc_banks_tbl
    change column organisation_id organization_id bigint not null comment 'Organization reference';

alter table eirc_organisation_descriptions_tbl
    change column organisation_id organization_id bigint not null comment 'Organization reference',
    rename to eirc_organization_descriptions_tbl;

alter table eirc_organisation_names_tbl
    change column organisation_id organization_id bigint not null comment 'Organization reference',
    rename to eirc_organization_names_tbl;

rename table eirc_organisations_tbl to eirc_organizations_tbl;

alter table eirc_quittances_tbl
    change column service_organisation_id service_organization_id bigint not null comment 'Service organization reference';

alter table eirc_registries_tbl
    modify column sender_id bigint comment 'Sender organization reference',
    modify column recipient_id bigint comment 'Recipient organization reference';

alter table eirc_service_organisation_descriptions_tbl
    change column service_organisation_id service_organization_id bigint not null comment 'Organization reference',
    rename to eirc_service_organization_descriptions_tbl;

alter table eirc_service_organisations_tbl
    change column organisation_id organization_id bigint not null comment 'Organization reference',
    rename to eirc_service_organizations_tbl;

alter table eirc_service_providers_tbl
    change column organisation_id organization_id bigint not null comment 'Organization reference';

alter table eirc_subdivisions_tbl
    change column head_organisation_id head_organization_id bigint not null comment 'Head organization reference',
    modify column juridical_person_id bigint comment 'Juridical person (organization) reference if any';

alter table eirc_subdivisions_tbl comment = 'Organization subdivisions';

alter table eirc_tickets_tbl
    change column service_organisation_id service_organization_id bigint not null comment 'Service organization reference';

alter table ab_buildings_tbl
    add index FK_eirc_building_service_organization (eirc_service_organization_id),
    add constraint FK_eirc_building_service_organization
    foreign key (eirc_service_organization_id)
    references eirc_service_organizations_tbl (id);

alter table eirc_bank_accounts_tbl
    add index FK_eirc_bank_accounts_tbl_organization_id (organization_id),
    add constraint FK_eirc_bank_accounts_tbl_organization_id
    foreign key (organization_id)
    references eirc_organizations_tbl (id);

alter table eirc_banks_tbl
    add index FK_eirc_banks_tbl_organization_id (organization_id), 
    add constraint FK_eirc_banks_tbl_organization_id
    foreign key (organization_id)
    references eirc_organizations_tbl (id);

alter table eirc_organization_descriptions_tbl
    add index FK_eirc_organization_description_organization (organization_id),
    add constraint FK_eirc_organization_description_organization
    foreign key (organization_id)
    references eirc_organizations_tbl (id);

alter table eirc_organization_descriptions_tbl
    add index FK_eirc_organization_description_language (language_id),
    add constraint FK_eirc_organization_description_language
    foreign key (language_id)
    references common_languages_tbl (id);

alter table eirc_organization_names_tbl
    add index FK_eirc_organization_name_organization (organization_id),
    add constraint FK_eirc_organization_name_organization
    foreign key (organization_id)
    references eirc_organizations_tbl (id);

alter table eirc_organization_names_tbl
    add index FK_eirc_organization_name_language (language_id),
    add constraint FK_eirc_organization_name_language
    foreign key (language_id)
    references common_languages_tbl (id);

alter table eirc_quittances_tbl
    add index FK_eirc_quittances_service_organization (service_organization_id),
    add constraint FK_eirc_quittances_service_organization
    foreign key (service_organization_id)
    references eirc_service_organizations_tbl (id);

alter table eirc_registries_tbl
    add index FK_eirc_registry_sender (sender_id),
    add constraint FK_eirc_registry_sender
    foreign key (sender_id)
    references eirc_organizations_tbl (id);

alter table eirc_registries_tbl
    add index FK_eirc_registry_recipient (recipient_id),
    add constraint FK_eirc_registry_recipient
    foreign key (recipient_id)
    references eirc_organizations_tbl (id);

alter table eirc_service_organization_descriptions_tbl
    add index FK_eirc_service_organization_description_service_organization (service_organization_id),
    add constraint FK_eirc_service_organization_description_service_organization
    foreign key (service_organization_id)
    references eirc_service_organizations_tbl (id);

alter table eirc_service_organization_descriptions_tbl
    add index FK_eirc_service_organization_description_language (language_id),
    add constraint FK_eirc_service_organization_description_language
    foreign key (language_id)
    references common_languages_tbl (id);

alter table eirc_service_organizations_tbl
    add index FK_eirc_service_organization_organization (organization_id),
    add constraint FK_eirc_service_organization_organization
    foreign key (organization_id)
    references eirc_organizations_tbl (id);

alter table eirc_service_providers_tbl
    add index FK_eirc_service_provider_organization (organization_id),
    add constraint FK_eirc_service_provider_organization
    foreign key (organization_id)
    references eirc_organizations_tbl (id);

alter table eirc_subdivisions_tbl
    add index FK_eirc_subdivisions_tbl_head_organization_id (head_organization_id),
    add constraint FK_eirc_subdivisions_tbl_head_organization_id
    foreign key (head_organization_id)
    references eirc_organizations_tbl (id);

alter table eirc_subdivisions_tbl
    add index FK_eirc_subdivisions_tbl_juridical_person_id (juridical_person_id),
    add constraint FK_eirc_subdivisions_tbl_juridical_person_id
    foreign key (juridical_person_id)
    references eirc_organizations_tbl (id);

alter table eirc_tickets_tbl
    add index FK_eirc_ticket_service_organization (service_organization_id),
    add constraint FK_eirc_ticket_service_organization
    foreign key (service_organization_id)
    references eirc_service_organizations_tbl (id);

update common_version_tbl set last_modified_date='2008-12-01', date_version=1;