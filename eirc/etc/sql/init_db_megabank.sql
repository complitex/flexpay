-- MegaBank integration data (just 0x5000 does not work, added +0)
select @eirc_base:=0x5000 + 0;
select @orgs_base:=0x4000 + 0;
select @payments_base:=0x3000 + 0;
select @ds_megabank:=20481;	

-- add correction from Megabank's КП "ЖИЛКОМСЕРВИС"=1033
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	values (1, '1033', @orgs_base + 0x003, @ds_megabank);
