-- Tarif calculation module init data
INSERT INTO common_flexpay_modules_tbl (name) VALUES ('tc');
SELECT @module_bti:=last_insert_id();
