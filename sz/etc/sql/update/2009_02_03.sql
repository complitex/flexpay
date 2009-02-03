
alter table sz_files_tbl
    modify column id bigint not null auto_increment comment 'Primary key identifier',
    modify column import_date datetime not null comment 'Import date',
    modify column user_name varchar(255) not null comment 'Author of this file',
    modify column oszn_id bigint not null comment 'OSZN reference',
    modify column uploaded_file_id bigint not null unique comment 'Uploaded file reference',
    modify column file_to_download_id bigint unique comment 'File to download reference',
    add column file_date date not null comment 'File date' after id,
    drop column is_actually,
    drop column file_year,
    drop column file_month;

update common_version_tbl set last_modified_date='2009-02-03', date_version=0;
