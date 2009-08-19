create table payments_registry_delivery_history_tbl (
    id bigint not null auto_increment comment 'Primary key',
    version integer not null comment 'Optimistic lock version',
    registry_id bigint not null comment 'Registry reference',
    delivery_date datetime not null comment 'Delivery date',
    email varchar(255) not null comment 'E-mail',
    file_id bigint not null comment 'File reference',
    primary key (id)
) comment='Registry delivery history';

alter table payments_registry_delivery_history_tbl
    add index FK_payments_registry_delivery_history_tbl_file_id (file_id),
    add constraint FK_payments_registry_delivery_history_tbl_file_id
    foreign key (file_id)
    references common_files_tbl (id);

alter table payments_registry_delivery_history_tbl
    add index FK_payments_registry_delivery_history_tbl_registry_id (registry_id),
    add constraint FK_payments_registry_delivery_history_tbl_registry_id
    foreign key (registry_id)
    references common_registries_tbl (id);

update common_version_tbl set last_modified_date='2009-08-19', date_version=0;
