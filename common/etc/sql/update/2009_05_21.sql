alter table common_import_errors_tbl  
  modify column error_key VARCHAR(4000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL;

update common_version_tbl set last_modified_date='2009-05-21', date_version=0;