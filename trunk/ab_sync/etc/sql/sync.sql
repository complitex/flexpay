-- Create and initialize sync tables
create table ab_sync_config_tbl (
	last_update datetime not null,
	last_record_update datetime
);

insert into ab_sync_config_tbl (last_update, last_record_update) values ('1900-01-01', '2100-12-31');