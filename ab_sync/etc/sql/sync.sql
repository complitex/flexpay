drop table if exists ab_sync_config_tbl;
drop table if exists ab_sync_changes_tbl;

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

ALTER TABLE ab_sync_changes_tbl ADD INDEX Index_weight_id_date (order_weight, record_id, record_date);
