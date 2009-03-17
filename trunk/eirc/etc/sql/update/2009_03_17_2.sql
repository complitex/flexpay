-- Update EIRC object types (range in 0x5xxxx)
update common_data_corrections_tbl set object_type=0x5101 where object_type=0x101;
update common_diffs_tbl set object_type=0x5101 where object_type=0x101;
update common_import_errors_tbl set object_type=0x5101 where object_type=0x101;

update common_data_corrections_tbl set object_type=0x5102 where object_type=0x102;
update common_diffs_tbl set object_type=0x5102 where object_type=0x102;
update common_import_errors_tbl set object_type=0x5102 where object_type=0x102;
