ALTER TABLE eirc_sp_registry_types_tbl DROP COLUMN name,
 DROP COLUMN direction,
 CHANGE COLUMN type_enum_id code INTEGER NOT NULL;