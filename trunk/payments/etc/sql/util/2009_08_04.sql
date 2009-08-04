-- AHTUNG!!! swap ids of service types КВАРПЛАТА(9) and ВОДООТВЕДЕНИЕ(1)
SET FOREIGN_KEY_CHECKS = 0;

update payments_service_types_tbl t, payments_service_type_name_translations_tbl n, payments_services_tbl s
	set t.id=2000, n.service_type_id=2000, s.type_id=2000 where t.id=1 and n.service_type_id=1 and s.type_id=1;
update ignore payments_service_types_tbl t, payments_service_type_name_translations_tbl n, payments_services_tbl s
	set t.id=1, n.service_type_id=1, s.type_id=1 where t.id=9 and n.service_type_id=9 and s.type_id=9;
update ignore payments_service_types_tbl t, payments_service_type_name_translations_tbl n, payments_services_tbl s
	set t.id=9, n.service_type_id=9, s.type_id=9 where t.id=2000 and n.service_type_id=2000 and s.type_id=2000;

update common_version_tbl set last_modified_date='2009-08-04', date_version=0;

-- END AHTUNG!!!
