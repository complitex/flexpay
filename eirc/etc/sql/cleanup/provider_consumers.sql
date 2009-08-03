select @provider_id:=;
select @ds_id:=data_source_description_id from orgs_service_providers_tbl where id=@provider_id;

delete a
from eirc_consumer_attributes_tbl a
	left outer join eirc_consumers_tbl c on a.consumer_id=c.id
	left outer join payments_services_tbl s on c.service_id=s.id
where s.provider_id=@provider_id;

delete c
from eirc_consumers_tbl c
	left outer join payments_services_tbl s on c.service_id=s.id
where s.provider_id=@provider_id;

delete from common_data_corrections_tbl where data_source_description_id=@ds_id;

-- remove eirc accounts where there is no consumers
delete a
from eirc_eirc_accounts_tbl a
	left outer join eirc_consumers_tbl c on c.eirc_account_id=a.id
where c.id is null;
