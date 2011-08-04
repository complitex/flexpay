alter table orgs_payment_collectors_tbl add column tradingDayEndTime time comment 'End trading day time';

update common_version_tbl set last_modified_date='2011-06-27', date_version=0;