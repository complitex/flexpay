alter table payments_document_types_tbl
	change column code code integer not null unique comment 'Type unique code';

update common_version_tbl set last_modified_date='2009-04-14', date_version=2;
