select @ru_id :=1;
select @en_id :=2;

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (17, 0, 0);
select @attribute_group_17:=17;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Тарифы', @ru_id, @attribute_group_17);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Tariff', @en_id, @attribute_group_17);

INSERT INTO `bti_building_attribute_types_tbl` (id, discriminator, group_id, unique_code, is_temporal)
VALUES (49,'simple',@attribute_group_17,'ATTR_NEARHOUSE_TERRITORY_CLEANUP_TARIF', 1),
(50,'simple',@attribute_group_17,'ATTR_RUBBISH_CHUTE_CLEANUP_TARIF', 1),
(51,'simple',@attribute_group_17,'ATTR_BASEMENTS_CLEANUP_TARIF', 1),
(52,'simple',@attribute_group_17,'ATTR_BIG_GARBAGE_REMOVAL_TARIF', 1),
(53,'simple',@attribute_group_17,'ATTR_LIFT_SUPPORT_TARIF', 1),
(54,'simple',@attribute_group_17,'ATTR_DISPETCHER_SYSTEMS_SUPPORT_TARIF', 1),
(55,'simple',@attribute_group_17,'ATTR_WATER_SUPPLY_NETS_SUPPORT_TARIF', 1),
(56,'simple',@attribute_group_17,'ATTR_WATER_REMOVAL_NETS_SUPPORT_TARIF', 1),
(57,'simple',@attribute_group_17,'ATTR_WARM_SUPPLY_NETS_SUPPORT_TARIF', 1),
(58,'simple',@attribute_group_17,'ATTR_HOT_WATER_SUPPLY_NETS_SUPPORT_TARIF', 1),
(59,'simple',@attribute_group_17,'ATTR_BOILERS_SUPPORT_TARIF', 1),
(60,'simple',@attribute_group_17,'ATTR_VENT_CHANNEL_SUPPORT_TARIF', 1),
(61,'simple',@attribute_group_17,'ATTR_COURT_TOILETS_CLEANUP_TARIF', 1),
(62,'simple',@attribute_group_17,'ATTR_COMMON_PLACES_LIGHTENING_TARIF', 1),
(63,'simple',@attribute_group_17,'ATTR_WATER_PUMPS_ELECTRICITY_TARIF', 1),
(64,'simple',@attribute_group_17,'ATTR_LIFT_ENERGYSAVE_TARIF', 1);


INSERT INTO `bti_building_attribute_type_names_tbl` (id, name, language_id, attribute_type_id)
VALUES (54,'Тариф за уборку придомовой территории ',@ru_id,49),
(55,'Тариф за очистку мусоропроводов и загрузку контейнеров ',@ru_id,50),
(56,'Тариф за уборку подвалов, тех.этажей и кровель ',@ru_id,51),
(57,'Тариф за вывоз и утилизацию твердых бытовых и негабаритных отходов ',@ru_id,52),
(58,'Тариф за тех.обслуживание лифтов ',@ru_id,53),
(59,'Тариф за обслуживание систем диспетчеризации ',@ru_id,54),
(60,'Тариф за тех.обслуживание внутридомовых сетей водоснабжения ',@ru_id,55),
(61,'Тариф за тех.обслуживание внутридомовых сетей водоотведения ',@ru_id,56),
(62,'Тариф за тех.обслуживание внутридомовых сетей теплоснабжения ',@ru_id,57),
(63,'Тариф за тех.обслуживание внутридомовых сетей горячего водоснабжения ',@ru_id,58),
(64,'Тариф за тех.обслуживание бойлеров ',@ru_id,59),
(65,'Тариф за обслуживание дымовентиляционных каналов ',@ru_id,60),
(66,'Тариф за очистку дворовых туалетов ',@ru_id,61),
(67,'тариф за освещение мест общего пользования ',@ru_id,62),
(68,'Тариф за энергоснабжение насосов подкачки воды ',@ru_id,63),
(69,'Тариф за энергоснабжение лифтов ',@ru_id,64);
