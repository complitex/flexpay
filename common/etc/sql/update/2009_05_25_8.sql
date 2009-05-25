ALTER TABLE common_import_errors_tbl MODIFY COLUMN error_key VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;

update common_version_tbl set last_modified_date='2009-05-25', date_version=8;
