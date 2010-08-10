
alter table orgs_payment_collectors_tbl
    add column tradingDayProcessInstance_Id bigint comment 'Trading date process instance id';

alter table orgs_payment_points_tbl
    drop column tradingDayProcessInstance_Id;

update common_version_tbl set last_modified_date='2010-08-03', date_version=0;
