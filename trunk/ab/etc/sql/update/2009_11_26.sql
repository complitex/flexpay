alter table ab_town_type_translations_tbl
    change column ID id bigint(20) not null auto_increment,
    drop primary key,
    add primary key using btree(id);

update common_version_tbl set last_modified_date='2009-11-26', date_version=0;
