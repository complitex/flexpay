alter table common_file_statuses_tbl
	modify column description varchar(255) not null comment 'Flexpay filestatus description';

alter table common_file_types_tbl
	modify column description varchar(255) not null comment 'Filetype description';


create table eirc_payment_points_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	status integer not null comment 'Enabled-disabled status',
	address varchar(255) not null comment 'Address',
	collector_id bigint not null comment 'Payments collector reference',
	primary key (id)
) comment='Payment points';

create table eirc_payments_collectors_descriptions_tbl (
	id bigint not null auto_increment,
	name varchar(255) not null comment 'Description value',
	language_id bigint not null comment 'Language reference',
	collector_id bigint not null comment 'Payment collector reference',
	primary key (id),
	unique (language_id, collector_id)
) comment='Payment collector desriptions';

create table eirc_payments_collectors_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	status integer not null comment 'Enabled/Disabled status',
	organization_id bigint not null comment 'Organization reference',
	primary key (id)
) comment='Payment collectors';


alter table eirc_payment_points_tbl
	add index FK_eirc_payment_points_tbl_collector_id (collector_id),
	add constraint FK_eirc_payment_points_tbl_collector_id
	foreign key (collector_id)
	references eirc_payments_collectors_tbl (id);

alter table eirc_payments_collectors_descriptions_tbl
	add index FK_eirc_payments_collector_descriptions_tbl_collector_id (collector_id),
	add constraint FK_eirc_payments_collector_descriptions_tbl_collector_id
	foreign key (collector_id)
	references eirc_payments_collectors_tbl (id);

alter table eirc_payments_collectors_descriptions_tbl
	add index FK_eirc_payments_collector_descriptions_tbl_language_id (language_id),
	add constraint FK_eirc_payments_collector_descriptions_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table eirc_payments_collectors_tbl
	add index FK_eirc_payments_collectors_tbl_organization_id (organization_id),
	add constraint FK_eirc_payments_collectors_tbl_organization_id
	foreign key (organization_id)
	references eirc_organizations_tbl (id);



update common_version_tbl set last_modified_date='2008-12-23', date_version=0;
