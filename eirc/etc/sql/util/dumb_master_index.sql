-- create master indexes without actual history generation

select @ds:=id from common_data_source_descriptions_tbl where description='Master-index';
select @common_base:=0x6000 + 0;
select @ab_base:=0x1000 + 0;
select @bti_base:=0x2000 + 0;
select @eirc_base:=0x5000 + 0;
select @orgs_base:=0x4000 + 0;
select @payments_base:=0x3000 + 0;
select @instId:='090-';

-- common objects
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @common_base + 0x09, @ds from common_measure_units_tbl);

-- ab objects
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x09, @ds from ab_persons_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x08, @ds from ab_apartments_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x07, @ds from ab_building_addresses_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x0A, @ds from ab_buildings_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x0B, @ds from ab_building_address_attribute_types_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x06, @ds from ab_streets_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x05, @ds from ab_districts_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x04, @ds from ab_street_types_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x03, @ds from ab_towns_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x10, @ds from ab_town_types_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x02, @ds from ab_regions_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x01, @ds from ab_countries_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @ab_base + 0x0C, @ds from ab_identity_types_tbl);

-- bti objects
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @bti_base + 0x001, @ds from bti_building_attribute_type_groups_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @bti_base + 0x002, @ds from bti_building_attribute_types_tbl);

-- orgs objects
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x001, @ds from orgs_organizations_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x002, @ds from orgs_banks_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x003, @ds from orgs_service_providers_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x004, @ds from orgs_service_organizations_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x005, @ds from orgs_payment_collectors_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x006, @ds from orgs_payment_points_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x007, @ds from orgs_subdivisions_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @orgs_base + 0x008, @ds from orgs_cashboxes_tbl);

-- payments objects
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @payments_base + 0x0201, @ds from payments_services_tbl);
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @payments_base + 0x002, @ds from payments_service_types_tbl);

-- eirc objects
insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @eirc_base + 0x0101, @ds from eirc_consumers_tbl);
