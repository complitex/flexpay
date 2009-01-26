-- Tarif calculation module init data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('tc');
SELECT @module_tc:=last_insert_id();

-- Init TC file types
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('tc.file_type.tariff_rules', '.*\\u002E(d|D)(r|R)(l|L)', 1, @module_tc);
