-- Tarif calculation module init data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('tc');
SELECT @module_tc:=last_insert_id();

-- Init TC file types
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('tc.file_type.tariff_rules', '.*\\u002E(d|D)(r|R)(l|L)', 1, @module_tc);

-- Init TC Tariffs
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "010");
SELECT @tariff_10:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Уборка территории', @tariff_10, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "020");
SELECT @tariff_20:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Очистка мусоросборников', @tariff_20, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "030");
SELECT @tariff_30:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Уборка подвалов, тех.этажей, крыш', @tariff_30, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "040");
SELECT @tariff_40:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Вывоз и утилизация ТБО', @tariff_40, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "050");
SELECT @tariff_50:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО лифтов', @tariff_50, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "060");
SELECT @tariff_60:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем диспетчеризации', @tariff_60, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "070");
SELECT @tariff_70:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем водоснабжения', @tariff_70, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "080");
SELECT @tariff_80:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем водоотведения', @tariff_80, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "090");
SELECT @tariff_90:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем теплоснабжения', @tariff_90, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "100");
SELECT @tariff_100:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем горячего водоснабжения', @tariff_100, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "110");
SELECT @tariff_110:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО бойлеров', @tariff_110, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "120");
SELECT @tariff_120:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Обслуживание дымоотв. каналов', @tariff_120, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "130");
SELECT @tariff_130:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Очистка дворовых туалетов', @tariff_130, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "140");
SELECT @tariff_140:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Освещение мест общего пльзования', @tariff_140, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "150");
SELECT @tariff_150:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Энергоснабж. для подкачки воды', @tariff_150, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "160");
SELECT @tariff_160:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Энергоснабжение для лифтов', @tariff_160, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "170");
SELECT @tariff_170:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Уборка лестничных клеток', @tariff_170, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "180");
SELECT @tariff_180:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Дератизация и дезинфекция', @tariff_180, @ru_id);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, "190");
SELECT @tariff_190:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО бытовых электроплит', @tariff_190, @ru_id);

-- Init TC Tariff calculation results for tests
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('170.56', '2008-01-30', '2009-01-30', @building_id_1, 1);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1235.56', '2008-05-30', '2009-01-30', @building_id_1, 2);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('6773.56', '2008-01-30', '2009-01-30', @building_id_1, 3);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('3142.56', '2008-03-30', '2009-01-30', @building_id_1, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('5670.56', '2008-06-30', '2009-01-30', @building_id_2, 1);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1360.56', '2008-09-23', '2009-01-30', @building_id_2, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1457470.56', '2008-01-12', '2009-01-30', @building_id_2, 8);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('16766.56', '2008-01-11', '2009-01-30', @building_id_2, 12);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('15346.56', '2008-01-12', '2009-01-30', @building_id_2, 13);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('167.56', '2008-01-23', '2009-01-30', @building_id_3, 10);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1232365326.56', '2008-01-11', '2009-01-30', @building_id_3, 12);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('8485.575', '2008-01-11', '2009-01-30', @building_id_3, 15);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('32525.678', '2008-01-11', '2009-01-30', @building_id_3, 17);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('2525.437', '2008-01-06', '2009-01-30', @building_id_4, 1);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1253.56', '2008-01-12', '2009-01-30', @building_id_4, 2);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('2346.56', '2008-01-07', '2009-01-30', @building_id_4, 3);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('3393.56', '2008-01-09', '2009-01-30', @building_id_4, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('12262.56', '2008-01-10', '2009-01-30', @building_id_4, 5);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('23456456.56', '2008-01-10', '2009-01-30', @building_id_4, 6);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('132466.56', '2008-01-10', '2009-01-30', @building_id_4, 7);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('467436.56', '2008-01-10', '2009-01-30', @building_id_4, 8);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('3222.56', '2008-01-10', '2009-01-30', @building_id_4, 9);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1111.1223', '2008-01-10', '2009-01-30', @building_id_4, 10);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('11251.3257', '2008-01-10', '2009-01-30', @building_id_4, 11);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('34345.56', '2008-05-18', '2009-01-30', @building_id_4, 12);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('345340.56', '2008-05-14', '2009-01-30', @building_id_4, 13);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('245423.56', '2008-05-13', '2009-01-30', @building_id_5, 14);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1333.56', '2008-06-30', '2009-01-30', @building_id_5, 15);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('25352.56', '2008-06-13', '2009-01-30', @building_id_5, 16);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('456456.56', '2008-06-11', '2009-01-30', @building_id_5, 17);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('29874.56', '2008-06-23', '2009-01-30', @building_id_6, 18);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('4444.56', '2008-06-11', '2009-01-23', @building_id_7, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('83932.56', '2008-01-11', '2009-01-23', @building_id_7, 6);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('9087.56', '2008-01-30', '2009-01-23', @building_id_8, 5);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('789170.56', '2008-01-30', '2009-01-23', @building_id_8, 6);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1234560.56', '2008-01-30', '2009-01-23', @building_id_8, 7);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('684226.56', '2008-01-30', '2009-01-23', @building_id_8, 18);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('8493.56', '2008-01-30', '2009-01-23', @building_id_8, 19);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('937484.56', '2008-01-30', '2009-01-23', @building_id_9, 2);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('123455.56', '2008-01-30', '2009-01-23', @building_id_9, 3);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('23423.56', '2008-01-30', '2009-01-23', @building_id_9, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('67382.56', '2008-01-30', '2009-01-23', @building_id_9, 5);
