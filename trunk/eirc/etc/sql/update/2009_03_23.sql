alter table eirc_quittance_details_payments_tbl
	drop foreign key FK3B002EBEF2132330,
	drop index FK3B002EBEF2132330;

alter table eirc_quittance_details_payments_tbl
	drop column quittance_payment_id;

alter table eirc_quittance_payments_tbl
	modify column packet_id bigint comment 'Optional quittances packet reference';

update common_version_tbl set last_modified_date='2009-03-23', date_version=0;
