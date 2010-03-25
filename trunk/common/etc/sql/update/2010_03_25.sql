create table common_user_role_name_translations_tbl (
	id bigint not null auto_increment,
	name varchar(255),
	user_role_id bigint not null,
	language_id bigint not null,
	primary key (id),
	unique (user_role_id, language_id)
);

create table common_user_roles_tbl (
	id bigint not null auto_increment,
	status integer not null,
	external_id varchar(255) not null unique,
	primary key (id)
);

alter table common_users_tbl
	add column user_role_id bigint comment 'Optional user role reference';

-- Init User Roles table
INSERT INTO common_user_roles_tbl (id, status, external_id) values (1, 0, 'buhgalter');
SELECT @buhgalter_id:=1;
INSERT INTO common_user_roles_tbl (id, status, external_id) values (2, 0, 'cashier');
SELECT @cashier_id:=2;

SELECT @ru_id:=(select id from common_languages_tbl where lang_iso_code='ru');
SELECT @en_id:=(select id from common_languages_tbl where lang_iso_code='en');

INSERT INTO common_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Бухгалтер', @buhgalter_id, @ru_id);
INSERT INTO common_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Кассир', @cashier_id, @ru_id);
INSERT INTO common_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Accountant', @buhgalter_id, @en_id);
INSERT INTO common_user_role_name_translations_tbl (name, user_role_id, language_id)
	VALUES ('Cashier', @cashier_id, @en_id);

INSERT INTO common_version_tbl (last_modified_date, date_version) VALUES ('2010-03-25', 0);
