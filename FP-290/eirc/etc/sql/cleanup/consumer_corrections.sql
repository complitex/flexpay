delete from common_data_corrections_tbl
where object_type=0x0101 and
	not exists (select c.id from eirc_consumers_tbl c where c.id=internal_object_id);
