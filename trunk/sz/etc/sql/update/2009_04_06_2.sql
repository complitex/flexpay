
alter table sz_subsidy_records_tbl
  change column appartment apartment varchar(255),
  change column tarif tariff double precision;

alter table sz_service_type_records_tbl
  change column tarifCode tariffCode integer;

update common_version_tbl set last_modified_date='2009-04-06', date_version=2;
