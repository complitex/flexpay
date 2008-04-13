-- Create and initialize sync tables
create table ab_sync_config_tbl (
	last_update datetime not null,
	last_record_id BIGINT NOT NULL
);

insert into ab_sync_config_tbl (last_update, last_record_id) values ('1900-01-01', -1);

create table ab_sync_changes_tbl (
	record_id BIGINT NOT NULL,
	record_date DATETIME NOT NULL,
	old_value VARCHAR(2000),
	current_value VARCHAR(2000),
	object_type INTEGER NOT NULL,
	object_id BIGINT NOT NULL,
	field INTEGER,
	action_type INTEGER NOT NULL,
	processed INTEGER NOT NULL DEFAULT 0,
	order_weight INTEGER NOT NULL DEFAULT 0
);

-- Set order weigths (street types before streets)
update ab_sync_changes_tbl set order_weight=0 where object_type=0;
update ab_sync_changes_tbl set order_weight=1 where object_type=1;
update ab_sync_changes_tbl set order_weight=3 where object_type=2;
update ab_sync_changes_tbl set order_weight=4 where object_type=3;
update ab_sync_changes_tbl set order_weight=2 where object_type=4;
update ab_sync_changes_tbl set order_weight=5 where object_type=5;
