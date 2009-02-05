alter table tc_tariff_calc_rules_files_tbl drop foreign key FKDA48352F62EB9A29;
alter table tc_tariff_calc_rules_files_tbl drop column status_id;

delete from common_file_statuses_tbl where module_id in (select (id) from common_flexpay_modules_tbl where name = 'tc');

update common_file_types_tbl set file_mask='*.{8}\\u002E(d|D)(r|R)(l|L)' where name = 'tc.file_type.tariff_rules';

update common_version_tbl set last_modified_date='2009-01-23', date_version=0;
