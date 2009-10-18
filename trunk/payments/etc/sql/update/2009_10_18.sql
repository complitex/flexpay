create table config_payments_mbservices_tbl (
	id bigint not null auto_increment comment 'Primary key',
	version integer not null comment 'Optimistic lock version',
	mb_service_code varchar(255) not null unique comment 'MegaBank service code',
	mb_service_name varchar(255) not null unique comment 'MegaBank service name',
	service_type_id bigint not null unique comment 'Internal service type reference',
	primary key (id)
) comment='Mapping of MegaBank services to internal types';

alter table config_payments_mbservices_tbl
	add index FK_config_payments_mbservices_tbl_type_id (service_type_id),
	add constraint FK_config_payments_mbservices_tbl_type_id
	foreign key (service_type_id)
	references payments_service_types_tbl (id);

-- !!!!!!!!! TODO: add initialization data manually, see init_db_test.sql for example

update common_version_tbl set last_modified_date='2009-10-18', date_version=0;
