alter table orgs_payment_points_tbl
        add column tradingDayProcessInstance_Id bigint comment 'Trading date process instance id';

update common_version_tbl set last_modified_date='2009-06-11', date_version=0;