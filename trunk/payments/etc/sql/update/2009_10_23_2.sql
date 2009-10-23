select @module_payments:=id from common_flexpay_modules_tbl where name='payments';
select @reg_type_payments:=id from common_registry_types_tbl where code=7;
select @reg_type_bank_payments:=id from common_registry_types_tbl where code=12;

update common_registries_tbl set module_id=@module_payments
where registry_type_id in (@reg_type_payments, @reg_type_bank_payments);

update common_version_tbl set last_modified_date='2009-10-23', date_version=2;
