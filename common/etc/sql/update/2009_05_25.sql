alter table common_currency_infos_tbl ENGINE = InnoDB;
alter table common_currency_names_tbl ENGINE = InnoDB;
alter table common_diffs_tbl ENGINE = InnoDB;
alter table common_history_consumers_tbl ENGINE = InnoDB;
alter table common_history_consumption_groups_tbl ENGINE = InnoDB;
alter table common_history_consumptions_tbl ENGINE = InnoDB;
alter table common_history_records_tbl ENGINE = InnoDB;


update common_version_tbl set last_modified_date='2009-05-25', date_version=0;
