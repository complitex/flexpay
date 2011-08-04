alter table orgs_payment_collectors_tbl add column tradingDayBeginTime time comment 'Begin trading day time';

update common_version_tbl set last_modified_date='2011-07-04', date_version=0;