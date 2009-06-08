alter table orgs_payment_points_tbl
        add column email varchar(255) not null comment 'E-mail';

update common_version_tbl set last_modified_date='2009-06-08', date_version=0;
