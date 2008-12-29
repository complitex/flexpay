alter table eirc_registries_tbl
    drop foreign key FK_eirc_registry_file;

-- See init_db for common
SELECT @module_eirc:=4;

insert into common_files_tbl (id, name_on_server, original_name, user_name, module_id, create_date)
	values (select id, internal_request_file_name, request_file_name, user_name, @module_eirc, import_date from eirc_registry_files_tbl);

alter table eirc_registries_tbl
    add index FK_eirc_registry_file (sp_file_id),
    add constraint FK_eirc_registry_file
    foreign key (sp_file_id)
    references common_files_tbl (id);

drop table if exists eirc_registry_files_tbl;

update common_version_tbl set last_modified_date='2008-12-18', date_version=0;
