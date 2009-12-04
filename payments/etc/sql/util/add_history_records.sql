-- there were no juridical address in diff #2095257 (MegaBank), and kpp-postal address-tax number as well 
select @diff:=2095257 from dual;
select @FIELD_INDIVIDUAL_TAX_NUMBER := 3 from dual;
select @FIELD_KPP := 4 from dual;
select @FIELD_JURIDICAL_ADDRESS := 5 from dual;
select @FIELD_POSTAL_ADDRESS := 6 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;

insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('МБ-0', @FIELD_INDIVIDUAL_TAX_NUMBER, @maxorder+1, 'ru', 0, @diff),
	('123', @FIELD_KPP, @maxorder+2, 'ru', 0, @diff),
	('Харьков', @FIELD_JURIDICAL_ADDRESS, @maxorder+3, 'ru', 0, @diff),
	('Харьков', @FIELD_POSTAL_ADDRESS, @maxorder+4, 'ru', 0, @diff);

update common_diffs_tbl set error_message=NULL where id=@diff;

-- there were no description in service types
select @diff:=2095293 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095294 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095295 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095296 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095297 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095298 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095299 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095300 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095301 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095302 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095303 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;
select @diff:=2095304 from dual;
select @FIELD_DESCRIPTION := 3 from dual;
select @maxorder:=max(operation_order) from common_history_records_tbl where diff_id=@diff;
insert common_history_records_tbl (new_string_value, field_type, operation_order, language_code, processing_status, diff_id)
	values ('Описание', @FIELD_DESCRIPTION, @maxorder+1, 'ru', 0, @diff);
update common_diffs_tbl set error_message=NULL where id=@diff;

-- generated town history in eirc, but received it too late, replace ids
begin;
select @oldid:=2533515 from dual;
select @newid:=2095228 from dual;
SET foreign_key_checks = 0;
update common_history_records_tbl set diff_id=@newid where diff_id=@oldid;
update common_diffs_tbl set id=@newid where id=@oldid;
SET foreign_key_checks = 1;
commit;

-- town type
begin;
select @oldid:=2533513 from dual;
select @newid:=2095227 from dual;
SET foreign_key_checks = 0;
update common_history_records_tbl set diff_id=@newid where diff_id=@oldid;
update common_diffs_tbl set id=@newid where id=@oldid;
SET foreign_key_checks = 1;
commit;
update common_diffs_tbl set error_message=null where id=2095228;
