
alter table common_files_tbl
    drop index common_file_types_tbl_module_id,
    drop foreign key common_file_types_tbl_module_id,
    drop index common_file_statuses_tbl_module_id,
    drop foreign key common_file_statuses_tbl_module_id,
    drop column type_id,
    drop column status_id;

alter table common_flexpay_modules_tbl
    add unique index name using btree(name);

alter table common_file_statuses_tbl
    MODIFY COLUMN name varchar(255) NOT NULL COMMENT 'Filestatus name',
    MODIFY COLUMN description varchar(255) COMMENT 'Filestatus description',
    add column code bigint not null comment 'Unique filestatus code',
    add unique index code using btree(code, module_id);

alter table common_file_types_tbl
    MODIFY COLUMN name varchar(255) NOT NULL COMMENT 'Filetype name',
    MODIFY COLUMN description varchar(255) COMMENT 'Filetype description',
    add column code bigint not null comment 'Unique filetype code',
    add unique index code using btree(code, module_id);

update common_version_tbl set last_modified_date='2008-12-26', date_version=0;
