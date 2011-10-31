
create table payments_operation_action_logs_tbl (
    id bigint not null auto_increment comment 'Primary key',
    version integer not null comment 'Optimistic lock version',
    user_name varchar(255) not null comment 'User name',
    action_time datetime not null comment 'Action time',
    action integer not null comment 'Action (1 - search by address, 2 - search by quittance number, 3 - search by EIRC account, 4 - print quittance)',
    action_string varchar(300) comment 'Action string',
    cashbox_id bigint not null comment 'Cashbox reference',
    primary key (id)
) comment='Operation action logs table';

alter table payments_operation_action_logs_tbl
    add index FK_payments_operation_action_logs_tbl_cashbox_id (cashbox_id),
    add constraint FK_payments_operation_action_logs_tbl_cashbox_id
    foreign key (cashbox_id)
    references orgs_cashboxes_tbl (id);


update common_version_tbl set last_modified_date='2011-10-30', date_version=0;
