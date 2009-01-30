
ALTER TABLE tc_tariff_tbl MODIFY COLUMN subservice_code varchar(255) NOT NULL COMMENT 'Subservice code';

update common_version_tbl set last_modified_date='2009-01-30', date_version=1;
