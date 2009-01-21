INSERT INTO common_flexpay_modules_tbl (name) VALUES ('eirc');
SELECT @module_eirc:=last_insert_id();

update common_version_tbl set last_modified_date='2008-12-26', date_version=2;
