alter table payments_operations_tbl
        add column cashbox_id bigint comment 'Cashbox';

alter table payments_operations_tbl
add index FK_payments_cashboxes_tbl_cashbox_id (cashbox_id),
add constraint FK_payments_cashboxes_tbl_cashbox_id
foreign key (cashbox_id)
references payments_cashboxes_tbl (id);

update common_version_tbl set last_modified_date='2009-06-08', date_version=1;
