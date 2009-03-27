
alter table tc_tariff_calculation_result_tbl
    drop index FK_tc_tariff_calculation_result_tbl_tariff_export_code_id,
    drop foreign key FK_tc_tariff_calculation_result_tbl_tariff_export_code_id,
    drop column tariff_export_code_id;

create table tc_tariff_export_log_record_tbl (
    id bigint not null auto_increment,
    export_date datetime not null comment 'Export date',
    tariff_begin_date datetime not null comment 'Tariff Begin Date',
    building_id bigint not null comment 'Building reference',
    tariff_id bigint not null comment 'Tariff reference',
    tariff_export_code_id bigint comment 'Tariff export code',
    primary key (id)
);

alter table tc_tariff_export_log_record_tbl
    add index FK_tc_tariff_export_log_record_tbl_tariff_export_code_id (tariff_export_code_id),
    add constraint FK_tc_tariff_export_log_record_tbl_tariff_export_code_id
    foreign key (tariff_export_code_id)
    references tc_tariff_export_code_tbl (id);

alter table tc_tariff_export_log_record_tbl
    add index FK_tc_tariff_export_log_record_tbl_building_id (building_id),
    add constraint FK_tc_tariff_export_log_record_tbl_building_id
    foreign key (building_id)
    references ab_buildings_tbl (id);

alter table tc_tariff_export_log_record_tbl
    add index FK_tc_tariff_export_log_record_tbl_tariff_id (tariff_id),
    add constraint FK_tc_tariff_export_log_record_tbl_tariff_id
    foreign key (tariff_id)
    references tc_tariff_tbl (id);

alter table tc_tariff_calculation_result_tbl add column last_tariff_export_log_record_id bigint comment 'Last tariff export log record';

alter table tc_tariff_calculation_result_tbl
    add index FK_tc_tariff_calculation_result_tbl_tariff_export_log_record_id (last_tariff_export_log_record_id),
    add constraint FK_tc_tariff_calculation_result_tbl_tariff_export_log_record_id
    foreign key (last_tariff_export_log_record_id)
    references tc_tariff_export_log_record_tbl (id);

update common_version_tbl set last_modified_date='2009-03-20', date_version=0;
