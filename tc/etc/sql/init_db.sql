-- Tariff calculation module init data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('tc');
SELECT @module_tc:=last_insert_id();

-- Init TC file types
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('tc.file_type.tariff_rules', '.*\\u002E(d|D)(r|R)(l|L)', 1, @module_tc);

-- Init TC Tariffs
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "010");
SELECT @tariff_010:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "020");
SELECT @tariff_020:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "030");
SELECT @tariff_030:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "040");
SELECT @tariff_040:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "050");
SELECT @tariff_050:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "060");
SELECT @tariff_060:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "070");
SELECT @tariff_070:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "080");
SELECT @tariff_080:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "090");
SELECT @tariff_090:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "100");
SELECT @tariff_100:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "110");
SELECT @tariff_110:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "120");
SELECT @tariff_120:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "130");
SELECT @tariff_130:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "140");
SELECT @tariff_140:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "150");
SELECT @tariff_150:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "160");
SELECT @tariff_160:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "170");
SELECT @tariff_170:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "180");
SELECT @tariff_180:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "190");
SELECT @tariff_190:=last_insert_id();

INSERT INTO tc_tariff_translations_tbl (tariff_id, language_id, name) VALUES
    (@tariff_010, @ru_id, 'Уборка территории'),
    (@tariff_020, @ru_id, 'Очистка мусоросборников'),
    (@tariff_030, @ru_id, 'Уборка подвалов, тех.этажей, крыш'),
    (@tariff_040, @ru_id, 'Вывоз и утилизация ТБО'),
    (@tariff_050, @ru_id, 'ТО лифтов'),
    (@tariff_060, @ru_id, 'ТО систем диспетчеризации'),
    (@tariff_070, @ru_id, 'ТО систем водоснабжения'),
    (@tariff_080, @ru_id, 'ТО систем водоотведения'),
    (@tariff_090, @ru_id, 'ТО систем теплоснабжения'),
    (@tariff_100, @ru_id, 'ТО систем горячего водоснабжения'),
    (@tariff_110, @ru_id, 'ТО бойлеров'),
    (@tariff_120, @ru_id, 'Обслуживание дымоотв. каналов'),
    (@tariff_130, @ru_id, 'Очистка дворовых туалетов'),
    (@tariff_140, @ru_id, 'Освещение мест общего пользования'),
    (@tariff_150, @ru_id, 'Энергоснабжение для подкачки воды'),
    (@tariff_160, @ru_id, 'Энергоснабжение для лифтов'),
    (@tariff_170, @ru_id, 'Уборка лестничных клеток'),
    (@tariff_180, @ru_id, 'Дератизация и дезинфекция'),
    (@tariff_190, @ru_id, 'ТО бытовых электроплит');

-- Init TC Tariff calculation results for tests
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id) VALUES
    ('170.56', '2008-01-30', '2009-01-30', @building_id_1, @tariff_010),
    ('1235.56', '2008-05-30', '2009-01-30', @building_id_1, @tariff_020),
    ('6773.56', '2008-01-30', '2009-01-30', @building_id_1, @tariff_030),
    ('3142.56', '2008-03-30', '2009-01-30', @building_id_1, @tariff_040),
    ('5670.56', '2008-06-30', '2009-01-30', @building_id_2, @tariff_010),
    ('1360.56', '2008-09-23', '2009-01-30', @building_id_2, @tariff_040),
    ('1457470.56', '2008-01-12', '2009-01-30', @building_id_2, @tariff_080),
    ('16766.56', '2008-01-11', '2009-01-30', @building_id_2, @tariff_120),
    ('15346.56', '2008-01-12', '2009-01-30', @building_id_2, @tariff_130),
    ('167.56', '2008-01-23', '2009-01-30', @building_id_3, @tariff_100),
    ('1232365326.56', '2008-01-11', '2009-01-30', @building_id_3, @tariff_120),
    ('8485.575', '2008-01-11', '2009-01-30', @building_id_3, @tariff_150),
    ('32525.678', '2008-01-11', '2009-01-30', @building_id_3, @tariff_170),
    ('2525.437', '2008-01-06', '2009-01-30', @building_id_4, @tariff_010),
    ('1253.56', '2008-01-12', '2009-01-30', @building_id_4, @tariff_020),
    ('2346.56', '2008-01-07', '2009-01-30', @building_id_4, @tariff_030),
    ('3393.56', '2008-01-09', '2009-01-30', @building_id_4, @tariff_040),
    ('12262.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_050),
    ('23456456.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_060),
    ('132466.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_070),
    ('467436.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_080),
    ('3222.56', '2008-01-10', '2009-01-30', @building_id_4, @tariff_090),
    ('1111.1223', '2008-01-10', '2009-01-30', @building_id_4, @tariff_100),
    ('11251.3257', '2008-01-10', '2009-01-30', @building_id_4, @tariff_110),
    ('34345.56', '2008-05-18', '2009-01-30', @building_id_4, @tariff_120),
    ('345340.56', '2008-05-14', '2009-01-30', @building_id_4, @tariff_130),
    ('245423.56', '2008-05-13', '2009-01-30', @building_id_5, @tariff_140),
    ('1333.56', '2008-06-30', '2009-01-30', @building_id_5, @tariff_150),
    ('25352.56', '2008-06-13', '2009-01-30', @building_id_5, @tariff_160),
    ('456456.56', '2008-06-11', '2009-01-30', @building_id_5, @tariff_170),
    ('29874.56', '2008-06-23', '2009-01-30', @building_id_6, @tariff_180),
    ('4444.56', '2008-06-11', '2009-01-23', @building_id_7, @tariff_040),
    ('83932.56', '2008-01-11', '2009-01-23', @building_id_7, @tariff_060),
    ('9087.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_050),
    ('789170.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_060),
    ('1234560.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_070),
    ('684226.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_180),
    ('8493.56', '2008-01-30', '2009-01-23', @building_id_8, @tariff_190),
    ('937484.56', '2008-01-30', '2009-01-23', @building_id_9, @tariff_020),
    ('123455.56', '2008-01-30', '2009-01-23', @building_id_9, @tariff_030),
    ('23423.56', '2008-01-30', '2009-01-23', @building_id_9, @tariff_040),
    ('67382.56', '2008-01-30', '2009-01-23', @building_id_9, @tariff_050);
