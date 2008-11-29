create table eirc_quittances_tbl (
	id bigint not null auto_increment,
	service_organization_id bigint not null comment 'Service organization reference',
	eirc_account_id bigint not null comment 'Eirc account reference',
	order_number integer not null comment 'quittance order number for date till',
	date_from datetime not null comment 'Quittance date from',
	date_till datetime not null comment 'Quittance date till',
	creation_date datetime not null comment 'Quittance creation date',
	primary key (id)
) comment='Quittance';

alter table eirc_quittances_tbl
	add index FK_eirc_quittances_service_organization (service_organization_id),
	add constraint FK_eirc_quittances_service_organization
	foreign key (service_organization_id) references eirc_service_organizations_tbl (id);

alter table eirc_quittances_tbl
	add index FK_eirc_quittance_services_eirc_account (eirc_account_id),
	add constraint FK_eirc_quittance_services_eirc_account
	foreign key (eirc_account_id) references eirc_eirc_accounts_tbl (id);


create table eirc_quittance_details_quittances_tbl (
	id bigint not null auto_increment,
	quittance_details_id bigint not null comment 'QuittanceDetails reference',
	quittance_id bigint not null comment 'Quittance reference',
	primary key (id)
);

alter table eirc_quittance_details_quittances_tbl
	add index FP_eirc_quittance_details_quittances_quittance (quittance_id),
	add constraint FP_eirc_quittance_details_quittances_quittance
	foreign key (quittance_id) references eirc_quittances_tbl (id);

alter table eirc_quittance_details_quittances_tbl
	add index FP_eirc_quittance_details_quittances_quittance_details (quittance_details_id),
	add constraint FP_eirc_quittance_details_quittances_quittance_details
	foreign key (quittance_details_id) references eirc_quittance_details_tbl (id);

update common_version_tbl set last_modified_date='2008-07-08', date_version=1;
