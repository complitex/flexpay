-- AHTUNG!!! swap ids of service types КВАРПЛАТА(9) and ВОДООТВЕДЕНИЕ(1)
select @maxId:=max(id) from payments_service_types_tbl;
update payments_service_types_tbl set id=@maxId + 1 where id=1;
update payments_service_types_tbl set id=1 where id=9;
update payments_service_types_tbl set id=9 where id=@maxId + 1;

update common_version_tbl set last_modified_date='2009-08-04', date_version=0;
-- END AHTUNG!!!
