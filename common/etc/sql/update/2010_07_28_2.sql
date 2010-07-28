alter table common_registry_records_tbl
      change column city town_name VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
      add column town_type varchar(255);

update common_version_tbl set last_modified_date='2010-07-28', date_version=2;
