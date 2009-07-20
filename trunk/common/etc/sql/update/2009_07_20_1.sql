create table common_users_tbl (
	id bigint not null auto_increment comment 'Primary key',
	discriminator varchar(255) not null comment 'Class hierarchy discriminator',
	full_name varchar(255) not null comment 'Full user name',
	last_name varchar(255) not null comment 'Last user name',
	user_name varchar(255) not null unique comment 'User login name',
	language_code varchar(255) not null comment 'Preferred language ISO code',
	page_size integer comment 'Preferred listing page size',
	primary key (id)
) comment='User details';


update common_version_tbl set last_modified_date='2009-07-20', date_version=1;
