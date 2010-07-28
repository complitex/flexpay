alter table payments_documents_tbl
      change column summ sum decimal(19,2) not null comment 'Sum',
      change column debt debt decimal(19,2) comment 'Debt sum',
      change column town town_name varchar(255) comment 'Prividers consumer town name',
      add column town_type varchar(255) comment 'Prividers consumer town type name';

alter table payments_operations_tbl
      change column operation_summ operation_sum decimal(19,2) comment 'Operation sum',
      change column operation_input_summ operation_input_sum decimal(19,2) comment 'Operation input sum',
      change column change_summ change_sum decimal(19,2) comment 'Change';

update common_version_tbl set last_modified_date='2010-07-28', date_version=0;
