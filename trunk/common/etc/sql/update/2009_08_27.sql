create table common_registry_fpfile_types_tbl (
    id bigint not null auto_increment,
    version integer not null comment 'Optimistic locking version',
    code integer not null unique comment 'FP file registry type code',
    primary key (id)
);

create table common_registry_fpfiles_tbl (
    registry_id bigint not null,
    elt bigint not null,
    idx bigint not null,
    primary key (registry_id, idx)
);

alter table common_registry_fpfiles_tbl
    add index FKC93365EB7DDE2622 (idx),
    add constraint FKC93365EB7DDE2622
    foreign key (idx)
    references common_registry_fpfile_types_tbl (id);

alter table common_registry_fpfiles_tbl
    add index FKC93365EB3B128842 (registry_id),
    add constraint FKC93365EB3B128842
    foreign key (registry_id)
    references common_registries_tbl (id);

alter table common_registry_fpfiles_tbl
    add index FKC93365EB925958FC (elt),
    add constraint FKC93365EB925958FC
    foreign key (elt)
    references common_files_tbl (id);

INSERT INTO common_registry_fpfile_types_tbl (version, code) VALUES (0, 0);
SELECT @sp_registry_file_fp_format:=last_insert_id();

INSERT INTO common_registry_fpfile_types_tbl (version, code) VALUES (0, 1);
SELECT @sp_registry_file_mb_format:=last_insert_id();

INSERT INTO common_registry_fpfiles_tbl (registry_id, elt, idx) VALUES
        (select r.id, r.file_id, @sp_registry_file_fp_format from common_registries_tbl r
                inner join common_registry_types_tbl rt on r.registry_type_id=rt.id
                where rt.code=12);

INSERT INTO common_registry_fpfiles_tbl (registry_id, elt, idx) VALUES
        (select r.id, r.file_id, @sp_registry_file_mb_format from common_registries_tbl r
                inner join common_registry_types_tbl rt on r.registry_type_id=rt.id
                where rt.code!=12);

alter table common_files_tbl drop index FK_common_registries_tbl_file_id, drop column file_id;

update common_version_tbl set last_modified_date='2009-08-27', date_version=0;