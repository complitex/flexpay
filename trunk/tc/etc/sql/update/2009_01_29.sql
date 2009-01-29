
ALTER TABLE `flexpay_db`.`tc_tariff_calculation_result_tbl`
MODIFY COLUMN `calculation_date` DATE NOT NULL COMMENT 'Calculation result calculation date';

update common_version_tbl set last_modified_date='2009-01-29', date_version=0;
