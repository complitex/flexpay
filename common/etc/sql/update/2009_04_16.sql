alter table common_history_consumers_tbl
	add column out_transport_config_id bigint not null comment 'Out transport config reference';

create table common_out_transport_configs_tbl (
	id bigint not null auto_increment,
	config_type varchar(255) not null comment 'Class hierarchy descriminator',
	version integer not null comment 'Optimistic lock version',
	ws_url varchar(255) comment 'Web service url',
	primary key (id)
) comment='Out transport configs';

insert into common_out_transport_configs_tbl (id, config_type, version, ws_url)
	values (1, 'ws', 0, 'http://localhost:8080/eirc/ws/ShareHistory');
select @out_transport_1:=1;
update common_history_consumers_tbl set out_transport_config_id=@out_transport_1 where id=1;

alter table common_history_consumers_tbl
	add index FK_common_history_consumers_tbl_out_transport_config_id (out_transport_config_id),
	add constraint FK_common_history_consumers_tbl_out_transport_config_id
	foreign key (out_transport_config_id)
	references common_out_transport_configs_tbl (id);

update common_version_tbl set last_modified_date='2009-04-16', date_version=0;
