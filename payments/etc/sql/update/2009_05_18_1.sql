
alter table payments_documents_tbl
	add column first_name varchar(255) comment 'Prividers consumer first name',
	add column middle_name varchar(255) comment 'Prividers consumer middle name',
	add column last_name varchar(255) comment 'Prividers consumer last name',
	add column country varchar(255) comment 'Prividers consumer country name',
	add column region varchar(255) comment 'Prividers consumer region name',
	add column town varchar(255) comment 'Prividers consumer town name',
	add column street_type varchar(255) comment 'Prividers consumer street type name',
	add column street_name varchar(255) comment 'Prividers consumer street name',
	add column building_number varchar(255) comment 'Prividers consumer building number',
	add column building_bulk varchar(255) comment 'Prividers consumer building bulk',
	add column apartment_number varchar(255) comment 'Prividers consumer apartment number';


update common_version_tbl set last_modified_date='2009-05-18', date_version=1;
