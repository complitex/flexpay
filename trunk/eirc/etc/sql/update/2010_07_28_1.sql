alter table eirc_consumer_infos_tbl
      change column city_name town_name VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL comment 'Prividers consumer town name',
      add column town_type varchar(255) comment 'Prividers consumer town type name';

alter table eirc_quittance_packets_tbl
      change column control_overall_summ control_overall_sum decimal(19,2) not null comment 'Control overall sum',
      change column overall_summ overall_sum decimal(19,2) not null comment 'Control overall sum';

update common_version_tbl set last_modified_date='2010-07-28', date_version=1;
