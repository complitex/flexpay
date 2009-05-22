alter table payments_operations_tbl
		add column payment_point_id bigint not null comment 'Payment point operation created in';

update payments_operations_tbl set payment_point_id=1;

alter table payments_operations_tbl
	add index FK_payments_operations_tbl_payment_point_id (payment_point_id),
	add constraint FK_payments_operations_tbl_payment_point_id
	foreign key (payment_point_id)
	references orgs_payment_points_tbl (id);

update common_version_tbl set last_modified_date='2009-05-22', date_version=0;
