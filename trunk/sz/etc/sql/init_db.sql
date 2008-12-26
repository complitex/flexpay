-- Init Sz file module

INSERT INTO common_flexpay_modules_tbl (name) VALUES ('sz');
SELECT @module_sz:=last_insert_id();

-- Init Sz file types
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл тарифов', '(t|T)(a|A)(R|R)(i|I)(f|F)\\u002E(d|D)(b|B)(f|F)', 1, @module_sz);
SELECT @tariff_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл запроса характеристик жилья', '\\d{8}\\u002E(a|A)\\d{2}', 2, @module_sz);
SELECT @characteristics_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл запроса жилищно-коммунальных услуг и услуг связи', '\\d{8}\\u002E(b|B)\\d{2}', 3, @module_sz);
SELECT @srv_types_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Форма «2-льгота»', '\\d{8}\\u002E(e|E)\\d{2}', 4, @module_sz);
SELECT @form2_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('sz.file_type.characteristics_response', '\\d{8}\\u002E(c|C)\\d{2}', 5, @module_sz);
SELECT @characteristics_response_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('sz.file_type.srv_types_response', '\\d{8}\\u002E(d|D)\\d{2}', 6, @module_sz);
SELECT @srv_types_response_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл запроса субсидий', '.*\\d{4}\\u002E(d|D)(b|B)(f|F)', 7, @module_sz);
SELECT @subsidy_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл запроса субсидий', '\\d{8}\\u002E(a|A)\\d{2}', 8, @module_sz);
SELECT @subsidy_type_id2:=last_insert_id();

-- Init Sz file statuses
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Импортирован в систему', 1, @module_sz);
SELECT @imported_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Помечен на обработку', 2, @module_sz);
SELECT @marked_for_processing_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Обрабатывается', 3, @module_sz);
SELECT @processing_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Обработан без ошибок и замечаний', 4, @module_sz);
SELECT @processed_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Обработан с ошибками (замечаниями)', 5, @module_sz);
SELECT @processed_with_warnings_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Помечен на удаление', 6, @module_sz);
SELECT @marked_as_deleted_status_id:=last_insert_id();

-- Init OSZN orgs
insert into sz_oszns_tbl (description, district_id) values ('Заельцовский', 1);
insert into sz_oszns_tbl (description, district_id) values ('Дзержинский', 2);
insert into sz_oszns_tbl (description, district_id) values ('Железнодорожный', 3);
insert into sz_oszns_tbl (description, district_id) values ('Калининский', 4);
insert into sz_oszns_tbl (description, district_id) values ('Кировский', 5);
insert into sz_oszns_tbl (description, district_id) values ('Лениниский', 6);
insert into sz_oszns_tbl (description, district_id) values ('Октябрьский', 7);
insert into sz_oszns_tbl (description, district_id) values ('Первомайский', 8);
insert into sz_oszns_tbl (description, district_id) values ('Советский', 9);
insert into sz_oszns_tbl (description, district_id) values ('Центральный', 10);
insert into sz_oszns_tbl (description, district_id) values ('Дёмский', 11);
insert into sz_oszns_tbl (description, district_id) values ('Калиниский', 12);
insert into sz_oszns_tbl (description, district_id) values ('Кировский', 13);
insert into sz_oszns_tbl (description, district_id) values ('Лениниский', 14);
insert into sz_oszns_tbl (description, district_id) values ('Октябрьский', 15);
insert into sz_oszns_tbl (description, district_id) values ('Орджоникидзевский', 16);
insert into sz_oszns_tbl (description, district_id) values ('Советский', 17);
