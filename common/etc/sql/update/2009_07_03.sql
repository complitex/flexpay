create table common_report_print_history_record_tbl (
	id bigint not null auto_increment,
	version integer not null,
	user_name varchar(255) not null comment 'Name of user who printed report',
	print_date datetime not null comment 'Printing date',
	report_type integer not null comment 'Report type',
	primary key (id)
);

update common_version_tbl set last_modified_date='2009-07-03', date_version=0;