alter table eirc_subdivisions_tbl
	drop foreign key FK6E7B404F7F30FD59;

alter table eirc_subdivisions_tbl
	drop column organisation_id,
	add column tree_path varchar(255) not null comment 'Subdivisions tree branch path';

create index INDX_tree_path on eirc_subdivisions_tbl (tree_path);

alter table eirc_organisations_tbl
	drop column real_address;

update common_version_tbl set last_modified_date='2008-07-24', date_version=0;


-- Insert test data
select @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
select @en_id:=id from common_languages_tbl where lang_iso_code='en';

select @organisation_cn:=o.id
from eirc_organisations_tbl o
	inner join eirc_organisation_names_tbl n on o.id=n.organisation_id
where n.name='ЦН';
select @organisation_eirc:=o.id
from eirc_organisations_tbl o
	inner join eirc_organisation_names_tbl n on o.id=n.organisation_id
where n.name='EIRC';


-- Init subdivisions
INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, '3-я серверная стойка', null, @organisation_eirc, @organisation_eirc);
SELECT @subdivision_eirc_it:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('АйТи', @ru_id, @subdivision_eirc_it);
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('IT', @en_id, @subdivision_eirc_it);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Отдел информационных технологий', @ru_id, @subdivision_eirc_it);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Informational technoligies department', @en_id, @subdivision_eirc_it);

INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, '1-я серверная стойка', @subdivision_eirc_it, @organisation_eirc, null);
SELECT @subdivision_eirc_it_java:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Java', @ru_id, @subdivision_eirc_it_java);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Жабный сектор', @ru_id, @subdivision_eirc_it_java);

INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, '2-я серверная стойка', @subdivision_eirc_it, @organisation_eirc, null);
SELECT @subdivision_eirc_it_web:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Web', @ru_id, @subdivision_eirc_it_web);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Вэббизнес', @ru_id, @subdivision_eirc_it_web);

INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, 'Кабинет направо', null, @organisation_eirc, @organisation_eirc);
SELECT @subdivision_eirc_buch:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Бухгалтерия', @ru_id, @subdivision_eirc_buch);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Бухгалтерский отдел', @ru_id, @subdivision_eirc_buch);

INSERT INTO eirc_subdivisions_tbl (status, real_address, parent_subdivision_id, head_organisation_id, juridical_person_id)
	VALUES (0, 'Центр клининг-услуг', null, @organisation_eirc, @organisation_cn);
SELECT @subdivision_eirc_cleaning:=last_insert_id();
INSERT INTO eirc_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Уборщики', @ru_id, @subdivision_eirc_cleaning);
INSERT INTO eirc_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('сектор Очистки помещений', @ru_id, @subdivision_eirc_cleaning);
