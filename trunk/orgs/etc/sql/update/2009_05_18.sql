create table orgs_payment_point_names_tbl (
	id bigint not null auto_increment,
	name varchar(255) not null comment 'Name value',
	language_id bigint not null comment 'Language reference',
	payment_point_id bigint not null comment 'Payment point reference',
	primary key (id),
	unique (language_id, payment_point_id)
);

alter table orgs_payment_point_names_tbl
	add index FK_orgs_payment_point_names_tbl_point (payment_point_id),
	add constraint FK_orgs_payment_point_names_tbl_point
	foreign key (payment_point_id)
	references orgs_payment_points_tbl (id);

alter table orgs_payment_point_names_tbl
	add index FK_orgs_payment_point_names_tbl_name_language (language_id),
	add constraint FK_orgs_payment_point_names_tbl_name_language
	foreign key (language_id)
	references common_languages_tbl (id);


update common_version_tbl set last_modified_date='2009-05-18', date_version=0;
