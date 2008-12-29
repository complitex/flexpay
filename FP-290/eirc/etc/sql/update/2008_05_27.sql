ALTER TABLE eirc_service_providers_tbl ADD COLUMN status INTEGER NOT NULL DEFAULT 0;
ALTER TABLE eirc_service_providers_tbl DROP COLUMN provider_number;
