-- put here module test initialization data
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp)
	VALUES (2, 0, 0, '', '', '123123123', '123');
SELECT @organization_zhko:=2;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Test organization', @ru_id, @organization_zhko);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('ЖКО', @ru_id, @organization_zhko);
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp)
	VALUES (3, 0, 0, '', '', '456456456', '56');
SELECT @organization_tszh:=3;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Test organization 2', @ru_id, @organization_tszh);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('ТСЖ', @ru_id, @organization_tszh);
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp)
	VALUES (4, 0, 0, '', '', '1111111', '56');
SELECT @organization_cn:=4;
INSERT INTO orgs_organization_descriptions_tbl (name, language_id, organization_id)
	VALUES ('Calculation center', @ru_id, @organization_cn);
INSERT INTO orgs_organization_names_tbl (name, language_id, organization_id)
	VALUES ('ЦН', @ru_id, @organization_cn);

-- Init subdivisions
INSERT INTO orgs_subdivisions_tbl (status,  version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', '3-я серверная стойка', null, @organization_eirc, @organization_eirc);
SELECT @subdivision_eirc_it:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('АйТи', @ru_id, @subdivision_eirc_it);
INSERT INTO orgs_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('IT', @en_id, @subdivision_eirc_it);
INSERT INTO orgs_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Отдел информационных технологий', @ru_id, @subdivision_eirc_it);
INSERT INTO orgs_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Informational technoligies department', @en_id, @subdivision_eirc_it);

INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', '1-я серверная стойка', @subdivision_eirc_it, @organization_eirc, null);
SELECT @subdivision_eirc_it_java:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Java', @ru_id, @subdivision_eirc_it_java);
INSERT INTO orgs_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Жабный сектор', @ru_id, @subdivision_eirc_it_java);

INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', '2-я серверная стойка', @subdivision_eirc_it, @organization_eirc, null);
SELECT @subdivision_eirc_it_web:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Web', @ru_id, @subdivision_eirc_it_web);
INSERT INTO orgs_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Вэббизнес', @ru_id, @subdivision_eirc_it_web);

INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', 'Кабинет направо', null, @organization_eirc, @organization_eirc);
SELECT @subdivision_eirc_buch:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Бухгалтерия', @ru_id, @subdivision_eirc_buch);
INSERT INTO orgs_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('Бухгалтерский отдел', @ru_id, @subdivision_eirc_buch);

INSERT INTO orgs_subdivisions_tbl (status, version, tree_path, real_address, parent_subdivision_id, head_organization_id, juridical_person_id)
	VALUES (0, 0, '', 'Центр клининг-услуг', null, @organization_eirc, @organization_cn);
SELECT @subdivision_eirc_cleaning:=last_insert_id();
INSERT INTO orgs_subdivision_names_tbl (name, language_id, subdivision_id)
	VALUES ('Уборщики', @ru_id, @subdivision_eirc_cleaning);
INSERT INTO orgs_subdivision_descriptions_tbl (name, language_id, subdivision_id)
	VALUES ('сектор Очистки помещений', @ru_id, @subdivision_eirc_cleaning);


-- Init banks
INSERT INTO orgs_banks_tbl (status, version, organization_id, bank_identifier_code, corresponding_account)
	VALUES (0, 0, @organization_cn, '044525957', '30101810600000000957');
SELECT @bank_cn:=last_insert_id();
INSERT INTO orgs_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('Мега Банк', @ru_id, @bank_cn);
INSERT INTO orgs_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('Mega Bank', @en_id, @bank_cn);
INSERT INTO orgs_banks_tbl (status, version, organization_id, bank_identifier_code, corresponding_account)
	VALUES (0, 0, @organization_eirc, '1233455', '30101810600000000455');
SELECT @bank_eirc:=last_insert_id();
INSERT INTO orgs_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('ЕИРЦ Банк', @ru_id, @bank_eirc);
INSERT INTO orgs_bank_descriptions_tbl (name, language_id, bank_id)
	VALUES ('EIRC Bank', @en_id, @bank_eirc);


-- Init service providers
INSERT INTO orgs_service_providers_tbl(id, status, organization_id, data_source_description_id)
	VALUES (1, 0, @organization_cn, @source_description_id);
SELECT @service_provider_cn:=1;
INSERT INTO orgs_service_provider_descriptions_tbl (name, language_id, service_provider_id)
	VALUES ('ПУ ЦН', @ru_id, @service_provider_cn);

-- Init service organizations
INSERT INTO orgs_organizations_tbl (id, status, version, juridical_address, postal_address, individual_tax_number, kpp)
	VALUES (5, 0, 0, '', '', '', '56');
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

-- Setup service organization
update ab_buildings_tbl set eirc_service_organization_id=@service_org_1 where id=@building_ivanova_27_id;

-- Payment collectors
insert INTO orgs_payments_collectors_tbl (id, status, version, organization_id)
	values (1, 0, 0, @organization_cn);
select @collector_1:=1;
insert INTO orgs_payments_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@ru_id, @collector_1, 'Сборщик ЦН');
insert INTO orgs_payments_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@en_id, @collector_1, 'Collector CN');

insert INTO orgs_payments_collectors_tbl (id, status, version, organization_id)
	values (2, 0, 0, @organization_tszh);
select @collector_2:=2;
insert INTO orgs_payments_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@ru_id, @collector_2, 'Сборщик ТСЖ');
insert INTO orgs_payments_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@en_id, @collector_2, 'Collector TSZH');

-- Payment points
insert INTO orgs_payment_points_tbl (id, status, version, collector_id, address)
	values (1, 0, 0, @collector_1, 'сборщик ЦН адрес');
select @payment_point_1:=1;
insert INTO orgs_payment_points_tbl (id, status, version, collector_id, address)
	values (2, 0, 0, @collector_1, 'сборщик ЦН #2');
select @payment_point_2:=2;

insert INTO orgs_payment_points_tbl (id, status, version, collector_id, address)
	values (3, 0, 0, @collector_2, 'сборщик ТСЖ #1');
select @payment_point_3:=3;
