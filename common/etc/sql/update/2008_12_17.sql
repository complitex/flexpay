
create table common_file_statuses_tbl (
    id bigint not null auto_increment comment 'Primary key',
    name varchar(255) not null comment 'Flexpay filestatus title',
    description varchar(255) comment 'Flexpay filestatus description',
    module_id bigint not null comment 'Flexpay module reference',
    primary key (id)
) comment='Information about file statuses';

create table common_file_types_tbl (
    id bigint not null auto_increment comment 'Primary key',
    name varchar(255) not null comment 'Filetype title',
    description varchar(255) comment 'Filetype description',
    file_mask varchar(255) not null comment 'Mask of files for this type',
    module_id bigint not null comment 'Flexpay module reference',
    primary key (id)
) comment='Information about known filetypes';

create table common_files_tbl (
    id bigint not null auto_increment comment 'Primary key',
    name_on_server varchar(255) not null comment 'File name on flexpay server',
    original_name varchar(255) not null comment 'Original file name',
    description varchar(255) comment 'File description',
    creation_date datetime not null comment 'File creation date',
    user_name varchar(255) not null comment 'User name who create this file',
    size bigint comment 'File size',
    type_id bigint comment 'Flexpay file type reference',
    status_id bigint comment 'Flexpay file status reference',
    module_id bigint not null comment 'Flexpay module reference',
    primary key (id)
) comment='Table, where store information about all flexpay files';

create table common_flexpay_modules_tbl (
    id bigint not null auto_increment comment 'Primary key',
    name varchar(255) not null comment 'Flexpay module name',
    primary key (id)
) comment='Information about all flexpay modules';

alter table common_file_statuses_tbl
    add index common_file_statuses_tbl_module_id (module_id),
    add constraint common_file_statuses_tbl_module_id
    foreign key (module_id)
    references common_flexpay_modules_tbl (id);

alter table common_file_types_tbl
    add index common_file_types_tbl_module_id (module_id),
    add constraint common_file_types_tbl_module_id
    foreign key (module_id)
    references common_flexpay_modules_tbl (id);

alter table common_files_tbl
    add index common_files_tbl_module_id (module_id),
    add constraint common_files_tbl_module_id
    foreign key (module_id)
    references common_flexpay_modules_tbl (id);

alter table common_files_tbl
    add index common_files_tbl_status_id (status_id),
    add constraint common_files_tbl_status_id
    foreign key (status_id)
    references common_file_statuses_tbl (id);

alter table common_files_tbl
    add index common_files_tbl_type_id (type_id),
    add constraint common_files_tbl_type_id
    foreign key (type_id)
    references common_file_types_tbl (id);

update common_version_tbl set last_modified_date='2008-12-17', date_version=0;

INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (1, 'common');
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (2, 'ab');
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (3, 'bti');
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (4, 'eirc');
INSERT INTO common_flexpay_modules_tbl (id, name) VALUES (5, 'sz');

