alter table eirc_banks_tbl
        add column bank_identifier_code varchar(255) not null comment 'Bank identifier code (BIK)',
        add column corresponding_account varchar(255) not null comment 'Bank identifier code (BIK)';

alter table eirc_organisations_tbl
		drop column unique_id;

update common_version_tbl set last_modified_date='2008-07-22', date_version=0;


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

INSERT INTO eirc_banks_tbl (status, organisation_id, bank_identifier_code, corresponding_account)
	VALUES (0, @organisation_cn, '044525957', '30101810600000000957');
SELECT @bank_cn:=last_insert_id();
INSERT INTO eirc_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('Мега Банк', @ru_id, @bank_cn);
INSERT INTO eirc_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('Mega Bank', @en_id, @bank_cn);
INSERT INTO eirc_banks_tbl (status, organisation_id, bank_identifier_code, corresponding_account)
	VALUES (0, @organisation_eirc, '1233455', '30101810600000000455');
SELECT @bank_eirc:=last_insert_id();
INSERT INTO eirc_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('ЕИРЦ Банк', @ru_id, @bank_eirc);
INSERT INTO eirc_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('EIRC Bank', @en_id, @bank_eirc);
