
alter table ab_apartments_tbl
  add column apartment_type varchar(255) not null comment 'Class hierarchy descriminator, all apartments should have the same value',
  comment='Apartments';

update ab_apartments_tbl set apartment_type='ab';

update common_version_tbl set last_modified_date='2009-05-20', date_version=0;
