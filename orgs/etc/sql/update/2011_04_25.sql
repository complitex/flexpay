alter table payments_service_provider_attribute_tbl
    drop foreign key FK_payments_service_provider_attribute_tbl_service_provider_id;
drop index FK_payments_service_provider_attribute_tbl_service_provider_id on payments_service_provider_attribute_tbl;

alter table payments_service_provider_attribute_tbl rename to orgs_service_provider_attributes_tbl;

alter table orgs_service_provider_attributes_tbl
	add index FK_payments_service_provider_attributes_tbl_service_provider_id (service_provider_id),
	add constraint FK_payments_service_provider_attributes_tbl_service_provider_id
	foreign key (service_provider_id)
    references orgs_service_providers_tbl (id);

alter table orgs_subdivisions_tbl
    drop foreign key FK_eirc_subdivisions_tbl_parent_subdivision_id;
drop index FK_eirc_subdivisions_tbl_parent_subdivision_id on orgs_subdivisions_tbl;

alter table orgs_subdivisions_tbl
    add index FK_orgs_subdivisions_tbl_parent_subdivision_id (parent_subdivision_id),
    add constraint FK_orgs_subdivisions_tbl_parent_subdivision_id
    foreign key (parent_subdivision_id)
    references orgs_subdivisions_tbl (id);

alter table orgs_subdivisions_tbl
    drop foreign key FK_eirc_subdivisions_tbl_head_subdivision_id;
drop index FK_eirc_subdivisions_tbl_head_subdivision_id on orgs_subdivisions_tbl;

alter table orgs_subdivisions_tbl
    add index FK_orgs_subdivisions_tbl_head_organization_id (head_organization_id),
    add constraint FK_orgs_subdivisions_tbl_head_organization_id
    foreign key (head_organization_id)
    references orgs_organizations_tbl (id);

alter table orgs_subdivisions_tbl
    drop foreign key FK_eirc_subdivisions_tbl_juridical_person_id;
drop index FK_eirc_subdivisions_tbl_juridical_person_id on orgs_subdivisions_tbl;

alter table orgs_subdivisions_tbl
    add index FK_orgs_subdivisions_tbl_juridical_person_id (juridical_person_id),
    add constraint FK_orgs_subdivisions_tbl_juridical_person_id
    foreign key (juridical_person_id)
    references orgs_organizations_tbl (id);

update common_version_tbl set last_modified_date='2011-04-25', date_version=0;
