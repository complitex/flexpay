ALTER TABLE building_attribute_types_tbl ADD COLUMN type INT  AFTER id;
ALTER TABLE building_statuses_tbl DROP COLUMN biulding_id;

update common_version_tbl set last_modified_date='2008-02-18', date_version=0;
