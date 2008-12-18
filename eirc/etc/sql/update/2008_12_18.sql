drop table if exists eirc_registry_files_tbl;

alter table eirc_registries_tbl
    drop foreign key FK_eirc_registry_file;

alter table eirc_registries_tbl
    add index FK_eirc_registry_file (sp_file_id),
    add constraint FK_eirc_registry_file
    foreign key (sp_file_id)
    references common_files_tbl (id);
