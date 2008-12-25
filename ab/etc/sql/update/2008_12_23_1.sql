alter table ab_street_types_temporal_tbl
	drop index FK_street,
	drop foreign key FK_street;
alter table ab_street_types_temporal_tbl
	drop index FK_street_type,
	drop foreign key FK_stree_type;


alter table ab_street_types_temporal_tbl
	modify column street_id bigint not null comment 'Street reference';

alter table ab_street_types_temporal_tbl
	add index FK_ab_street_types_temporal_tbl_street_id (street_id),
	add constraint FK_ab_street_types_temporal_tbl_street_id
	foreign key (street_id)
	references ab_streets_tbl (id);

alter table ab_street_types_temporal_tbl
	add index FK_ab_street_types_temporal_tbl_street_type_id (street_type_id),
	add constraint FK_ab_street_types_temporal_tbl_street_type_id
	foreign key (street_type_id)
	references ab_street_types_tbl (id);

update common_version_tbl set last_modified_date='2008-12-23', date_version=1;
