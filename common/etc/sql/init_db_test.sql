insert into common_out_transport_configs_tbl (id, config_type, version, ws_url)
	values (1, 'ws', 0, 'http://localhost:8080/eirc/ws/ShareHistory');
select @out_transport_1:=1;

insert into common_history_consumers_tbl (id, active, name, description, out_transport_config_id)
	values (1, 0, 'Test-History-Consumer', 'Sample history consumer', @out_transport_1);
select @history_consumer_1:=1;
