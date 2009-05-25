alter table common_currency_names_tbl
	add constraint FK_common_currency_names_tbl_currency_info_id
	foreign key (currency_info_id)
	references common_currency_infos_tbl (id);

alter table common_currency_names_tbl
	add constraint common_currency_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

update common_history_consumers_tbl set last_diff_id = null;
alter table common_history_consumers_tbl
	add constraint FK_common_history_consumers_tbl_last_diff_id
	foreign key (last_diff_id)
	references common_diffs_tbl (id);

alter table common_history_consumers_tbl
	add constraint FK_common_history_consumers_tbl_out_transport_config_id
	foreign key (out_transport_config_id)
	references common_out_transport_configs_tbl (id);

alter table common_history_consumption_groups_tbl
	add constraint FK_common_history_consumption_groups_tbl_consumer_id
	foreign key (consumer_id)
	references common_history_consumers_tbl (id);

alter table common_history_consumptions_tbl
	add constraint FK_common_history_consumptions_tbl_group_id
	foreign key (group_id)
	references common_history_consumption_groups_tbl (id);

alter table common_history_consumptions_tbl
	add constraint FK_common_history_consumptions_tbl_record_id
	foreign key (record_id)
	references common_history_records_tbl (id);

alter table common_history_records_tbl
	add constraint FK_common_history_records_tbl_diff_id
	foreign key (diff_id)
	references common_diffs_tbl (id);

update common_version_tbl set last_modified_date='2009-05-25', date_version=4;
