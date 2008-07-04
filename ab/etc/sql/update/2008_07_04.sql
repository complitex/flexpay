SELECT @ru_id:=id from languages_tbl where lang_iso_code='ru';
SELECT @en_id:=id from languages_tbl where lang_iso_code='en';

update identity_types_tbl set type_enum=3 where type_enum=2;
update identity_types_tbl set type_enum=2 where type_enum=1;

INSERT INTO identity_types_tbl (status, type_enum) VALUES (0, 1);
SELECT @identity_type_fio_id:=last_insert_id();
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('Фамилия Имя Отчество', @ru_id, @identity_type_fio_id);
INSERT INTO identity_type_translations_tbl (name, language_id, identity_type_id)
	VALUES ('FIO', @en_id, @identity_type_fio_id);

ALTER TABLE languages_tbl RENAME TO common_languages_tbl;
ALTER TABLE language_names_tbl RENAME TO common_language_names_tbl;

ALTER TABLE identity_types_tbl RENAME TO ab_identity_types_tbl;
ALTER TABLE identity_type_translations_tbl RENAME TO ab_identity_type_translations_tbl;
ALTER TABLE persons_tbl RENAME TO ab_persons_tbl;
ALTER TABLE person_attributes_tbl RENAME TO ab_person_attributes_tbl;
ALTER TABLE person_identities_tbl RENAME TO ab_person_identities_tbl;
ALTER TABLE person_identity_attributes_tbl RENAME TO ab_person_identity_attributes_tbl;

alter table ab_person_identities_tbl
	add column status integer not null;

update common_version_tbl set last_modified_date='2008-07-04', date_version=0;
