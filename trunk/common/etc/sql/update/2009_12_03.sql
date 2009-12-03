alter table common_external_history_packs_tbl
		add column unpuck_tries integer not null comment 'Number of attempts to unpack the pack';

update common_version_tbl set last_modified_date='2009-12-03', date_version=0;
