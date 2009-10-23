-- put here module initialization data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('${module_name}');
SELECT @module_${module_name}:=last_insert_id();
