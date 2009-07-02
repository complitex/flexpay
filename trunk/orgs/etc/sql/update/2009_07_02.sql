alter table orgs_payment_points_tbl drop column email;

update common_version_tbl set last_modified_date='2009-07-02', date_version=0;