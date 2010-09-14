alter table orgs_cashboxes_tbl add column tradingDayProcessInstance_Id bigint comment 'Trading date process instance id';
alter table orgs_payment_collectors_tbl add column tradingDayProcessInstance_Id bigint comment 'Trading date process instance id';

update common_version_tbl set last_modified_date='2010-09-13', date_version=0;
