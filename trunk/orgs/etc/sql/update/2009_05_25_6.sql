alter table orgs_payment_point_names_tbl
	add constraint FK_orgs_payment_point_names_tbl_point
	foreign key (payment_point_id)
	references orgs_payment_points_tbl (id);

alter table orgs_payment_point_names_tbl
	add constraint FK_orgs_payment_point_names_tbl_name_language
	foreign key (language_id)
	references common_languages_tbl (id);

update common_version_tbl set last_modified_date='2009-05-25', date_version=6;
