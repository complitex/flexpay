
alter table tc_tariff_calculation_result_tbl drop column tariff_export_code_id;
alter table tc_tariff_calculation_result_tbl add column last_tariff_export_log_record_id bigint comment 'Last tariff export log record';

create table tc_tariff_export_log_record_tbl (
    id bigint not null auto_increment,
    export_date datetime not null comment 'Export date',
    tariff_begin_date datetime not null comment 'Tariff Begin Date',
    building_id bigint not null comment 'Building reference',
    tariff_id bigint not null comment 'Tariff reference',
    tariff_export_code_id bigint comment 'Tariff export code',
    primary key (id)
);


update common_version_tbl set last_modified_date='2009-03-20', date_version=0;