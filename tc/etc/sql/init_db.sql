-- Tarif calculation module init data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('tc');
SELECT @module_tc:=last_insert_id();

-- Init TC file types
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('tc.file_type.tariff_rules', '.{8}\\u002E(d|D)(r|R)(l|L)', 1, @module_tc);

-- Init TC file statuses
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('tc.file_status.importing', 0, @module_tc);
SELECT @tc_importing_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('tc.file_status.imported', 1, @module_tc);
SELECT @tc_imported_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('tc.file_status.deleting', 2, @module_tc);
SELECT @tc_deleting_status_id:=last_insert_id();
