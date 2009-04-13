INSERT INTO common_flexpay_modules_tbl (name) VALUES ('eirc');
SELECT @module_eirc:=last_insert_id();

-- Init Sequences table
INSERT INTO common_sequences_tbl (id, counter, description) VALUES (1, 10, 'Последовательность для ЛС модуля ЕИРЦ');

