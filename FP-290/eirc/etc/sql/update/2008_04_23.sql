create table eirc_sp_registry_statuses_tbl (
    id bigint not null auto_increment,
    code integer not null unique,
    primary key (id)
);

create table eirc_sp_registry_archive_statuses_tbl (
    id bigint not null auto_increment,
    code integer not null unique,
    primary key (id)
);

alter table eirc_sp_registries_tbl
    ADD COLUMN registry_status_id bigint not null;
    ADD COLUMN archive_status_id bigint not null;

alter table eirc_sp_registries_tbl
    add index FK8F6F495212E06FB1 (registry_status_id), 
    add constraint FK8F6F495212E06FB1 
    foreign key (registry_status_id) 
    references eirc_sp_registry_statuses_tbl (id);

alter table eirc_sp_registries_tbl 
    add index FK8F6F49524B0FEDC6 (archive_status_id), 
    add constraint FK8F6F49524B0FEDC6 
    foreign key (archive_status_id) 
    references eirc_sp_registry_archive_statuses_tbl (id);