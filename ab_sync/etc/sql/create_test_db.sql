drop database if exists flexpay_sync_db;
create database flexpay_sync_db DEFAULT CHARACTER SET 'utf8' COLLATE 'utf8_general_ci';
use flexpay_sync_db;
grant all privileges on flexpay_sync_db.* to flexpay_user;
flush privileges;

create table vw_cn_changes (
	id BIGINT NOT NULL,
	record_date DATETIME NOT NULL,
	old_value VARCHAR(2000),
	current_value VARCHAR(2000),
	object_type INTEGER NOT NULL,
	object_id BIGINT NOT NULL,
	field INTEGER,
	action_type INTEGER NOT NULL
);

ALTER TABLE vw_cn_changes ADD INDEX Index_id (id);
