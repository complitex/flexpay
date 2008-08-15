-- Init Sz file types
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (1, '(t|T)(a|A)(R|R)(i|I)(f|F)\\u002E(d|D)(b|B)(f|F)', 'sz.file_type.tarif');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (2, '\\d{8}\\u002E(a|A)\\d{2}', 'sz.file_type.characteristics');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (3, '\\d{8}\\u002E(b|B)\\d{2}', 'sz.file_type.srv_types');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (4, '\\d{8}\\u002E(e|E)\\d{2}', 'sz.file_type.form2');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (5, '\\d{8}\\u002E(c|C)\\d{2}', 'sz.file_type.characteristics_response');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (6,'\\d{8}\\u002E(d|D)\\d{2}', 'sz.file_type.srv_types_response');
INSERT INTO sz_file_types_tbl (id, file_mask, description)
	VALUES (7,'.*\\d{4}\\u002E(d|D)(b|B)(f|F)', 'sz.file_type.subsidy');
INSERT INTO sz_file_types_tbl (file_mask, description)
	VALUES ('\\d{8}\\u002E(a|A)\\d{2}', 'sz.file_type.subsidy');

-- Init Sz file status
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (1, 'sz.file_status.imported');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (2, 'sz.file_status.marked_for_processing');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (3, 'sz.file_status.processing');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (4, 'sz.file_status.processed');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (5, 'sz.file_status.processed_with_warnings');
INSERT INTO sz_file_status_tbl (id, description)
	VALUES (6, 'sz.file_status.marked_as_deleted');

-- Init Sz file actuality status
INSERT INTO sz_file_actuality_status_tbl (id, description)
	VALUES (1, 'sz.file_actuality_status.not_actualy');
INSERT INTO sz_file_actuality_status_tbl (id, description)
	VALUES (2, 'sz.file_actuality_status.actualy');

-- Init OSZN orgs
insert into oszn_tbl (id, district_id,description) values (1, 1,'Заельцовский');
insert into oszn_tbl (id, district_id,description) values (2, 2,'Дзержинский');
insert into oszn_tbl (id, description, district_id) values (3, 'Железнодорожный', 3);
insert into oszn_tbl (id, description, district_id) values (4, 'Калининский', 4);
insert into oszn_tbl (id, description, district_id) values (5, 'Кировский', 5);
insert into oszn_tbl (id, description, district_id) values (6, 'Лениниский', 6);
insert into oszn_tbl (id, description, district_id) values (7, 'Октябрьский', 7);
insert into oszn_tbl (id, description, district_id) values (8, 'Первомайский', 8);
insert into oszn_tbl (id, description, district_id) values (9, 'Советский', 9);
insert into oszn_tbl (id, description, district_id) values (10, 'Центральный', 10);
insert into oszn_tbl (id, description, district_id) values (11, 'Дёмский', 11);
insert into oszn_tbl (id, description, district_id) values (12, 'Калиниский', 12);
insert into oszn_tbl (id, description, district_id) values (13, 'Кировский', 13);
insert into oszn_tbl (id, description, district_id) values (14, 'Лениниский', 14);
insert into oszn_tbl (id, description, district_id) values (15, 'Октябрьский', 15);
insert into oszn_tbl (id, description, district_id) values (16, 'Орджоникидзевский', 16);
insert into oszn_tbl (id, description, district_id) values (17, 'Советский', 17);
