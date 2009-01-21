
    create table tc_tariff_calculation_result_tbl (
        id bigint not null auto_increment comment 'Primary key identifier',
        value decimal(19,4) not null comment 'Result value',
        creation_date datetime not null comment 'Calculation result creation date',
        calculation_date datetime not null comment 'Calculation result calculation date',
        building_id bigint not null comment 'Building reference',
        tariff_id bigint not null comment 'Tariff reference',
        primary key (id)
    ) comment='Table contains tariff calculation results information';

    create table tc_tariff_tbl (
        id bigint not null auto_increment comment 'Primary key',
        status integer not null comment 'Enabled/Disabled status',
        subservice_code bigint not null unique comment 'Subservice code',
        primary key (id)
    ) comment='Table, where store information about tariffs';
    
    create table tc_tariff_translations_tbl (
        id bigint not null auto_increment comment 'Primary key',
        name varchar(255) not null comment 'Tariff name translation',
        tariff_id bigint not null comment 'Tariff reference',
        language_id bigint not null comment 'Language reference',
        primary key (id),
        unique (tariff_id, language_id)
    ) comment='Tariff translations';


    alter table tc_tariff_calculation_result_tbl
        add index FK_tc_tariff_calculation_result_tbl_building_id (building_id),
        add constraint FK_tc_tariff_calculation_result_tbl_building_id
        foreign key (building_id)
        references ab_buildings_tbl (id);

    alter table tc_tariff_calculation_result_tbl
        add index FK_tc_tariff_calculation_result_tbl_tariff_id (tariff_id),
        add constraint FK_tc_tariff_calculation_result_tbl_tariff_id
        foreign key (tariff_id)
        references tc_tariff_tbl (id);

    alter table tc_tariff_translations_tbl
        add index tc_tariff_translations_tbl_tariff_id (tariff_id),
        add constraint tc_tariff_translations_tbl_tariff_id
        foreign key (tariff_id)
        references tc_tariff_tbl (id);

    alter table tc_tariff_translations_tbl
        add index lang_tariff_pair_language_id (language_id),
        add constraint lang_tariff_pair_language_id
        foreign key (language_id)
        references common_languages_tbl (id);

update common_version_tbl set last_modified_date='2009-01-21', date_version=1;
