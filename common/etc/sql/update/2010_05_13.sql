INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 14);
SELECT @registry_status_processed_import_consumer:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 15);
SELECT @registry_status_processed_import_consumer_with_error:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 16);
SELECT @registry_status_processing_import_consumer:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 17);
SELECT @registry_status_processing_import_consumer_with_error:=last_insert_id();

INSERT INTO common_registry_statuses_tbl (version, code) VALUES (0, 18);
SELECT @registry_status_start_processing:=last_insert_id();

INSERT INTO common_version_tbl (last_modified_date, date_version) VALUES ('2010-05-13', 0);
