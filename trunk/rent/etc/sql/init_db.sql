-- put here module initialization data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('rent');
SELECT @module_rent:=last_insert_id();
