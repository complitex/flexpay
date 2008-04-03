drop database if exists flexpay_sync_db;
create database flexpay_sync_db;
use flexpay_sync_db;
grant all privileges on flexpay_sync_db.* to flexpay_user;
flush privileges;

-- Create and initialize sync tables
create table ab_sync_config_tbl (
	last_update datetime not null,
	last_record_update datetime
);

insert into ab_sync_config_tbl (last_update, last_record_update) values ('1900-01-01', '1900-01-01');

create table vw_cn_changes {
	record_date DATETIME NOT NULL,
	old_value VARCHAR(2000),
	current_value VARCHAR(2000),
	object_type INTEGER NOT NULL,
	object_id BIGINT NOT NULL,
	field INTEGER,
	action_type INTEGER NOT NULL,
	processed INTEGER NOT NULL DEFAULT 0,
	order_weight INTEGER NOT NULL DEFAULT 0;
);

-- Set order weigths (street types before streets)
update vw_cn_changes set order_weight=0 where object_type=0;
update vw_cn_changes set order_weight=1 where object_type=1;
update vw_cn_changes set order_weight=3 where object_type=2;
update vw_cn_changes set order_weight=4 where object_type=3;
update vw_cn_changes set order_weight=2 where object_type=4;
update vw_cn_changes set order_weight=5 where object_type=5;
