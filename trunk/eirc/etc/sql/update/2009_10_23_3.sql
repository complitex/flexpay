select @module_eirc:=id from common_flexpay_modules_tbl where name='eirc';

update common_registries_tbl
	set module_id=@module_eirc where module_id is null or module_id=0;

update common_version_tbl set last_modified_date='2009-10-23', date_version=3;
