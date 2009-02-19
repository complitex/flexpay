select @attribute_group_17:=17;

delete from bti_building_attribute_temp_values_tbl where attribute_id in (select id from bti_building_attribute_types_tbl where group_id = @attribute_group_17);

delete from bti_building_attribute_type_names_tbl where attribute_type_id in (select id from bti_building_attribute_types_tbl where group_id = @attribute_group_17);

delete FROM bti_building_attribute_temp_values_tbl where attribute_id in (select a.id from bti_building_attribute_types_tbl at, bti_building_attributes_tbl a  where at.group_id = 17 and a.attribute_type_id = at.id);

delete FROM bti_building_attributes_tbl where attribute_type_id in (select id from bti_building_attribute_types_tbl where group_id = @attribute_group_17);

delete from bti_building_attribute_type_group_names_tbl where group_id =@attribute_group_17;

delete from bti_building_attribute_types_tbl where group_id = @attribute_group_17;

delete from bti_building_attribute_type_groups_tbl where id =@attribute_group_17;