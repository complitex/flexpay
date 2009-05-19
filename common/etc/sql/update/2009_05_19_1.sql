create table common_currency_infos_tbl (
	id bigint not null auto_increment comment 'Primary key',
	iso_code varchar(255) not null comment 'ISO 4217 code of a currency',
	gender integer not null comment 'Gender (0-masculine, 1-feminine, 2-neuter)',
	primary key (id)
) comment='Currency infos';

create table common_currency_names_tbl (
	id bigint not null auto_increment comment 'Primary key',
	name varchar(255) not null comment 'Full currency name translation',
	short_name varchar(255) not null comment 'Short currency name translation',
	fraction_name varchar(255) not null comment 'Full currency fraction name translation',
	fraction_short_name varchar(255) not null comment 'Short currency fraction name translation',
	language_id bigint not null comment 'Language reference',
	currency_info_id bigint not null comment 'Currency info reference',
	primary key (id),
	unique (language_id, currency_info_id)
) comment='Currency name translation';

alter table common_currency_names_tbl
	add index FK_common_currency_names_tbl_currency_info_id (currency_info_id),
	add constraint FK_common_currency_names_tbl_currency_info_id
	foreign key (currency_info_id)
	references common_currency_infos_tbl (id);

alter table common_currency_names_tbl
	add index common_currency_names_tbl_language_id (language_id),
	add constraint common_currency_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

insert into common_currency_infos_tbl(id, iso_code, gender) values (1, 'UAH', 1);
select @currency_grivna:=1;
SELECT @ru_id:=id from common_languages_tbl where lang_iso_code='ru';
insert into common_currency_names_tbl(language_id, currency_info_id, name, short_name, fraction_name, fraction_short_name)
	values (@ru_id, @currency_grivna, 'Гривна', 'грн', 'Копейка', 'коп');


update common_version_tbl set last_modified_date='2009-05-19', date_version=1;
