-- an alternative update for moving cashboxes to orgs module

alter table payments_operations_tbl
	drop foreign key FK_payments_cashboxes_tbl_cashbox_id;

alter table payments_cashbox_name_translations_tbl
	drop index FK_payments_cashbox_name_translation_language,
	drop foreign key FK_payments_cashbox_name_translation_language,
	drop index FK_payments_cashbox_name_translation_cashbox,
	drop foreign key FK_payments_cashbox_name_translation_cashbox;

rename table payments_cashbox_name_translations_tbl to orgs_cashbox_name_translations_tbl;

alter table payments_cashboxes_tbl
	drop index FK_payments_cashboxes_tbl_payment_point_id,
	drop foreign key FK_payments_cashboxes_tbl_payment_point_id;

rename table payments_cashboxes_tbl to orgs_cashboxes_tbl;

alter table orgs_cashbox_name_translations_tbl
	add index FK_orgs_cashbox_name_translation_cashbox (cashbox_id),
	add constraint FK_orgs_cashbox_name_translation_cashbox
	foreign key (cashbox_id)
	references orgs_cashboxes_tbl (id),
	add index FK_orgs_cashbox_name_translation_language (language_id),
	add constraint FK_orgs_cashbox_name_translation_language
	foreign key (language_id)
	references common_languages_tbl (id);

alter table orgs_cashboxes_tbl
	add index FK_orgs_cashboxes_tbl_payment_point_id (payment_point_id),
	add constraint FK_orgs_cashboxes_tbl_payment_point_id
	foreign key (payment_point_id)
	references orgs_payment_points_tbl (id);

alter table payments_operations_tbl
	add index FK_orgs_cashboxes_tbl_cashbox_id (cashbox_id),
	add constraint FK_orgs_cashboxes_tbl_cashbox_id
	foreign key (cashbox_id)
	references orgs_cashboxes_tbl (id);

update common_version_tbl set last_modified_date='2009-06-21', date_version=2;