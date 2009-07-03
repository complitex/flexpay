-- TODO setup needed registry id
select @id:=6 from dual;

-- delete quittance quittance details
--delete qq
--	from eirc_registries_tbl r
--		left join eirc_registry_records_tbl rr on r.id=rr.registry_id
--		left join eirc_quittance_details_tbl q on rr.id=q.registry_record_id
--		left join eirc_quittance_details_quittances_tbl qq on q.id=qq.quittance_details_id
--	where r.id=@id;

-- delete quittances
--delete q
--	from eirc_quittances_tbl q
--		left outer join eirc_quittance_details_quittances_tbl qq on q.id=qq.quittance_id
--	where qq.id is null;

-- delete quittance details
--delete q
--	from eirc_registries_tbl r
--		left join eirc_registry_records_tbl rr on r.id=rr.registry_id
--		left join eirc_quittance_details_tbl q on rr.id=q.registry_record_id
--	where r.id=@id;

-- delete record containers
delete c
	from common_registries_tbl r
		left join common_registry_records_tbl rr on r.id=rr.registry_id
		left join common_registry_record_containers_tbl c on rr.id=c.record_id
	where r.id=@id;

-- delete record properties
delete p
	from common_registries_tbl r
		left join common_registry_records_tbl rr on r.id=rr.registry_id
		left join common_registry_record_properties_tbl p on rr.id=p.record_id
	where r.id=@id;

-- delete registry records
delete from common_registry_records_tbl where registry_id=@id;

-- delete registry containers
delete c
	from common_registries_tbl r
		left join common_registry_containers_tbl c on r.id=c.registry_id
	where r.id=@id;

-- delete registry properties
delete p
	from common_registries_tbl r
		left join common_registry_properties_tbl p on r.id=p.registry_id
	where r.id=@id;

-- delete registry
delete r
	from common_registries_tbl r
	where r.id=@id;
