update common_file_types_tbl set file_mask='.*\\u002E(d|D)(r|R)(l|L)' where name = 'tc.file_type.tariff_rules';
update common_version_tbl set last_modified_date='2009-01-24', date_version=0;
