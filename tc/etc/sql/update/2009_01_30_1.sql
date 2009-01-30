
ALTER TABLE tc_tariff_tbl MODIFY COLUMN subservice_code varchar(255) NOT NULL COMMENT 'Subservice code';

-- Init TC Tariffs
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 10);
SELECT @tariff_10:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Уборка территории', @tariff_10, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 20);
SELECT @tariff_20:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Очистка мусоросборников', @tariff_20, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 30);
SELECT @tariff_30:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Уборка подвалов, тех.этажей, крыш', @tariff_30, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 40);
SELECT @tariff_40:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Вывоз и утилизация ТБО', @tariff_40, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 50);
SELECT @tariff_50:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО лифтов', @tariff_50, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 60);
SELECT @tariff_60:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем диспетчеризации', @tariff_60, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 70);
SELECT @tariff_70:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем водоснабжения', @tariff_70, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 80);
SELECT @tariff_80:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем водоотведения', @tariff_80, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 90);
SELECT @tariff_90:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем теплоснабжения', @tariff_90, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 100);
SELECT @tariff_100:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО систем горячего водоснабжения', @tariff_100, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 110);
SELECT @tariff_110:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО бойлеров', @tariff_110, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 120);
SELECT @tariff_120:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Обслуживание дымоотв. каналов', @tariff_120, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 130);
SELECT @tariff_130:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Очистка дворовых туалетов', @tariff_130, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 140);
SELECT @tariff_140:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Освещение мест общего пльзования', @tariff_140, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 150);
SELECT @tariff_150:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Энергоснабж. для подкачки воды', @tariff_150, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 160);
SELECT @tariff_160:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Энергоснабжение для лифтов', @tariff_160, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 170);
SELECT @tariff_170:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Уборка лестничных клеток', @tariff_170, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 180);
SELECT @tariff_180:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('Дератизация и дезинфекция', @tariff_180, 1);

INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, 190);
SELECT @tariff_190:=last_insert_id();
INSERT INTO tc_tariff_translations_tbl (name, tariff_id, language_id)
    VALUES ('ТО бытовых электроплит', @tariff_190, 1);

-- Init TC Tariff calculation results for tests
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('170.56', '2008-01-30', '2009-01-30', 45, 1);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1235.56', '2008-05-30', '2009-01-30', 45, 2);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('6773.56', '2008-01-30', '2009-01-30', 45, 3);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('3142.56', '2008-03-30', '2009-01-30', 45, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('5670.56', '2008-06-30', '2009-01-30', 567, 1);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1360.56', '2008-09-23', '2009-01-30', 567, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1457470.56', '2008-01-12', '2009-01-30', 567, 8);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('16766.56', '2008-01-11', '2009-01-30', 567, 12);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('15346.56', '2008-01-12', '2009-01-30', 567, 13);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('167.56', '2008-01-23', '2009-01-30', 234, 10);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1232365326.56', '2008-01-11', '2009-01-30', 234, 12);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('8485.575', '2008-01-11', '2009-01-30', 234, 15);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('32525.678', '2008-01-11', '2009-01-30', 234, 17);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('2525.437', '2008-01-06', '2009-01-30', 1, 1);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1253.56', '2008-01-12', '2009-01-30', 1, 2);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('2346.56', '2008-01-07', '2009-01-30', 1, 3);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('3393.56', '2008-01-09', '2009-01-30', 1, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('12262.56', '2008-01-10', '2009-01-30', 1, 5);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('23456456.56', '2008-01-10', '2009-01-30', 1, 6);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('132466.56', '2008-01-10', '2009-01-30', 1, 7);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('467436.56', '2008-01-10', '2009-01-30', 1, 8);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('3222.56', '2008-01-10', '2009-01-30', 1, 9);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1111.1223', '2008-01-10', '2009-01-30', 1, 10);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('11251.3257', '2008-01-10', '2009-01-30', 1, 11);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('34345.56', '2008-05-18', '2009-01-30', 1, 12);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('345340.56', '2008-05-14', '2009-01-30', 1, 13);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('245423.56', '2008-05-13', '2009-01-30', 23, 14);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1333.56', '2008-06-30', '2009-01-30', 23, 15);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('25352.56', '2008-06-13', '2009-01-30', 23, 16);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('456456.56', '2008-06-11', '2009-01-30', 23, 17);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('29874.56', '2008-06-23', '2009-01-30', 78, 18);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('4444.56', '2008-06-11', '2009-01-23', 999, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('83932.56', '2008-01-11', '2009-01-23', 999, 6);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('9087.56', '2008-01-30', '2009-01-23', 2356, 5);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('789170.56', '2008-01-30', '2009-01-23', 2356, 6);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('1234560.56', '2008-01-30', '2009-01-23', 2356, 7);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('684226.56', '2008-01-30', '2009-01-23', 2356, 18);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('8493.56', '2008-01-30', '2009-01-23', 2356, 19);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('937484.56', '2008-01-30', '2009-01-23', 99, 2);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('123455.56', '2008-01-30', '2009-01-23', 99, 3);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('23423.56', '2008-01-30', '2009-01-23', 99, 4);
INSERT INTO tc_tariff_calculation_result_tbl (value, creation_date, calculation_date, building_id, tariff_id)
    VALUES ('67382.56', '2008-01-30', '2009-01-23', 99, 5);

update common_version_tbl set last_modified_date='2009-01-30', date_version=1;
