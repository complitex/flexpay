alter table common_history_consumers_tbl
		add column last_diff_id bigint comment 'Last packed diff reference';

create table common_history_consumption_groups_tbl (
	id bigint not null auto_increment,
	consumer_id bigint not null comment 'History consumer reference',
	creation_date datetime not null comment 'Group creation date',
	user_name varchar(255) not null comment 'User name created group',
	primary key (id)
) comment='Group of several consumptions';

alter table common_history_consumptions_tbl
	drop index FK_common_history_consumptions_tbl_consumer_id,
	drop foreign key FK_common_history_consumptions_tbl_consumer_id;

alter table common_history_consumptions_tbl
		drop column consumer_id,
		add column group_id bigint not null comment 'History consumption group reference';

alter table common_history_consumers_tbl
	add index FK_common_history_consumers_tbl_last_diff_id (last_diff_id),
	add constraint FK_common_history_consumers_tbl_last_diff_id
	foreign key (last_diff_id)
	references common_diffs_tbl (id);

alter table common_history_consumption_groups_tbl
	add index FK_common_history_consumption_groups_tbl_consumer_id (consumer_id),
	add constraint FK_common_history_consumption_groups_tbl_consumer_id
	foreign key (consumer_id)
	references common_history_consumers_tbl (id);

alter table common_history_consumptions_tbl
	add index FK_common_history_consumptions_tbl_group_id (group_id),
	add constraint FK_common_history_consumptions_tbl_group_id
	foreign key (group_id)
	references common_history_consumption_groups_tbl (id);

alter table common_history_records_tbl
        drop index FK_common_history_records_tbl_language_id,
        drop foreign key FK_common_history_records_tbl_language_id;

alter table common_history_records_tbl
	drop column language_id,
	add column language_code varchar(255) comment 'Optional language iso code for multilang fields';

update common_version_tbl set last_modified_date='2009-02-25', date_version=0;
