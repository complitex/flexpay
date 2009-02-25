create table common_diffs_tbl (
	id bigint not null auto_increment,
	operation_time datetime not null comment 'Operation timestamp',
	object_type integer not null comment 'Object type',
	object_id bigint not null comment 'Internal object reference',
	operation_type integer not null comment 'Operation type (create-update-delete)',
	user_name varchar(255) not null comment 'User name performed operation',
	processing_status integer default 0 not null comment 'Processing status',
	master_index varchar(255) not null comment 'Unique among several installations object id',
	primary key (id)
) comment='Set of history records for single object';

create table common_history_consumers_tbl (
	id bigint not null auto_increment,
	active integer not null comment 'Enabled-disabled status',
	name varchar(255) comment 'Consumer name',
	description varchar(255) comment 'Optional consumer description',
	primary key (id)
) comment='Some abstract history records consumer';

create table common_history_consumptions_tbl (
	id bigint not null auto_increment,
	record_id bigint not null comment 'History record reference',
	consumer_id bigint not null comment 'History consumer reference',
	primary key (id)
) comment='Consumption of single history record';

create table common_history_records_tbl (
	id bigint not null auto_increment,
	old_date_value datetime comment 'Optional old date value',
	new_date_value datetime comment 'Optional new date value',
	old_int_value integer comment 'Optional old int value',
	new_int_value integer comment 'Optional new int value',
	old_long_value bigint comment 'Optional old long value',
	new_long_value bigint comment 'Optional new long value',
	old_string_value varchar(255) comment 'Optional old string value',
	new_string_value varchar(255) comment 'Optional new string value',
	old_double_value double precision comment 'Optional old double value',
	new_double_value double precision comment 'Optional new double value',
	field_type integer not null comment 'Field type value is modified for',
	operation_order integer not null comment 'Object modification operation order',
	language_id bigint comment 'Optional language reference for multilang fields',
	begin_date date comment 'Optional begin date for temporal fields',
	end_date date comment 'Optional end date for temporal fields',
	field_key varchar(255) comment 'Optional key for field value',
	processing_status integer default 0 not null comment 'Processing status',
	diff_id bigint not null comment 'Diff (set of records) reference',
	primary key (id)
) comment='Single field update record';

alter table common_history_consumptions_tbl
	add index FK_common_history_consumptions_tbl_consumer_id (consumer_id),
	add constraint FK_common_history_consumptions_tbl_consumer_id
	foreign key (consumer_id)
	references common_history_consumers_tbl (id);

alter table common_history_consumptions_tbl
	add index FK_common_history_consumptions_tbl_record_id (record_id),
	add constraint FK_common_history_consumptions_tbl_record_id
	foreign key (record_id)
	references common_history_records_tbl (id);

alter table common_history_records_tbl
	add index FK_common_history_records_tbl_language_id (language_id),
	add constraint FK_common_history_records_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table common_history_records_tbl
	add index FK_common_history_records_tbl_diff_id (diff_id),
	add constraint FK_common_history_records_tbl_diff_id
	foreign key (diff_id)
	references common_diffs_tbl (id);


update common_version_tbl set last_modified_date='2009-02-25', date_version=0;
