alter table ab_identity_type_translations_tbl
	drop index FK2195EF63D8765DAA,
	drop foreign key FK2195EF63D8765DAA;

alter table ab_identity_type_translations_tbl
	drop index FK2195EF6361F37403,
	drop foreign key FK2195EF6361F37403;

alter table ab_person_attributes_tbl
	drop index FK634A41627095AEAD,
	drop foreign key FK634A41627095AEAD;

alter table ab_person_attributes_tbl
	drop index FK634A416261F37403,
	drop foreign key FK634A416261F37403;

alter table ab_person_identities_tbl
	drop index FKA24DD767D8765DAA,
	drop foreign key FKA24DD767D8765DAA;

alter table ab_person_identities_tbl
	drop index FKA24DD7677095AEAD,
	drop foreign key FKA24DD7677095AEAD;

alter table ab_person_identity_attributes_tbl
	drop index FKA1B9EF6B1F110398,
	drop foreign key FKA1B9EF6B1F110398;

alter table ab_person_identity_attributes_tbl
	drop index FKA1B9EF6B61F37403,
	drop foreign key FKA1B9EF6B61F37403;


ALTER TABLE ab_identity_type_translations_tbl
	MODIFY COLUMN name VARCHAR(150) NOT NULL COMMENT 'Identity type translation',
	MODIFY COLUMN language_id BIGINT(20) NOT NULL COMMENT 'Language reference',
	MODIFY COLUMN identity_type_id BIGINT(20) NOT NULL COMMENT 'Identity type reference',
	COMMENT = 'Person documents type translation';

ALTER TABLE ab_identity_types_tbl
	MODIFY COLUMN status integer not null comment 'Enabled-disabled status',
	MODIFY COLUMN type_enum integer not null comment 'Identity type code',
	COMMENT = 'Person documents type';

ALTER TABLE ab_person_attributes_tbl DROP INDEX language_id;
ALTER TABLE ab_person_attributes_tbl
	MODIFY COLUMN name varchar(50) not null comment 'Attribute name',
	MODIFY COLUMN value varchar(255) comment 'Attribute value',
	MODIFY COLUMN language_id bigint not null comment 'Language reference',
	MODIFY COLUMN person_id bigint not null comment 'Person reference',
	comment='Person attributes';

ALTER TABLE ab_person_identities_tbl
	MODIFY COLUMN status integer not null comment 'Enabled-Disabled status',
	MODIFY COLUMN begin_date date not null comment 'Begin of document valid interval',
	MODIFY COLUMN end_date date not null comment 'End of document valid interval',
	MODIFY COLUMN birth_date datetime not null comment 'Person birth date',
	MODIFY COLUMN serial_number varchar(10) not null comment 'Document serial number',
	MODIFY COLUMN document_number varchar(20) not null comment 'Document number',
	MODIFY COLUMN first_name varchar(255) not null comment 'Person first name',
	MODIFY COLUMN middle_name varchar(255) not null comment 'Person middle name',
	MODIFY COLUMN last_name varchar(255) not null comment 'Person last name',
	MODIFY COLUMN organization varchar(4000) not null comment 'Organisation gave document',
	MODIFY COLUMN is_default bit not null comment 'Default document flag',
	MODIFY COLUMN identity_type_id bigint not null comment 'Identity document type reference',
	MODIFY COLUMN person_id bigint not null comment 'Person reference',
	comment='Person documents';

ALTER TABLE ab_person_identity_attributes_tbl DROP INDEX language_id;
ALTER TABLE ab_person_identity_attributes_tbl
	MODIFY COLUMN name varchar(50) not null comment 'Attribute name',
	MODIFY COLUMN value varchar(255) comment 'Attribute value',
	MODIFY COLUMN language_id bigint not null comment 'Language reference',
	MODIFY COLUMN person_identity_id bigint not null comment 'Person identity reference',
	comment='Person document additional attributes';

ALTER TABLE ab_person_registrations_tbl
    MODIFY COLUMN begin_date date not null comment 'Registration begin date',
    MODIFY COLUMN end_date date not null comment 'Registration end date',
	comment='Person registrations';

ALTER TABLE ab_persons_tbl
    MODIFY COLUMN status integer not null comment 'Enabled-Disabled status',
	comment='Natural persons';


alter table ab_identity_type_translations_tbl
	add index ab_identity_type_translations_tbl_identity_type_id (identity_type_id),
	add constraint ab_identity_type_translations_tbl_identity_type_id
	foreign key (identity_type_id)
	references ab_identity_types_tbl (id);

alter table ab_identity_type_translations_tbl
	add index ab_identity_type_translations_tbl_language_id (language_id),
	add constraint ab_identity_type_translations_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table ab_person_attributes_tbl
	add index ab_person_attributes_tbl_person_id (person_id),
	add constraint ab_person_attributes_tbl_person_id
	foreign key (person_id)
	references ab_persons_tbl (id);

alter table ab_person_attributes_tbl
	add index ab_person_attributes_tbl_language_id (language_id),
	add constraint ab_person_attributes_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table ab_person_identities_tbl 
	add index ab_person_identities_tbl_identity_type_id (identity_type_id),
	add constraint ab_person_identities_tbl_identity_type_id
	foreign key (identity_type_id)
	references ab_identity_types_tbl (id);

alter table ab_person_identities_tbl
	add index ab_person_identities_tbl_person_id (person_id),
	add constraint ab_person_identities_tbl_person_id
	foreign key (person_id)
	references ab_persons_tbl (id);

alter table ab_person_identity_attributes_tbl
	add index ab_person_identity_attributes_tbl_person_identity_id (person_identity_id),
	add constraint ab_person_identity_attributes_tbl_person_identity_id
	foreign key (person_identity_id)
	references ab_person_identities_tbl (id);

alter table ab_person_identity_attributes_tbl
	add index ab_person_identity_attributes_tbl_language_id (language_id),
	add constraint ab_person_identity_attributes_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);


update common_version_tbl set last_modified_date='2008-07-10', date_version=0;




-- Insert some test data
select @person_id:=i.person_id
from ab_person_identities_tbl i
	inner join ab_identity_types_tbl t on i.identity_type_id=t.id
where t.type_enum=2 and first_name='Михаил' and middle_name='Анатольевич' and last_name='Федько';

select @person_identity_id:=i.id
from ab_person_identities_tbl i
	inner join ab_identity_types_tbl t on i.identity_type_id=t.id
where t.type_enum=1 and i.person_id=@person_id and i.status=0;

SELECT @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
SELECT @en_id:=id from common_languages_tbl where lang_iso_code='en';


INSERT INTO ab_person_identity_attributes_tbl (name, value, language_id, person_identity_id)
	VALUES ('ИНН', 'НЕТ', @ru_id, @person_identity_id);

INSERT INTO ab_person_identity_attributes_tbl (name, value, language_id, person_identity_id)
	VALUES ('INN', 'NONE', @en_id, @person_identity_id);

INSERT INTO ab_person_identity_attributes_tbl (name, value, language_id, person_identity_id)
	VALUES ('Аттрибут', 'Значение', @ru_id, @person_identity_id);

INSERT INTO ab_person_attributes_tbl (name, value, language_id, person_id)
	VALUES('Кол-во детей', '12', @ru_id, @person_id);
INSERT INTO ab_person_attributes_tbl (name, value, language_id, person_id)
	VALUES('Аттрибут', 'значение', @ru_id, @person_id);
INSERT INTO ab_person_attributes_tbl (name, value, language_id, person_id)
	VALUES('Children number', '13', @en_id, @person_id);
