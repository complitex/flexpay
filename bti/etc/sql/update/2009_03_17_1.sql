-- Update BTI object types (range in 0x2xxxx)
update common_data_corrections_tbl set object_type=0x2031 where object_type=0x31;
update common_diffs_tbl set object_type=0x2031 where object_type=0x31;
update common_import_errors_tbl set object_type=0x2031 where object_type=0x31;
