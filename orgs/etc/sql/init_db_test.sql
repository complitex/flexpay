-- put here module test initialization data
-- Init organizations
-- EIRC is the first one, ID=1
-- CN is the fourth one, ID=4
INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('EIRC DS');
select @sd:=last_insert_id();
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp, data_source_description_id)
	VALUES (1, 0, 0, 'ADDR', 'ADDR', '-------', '123', @sd);
SELECT @organization_eirc:=1;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Eirc itself', @ru_id, @organization_eirc);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('EIRC', @ru_id, @organization_eirc);

INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('ЖКО DS');
select @sd:=last_insert_id();
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp, data_source_description_id)
	VALUES (2, 0, 0, 'Иванова 27-315', 'Иванова 27-314', '123123123', '123', @sd);
SELECT @organization_zhko:=2;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Test organization', @ru_id, @organization_zhko);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('ЖКО', @ru_id, @organization_zhko);

INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('ТСЖ DS');
select @sd:=last_insert_id();
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp, data_source_description_id)
	VALUES (3, 0, 0, 'Иванова 1', 'Иванова 2', '456456456', '56', @sd);
SELECT @organization_tszh:=3;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Test organization 2', @ru_id, @organization_tszh);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('ТСЖ', @ru_id, @organization_tszh);

INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp, data_source_description_id)
	VALUES (4, 0, 0, 'Иванова 26-315', 'Иванова 26-314', '1111111', '56', @source_description_cn);
SELECT @organization_cn:=4;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Calculation center', @ru_id, @organization_cn);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('ЦН', @ru_id, @organization_cn);

-- Init subdivisions
INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', '3-я серверная стойка', null, @organization_eirc, @organization_eirc);
SELECT @subdivision_eirc_it:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'АйТи', @ru_id, @subdivision_eirc_it);
INSERT INTO orgs_subdivision_names_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'IT', @en_id, @subdivision_eirc_it);
INSERT INTO orgs_subdivision_descriptions_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Отдел информационных технологий', @ru_id, @subdivision_eirc_it);
INSERT INTO orgs_subdivision_descriptions_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Informational technoligies department', @en_id, @subdivision_eirc_it);

INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', '1-я серверная стойка', @subdivision_eirc_it, @organization_eirc, null);
SELECT @subdivision_eirc_it_java:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Java', @ru_id, @subdivision_eirc_it_java);
INSERT INTO orgs_subdivision_descriptions_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Жабный сектор', @ru_id, @subdivision_eirc_it_java);

INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', '2-я серверная стойка', @subdivision_eirc_it, @organization_eirc, null);
SELECT @subdivision_eirc_it_web:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Web', @ru_id, @subdivision_eirc_it_web);
INSERT INTO orgs_subdivision_descriptions_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Вэббизнес', @ru_id, @subdivision_eirc_it_web);

INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', 'Кабинет направо', null, @organization_eirc, @organization_eirc);
SELECT @subdivision_eirc_buch:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Бухгалтерия', @ru_id, @subdivision_eirc_buch);
INSERT INTO orgs_subdivision_descriptions_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Бухгалтерский отдел', @ru_id, @subdivision_eirc_buch);

INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', 'Центр клининг-услуг', null, @organization_eirc, @organization_cn);
SELECT @subdivision_eirc_cleaning:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'Уборщики', @ru_id, @subdivision_eirc_cleaning);
INSERT INTO orgs_subdivision_descriptions_tbl (version, name, language_id, subdivision_id)
	VALUES (0, 'сектор Очистки помещений', @ru_id, @subdivision_eirc_cleaning);


-- Init banks
INSERT INTO orgs_banks_tbl (id, status, version, organization_id, bank_identifier_code, corresponding_account)
	VALUES (1, 0, 0, @organization_cn, '044525957', '30101810600000000957');
SELECT @bank_cn:=1;
INSERT INTO orgs_bank_descriptions_tbl (version, name, language_id, bank_id)
	VALUES (0, 'Мега Банк', @ru_id, @bank_cn);
INSERT INTO orgs_bank_descriptions_tbl (version, name, language_id, bank_id)
	VALUES (0, 'Mega Bank', @en_id, @bank_cn);
INSERT INTO orgs_banks_tbl (id, status, version, organization_id, bank_identifier_code, corresponding_account)
	VALUES (2, 0, 0, @organization_eirc, '1233455', '30101810600000000455');
SELECT @bank_eirc:=2;
INSERT INTO orgs_bank_descriptions_tbl (version, name, language_id, bank_id)
	VALUES (0, 'ЕИРЦ Банк', @ru_id, @bank_eirc);
INSERT INTO orgs_bank_descriptions_tbl (version, name, language_id, bank_id)
	VALUES (0, 'EIRC Bank', @en_id, @bank_eirc);


-- Init service providers
INSERT INTO orgs_service_providers_tbl(id, status, version, organization_id)
	VALUES (1, 0, 0, @organization_cn);
SELECT @service_provider_cn:=1;
INSERT INTO orgs_service_provider_descriptions_tbl (version, name, language_id, service_provider_id)
	VALUES (0, 'ПУ ЦН', @ru_id, @service_provider_cn);

-- Init service organizations
INSERT INTO common_data_source_descriptions_tbl (description) VALUES ('EIRC DS');
select @sd:=last_insert_id();
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp, data_source_description_id)
	VALUES (5, 0, 0, 'Демакова-3', 'Демакова-3', '56', '56', @sd);
SELECT @organization_service_org_1:=5;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Тестовая обсл. организация', @ru_id, @organization_service_org_1);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('Участок-45', @ru_id, @organization_service_org_1);

INSERT INTO orgs_service_organizations_tbl(id, status, organization_id, org_type)
	VALUES (1, 0, @organization_service_org_1, 'orgs');
SELECT @service_org_1:=1;
INSERT INTO orgs_service_organization_descriptions_tbl (name, language_id, service_organization_id)
	VALUES ('ЖКО', @ru_id, @service_org_1);

-- Payment collectors
insert INTO orgs_payment_collectors_tbl (id, status, version, organization_id, email)
	values (1, 0, 0, @organization_cn, 'collector@cn.kharkov.ua');
select @collector_1:=1;
insert INTO orgs_payment_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@ru_id, @collector_1, 'Сборщик ЦН');
insert INTO orgs_payment_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@en_id, @collector_1, 'Collector CN');

insert INTO orgs_payment_collectors_tbl (id, status, version, organization_id, email)
	values (2, 0, 0, @organization_tszh, 'collector@tszh.kharkov.ua');
select @collector_2:=2;
insert INTO orgs_payment_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@ru_id, @collector_2, 'Сборщик ТСЖ');
insert INTO orgs_payment_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@en_id, @collector_2, 'Collector TSZH');

-- Payment points
insert INTO orgs_payment_points_tbl (id, status, version, collector_id, address)
	values (1, 0, 0, @collector_1, 'сборщик ЦН адрес');
select @payment_point_1:=1;
insert into orgs_payment_point_names_tbl (name, language_id, payment_point_id)
	values ('Касса ЦН №1', @ru_id, @payment_point_1);

insert INTO orgs_payment_points_tbl (id, status, version, collector_id, address)
	values (2, 0, 0, @collector_1, 'сборщик ЦН #2');
select @payment_point_2:=2;
insert into orgs_payment_point_names_tbl (name, language_id, payment_point_id)
	values ('Касса ЦН №2', @ru_id, @payment_point_2);

insert INTO orgs_payment_points_tbl (id, status, version, collector_id, address)
	values (3, 0, 0, @collector_2, 'сборщик ТСЖ #1');
select @payment_point_3:=3;
insert into orgs_payment_point_names_tbl (name, language_id, payment_point_id)
	values ('Касса ТСЖ №1', @ru_id, @payment_point_3);

-- init cashboxes
insert into orgs_cashboxes_tbl (id, status, version, payment_point_id)
	values (1, 0, 0, @payment_point_1);
select @cashbox_1_1:=1;
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_1_1, 'Тестовая касса1_1');

insert into orgs_cashboxes_tbl (id, status, version, payment_point_id)
	values (2, 0, 0, @payment_point_1);
select @cashbox_1_2:=2;
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_1_2, 'Тестовая касса1_2');

insert into orgs_cashboxes_tbl (status, version, payment_point_id)
	values (0, 0, @payment_point_2);
select @cashbox_2_1:=last_insert_id();
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_2_1, 'Тестовая касса2_1');

insert into orgs_cashboxes_tbl (status, version, payment_point_id)
	values (0, 0, @payment_point_3);
select @cashbox_3_1:=last_insert_id();
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_3_1, 'Тестовая касса3_1');

insert into orgs_cashboxes_tbl (status, version, payment_point_id)
	values (0, 0, @payment_point_3);
select @cashbox_3_2:=last_insert_id();
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_3_2, 'Тестовая касса3_2');

insert into orgs_cashboxes_tbl (status, version, payment_point_id)
	values (0, 0, @payment_point_3);
select @cashbox_3_2:=last_insert_id();
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_3_2, 'Тестовая касса3_3');
