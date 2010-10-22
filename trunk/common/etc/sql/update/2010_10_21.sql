alter table common_certificates_tbl
		add column user_preference_id bigint comment 'User preference reference',
		drop column alias;

alter table common_certificates_tbl
	add index FK_common_certificates_tbl_user_preference_id (user_preference_id),
	add constraint FK_common_certificates_tbl_user_preference_id
	foreign key (user_preference_id)
	references common_users_tbl (id);

update common_version_tbl set last_modified_date='2010-10-21', date_version=0;
