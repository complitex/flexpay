alter table bti_apartment_attribute_temp_values_tbl 
	add constraint FK_bti_apartment_attribute_temp_values_tbl_attr_id
	foreign key (attribute_id)
	references bti_apartment_attributes_tbl (id);

alter table bti_apartment_attribute_type_enum_values_tbl
	add constraint bti_apartment_attribute_type_enum_values_tbl_enum_id
	foreign key (attribute_type_enum_id)
	references bti_apartment_attribute_types_tbl (id);

alter table bti_apartment_attribute_type_group_names_tbl
	add constraint FK_bti_apartment_attribute_type_group_names_tbl_group_id
	foreign key (group_id)
	references bti_apartment_attribute_type_groups_tbl (id);

alter table bti_apartment_attribute_type_group_names_tbl
	add constraint FK_bti_apartment_attribute_type_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table bti_apartment_attribute_type_names_tbl
	add constraint bti_apartment_attribute_type_names_tbl_attribute_type_id
	foreign key (attribute_type_id)
	references bti_apartment_attribute_types_tbl (id);

alter table bti_apartment_attribute_type_names_tbl
	add constraint bti_apartment_attribute_type_names_tbl_language_id
	foreign key (language_id)
	references common_languages_tbl (id);

alter table bti_apartment_attribute_types_tbl
	add constraint bti_apartment_attribute_types_tbl
	foreign key (group_id)
	references bti_apartment_attribute_type_groups_tbl (id);

alter table bti_apartment_attributes_tbl
	add constraint bti_apartment_attributes_tbl_attribute_type_id
	foreign key (attribute_type_id)
	references bti_apartment_attribute_types_tbl (id);

alter table bti_apartment_attributes_tbl
	add constraint FK_bti_apartment_attributes_tbl_apartment_id
	foreign key (apartment_id)
	references ab_apartments_tbl (id);

update common_version_tbl set last_modified_date='2009-05-25', date_version=5;
