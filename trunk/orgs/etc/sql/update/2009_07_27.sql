select @orgs_base:=0x4000 + 0;
select @payments_base:=0x3000 + 0;
select @cashbox_type_old:=@payments_base + 0x006;
select @cashbox_type_new:=@payments_base + 0x008;

update common_data_corrections_tbl set object_type=@cashbox_type_new where object_type=@cashbox_type_old;
update common_diffs_tbl set object_type=@cashbox_type_new where object_type=@cashbox_type_old;
update common_import_errors_tbl set object_type=@cashbox_type_new where object_type=@cashbox_type_old;

update common_version_tbl set last_modified_date='2009-07-27', date_version=0;
