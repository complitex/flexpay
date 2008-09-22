alter table eirc_eirc_accounts_tbl
	modify column status integer not null comment 'Enabled-Disabled status',
	modify column person_id bigint comment 'Responsible person reference',
	add column consumer_info_id bigint comment 'Consumer info used to create account';

alter table eirc_eirc_accounts_tbl
	add index FK_eirc_eirc_accounts_consumer_info_id (consumer_info_id),
	add constraint FK_eirc_eirc_accounts_consumer_info_id
	foreign key (consumer_info_id)
	references eirc_consumer_infos_tbl (id);

alter table eirc_consumers_tbl
	modify column status integer not null comment 'Enabled-Disabled status',
	modify column person_id bigint comment 'Responsible person reference',
	comment='Consumer is a person that gets some service';

update common_version_tbl set last_modified_date='2008-09-22', date_version=0;
