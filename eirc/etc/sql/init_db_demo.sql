set @town_kharkov_id:=1;
set @street_pereulok_id:=6;
set @street_viaduk_id:=2;
set @attr_type_part_id:=3;
set @ru_id:=1;
set @en_id:=2;
set @identity_type_fio_id:=1;
set @eirc_registry_rec:=1;
set @service_garazh_id:=21;
set @service_zhivotnoe_id:=26;
set @service_kvartplata_id:=1;
set @service_pogreba_id:=25;
set @service_saray_id:=24;
set @attr_type_home_number_id:=1;
set @attr_type_part_id:=3;
set @service_org_1:=1;

INSERT INTO ab_districts_tbl (status, town_id) VALUES (0, @town_kharkov_id);
SELECT @district_id_kharkov_central:=last_insert_id();
INSERT INTO ab_district_names_tbl (district_id) VALUES (@district_id_kharkov_central);
SELECT @district_name_id:=last_insert_id();
INSERT INTO ab_district_name_translations_tbl (name, district_name_id, language_id)
	VALUES ('Центральный', @district_name_id, @ru_id);
INSERT INTO ab_district_names_temporal_tbl (district_id, district_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@district_id_kharkov_central, @district_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');

INSERT INTO ab_streets_tbl (status, town_id) values (0, @town_kharkov_id);
SELECT @street_id_aptekarskiy_per:=last_insert_id();
INSERT INTO ab_street_names_tbl (street_id) VALUES (@street_id_aptekarskiy_per);
SELECT @street_name_id:=last_insert_id();
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Аптекарский', @street_name_id, @ru_id);
INSERT INTO ab_street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_aptekarskiy_per, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_aptekarskiy_per, @street_pereulok_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_streets_districts_tbl (street_id, district_id) VALUES (@street_id_aptekarskiy_per, @district_id_kharkov_central);

INSERT INTO ab_streets_tbl (status, town_id) values (0, @town_kharkov_id);
SELECT @street_id_aptekarskiy_viaduk:=last_insert_id();
INSERT INTO ab_street_names_tbl (street_id) VALUES (@street_id_aptekarskiy_viaduk);
SELECT @street_name_id:=last_insert_id();
INSERT INTO ab_street_name_translations_tbl (name, street_name_id, language_id)
	VALUES ('Аптекарский', @street_name_id, @ru_id);
INSERT INTO ab_street_names_temporal_tbl (street_id, street_name_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_aptekarskiy_viaduk, @street_name_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_street_types_temporal_tbl (street_id, street_type_id, begin_date, end_date, create_date, invalid_date)
	VALUES (@street_id_aptekarskiy_viaduk, @street_viaduk_id, '1900-01-01', '2100-12-31', '2008-01-17', '2100-12-31');
INSERT INTO ab_streets_districts_tbl (street_id, district_id) VALUES (@street_id_aptekarskiy_viaduk, @district_id_kharkov_central);


INSERT INTO ab_buildings_tbl (status, building_type, district_id) VALUES (0, 'ab', @district_id_kharkov_central);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_aptekarskiy_per, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('9', 0, @attr_type_home_number_id, @buildings_id);
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('1', 0, @attr_type_part_id, @buildings_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '12', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '16', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '37', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '59', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '62', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '70', @apartment_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '103', @apartment_id);


INSERT INTO ab_buildings_tbl (status, building_type, district_id) VALUES (0, 'ab', @district_id_kharkov_central);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_aptekarskiy_per, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('9', 0, @attr_type_home_number_id, @buildings_id);
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('2', 0, @attr_type_part_id, @buildings_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '17', @apartment_id);


INSERT INTO ab_buildings_tbl (status, building_type, district_id) VALUES (0, 'ab', @district_id_kharkov_central);
SELECT @building_id:=last_insert_id();
INSERT INTO ab_building_addresses_tbl (status, primary_status, street_id, building_id)
	VALUES (0, b'1', @street_id_aptekarskiy_viaduk, @building_id);
SELECT @buildings_id:=last_insert_id();
INSERT INTO ab_building_address_attributes_tbl (value, status, attribute_type_id, buildings_id)
	VALUES ('4', 0, @attr_type_home_number_id, @buildings_id);

INSERT INTO ab_apartments_tbl (status, apartment_type, building_id) VALUES (0, 'ab', @building_id);
SELECT @apartment_id:=last_insert_id();
INSERT INTO ab_apartment_numbers_tbl (begin_date, end_date, value, apartment_id)
	VALUES ('1900-01-01', '2100-12-31', '1', @apartment_id);


INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_garust:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
	0, 'А', 'А', 'Гаруст', '',
	0, @identity_type_fio_id, @person_id_garust);
insert into eirc_consumer_infos_tbl (status, first_name, middle_name, last_name,
	town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
	values (0, 'А', 'А', 'Гаруст',
	'Харьков', 'в-д', 'Аптекарский', '4', '', '1');
select @consumer_info_garust:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_garust, '09000164839', @consumer_info_garust);
SELECT @account_id_garust:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000164839', @service_kvartplata_id,
	@person_id_garust, @apartment_id, @account_id_garust, '2000-01-01', '2020-12-31', @consumer_info_garust);
select @consumer_garust_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000164839', @service_garazh_id,
	@person_id_garust, @apartment_id, @account_id_garust, '2000-01-01', '2020-12-31', @consumer_info_garust);
select @consumer_garust_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_garust_garazh, @eirc_registry_rec,
	'00.00', '2.81', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_garust_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_garust_kvartplata, @eirc_registry_rec,
	'00.00', '19.03', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_garust_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_garust, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_garust:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_garust_garazh, @quittance_garust);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_garust_kvartplata, @quittance_garust);



INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_afonichev:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
	0, 'А', 'М', 'Афоничев', '',
	0, @identity_type_fio_id, @person_id_afonichev);
insert into eirc_consumer_infos_tbl ( status, first_name, middle_name, last_name,
	town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
	values (0, 'А', 'М', 'Афоничев',
	'Харьков', 'в-д', 'Аптекарский', '4', '', '2');
select @consumer_info_afonichev:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_afonichev, '09000164840',@consumer_info_afonichev);
SELECT @account_id_afonichev:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000164840', @service_kvartplata_id,
	@person_id_afonichev, @apartment_id, @account_id_afonichev, '2000-01-01', '2020-12-31', @consumer_info_afonichev);
select @consumer_afonichev_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000164840', @service_saray_id,
	@person_id_afonichev, @apartment_id, @account_id_afonichev, '2000-01-01', '2020-12-31', @consumer_info_afonichev);
select @consumer_afonichev_saray:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_afonichev_saray, @eirc_registry_rec,
	'00.00', '2.81', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_afonichev_saray:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_afonichev_kvartplata, @eirc_registry_rec,
	'00.00', '21.07', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_afonichev_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_afonichev, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_afonichev:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_afonichev_saray, @quittance_afonichev);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_afonichev_kvartplata, @quittance_afonichev);

INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_turchin:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
	0, 'Г', 'А', 'Турчин', '',
	0, @identity_type_fio_id, @person_id_turchin);
insert into eirc_consumer_infos_tbl (status, first_name, middle_name, last_name,
	town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
	values (0, 'Г', 'А', 'Турчин',
	'Харьков', 'пер.', 'Аптекарский', '9', 'ч. 1', '12');
select @consumer_info_turchin:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_turchin, '09000163986', @consumer_info_turchin);
SELECT @account_id_turchin:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000163986', @service_kvartplata_id,
	@person_id_turchin, @apartment_id, @account_id_turchin, '2000-01-01', '2020-12-31', @consumer_info_turchin);
select @consumer_turchin_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000163986', @service_zhivotnoe_id,
	@person_id_turchin, @apartment_id, @account_id_turchin, '2000-01-01', '2020-12-31', @consumer_info_turchin);
select @consumer_turchin_zhivotnoe:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_turchin_zhivotnoe, @eirc_registry_rec,
	'00.00', '1.42', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_turchin_zhivotnoe:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_turchin_kvartplata, @eirc_registry_rec,
	'00.00', '40.72', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_turchin_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_turchin, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_turchin:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_turchin_zhivotnoe, @quittance_turchin);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_turchin_kvartplata, @quittance_turchin);

INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_zaicev:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
	0, 'В', 'А', 'Зайцев', '',
	0, @identity_type_fio_id, @person_id_zaicev);
insert into eirc_consumer_infos_tbl ( status, first_name, middle_name, last_name,
	town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
    values (0, 'В', 'А', 'Зайцев',
    'Харьков', 'пер.', 'Аптекарский', '9', 'ч. 1', '16');
select @consumer_info_zaicev:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_zaicev, '09000163990', @consumer_info_zaicev);
SELECT @account_id_zaicev:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000163990', @service_kvartplata_id,
	@person_id_zaicev, @apartment_id, @account_id_zaicev, '2000-01-01', '2020-12-31', @consumer_info_zaicev);
select @consumer_zaicev_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000163990', @service_garazh_id,
	@person_id_zaicev, @apartment_id, @account_id_zaicev, '2000-01-01', '2020-12-31', @consumer_info_zaicev);
select @consumer_zaicev_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_zaicev_garazh, @eirc_registry_rec,
	'00.00', '2.81', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_zaicev_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_zaicev_kvartplata, @eirc_registry_rec,
	'00.00', '52.85', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_zaicev_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_zaicev, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_zaicev:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_zaicev_garazh, @quittance_zaicev);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_zaicev_kvartplata, @quittance_zaicev);

INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_capko:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
            0, 'Т', 'В', 'Цапко', '',
            0, @identity_type_fio_id, @person_id_capko);
insert into eirc_consumer_infos_tbl ( status, first_name, middle_name, last_name,
        town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
    values (0, 'Т', 'В', 'Цапко',
        'Харьков', 'пер.', 'Аптекарский', '9', 'ч. 1', '37');
select @consumer_info_capko:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_capko, '09000164013', @consumer_info_capko);
SELECT @account_id_capko:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000164013', @service_kvartplata_id,
	@person_id_capko, @apartment_id, @account_id_capko, '2000-01-01', '2020-12-31', @consumer_info_capko);
select @consumer_capko_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000164013', @service_pogreba_id,
	@person_id_capko, @apartment_id, @account_id_capko, '2000-01-01', '2020-12-31', @consumer_info_capko);
select @consumer_capko_pogreba:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_capko_pogreba, @eirc_registry_rec,
	'00.00', '2.81', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_capko_pogreba:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_capko_kvartplata, @eirc_registry_rec,
	'00.00', '28.15', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_capko_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_capko, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_capko:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_capko_pogreba, @quittance_capko);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_capko_kvartplata, @quittance_capko);

INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_goncharova:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
            0, 'В', 'М', 'Гончарова', '',
            0, @identity_type_fio_id, @person_id_goncharova);
insert into eirc_consumer_infos_tbl ( status, first_name, middle_name, last_name,
            town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
      values (0, 'В', 'М', 'Гончарова',
            'Харьков', 'пер.', 'Аптекарский', '9', 'ч. 1', '62');
select @consumer_info_goncharova:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_goncharova, '09000164041', @consumer_info_goncharova);
SELECT @account_id_goncharova:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000164041', @service_kvartplata_id,
	@person_id_goncharova, @apartment_id, @account_id_goncharova, '2000-01-01', '2020-12-31', @consumer_info_goncharova);
select @consumer_goncharova_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000164041', @service_pogreba_id,
	@person_id_goncharova, @apartment_id, @account_id_goncharova, '2000-01-01', '2020-12-31', @consumer_info_goncharova);
select @consumer_goncharova_pogreba:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000163990', @service_garazh_id,
	@person_id_goncharova, @apartment_id, @account_id_goncharova, '2000-01-01', '2020-12-31', @consumer_info_goncharova);
select @consumer_goncharova_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_goncharova_pogreba, @eirc_registry_rec,
	'00.00', '2.81', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_goncharova_pogreba:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_goncharova_kvartplata, @eirc_registry_rec,
	'00.00', '28.15', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_goncharova_kvartplata:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_goncharova_garazh, @eirc_registry_rec,
	'00.00', '2.81', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_goncharova_garazh:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_goncharova, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_goncharova:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_goncharova_pogreba, @quittance_goncharova);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_goncharova_kvartplata, @quittance_goncharova);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_goncharova_garazh, @quittance_goncharova);


INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_panchenko:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
            0, 'А', 'В', 'Панченко', '',
            0, @identity_type_fio_id, @person_id_panchenko);
insert into eirc_consumer_infos_tbl ( status, first_name, middle_name, last_name,
            town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
    values (0, 'А', 'В', 'Панченко',
            'Харьков', 'пер.', 'Аптекарский', '9', 'ч. 1', '59');
select @consumer_info_panchenko:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_panchenko, '09000164037', @consumer_info_panchenko);
SELECT @account_id_panchenko:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000164037', @service_kvartplata_id,
	@person_id_panchenko, @apartment_id, @account_id_panchenko, '2000-01-01', '2020-12-31', @consumer_info_panchenko);
select @consumer_panchenko_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000164037', @service_garazh_id,
	@person_id_panchenko, @apartment_id, @account_id_panchenko, '2000-01-01', '2020-12-31', @consumer_info_panchenko);
select @consumer_panchenko_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_panchenko_garazh, @eirc_registry_rec,
	'00.00', '3.28', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_panchenko_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_panchenko_kvartplata, @eirc_registry_rec,
	'00.00', '52.85', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_panchenko_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_panchenko, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_panchenko:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_panchenko_garazh, @quittance_panchenko);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_panchenko_kvartplata, @quittance_panchenko);


INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_yudin:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
            0, 'С', 'А', 'Юдин', '',
            0, @identity_type_fio_id, @person_id_yudin);
insert into eirc_consumer_infos_tbl ( status, first_name, middle_name, last_name,
            town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
    values (0, 'С', 'А', 'Юдин',
            'Харьков', 'пер.', 'Аптекарский', '9', 'ч. 1', '70');
select @consumer_info_yudin:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_yudin, '09000164050', @consumer_info_yudin);
SELECT @account_id_yudin:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000164050', @service_kvartplata_id,
	@person_id_yudin, @apartment_id, @account_id_yudin, '2000-01-01', '2020-12-31', @consumer_info_yudin);
select @consumer_yudin_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000164050', @service_pogreba_id,
	@person_id_yudin, @apartment_id, @account_id_yudin, '2000-01-01', '2020-12-31', @consumer_info_yudin);
select @consumer_yudin_pogreba:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_yudin_pogreba, @eirc_registry_rec,
	'00.00', '0.94', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_yudin_pogreba:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_yudin_kvartplata, @eirc_registry_rec,
	'00.00', '64.87', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_yudin_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_yudin, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_yudin:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_yudin_pogreba, @quittance_yudin);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_yudin_kvartplata, @quittance_yudin);

INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_miroshnichenko:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
            0, 'Галина', 'Николаевна', 'Мирошниченко', '',
            0, @identity_type_fio_id, @person_id_miroshnichenko);
insert into eirc_consumer_infos_tbl ( status, first_name, middle_name, last_name,
            town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
    values (0, 'Галина', 'Николаевна', 'Мирошниченко',
            'Харьков', 'пер.', 'Аптекарский', '9', 'ч. 1', '59');
select @consumer_info_miroshnichenko:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_miroshnichenko, '09000164090', @consumer_info_miroshnichenko);
SELECT @account_id_miroshnichenko:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000164090', @service_kvartplata_id,
	@person_id_miroshnichenko, @apartment_id, @account_id_miroshnichenko, '2000-01-01', '2020-12-31', @consumer_info_miroshnichenko);
select @consumer_miroshnichenko_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000164090', @service_garazh_id,
	@person_id_miroshnichenko, @apartment_id, @account_id_miroshnichenko, '2000-01-01', '2020-12-31', @consumer_info_miroshnichenko);
select @consumer_miroshnichenko_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_miroshnichenko_garazh, @eirc_registry_rec,
	'00.00', '0.94', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_miroshnichenko_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_miroshnichenko_kvartplata, @eirc_registry_rec,
	'00.00', '59.20', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_miroshnichenko_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_miroshnichenko, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_miroshnichenko:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_miroshnichenko_garazh, @quittance_miroshnichenko);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_miroshnichenko_kvartplata, @quittance_miroshnichenko);

INSERT INTO ab_persons_tbl (status) VALUES (0);
SELECT @person_id_klimko:=last_insert_id();

INSERT INTO ab_person_identities_tbl (status, sex, begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id)
	VALUES (0, 2, '1983-01-25', '2100-12-31', '1983-01-25', 0,
            0, 'А', 'М', 'Климко', '',
            0, @identity_type_fio_id, @person_id_klimko);
insert into eirc_consumer_infos_tbl ( status, first_name, middle_name, last_name,
            town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
    values (0, 'А', 'М', 'Климко',
            'Харьков', 'пер.', 'Аптекарский', '9', 'ч. 1', '103');
select @consumer_info_klimko:=last_insert_id();

INSERT INTO eirc_eirc_accounts_tbl (version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES ( 0, 0, @apartment_id, @person_id_klimko, '09000163981', @consumer_info_klimko);
SELECT @account_id_klimko:=last_insert_id();

insert into eirc_consumers_tbl ( status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values ( 0, '1000163981', @service_kvartplata_id,
	@person_id_klimko, @apartment_id, @account_id_klimko, '2000-01-01', '2020-12-31', @consumer_info_klimko);
select @consumer_klimko_kvartplata:=last_insert_id();

insert into eirc_consumers_tbl (status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (0, '1000163981', @service_garazh_id,
	@person_id_klimko, @apartment_id, @account_id_klimko, '2000-01-01', '2020-12-31', @consumer_info_klimko);
select @consumer_klimko_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_klimko_garazh, @eirc_registry_rec,
	'00.00', '2.81', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_klimko_garazh:=last_insert_id();

insert into eirc_quittance_details_tbl (consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (@consumer_klimko_kvartplata, @eirc_registry_rec,
	'00.00', '39.68', '00.00', '00.00', '00.00', '0.0', '0.0', '0.0', '00.00', '2009-01-01');
select @quittance_details_klimko_kvartplata:=last_insert_id();

insert into eirc_quittances_tbl (service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (@service_org_1, @account_id_klimko, 1, '2009-01-01', '2009-01-31', '2009-02-01');
select @quittance_klimko:=last_insert_id();
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_klimko_garazh, @quittance_klimko);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_klimko_kvartplata, @quittance_klimko);

-- Organization, Payment Collector, Payment points, Cashboxes
insert into orgs_organizations_tbl (status, version, juridical_address, postal_address, individual_tax_number, kpp)
	values (0, 0, '', '', '123123123', '123');
select @organization_zol_vor:=last_insert_id();
insert into orgs_organization_descriptions_tbl (name, language_id, organization_id)
	values ('Банк для тестирования', @ru_id, @organization_zol_vor);
insert into orgs_organization_descriptions_tbl (name, language_id, organization_id)
	values ('Bank for testing', @en_id, @organization_zol_vor);
insert into orgs_organization_names_tbl (name, language_id, organization_id)
	values ('Золотые ворота', @ru_id, @organization_zol_vor);
insert into orgs_organization_names_tbl (name, language_id, organization_id)
	values ('Zolotie vorota', @en_id, @organization_zol_vor);

insert into orgs_payment_collectors_tbl (status, version, organization_id, email)
	values (0, 0, @organization_zol_vor, 'collector@zolotievorota.kharkov.ua');
select @collector_zol_vor:=last_insert_id();
insert into orgs_payment_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@ru_id, @collector_zol_vor, 'Сборщик для банка "Золотые ворота"');
insert into orgs_payment_collectors_descriptions_tbl (language_id, collector_id, name)
	values (@en_id, @collector_zol_vor, 'Collector for bank "Zolotie vorota"');

insert into orgs_payment_points_tbl (status, version, collector_id, address)
	values (0, 0, @collector_zol_vor, 'address');
select @payment_point_zol_vor_central:=last_insert_id();
insert into orgs_payment_point_names_tbl (name, language_id, payment_point_id)
	values ('ППП Центральный', @ru_id, @payment_point_zol_vor_central);

insert into orgs_payment_points_tbl (status, version, collector_id, address)
	values (0, 0, @collector_zol_vor, 'address');
select @payment_point_zol_vor_sever:=last_insert_id();
insert into orgs_payment_point_names_tbl (name, language_id, payment_point_id)
	values ('ППП Северный', @ru_id, @payment_point_zol_vor_sever);

insert into orgs_cashboxes_tbl (status, version, payment_point_id)
	values (0, 0, @payment_point_zol_vor_central);
select @cashbox_central_1:=last_insert_id();
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_central_1, 'Первая центральная касса');

insert into orgs_cashboxes_tbl (status, version, payment_point_id)
	values (0, 0, @payment_point_zol_vor_central);
select @cashbox_central_2:=last_insert_id();
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_central_2, 'Вторая центральная касса');

insert into orgs_cashboxes_tbl (status, version, payment_point_id)
	values (0, 0, @payment_point_zol_vor_sever);
select @cashbox_sever_1:=last_insert_id();
insert into orgs_cashbox_name_translations_tbl (version, language_id, cashbox_id, name)
	values (0, @ru_id, @cashbox_sever_1, 'Первая северная касса');
