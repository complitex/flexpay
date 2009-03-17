-- Tariff calculation module init data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('tc');
SELECT @module_tc:=last_insert_id();

-- Init TC file types
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('tc.file_type.tariff_rules', '.*\\u002E(d|D)(r|R)(l|L)', 1, @module_tc);

-- Init TC Tariffs
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '010');
SELECT @tariff_010:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '020');
SELECT @tariff_020:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '030');
SELECT @tariff_030:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '040');
SELECT @tariff_040:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '050');
SELECT @tariff_050:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '060');
SELECT @tariff_060:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '070');
SELECT @tariff_070:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '080');
SELECT @tariff_080:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '090');
SELECT @tariff_090:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '100');
SELECT @tariff_100:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '110');
SELECT @tariff_110:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '120');
SELECT @tariff_120:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '130');
SELECT @tariff_130:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '140');
SELECT @tariff_140:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '150');
SELECT @tariff_150:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '160');
SELECT @tariff_160:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '170');
SELECT @tariff_170:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '180');
SELECT @tariff_180:=last_insert_id();
INSERT INTO tc_tariff_tbl (status, subservice_code) VALUES (0, '190');
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

INSERT INTO tc_tariff_export_code_tbl (id, code) values
        (1,0),(2,-1),(3,-2),(4,-3),
        (5,-4),(6,-5),(7,-100),(8,1) ;
