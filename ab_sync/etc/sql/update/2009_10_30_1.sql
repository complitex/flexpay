alter table ab_sync_changes_tbl
		add column id bigint not null auto_increment,
		add primary key (id);
