INSERT INTO common_flexpay_modules_tbl (name) VALUES ('bti');
SELECT @module_bti:=last_insert_id();

-- init building attribute groups
insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (1, 0, 0);
select @attribute_group_1:=1;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Другие', @ru_id, @attribute_group_1);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Other', @en_id, @attribute_group_1);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (2, 0, 0);
select @attribute_group_2:=2;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Архитектурные характеристики', @ru_id, @attribute_group_2);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Architectural characteristics', @en_id, @attribute_group_2);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (3, 0, 0);
select @attribute_group_3:=3;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Жильцы', @ru_id, @attribute_group_3);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Tenants', @en_id, @attribute_group_3);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (4, 0, 0);
select @attribute_group_4:=4;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Отопление', @ru_id, @attribute_group_4);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Heating', @en_id, @attribute_group_4);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (5, 0, 0);
select @attribute_group_5:=5;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Водоснабжение', @ru_id, @attribute_group_5);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Water supply', @en_id, @attribute_group_5);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (6, 0, 0);
select @attribute_group_6:=6;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Канализация и водосток', @ru_id, @attribute_group_6);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Sewer and drain', @en_id, @attribute_group_6);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (7, 0, 0);
select @attribute_group_7:=7;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Утилизация бытового мусора', @ru_id, @attribute_group_7);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Recycling', @en_id, @attribute_group_7);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (8, 0, 0);
select @attribute_group_8:=8;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Жилой фонд', @ru_id, @attribute_group_8);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Housing', @en_id, @attribute_group_8);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (9, 0, 0);
select @attribute_group_9:=9;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Вентиляция', @ru_id, @attribute_group_9);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Ventilation', @en_id, @attribute_group_9);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (10, 0, 0);
select @attribute_group_10:=10;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Лифтовое оборудование', @ru_id, @attribute_group_10);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Elevators', @en_id, @attribute_group_10);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (11, 0, 0);
select @attribute_group_11:=11;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Аварийно-диспетчерская служба', @ru_id, @attribute_group_11);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Emergency-dispatcher service', @en_id, @attribute_group_11);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (12, 0, 0);
select @attribute_group_12:=12;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Электрооборудование', @ru_id, @attribute_group_12);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Electrical equipment', @en_id, @attribute_group_12);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (13, 0, 0);
select @attribute_group_13:=13;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Придомовая территория', @ru_id, @attribute_group_13);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('House territory', @en_id, @attribute_group_13);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (14, 0, 0);
select @attribute_group_14:=14;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Тарифы', @ru_id, @attribute_group_14);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Tariffs', @en_id, @attribute_group_14);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (15, 0, 0);
select @attribute_group_15:=15;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Расходы', @ru_id, @attribute_group_15);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Costs', @en_id, @attribute_group_15);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (16, 0, 0);
select @attribute_group_16:=16;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Освещение мест общего пользования', @ru_id, @attribute_group_16);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Common places lighting', @en_id, @attribute_group_16);

insert into bti_building_attribute_type_groups_tbl (id, version, status)
	values (17, 0, 0);
select @attribute_group_17:=17;
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Тарифы', @ru_id, @attribute_group_17);
insert into bti_building_attribute_type_group_names_tbl (name, language_id, group_id)
	values ('Tariff', @en_id, @attribute_group_17);

INSERT INTO `bti_building_attribute_types_tbl` (id, discriminator, group_id, unique_code, is_temporal) VALUES
        (4,'simple',@attribute_group_1,'ATTR_SECTION_NUMBER', 0),
		(5,'simple',@attribute_group_3,'ATTR_HABITANTS_NUMBER', 0),
		(6,'simple',@attribute_group_2,'ATTR_APARTMENTS_TOTAL_SQUARE', 0),
		(7,'simple',@attribute_group_1,'ATTR_BUILD_YEAR', 0),
		(9,'enum',@attribute_group_2,'ATTR_HOUSE_TYPE', 0),
		(10,'simple',@attribute_group_2,'ATTR_FLOORS_NUMBER', 0),
		(11,'simple',@attribute_group_2,'ATTR_DOORWAYS_NUMBER', 0),
		(12,'simple',@attribute_group_2,'ATTR_APARTMENTS_NUMBER', 0),
		(13,'simple',@attribute_group_2,'ATTR_LIVE_SQUARE', 0),
		(14,'simple',@attribute_group_2,'ATTR_TOTAL_SQUARE', 0),
		(15,'simple',@attribute_group_4,'ATTR_BOILER_PLANT_NUMBER', 0),
		(16,'simple',@attribute_group_4,'ATTR_CENTRAL_WARN_NET_LENGTH', 0),
		(17,'simple',@attribute_group_2,'ATTR_BASEMENTS_NUMBER', 0),
		(18,'simple',@attribute_group_4,'ATTR_TOTAL_SQUARE_WITH_CENTRAL_WARM', 0),
		(19,'simple',@attribute_group_5,'ATTR_TOTAL_SQUARE_WITH_CENTRAL_HOT_WATER', 0),
		(20,'simple',@attribute_group_4,'ATTR_TOTAL_SQUARE_WITH_BOILER_PLANTS', 0),
		(21,'simple',@attribute_group_5,'ATTR_TOTAL_SQUARE_WITH_WATER', 0),
		(22,'simple',@attribute_group_6,'ATTR_TOTAL_SQUARE_WITH_SEWER', 0),
		(23,'simple',@attribute_group_7,'ATTR_TOTAL_SQUARE_WITH_REFUSE_CHUTES', 0),
		(24,'simple',@attribute_group_5,'ATTR_TOTAL_SQUARE_WITH_WATER_PUMPS', 0),
		(25,'simple',@attribute_group_11,'ATTR_ADS_SUITED_APARTMENTS_TOTAL_SQUARE', 0),
		(26,'simple',@attribute_group_6,'ATTR_GUTTER_EQUIPPED_TOTAL_SQUARE', 0),
		(27,'simple',@attribute_group_6,'ATTR_TOILETS_WITH_CENTRAL_SEWER_TOTAL_SQUARE', 0),
		(28,'simple',@attribute_group_2,'ATTR_SOFT_ROOF_SQUARE', 0),
		(29,'simple',@attribute_group_2,'ATTR_HARD_SLATE_ROOF_SQUARE', 0),
		(30,'simple',@attribute_group_2,'ATTR_HARD_METAL_ROOF_SQUARE', 0),
		(31,'simple',@attribute_group_2,'ATTR_BASEMENT_SQUARE', 0),
		(32,'simple',@attribute_group_2,'ATTR_TECHNICAL_FLOORS_SQUARE', 0),
		(33,'simple',@attribute_group_2,'ATTR_ARRET_SQUARE', 0),
		(34,'simple',@attribute_group_9,'ATTR_VENT_CHANNEL_NUMBER', 0),
		(35,'simple',@attribute_group_9,'ATTR_FLUES_NUMBER', 0),
		(36,'simple',@attribute_group_2,'ATTR_FIRST_FLOORS_TOTAL_SQUARE', 0),
		(37,'simple',@attribute_group_10,'ATTR_LIFTED_APARTMENTS_TOTAL_SQUARE', 0),
		(38,'simple',@attribute_group_13,'ATTR_NEAR_HOUSE_NONCATEGORY_TERRITORY_TOTAL_SQUARE', 0),
		(39,'simple',@attribute_group_13,'ATTR_NEAR_HOUSE_1ST_CATEGORY_TERRITORY_TOTAL_SQUARE', 0),
		(40,'simple',@attribute_group_13,'ATTR_NEAR_HOUSE_2ND_CATEGORY_TERRITORY_TOTAL_SQUARE', 0),
		(41,'simple',@attribute_group_13,'ATTR_NEAR_HOUSE_3D_CATEGORY_TERRITORY_TOTAL_SQUARE', 0),
		(42,'simple',@attribute_group_13,'ATTR_NEAR_HOUSE_TERRITORY_TOTAL_SQUARE', 0),
		(43,'simple',@attribute_group_6,'ATTR_INHOUSE_SEWER_LENGTH', 0),
		(44,'enum',@attribute_group_6,'ATTR_INHOUSE_SEWER_MATERIAL', 0),
		(45,'enum',@attribute_group_6,'ATTR_INHOUSE_SEWER_SYSTEM_TYPE', 0),
		(46,'simple',@attribute_group_10,'ATTR_LIFTS_TECH_SUPPORT', 0),
		(47,'simple',@attribute_group_11,'ATTR_DISPETCHER_SYSTEMS_TECH_SUPPORT', 0),
		(48,'simple',@attribute_group_16,'ATTR_COMMON_PLACES_LIGHTENING', 0),
        (65,'simple',@attribute_group_2,'ATTR_OPEN_ELECTRIC_WIRING_APARTMENT_NUMBER', 0),
        (66,'simple',@attribute_group_2,'ATTR_OPEN_ELECTRIC_WIRING_APARTMENT_NUMBER_2', 0);

INSERT INTO `bti_building_attribute_type_names_tbl` (id, name, language_id, attribute_type_id) VALUES
		(7,'Номер участка',@ru_id,4),
		(8,'Habitans count',@en_id,5),
		(9,'Количество жителей',@ru_id,5),
		(10,'Overall flat square',@en_id,6),
		(11,'Общая площадь квартир',@ru_id,6),
		(12,'Год постройки',@ru_id,7),
		(14,'Серия, тип дома',@ru_id,9),
		(15,'Количество этажей',@ru_id,10),
		(16,'Количество подъездов',@ru_id,11),
		(17,'Количество квартир',@ru_id,12),
		(18,'Жилая площадь',@ru_id,13),
		(19,'Общая площадь жилого фонда',@ru_id,14),
		(20,'Количество бойлерных установок',@ru_id,15),
		(21,'Протяженность сетей цетрального отопления',@ru_id,16),
		(22,'Количество подвалов',@ru_id,17),
		(23,'Общая площадь жилого фонда, оборудованного центральным отоплением',@ru_id,18),
		(24,'Общая площадь жилого фонда, оборудованного центральным горячим водоснабжением',@ru_id,19),
		(25,'Общая площадь жилого фонда с бойлерными установками',@ru_id,20),
		(26,'Общая площадь жилого фонда, оборудованного водопроводом ',@ru_id,21),
		(27,'Общая площадь жилого фонда, оборудованного канализацией',@ru_id,22),
		(28,'Общая площадь жилого фонда, оборудованного мусоропроводами',@ru_id,23),
		(29,'Общая площадь жилых помещений в домах, где вода подается при помощи насосов подкачки ',@ru_id,24),
		(30,'Общая площадь квартир в домах, оборудованных АДС',@ru_id,25),
		(31,'Общая площадь жилого фонда, оборудованного водостоками',@ru_id,26),
		(32,'Общая площадь жилого фонда, жильцы которого пользуются дворовыми туалетами, подключенными к центральной канализации',@ru_id,27),
		(33,'Площадь мягкой кровли ',@ru_id,28),
		(34,'Площадь шифера',@ru_id,29),
		(35,'Площадь металической кровли',@ru_id,30),
		(36,'Площадь подвалов',@ru_id,31),
		(37,'Площадь технических этажей',@ru_id,32),
		(38,'Площадь чердаков',@ru_id,33),
		(39,'Количество вентиляционных каналов',@ru_id,34),
		(40,'Количество дымоходов',@ru_id,35),
		(41,'Общая площадь первых этажей ',@ru_id,36),
		(42,'Общая площадь квартир, оборудованных лифтами',@ru_id,37),
		(43,'Площади внекатигорийной придомовой территории',@ru_id,38),
		(44,'Площади придомовой территории 1-й категории',@ru_id,39),
		(45,'Площади придомовой территории 2-й категории',@ru_id,40),
		(46,'Площади придомовой территории 3-й категории',@ru_id,41),
		(47,'Общая площади придомовой территории',@ru_id,42),
		(48,'Протяженность внутридомойвой канализации',@ru_id,43),
		(49,'Материал внутридомойвой канализации',@ru_id,44),
		(50,'Тип системы внутридомойвой канализации',@ru_id,45),
		(51,'Технологическое обслуживание лифтов в месяц',@ru_id,46),
		(52,'Диспетчеризация в месяц',@ru_id,47),
		(53,'Освещение мест общего пользования ',@ru_id,48),
		(70,'Количество квартир со скрытой электропроводкой ',@ru_id,65),
		(71,'Количество квартир с открытой электропроводкой ',@ru_id,66);

INSERT INTO `bti_building_attribute_type_enum_values_tbl` (id, order_value, value , attribute_type_enum_id)  VALUES
(7,296,'№163',9),(8,295,'эксперим.',9),(9,294,'эксперем.',9),(10,293,'экспер',9),
(11,292,'часть частного дома',9),(12,291,'смеш.',9),(13,290,'пан',9),(14,289,'к-23',9),(15,288,'к',9),(16,269,'П57-10А',9),(17,268,'П57-03/9',9),
(18,271,'П57011-10А',9),(19,270,'П57-70А/9',9),(20,265,'П57-03',9),(21,264,'П57',9),(22,267,'П57-03/3',9),(23,266,'П57-03/12',9),(24,261,'П120/1-10А',9),
(25,260,'П-5706-10А',9),(26,263,'П500715-10А',9),(27,262,'П500712-10А',9),(28,257,'Лит А-9',9),(29,256,'ИИД',9),(30,259,'П-57-10А9',9),(31,258,'П-57  03/9',9),
(32,284,'дерев.кирп.',9),(33,285,'инд  4Т',9),(34,286,'инд.',9),(35,287,'индивид.',9),(36,280,'ЭКСПЕРИМЕНТ',9),(37,281,'ЮА',9),(38,282,'блок/кн',9),(39,283,'д',9),
(40,276,'С-163',9),(41,277,'С3-162',9),(42,278,'ТП164/80-69/1',9),(43,279,'ХС',9),(44,272,'П57ЮА9',9),(45,273,'ПК-16',9),(46,274,'С- 163',9),(47,275,'С-162',9),
(48,241,'А-2',9),(49,240,'А-16',9),(50,243,'А-4',9),(51,242,'А-3',9),(52,245,'А-55',9),(53,244,'А-5',9),(54,247,'А-7',9),(55,246,'А-6',9),(56,249,'А-9,Б-9,В-9,Г-9',9),
(57,248,'А-9',9),(58,251,'А9',9),(59,250,'А-9163',9),(60,253,'Б-2',9),(61,252,'АС-16',9),(62,255,'ЗС-16',9),(63,254,'Ж/б кирп.',9),(64,224,'І-447 С26/25',9),
(65,225,'І-464 А-15',9),(66,226,'І-464 А-2',9),(67,227,'І-464 А-21',9),(68,228,'І-464 А-4',9),(69,229,'І-464-15',9),(70,230,'І-468',9),(71,231,'ІІ - 57 - 09ЮА',9),
(72,232,'ІІ-57',9),(73,233,'ІІ-57-04-ЮА   А-12',9),(74,234,'ІІ-57/03-12ЮА  А-9',9),(75,235,'ІР447С26/25',9),(76,236,'А',9),(77,237,'А-1',9),(78,238,'А-10',9),
(79,239,'А-12',9),(80,211,'III-162-16/1',9),(81,210,'III-162--16/1',9),(82,209,'III - 162/2',9),(83,208,'II-ПК-16',9),(84,215,'IV-438-A-31K',9),(85,214,'IV-438-31K',9),
(86,213,'III-94',9),(87,212,'III-162-2П',9),(88,219,'IV-483A-33K',9),(89,218,'IV-468-A-3',9),(90,217,'IV-438A-33K',9),(91,216,'IV-438A-31K',9),(92,223,'І-438 У-20',9),
(93,222,'w163',9),(94,221,'a',9),(95,220,'VI-438-31K',9),(96,194,'II-57-09ЮА',9),(97,195,'II-57/10-10A',9),(98,192,'II-57-07/12ЮА',9),(99,193,'II-57-07/ЮА',9),
(100,198,'II-5703-10AM',9),(101,199,'II-5703/12ЮА',9),(102,196,'II-5701-10AM',9),(103,197,'II-5703-01',9),(104,202,'II-5707-10A',9),(105,203,'II-5707/12ЮА',9),
(106,200,'II-57031-01',9),(107,201,'II-5704/12ЮА',9),(108,206,'II-57ЮА',9),(109,207,'II-67-04-ЮА',9),(110,103,'3С-16',9),(111,102,'3С',9),(112,90,'2-157',9),
(113,91,'2-57/10-10A',9),(114,88,'2 ПК-16',9),(115,89,'2 УПК 16',9),(116,94,'20-45',9),(117,95,'2ПК-16',9),(118,92,'2-574/04',9),(119,93,'2-ПК-16',9),(120,82,'1V-468',9),
(121,83,'1V-68а',9),(122,80,'1V-435',9),(123,81,'1V-438',9),(124,86,'2 57/03 1210А',9),(125,87,'2 ПК',9),(126,84,'1У-438А',9),(127,85,'1у-468-55',9),
(128,75,'163-02 II / I',9),(129,74,'163, ком.2А',9),(130,73,'163',9),(131,72,'157',9),(132,79,'1V- 438',9),(133,78,'1V- 433',9),(134,77,'176',9),(135,76,'164-80-01',9),
(136,67,'124-87-151-87',9),(137,66,'124-87',9),(138,65,'1210А',9),(139,64,'12-юа',9),(140,71,'1468 А-55-59/4',9),(141,70,'1468 А -55-59/4',9),(142,69,'1468 А -55',9),
(143,68,'140-м-88',9),(144,60,'11-57/03',9),(145,61,'1157',9),(146,62,'115703',9),(147,63,'115706',9),(148,56,'11-57-06/ЮА   А-9',9),(149,57,'11-57-06ю',9),
(150,58,'11-57-07 ЮА',9),(151,59,'11-57-10',9),(152,52,'11-57-03ю',9),(153,53,'11-57-04',9),(154,54,'11-57-04/ЮА   А-12',9),(155,55,'11-57-04юа',9),(156,48,'11-57-03',9),
(157,49,'11-57-03/12 ЮА',9),(158,24,'1-464-17',9),(159,25,'1-464А - 17',9),(160,22,'1-464 А-4',9),(161,23,'1-464 А2',9),(162,20,'1-447 С-42',9),(163,21,'1-464 А-2',9),
(164,18,'1-438у-9',9),(165,19,'1-447 С-41',9),(166,16,'1-438у-5',9),(167,17,'1-438у-6',9),(168,15,'1-438у',9),(169,14,'1-438У-16',9),(170,1,'0',9),
(171,2,'1 - 468 А-55 59/04',9),(172,3,'1-215-1',9),(173,4,'1-215-а1',9),(174,5,'1-252а-1',9),(175,6,'1-252а-1ш',9),(176,7,'1-253а-7',9),(177,8,'1-300-3',9),
(178,9,'1-438',9),(179,10,'1-438-5',9),(180,11,'1-438-6',9),(181,12,'1-438-6М',9),(182,13,'1-438-9',9),(183,27,'1-468',9),(184,26,'1-464А-17',9),(185,29,'1-468 9/4',9),
(186,28,'1-468 9/14',9),(187,31,'1-468 А-55 59',9),(188,30,'1-468 9/7г',9),(189,34,'1-468-10А',9),(190,35,'1-468-2',9),(191,32,'1-468 А-55 59/06',9),
(192,33,'1-468 А-55,59/06',9),(193,38,'1-468А5559',9),(194,39,'1-468а',9),(195,36,'1-468-5539/04',9),(196,37,'1-468А-55,59/04 А-9',9),(197,42,'1-4Б4А-17',9),
(198,43,'1-574/03',9),(199,40,'1-468а-3',9),(200,41,'1-488-5539/04',9),(201,46,'11-57 12/12',9),(202,47,'11-57 12/7',9),(203,44,'1-646 А-4',9),(204,45,'11-57',9),
(205,51,'11-57-03:12юа',9),(206,50,'11-57-03/12ЮА А-12',9),(207,100,'3C-16',9),(208,101,'3П/1',9),(209,98,'3-С-16',9),(210,99,'3C - 16',9),(211,96,'2пк- 16',9),
(212,97,'3-С',9),(213,110,'468А55/59-04',9),(214,111,'468Э-55',9),(215,108,'468 А-55-59',9),(216,109,'468А-55/59-04 А-9',9),(217,106,'3с-16',9),(218,107,'468',9),
(219,104,'3С-16 2 5703',9),(220,105,'3С16',9),(221,119,'5Т-16',9),(222,118,'5T-16',9),(223,117,'5716',9),(224,116,'57/03 1210А',9),(225,115,'5-Т-16',9),
(226,114,'5-Т',9),(227,113,'49-н-88',9),(228,112,'468Э-59',9),(229,127,'87/094',9),(230,126,'87-094',9),(231,125,'87-093-2',9),(232,124,'87-020',9),
(233,123,'87-019/75',9),(234,122,'87',9),(235,121,'810 12/1,2',9),(236,120,'5т-16',9),(237,137,'I I-5709/10А',9),(238,136,'I I-5707/10А',9),(239,139,'I-447-C-42',9),
(240,138,'I-3C-16',9),(241,141,'I-4476-42',9),(242,140,'I-4476-41',9),(243,143,'I-464-17',9),(244,142,'I-464-15',9),(245,129,'An,A-2',9),(246,128,'94',9),
(247,131,'C - 163',9),(248,130,'An,A-5',9),(249,133,'I - 648A-5559',9),(250,132,'C-163',9),(251,135,'I I-5703/10А',9),(252,134,'I I-57',9),(253,152,'I-464А-17',9),
(254,153,'I-465A-55-59',9),(255,154,'I-468',9),(256,155,'I-468-5538/04',9),(257,156,'I-468-59/4',9),(258,157,'I-468-7-59',9),(259,158,'I-468-A-3',9),
(260,159,'I-468-A-55',9),(261,144,'I-464-A-15',9),(262,145,'I-464-A-17',9),(263,146,'I-464-А-15',9),(264,147,'I-464-А-16',9),(265,148,'I-464-А-17',9),
(266,149,'I-464A-15',9),(267,150,'I-464A-15A',9),(268,151,'I-464A-17',9),(269,171,'II - 57/03',9),(270,170,'I464А-17',9),(271,169,'I4643-17',9),(272,168,'I464-А',9),
(273,175,'II-2ПК-16',9),(274,174,'II 57/03-1210A',9),(275,173,'II 57-03/9ЮА',9),(276,172,'II - ПК-16',9),(277,163,'I-468A-55',9),(278,162,'I-468-F-56',9),
(279,161,'I-468-C-3',9),(280,160,'I-468-A-6',9),(281,167,'I4384-20',9),(282,166,'I-574/03',9),(283,165,'I-57011-A',9),(284,164,'I-468A-55-59',9),(285,186,'II-57-04/12ЮА',9),
(286,187,'II-57-04/9ЮА',9),(287,184,'II-57-03/9ЮА',9),(288,185,'II-57-04/104-9',9),(289,190,'II-57-07/10A',9),(290,191,'II-57-07/12',9),(291,188,'II-57-06/12ЮА',9),
(292,189,'II-57-07-10A',9),(293,178,'II-5109/12ЮА',9),(294,179,'II-5124/12ЮА',9),(295,176,'II-37-07/09ЮА',9),(296,177,'II-468-55/59-04',9),(297,182,'II-57-03,4-9-10А',9),
(298,183,'II-57-03/12ЮА',9),(299,180,'II-57',9),(300,181,'II-57-011/ЮА',9),(301,205,'II-574/04',9),(302,204,'II-5709-10AM',9),
(303,28,'чавунові',44),(304,29,'чугун',44),(305,26,'чавун',44),(306,27,'чавун, пластм.',44),(307,13,'пласм',44),(308,12,'металеві',44),(309,11,'метал',44),(310,10,'залізо',44),(311,9,'відкрита',44),
(312,8,'асбест.',44),(313,7,'асбест',44),(314,6,'99',44),(315,5,'66',44),(316,4,'54',44),(317,3,'5',44),(318,2,'280',44),(319,1,'231',44),(320,14,'пласт',44),
(321,15,'пласт.',44),(322,17,'пластм',44),(323,16,'пластик',44),(324,19,'плат',44),(325,18,'пластмас',44),(326,21,'поастм.',44),(327,20,'плсатм.',44),(328,23,'сталь',44),
(329,22,'сталева',44),(330,25,'чав/плас',44),(331,24,'ч\\п',44),(332,4,'чавун',45),(333,3,'закрытая',45),(334,2,'відкрита',45),(335,1,'135',45);



-- initialize correction for a test attributes of CN building
insert into common_data_corrections_tbl(external_object_id, internal_object_id, object_type, data_source_description_id)
	values ('105471645', @buildings_ivanova_27, 0x07, @source_description_test_data);
