-- Setup service organization
update ab_buildings_tbl set eirc_service_organization_id=@service_org_1 where id=@building_ivanova_27_id;

-- Init Consumer infos
insert into eirc_consumer_infos_tbl (id, status, first_name, middle_name, last_name,
	town_name, street_type_name, street_name, building_number, building_bulk, apartment_number)
	values (1, 0, 'М', 'А', 'Иванофф',
	'Н-ск', 'ул', 'ИВОНОВА', '27', '', '330');
select @consumer_info:=1;

-- Init EIRC accounts
INSERT INTO eirc_eirc_accounts_tbl (id, version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES (1, 0, 0, @apartment_ivanova_329_id, @person_id, '09012345067', @consumer_info);
SELECT @account_id_1:=1;
INSERT INTO eirc_eirc_accounts_tbl (id, version, status, apartment_id, person_id, account_number, consumer_info_id)
	VALUES (2, 0, 0, @apartment_ivanova_330_id, @person_id, '09076543021', @consumer_info);
SELECT @account_id_2:=2;

-- Init registry
INSERT INTO common_registries_tbl (id, version, registry_type_id, registry_status_id, archive_status_id, module_id)
	values (1, 0, @registry_type_info, @registry_status_loaded, @sp_registry_archive_status_none, @module_eirc);
select @eirc_registry:=1;
insert into common_registry_properties_tbl (version, props_type, registry_id) values (0, 'common', @eirc_registry);

-- Init registry records
INSERT INTO common_registry_records_tbl (id, version, service_code, registry_id, record_status_id, operation_date, personal_account_ext)
	values (1, 0, '', @eirc_registry, @record_status_loaded, '2008-01-01', '123456');
select @eirc_registry_rec:=1;
insert into common_registry_record_properties_tbl (version, props_type, record_id) values (0, 'common', @eirc_registry_rec);

-- Init consumers
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (1, 0, '123123123', @service_kvartplata_id,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_1:=1;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (3, 0, '123123123', @service_2,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_2:=3;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (4, 0, '123123123', @service_3,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_3:=4;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (5, 0, '123123123', @service_4,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_4:=5;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (14, 0, '123123123', @service_12,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_12:=14;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (6, 0, '123123123', @service_13,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_13:=6;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (7, 0, '123123123', @service_10,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_10:=7;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (8, 0, '123123123', @service_20,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_20:=8;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (15, 0, '123123123', @service_30,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_30:=15;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (9, 0, '123123123', @service_40,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_40:=9;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (10, 0, '123123123', @service_50,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_50:=10;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (11, 0, '123123123', @service_60,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_60:=11;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (12, 0, '123123123', @service_70,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_70:=12;
insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (13, 0, '123123123', @service_80,
	@person_id, @apartment_ivanova_330_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_1_80:=13;

insert into eirc_consumers_tbl (id, status, external_account_number, service_id,
	person_id, apartment_id, eirc_account_id, begin_date, end_date, consumer_info_id)
	values (2, 0, '67676767', @service_kvartplata_id,
	@person_id, @apartment_ivanova_329_id, @account_id_1, '2000-01-01', '2020-12-31', @consumer_info);
select @consumer_2:=2;

-- Init quittance details
-- Quittance details for consumer_1 (kvartplata)
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (1, @consumer_1_1, @eirc_registry_rec,
	'0.00', '40.34', '40.34', '50.34', '1.1', '-4.0', '-5.0', '-1.0', '0.0', '2007-12-01');
select @quittance_details_1:=1;
-- Quittance details for consumer_1_1 (kvartplata)
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (2, @consumer_1_1, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '123', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_2:=2;
-- Quittance details for consumer_1_10
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (5, @consumer_1_10, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '10.10', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_10:=5;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (6, @consumer_1_2, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '2.0', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_2:=6;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (7, @consumer_1_3, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '3.003', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_3:=7;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (8, @consumer_1_4, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '4.04', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_4:=8;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (9, @consumer_1_12, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '12.012', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_12:=9;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (10, @consumer_1_13, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '13.13', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_13:=10;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (11, @consumer_1_10, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '10.010', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_10:=11;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (12, @consumer_1_20, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '20.20', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_20:=12;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (13, @consumer_1_30, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '30.030', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_30:=13;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (14, @consumer_1_40, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '40.040', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_40:=14;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (15, @consumer_1_50, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '50.050', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_50:=15;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (16, @consumer_1_60, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '60.060', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_60:=16;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (17, @consumer_1_70, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '70.070', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_70:=17;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (18, @consumer_1_80, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '80.80', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_1_80:=18;

insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (3, @consumer_2, @eirc_registry_rec,
	'0.00', '40.34', '40.34', '50.34', '2.02', '-4.0', '-5.0', '-1.0', '0.0', '2007-12-01');
select @quittance_details_3:=3;
insert into eirc_quittance_details_tbl (id, consumer_id, registry_record_id,
	incoming_balance, outgoing_balance, amount, expence, rate, recalculation, benefit, subsidy, payment, month)
	values (4, @consumer_2, @eirc_registry_rec,
	'-10.00', '50.00', '60.00', '60.00', '2.2', '0.0', '0.0', '0.0', '50.34', '2008-01-01');
select @quittance_details_4:=4;


-- Init quittances

-- Unique quittance details
insert into eirc_quittances_tbl (id, service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (1, @service_org_1, @account_id_1, 1, '2007-12-01', '2007-12-31', '2008-01-05');
select @quittance_1:=1;
insert into eirc_quittance_details_quittances_tbl (id, quittance_details_id, quittance_id)
	values (1, @quittance_details_1, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_10, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_20, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_30, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_40, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_50, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_60, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_70, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_80, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_2, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_3, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_4, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_12, @quittance_1);
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1_13, @quittance_1);

-- Quittance with 2 details
insert into eirc_quittances_tbl (id, service_organization_id, eirc_account_id, order_number, date_from, date_till, creation_date)
	values (2, @service_org_1, @account_id_1, 2, '2007-12-01', '2008-01-31', '2008-02-05');
select @quittance_2:=2;
insert into eirc_quittance_details_quittances_tbl (quittance_details_id, quittance_id)
	values (@quittance_details_1, @quittance_2);

insert into eirc_quittance_payment_statuses_tbl (id, version, code, i18n_name)
	values (1, 1, 1, 'eirc.quittance.payment.status.full');
select @payment_status_full:=1;
insert into eirc_quittance_payment_statuses_tbl (id, version, code, i18n_name)
	values (2, 1, 2, 'eirc.quittance.payment.status.partially');
select @payment_status_partial:=2;

-- Quittance packets
insert into eirc_quittance_packets_tbl (id, status, version, packet_number, payment_point_id,
		creation_date, begin_date, close_date, creator_user_name, closer_user_name,
		control_quittances_number, control_overall_sum, quittances_number, overall_sum)
	values (1, 0, 0, 123, @payment_point_1,
		'2009-01-12', '2100-12-31', '2100-12-31', 'test user', '',
		2, 123.45, 1, 12.21);

-- Partial quittance payment
insert into eirc_quittance_payments_tbl (id, version, quittance_id, packet_id, payment_status_id, amount)
	values (1, 0, @quittance_1, null, @payment_status_partial, '100.0');
select @q_payment_1:=1;
insert into eirc_quittance_details_payments_tbl (version, details_id, payment_status_id, payment_id, amount)
	values (0, @quittance_details_1_2, @payment_status_full, @q_payment_1, '50.0');
insert into eirc_quittance_details_payments_tbl (version, details_id, payment_status_id, payment_id, amount)
	values (0, @quittance_details_1_3, @payment_status_partial, @q_payment_1, '20.0');
insert into eirc_quittance_details_payments_tbl (version, details_id, payment_status_id, payment_id, amount)
	values (0, @quittance_details_1_4, @payment_status_partial, @q_payment_1, '30.0');

-- MegaBank integration data
select @eirc_base:=0x5000 + 0;
select @orgs_base:=0x4000 + 0;

-- add correction from Megabank's КП "ЖИЛКОМСЕРВИС"=1033
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	values (@service_provider_cn, '1033', @orgs_base + 0x003, @ds_megabank);

-- Consumer attribute types
insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (2, 0, 0, null, 1, 'enum', null);
select @cons_attr_type_checkbook_color:=2;
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Цвет чековой книжки', @ru_id, @cons_attr_type_checkbook_color);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Красный', 0, @cons_attr_type_checkbook_color);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Оранжевый', 1, @cons_attr_type_checkbook_color);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Желтый', 2, @cons_attr_type_checkbook_color);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Зеленый', 3, @cons_attr_type_checkbook_color);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Голубой', 4, @cons_attr_type_checkbook_color);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Синий', 5, @cons_attr_type_checkbook_color);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Фиолетовый', 6, @cons_attr_type_checkbook_color);

insert into eirc_consumer_attribute_types_tbl (id, version, status, unique_code, is_temporal, discriminator, measure_unit_id)
	values (3, 0, 0, null, 1, 'enum', null);
select @cons_attr_type_rainbow_remainder:=3;
insert into eirc_consumer_attribute_type_names_tbl (name, language_id, attribute_type_id)
	values ('Цвета радуги', @ru_id, @cons_attr_type_rainbow_remainder);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Каждый', 0, @cons_attr_type_rainbow_remainder);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Охотник', 0, @cons_attr_type_rainbow_remainder);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Желает', 1, @cons_attr_type_rainbow_remainder);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Знать', 1, @cons_attr_type_rainbow_remainder);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Где', 1, @cons_attr_type_rainbow_remainder);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Сидит', 1, @cons_attr_type_rainbow_remainder);
insert into eirc_consumer_attribute_type_enum_values_tbl (value_type, string_value, order_value, attribute_type_enum_id)
	values (4, 'Фазан', 1, @cons_attr_type_rainbow_remainder);

-- init consumer attributes
insert into eirc_consumer_attributes_tbl (value_type, string_value, consumer_id, type_id, begin_date, end_date, temporal_flag)
	values (4, '123141', @consumer_1_1, @cons_attr_type_erc_account, '1900-01-01', '2100-12-31', 0);
insert into eirc_consumer_attributes_tbl (value_type, string_value, consumer_id, type_id, begin_date, end_date, temporal_flag)
	values (4, 'Красный', @consumer_1_1, @cons_attr_type_checkbook_color, '1900-01-01', '2000-12-31', 1);
insert into eirc_consumer_attributes_tbl (value_type, string_value, consumer_id, type_id, begin_date, end_date, temporal_flag)
	values (4, 'Синий', @consumer_1_1, @cons_attr_type_checkbook_color, '2001-01-01', '2100-12-31', 1);

-- master index data
select @ds:=id from common_data_source_descriptions_tbl where description='Master-index';
select @instId:='090-';
select @eirc_base:=0x5000 + 0;

insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @eirc_base + 0x0101, @ds from eirc_consumers_tbl);