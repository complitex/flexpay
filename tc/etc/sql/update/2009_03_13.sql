create table tc_tariff_export_code_tbl (
    id bigint not null auto_increment,
    code integer not null unique comment 'Tariff export status code',
    primary key (id)
);

alter table tc_tariff_calculation_result_tbl
    add column tariff_export_code_id bigint comment 'Tariff export code';

alter table tc_tariff_calculation_result_tbl
    add index FK_tc_tariff_calculation_result_tbl_tariff_export_code_id (tariff_export_code_id),
    add constraint FK_tc_tariff_calculation_result_tbl_tariff_export_code_id
    foreign key (tariff_export_code_id)
    references tc_tariff_export_code_tbl (id);

INSERT INTO tc_tariff_export_code_tbl (id, code) values
        (1,0),(2,-1),(3,-2),(4,-3),
        (5,-4),(6,-5),(7,-100);
