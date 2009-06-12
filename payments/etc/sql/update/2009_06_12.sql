alter table payments_services_tbl
	modify column id bigint not null auto_increment comment 'Primary key',
	add column version integer not null comment 'Optimistic lock version',
	add column status integer not null comment 'Enabled-disabled status',
	comment='Services';


update common_version_tbl set last_modified_date='2009-06-12', date_version=0;
