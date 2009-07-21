alter table payments_document_addition_types_tbl add column code integer comment 'Document addition type code';

select @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
select @en_id:=id from common_languages_tbl where lang_iso_code='en';

insert into payments_document_addition_types_tbl (id, version, code)
		values (1, 0, 1);
insert into payments_document_addition_type_translations_tbl (name, language_id, type_id)
		values ('ЕРЦ счёт', @ru_id, 1);
insert into payments_document_addition_type_translations_tbl (name, language_id, type_id)
		values ('ERC account', @en_id, 1);

update common_version_tbl set last_modified_date='2009-07-13', date_version=0;