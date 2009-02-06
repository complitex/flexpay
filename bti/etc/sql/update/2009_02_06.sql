
alter table bti_sewer_type_translations_tbl
    drop index bti_sewer_type_translations_tbl_sewer_type_id,
    drop foreign key bti_sewer_type_translations_tbl_sewer_type_id,
    drop index lang_sewer_type_pair_language_id,
    drop foreign key lang_sewer_type_pair_language_id;

drop table bti_sewer_type_translations_tbl;
drop table bti_sewer_types_tbl;

alter table bti_sewer_material_type_translations_tbl
    drop index bti_sewer_material_type_tbl_sewer_material_type_id,
    drop foreign key bti_sewer_material_type_tbl_sewer_material_type_id,
    drop index lang_sewer_material_type_pair_language_id,
    drop foreign key lang_sewer_material_type_pair_language_id;

drop table bti_sewer_material_type_translations_tbl;
drop table bti_sewer_material_types_tbl;

update common_version_tbl set last_modified_date='2009-02-06', date_version=0;
