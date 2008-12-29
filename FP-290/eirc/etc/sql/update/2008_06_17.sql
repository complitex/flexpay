create table eirc_quittance_details_tbl (
	id bigint not null auto_increment,
	subservice_id bigint comment 'Subservice reference',
	consumer_id bigint not null comment 'Consumer reference',
	registry_record_id bigint not null comment 'Source registry record reference',
	incoming_balance decimal(19,2) comment 'incoming balance',
	outgoing_balance decimal(19,2) comment 'Outgoing balance',
	amount decimal(19,5) comment 'Amount',
	expence decimal(19,5) comment 'Expence',
	rate varchar(10) comment 'Rate',
	recalculation decimal(19,5) comment 'Recalculation',
	benefit decimal(19,5) comment 'Benefits amount',
	subsidy decimal(19,5) comment 'Subsidy amount',
	payment decimal(19,5) comment 'Payments amount',
	month datetime not null comment 'Quittance month',
	primary key (id)
) comment='Service provider quittance details';

alter table eirc_quittance_details_tbl
	add index FK_eirc_quittance_details_tbl_registry_record_id (registry_record_id),
	add constraint FK_eirc_quittance_details_tbl_registry_record_id
	foreign key (registry_record_id)
	references eirc_registry_records_tbl (id);

alter table eirc_quittance_details_tbl
	add index FK_eirc_quittance_details_tbl_subservice_id (subservice_id),
	add constraint FK_eirc_quittance_details_tbl_subservice_id
	foreign key (subservice_id)
	references eirc_services_tbl (id);

alter table eirc_quittance_details_tbl
	add index FK_eirc_quittance_details_tbl_consumer_id (consumer_id),
	add constraint FK_eirc_quittance_details_tbl_consumer_id
	foreign key (consumer_id)
	references eirc_consumers_tbl (id);

