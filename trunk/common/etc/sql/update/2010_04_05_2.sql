create table common_certificates_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	alias varchar(255) not null comment 'Alias',
	description varchar(255) not null comment 'Description',
	primary key (id)
) comment='Security certificate';

update common_version_tbl set last_modified_date='2010-04-05', date_version=1;