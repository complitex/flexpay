alter table eirc_quittance_packets_tbl
	drop index eirc_quittance_packets_tbl_payment_id,
	drop foreign key eirc_quittance_packets_tbl_payment_id;

alter table eirc_quittance_packets_tbl
	change column payment_id payment_point_id bigint not null comment 'Payment point reference';

alter table eirc_quittance_packets_tbl
	add index eirc_quittance_packets_tbl_payment_id (payment_point_id),
	add constraint eirc_quittance_packets_tbl_payment_id
	foreign key (payment_point_id)
	references eirc_payment_points_tbl (id);

update common_version_tbl set last_modified_date='2009-01-12', date_version=0;
