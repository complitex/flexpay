-- DO NOT ENTER FOR EIRC AND REFERENCE MODULES

alter table common_registry_properties_tbl
        add column service_provider_id bigint comment 'Service provider reference';
alter table common_registry_properties_tbl
        add column sender_organisation_id bigint comment 'Sender organization reference';
alter table common_registry_properties_tbl 
        add column recipient_organisation_id bigint comment 'Recipient organization reference';

alter table common_registry_properties_tbl
        add index FK_common_registry_properties_tbl_service_provider_id (service_provider_id),
        add constraint FK_common_registry_properties_tbl_service_provider_id
        foreign key (service_provider_id)
        references orgs_service_providers_tbl (id);

alter table common_registry_properties_tbl
        add index FK_common_registry_properties_tbl_recipient_organisation_id (recipient_organisation_id),
        add constraint FK_common_registry_properties_tbl_recipient_organisation_id
        foreign key (recipient_organisation_id)
        references orgs_organizations_tbl (id);

alter table common_registry_properties_tbl
        add index FK_common_registry_properties_tbl_sender_organisation_id (sender_organisation_id),
        add constraint FK_common_registry_properties_tbl_sender_organisation_id
        foreign key (sender_organisation_id)
        references orgs_organizations_tbl (id);

update common_version_tbl set last_modified_date='2009-06-03', date_version=1;
