insert into common_out_transport_configs_tbl (id, config_type, version, ws_url)
	values (1, 'ws', 0, 'http://localhost:8080/synctgt2-eirc/ws/ShareHistory');
select @out_transport_1:=1;

insert into common_history_consumers_tbl (id, active, name, description, out_transport_config_id)
	values (1, 0, 'Test-History-Consumer', 'Sample history consumer', @out_transport_1);
select @history_consumer_1:=1;

insert into common_users_tbl (id, discriminator, user_name, full_name, last_name, first_name, language_code, page_size)
	values (1, 'common', 'test', 'Тест Т. Тестов', 'Тестов', 'Тест', 'en', 30);
select @user_test:=1;

insert into common_users_tbl (id, discriminator, user_name, full_name, last_name, first_name, language_code)
	values (2, 'common', 'ivanova.ap', 'Иванова Александра Петровна', 'Иванова', 'Александра' , 'ru');
select @user_ivanova:=2;

-- master index data
select @ds:=id from common_data_source_descriptions_tbl where description='Master-index';
select @instId:='090-';
select @common_base:=0x6000 + 0;

insert into common_data_corrections_tbl (internal_object_id, external_object_id, object_type, data_source_description_id)
	(select id, concat(@instId, id), @common_base + 0x09, @ds from common_measure_units_tbl);

-- certificates
insert into common_certificates_tbl (version, alias, description) values (0, 'testlogin', 'Tecтовый логин');
