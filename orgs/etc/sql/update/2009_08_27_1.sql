alter table orgs_organizations_tbl
	add column data_source_description_id bigint not null comment 'Data source description reference';

-- migrate old source descriptions
update orgs_organizations_tbl o
	left outer join orgs_service_providers_tbl p on p.organization_id=o.id
set o.data_source_description_id=p.data_source_description_id;

-- create new source descriptions and set them to orgs
drop table if exists tmp_ds_tbl;
create temporary table tmp_ds_tbl (
	id bigint not null auto_increment,
	description varchar(255) not null,
	org_id bigint not null,
	primary key (id)
);
insert into tmp_ds_tbl (description, org_id)
	select concat('DS ORG #', id), id from orgs_organizations_tbl where data_source_description_id is null or data_source_description_id = 0;
-- delete from common_data_source_descriptions_tbl where left(description, 8)= 'DS ORG #';
insert into common_data_source_descriptions_tbl(description)
	select description from tmp_ds_tbl;
update orgs_organizations_tbl o, common_data_source_descriptions_tbl d
	set o.data_source_description_id=d.id
where substring(d.description, 9)=o.id;

alter table orgs_service_providers_tbl
	drop index FK_orgs_service_provider_data_source_description,
	drop foreign key FK_orgs_service_provider_data_source_description;

alter table orgs_service_providers_tbl
	drop column data_source_description_id;

alter table orgs_organizations_tbl
	add index FK_orgs_organization_data_source_description (data_source_description_id),
	add constraint FK_orgs_organization_data_source_description
	foreign key (data_source_description_id)
	references common_data_source_descriptions_tbl (id);

update common_version_tbl set last_modified_date='2009-08-27', date_version=1;
