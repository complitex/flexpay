alter table bti_apartment_attribute_temp_values_tbl ENGINE = InnoDB;
alter table bti_apartment_attribute_type_enum_values_tbl ENGINE = InnoDB;
alter table bti_apartment_attribute_type_group_names_tbl ENGINE = InnoDB;
alter table bti_apartment_attribute_type_groups_tbl ENGINE = InnoDB;
alter table bti_apartment_attribute_type_names_tbl ENGINE = InnoDB;
alter table bti_apartment_attribute_types_tbl ENGINE = InnoDB;
alter table bti_apartment_attributes_tbl ENGINE = InnoDB;
drop table if exists bti_building_temp_attributes_tbl ;


update common_version_tbl set last_modified_date='2009-05-25', date_version=1;
