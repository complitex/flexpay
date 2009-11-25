delete from ab_building_addresses_tbl where building_id<58;
delete from ab_buildings_tbl where id < 58;
delete from common_data_corrections_tbl
	where object_type in (0x1007, 0x100A) and internal_object_id < 58; 
