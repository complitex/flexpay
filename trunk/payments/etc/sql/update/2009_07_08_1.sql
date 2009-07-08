insert into payments_operation_statuses_tbl (id, version, code) values (6, 0, 6);
insert into payments_operation_status_translations_tbl(name, language_id, status_id) values ('Бланк операции', 1, 6);
insert into payments_operation_status_translations_tbl(name, language_id, status_id) values ('Blank', 2, 6);

update common_version_tbl set last_modified_date='2009-07-08', date_version=1;