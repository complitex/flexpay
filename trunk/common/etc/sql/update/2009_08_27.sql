DROP TABLE IF EXISTS common_registry_fpfiles_tbl;
DROP TABLE IF EXISTS common_registry_fpfile_types_tbl;

create table common_registry_fpfile_types_tbl (
    id bigint not null auto_increment,
    version integer not null comment 'Optimistic locking version',
    code integer not null unique comment 'FP file registry type code',
    primary key (id)
);

create table common_registry_fpfiles_tbl (
    registry_id bigint not null,
    fpfile_id bigint not null,
    registry_fpfile_type_id bigint not null,
    primary key (registry_id, registry_fpfile_type_id)
);

alter table common_registry_fpfiles_tbl
    add index FK_common_registry_fpfiles_tbl_fpfile_id (fpfile_id),
    add constraint FK_common_registry_fpfiles_tbl_fpfile_id
    foreign key (fpfile_id)
    references common_files_tbl (id);

alter table common_registry_fpfiles_tbl
    add index FK_common_registry_fpfiles_tbl_registry_fpfile_type_id (registry_fpfile_type_id),
    add constraint FK_common_registry_fpfiles_tbl_registry_fpfile_type_id
    foreign key (registry_fpfile_type_id)
    references common_registry_fpfile_types_tbl (id);

alter table common_registry_fpfiles_tbl
    add index FK_common_registry_fpfiles_tbl_registry_id (registry_id),
    add constraint FK_common_registry_fpfiles_tbl_registry_id
    foreign key (registry_id)
    references common_registries_tbl (id);

INSERT INTO common_registry_fpfile_types_tbl (version, code) VALUES (0, 0);
select @type_0:=last_insert_id();
INSERT INTO common_registry_fpfiles_tbl (registry_id, fpfile_id, registry_fpfile_type_id)
        (select r.id, r.file_id, @type_0 from common_registries_tbl r
                inner join common_registry_types_tbl rt on r.registry_type_id=rt.id
                where rt.code=12 and r.file_id is not null);

INSERT INTO common_registry_fpfile_types_tbl (version, code) VALUES (0, 1);
select @type_1:=last_insert_id();
INSERT INTO common_registry_fpfiles_tbl (registry_id, fpfile_id, registry_fpfile_type_id)
        (select r.id, r.file_id, @type_1 from common_registries_tbl r
                inner join common_registry_types_tbl rt on r.registry_type_id=rt.id
                where rt.code<>12 and r.file_id is not null);

alter table common_registries_tbl
	drop foreign key FK_common_registries_tbl_file_id,
	drop index FK_common_registries_tbl_file_id;
alter table common_registries_tbl drop column file_id;

update common_version_tbl set last_modified_date='2009-08-27', date_version=0;
