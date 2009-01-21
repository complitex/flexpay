create table common_master_index_bounds_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	object_type integer not null comment 'Type of objects index is used for',
	lower_bound bigint not null comment 'Lower index bound',
	upper_bound bigint not null comment 'Upper index bound',
	primary key (id)
) comment='Master index bounds got from external source';

create table common_master_index_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	object_type integer not null comment 'Type of objects index is used for',
	index_value bigint not null comment 'Index value',
	primary key (id)
) comment='Master index, unique value among integrated systems';

update common_version_tbl set last_modified_date='2009-01-21', date_version=0;
