create table sp_files_tbl (
	id bigint not null auto_increment,
	request_file_name varchar(255) not null,
	internal_request_file_name varchar(255) not null,
	internal_response_file_name varchar(255),
	user_name varchar(255) not null,
	import_date datetime not null,
	primary key (id)
);