alter table ab_street_names_temporal_tbl
	modify column street_id bigint not null comment 'Street reference',
	modify column street_name_id bigint comment 'Street name reference',
	comment='Street name temporals';

alter table ab_street_names_temporal_tbl
	drop index FKAEC123D6311847ED,
	drop foreign key FKAEC123D6311847ED,
	drop index FKAEC123D6D80067D4,
	drop foreign key FKAEC123D6D80067D4;


alter table ab_street_names_temporal_tbl
	add index ab_street_names_temporal_tbl_street_id (street_id),
	add constraint ab_street_names_temporal_tbl_street_id
	foreign key (street_id)
	references ab_streets_tbl (id);

alter table ab_street_names_temporal_tbl
	add index ab_street_names_temporal_tbl_street_name_id (street_name_id),
	add constraint ab_street_names_temporal_tbl_street_name_id
	foreign key (street_name_id)
	references ab_street_names_tbl (id);

create index I_external_account_number on eirc_consumers_tbl (external_account_number);

update common_version_tbl set last_modified_date='2008-08-11', date_version=0;
