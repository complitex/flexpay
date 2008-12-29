create table eirc_quittance_details_payments_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	payment_id bigint not null comment 'Quittances payment reference',
	payment_status_id bigint not null comment 'Payment status reference',
	details_id bigint not null comment 'Quittance details reference',
	amount decimal(19,2) not null comment 'Amount payed for quittance',
	quittance_payment_id bigint not null,
	primary key (id)
) comment='Quittance details payments';

create table eirc_quittance_packets_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	status integer not null comment 'Enabled-disabled status',
	packet_number bigint not null comment 'Packet number',
	creation_date datetime not null comment 'Creation date',
	begin_date datetime comment 'First quittance added date',
	close_date datetime comment 'Packet close date',
	payment_id bigint not null comment 'Payment point reference',
	control_quittances_number integer not null comment 'Control quittances number',
	control_overall_summ decimal(19,2) not null comment 'Control overall summ',
	quittances_number integer not null comment 'Inputed quittances number',
	overall_summ decimal(19,2) not null comment 'Inputed overall summ',
	creator_user_name varchar(255) not null comment 'User name that created packet',
	closer_user_name varchar(255) not null comment 'User name that closed packet',
	primary key (id)
) comment='Quittance payment packets';

create table eirc_quittance_payment_statuses_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	code integer not null comment 'System known code',
	i18n_name varchar(255) not null comment 'Translation code',
	primary key (id)
) comment='Statuses of quittance payments';

create table eirc_quittance_payments_tbl (
	id bigint not null auto_increment,
	version integer not null comment 'Optimistic lock version',
	packet_id bigint not null comment 'Quittances packet reference',
	payment_status_id bigint not null comment 'Payment status reference',
	quittance_id bigint not null comment 'Quittance reference',
	amount decimal(19,2) not null comment 'Amount payed for quittance',
	primary key (id)
) comment='Quittance payments';

alter table eirc_quittance_details_payments_tbl
	add index FK_eirc_quittance_details_payments_tbl_payment_status_id (payment_status_id),
	add constraint FK_eirc_quittance_details_payments_tbl_payment_status_id
	foreign key (payment_status_id)
	references eirc_quittance_payment_statuses_tbl (id);

alter table eirc_quittance_details_payments_tbl
	add index FK_eirc_quittance_details_payments_tbl_payment_id (payment_id),
	add constraint FK_eirc_quittance_details_payments_tbl_payment_id
	foreign key (payment_id)
	references eirc_quittance_payments_tbl (id);

alter table eirc_quittance_details_payments_tbl
	add index FK3B002EBEF2132330 (quittance_payment_id),
	add constraint FK3B002EBEF2132330
	foreign key (quittance_payment_id)
	references eirc_quittance_payments_tbl (id);

alter table eirc_quittance_details_payments_tbl
	add index FK_eirc_quittance_details_payments_tbl_details_id (details_id),
	add constraint FK_eirc_quittance_details_payments_tbl_details_id
	foreign key (details_id)
	references eirc_quittance_details_tbl (id);

alter table eirc_quittance_packets_tbl
	add index eirc_quittance_packets_tbl_payment_id (payment_id),
	add constraint eirc_quittance_packets_tbl_payment_id
	foreign key (payment_id)
	references eirc_payment_points_tbl (id);

alter table eirc_quittance_payments_tbl
	add index FK_eirc_quittance_payments_tbl_payment_status_id (payment_status_id),
	add constraint FK_eirc_quittance_payments_tbl_payment_status_id
	foreign key (payment_status_id)
	references eirc_quittance_payment_statuses_tbl (id);

alter table eirc_quittance_payments_tbl
	add index FK_eirc_quittance_payments_tbl_quittance_id (quittance_id),
	add constraint FK_eirc_quittance_payments_tbl_quittance_id
	foreign key (quittance_id)
	references eirc_quittances_tbl (id);

alter table eirc_quittance_payments_tbl
	add index FK_eirc_quittance_payments_tbl_packet_id (packet_id),
	add constraint FK_eirc_quittance_payments_tbl_packet_id
	foreign key (packet_id)
	references eirc_quittance_packets_tbl (id);

insert into eirc_quittance_payment_statuses_tbl (id, code, i18n_name)
	values (1, 1, 'eirc.quittance.payment.status.full');
select @payment_status_full:=1;
insert into eirc_quittance_payment_statuses_tbl (id, code, i18n_name)
	values (2, 2, 'eirc.quittance.payment.status.partially');
select @payment_status_full:=2;


update common_version_tbl set last_modified_date='2008-12-29', date_version=0;
