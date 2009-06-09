alter table orgs_payment_points_tbl
         modify column email varchar(255) null comment 'E-mail';

update common_version_tbl set last_modified_date='2009-06-09', date_version=0;
