create table eirc_bank_accounts_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optiomistic lock version',
	status integer not null comment 'Enabled/Disabled status',
	account_number varchar(255) not null comment 'Bank account number',
	is_default bit not null comment 'Juridical person default account flag',
	bank_id bigint not null comment 'Bank reference',
	organisation_id bigint not null comment 'Juridical person (organisation) reference',
	primary key (id)
) comment='Bank accounts';

create table eirc_bank_descriptions_tbl (
	id bigint not null auto_increment,
	name varchar(255) not null comment 'Description value',
	language_id bigint not null comment 'Language reference',
	bank_id bigint not null comment 'Bank reference',
	primary key (id),
	unique (language_id, bank_id)
) comment='Bank desriptions';

create table eirc_banks_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optiomistic lock version',
	status integer not null comment 'Enabled/Disabled status',
	organisation_id bigint not null comment 'Organisation reference',
	primary key (id)
) comment='Banks';

alter table eirc_organisations_tbl
	add column juridical_address varchar(255) not null comment 'Juridical address',
	add column postal_address varchar(255) not null comment 'Postal address',
	add column real_address varchar(255) not null comment 'Real address';


create table eirc_subdivision_descriptions_tbl (
	id bigint not null auto_increment,
	name varchar(255) not null comment 'Description value',
	language_id bigint not null comment 'Language reference',
	subdivision_id bigint not null comment 'Subdivision reference',
	primary key (id),
	unique (language_id, subdivision_id)
);

create table eirc_subdivision_names_tbl (
	id bigint not null auto_increment,
	name varchar(255) not null comment 'Name value',
	language_id bigint not null comment 'Language reference',
	subdivision_id bigint not null comment 'Subdivision reference',
	primary key (id),
	unique (language_id, subdivision_id)
);

create table eirc_subdivisions_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optiomistic lock version',
	status integer not null comment 'Enabled/Disabled status',
	real_address varchar(255) not null comment 'Subdivision real address',
	parent_subdivision_id bigint comment 'Parent subdivision reference if any',
	head_organisation_id bigint not null comment 'Head organisation reference',
	juridical_person_id bigint comment 'Juridical person (organisation) reference if any',
	organisation_id bigint not null,
	primary key (id)
) comment='Organisation subdivisions';


alter table eirc_bank_accounts_tbl
	add index FK_eirc_bank_accounts_tbl_bank_id (bank_id),
	add constraint FK_eirc_bank_accounts_tbl_bank_id
	foreign key (bank_id)
	references eirc_banks_tbl (id);

alter table eirc_bank_accounts_tbl
	add index FK_eirc_bank_accounts_tbl_organisation_id (organisation_id),
	add constraint FK_eirc_bank_accounts_tbl_organisation_id
	foreign key (organisation_id)
	references eirc_organisations_tbl (id);

alter table eirc_bank_descriptions_tbl
	add index FK_eirc_bank_descriptions_tbl_bank_id (bank_id),
	add constraint FK_eirc_bank_descriptions_tbl_bank_id
	foreign key (bank_id)
	references eirc_banks_tbl (id);

alter table eirc_bank_descriptions_tbl
	add index FK_eirc_bank_descriptions_tbl_language_id (language_id),
	add constraint FK_eirc_bank_descriptions_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table eirc_banks_tbl
	add index FK_eirc_banks_tbl_organisation_id (organisation_id),
	add constraint FK_eirc_banks_tbl_organisation_id
	foreign key (organisation_id)
	references eirc_organisations_tbl (id);


alter table eirc_subdivision_descriptions_tbl
	add index FK_eirc_subdivision_descriptions_tbl_subdivision_id (subdivision_id),
	add constraint FK_eirc_subdivision_descriptions_tbl_subdivision_id
	foreign key (subdivision_id)
	references eirc_subdivisions_tbl (id);

alter table eirc_subdivision_descriptions_tbl
	add index FK_eirc_subdivision_descriptions_tbl_language_id (language_id),
	add constraint FK_eirc_subdivision_descriptions_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table eirc_subdivision_names_tbl
	add index FK_eirc_subdivision_names_tbl_subdivision_id (subdivision_id),
	add constraint FK_eirc_subdivision_names_tbl_subdivision_id
	foreign key (subdivision_id)
	references eirc_subdivisions_tbl (id);

alter table eirc_subdivision_names_tbl
	add index FK_eirc_subdivision_names_tbl_language_id (language_id),
	add constraint FK_eirc_subdivision_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table eirc_subdivisions_tbl
	add index FK_eirc_subdivisions_tbl_parent_subdivision_id (parent_subdivision_id),
	add constraint FK_eirc_subdivisions_tbl_parent_subdivision_id
	foreign key (parent_subdivision_id)
	references eirc_subdivisions_tbl (id);

alter table eirc_subdivisions_tbl
	add index FK_eirc_subdivisions_tbl_head_organisation_id (head_organisation_id),
	add constraint FK_eirc_subdivisions_tbl_head_organisation_id
	foreign key (head_organisation_id)
	references eirc_organisations_tbl (id);

alter table eirc_subdivisions_tbl
	add index FK_eirc_subdivisions_tbl_juridical_person_id (juridical_person_id),
	add constraint FK_eirc_subdivisions_tbl_juridical_person_id
	foreign key (juridical_person_id)
	references eirc_organisations_tbl (id);

alter table eirc_subdivisions_tbl
	add index FK6E7B404F7F30FD59 (organisation_id),
	add constraint FK6E7B404F7F30FD59
	foreign key (organisation_id)
	references eirc_organisations_tbl (id);

update common_version_tbl set last_modified_date='2008-07-21', date_version=0;
