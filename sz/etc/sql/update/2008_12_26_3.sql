
alter table `flexpay_db`.`subsreqvnach` rename to `flexpay_db`.`sz_subsidy_records_tbl`;
alter table `flexpay_db`.`oszn_tbl` rename to `flexpay_db`.`sz_oszns_tbl`;
alter table `flexpay_db`.`characteristic_records_tbl` rename to `flexpay_db`.`sz_characteristic_records_tbl`;

alter table sz_files_tbl
    drop index FKC3A70A1E96D0F83F,
    drop index FKC3A70A1E8A1D7FFF,
    drop index FKC3A70A1EFB383BC0;

drop table if exists sz_file_status_tbl;
drop table if exists sz_file_actuality_status_tbl;
drop table if exists sz_file_types_tbl;
drop table if exists sz_files_tbl;

create table sz_files_tbl (
    id bigint not null auto_increment,
    file_year integer not null,
    file_month integer not null,
    import_date datetime not null,
    is_actually bit not null,
    user_name varchar(255) not null,
    oszn_id bigint not null,
    uploaded_file_id bigint not null unique,
    file_to_download_id bigint unique,
    type_id bigint comment 'SZ file type reference',
    status_id bigint comment 'SZ file status reference',
    primary key (id)
);

alter table sz_files_tbl
    add index FKC3A70A1E857550AE (uploaded_file_id),
    add constraint FKC3A70A1E857550AE
    foreign key (uploaded_file_id)
    references common_files_tbl (id);

alter table sz_files_tbl
    add index sz_files_tbl_status_id (status_id),
    add constraint sz_files_tbl_status_id
    foreign key (status_id)
    references common_file_statuses_tbl (id);

alter table sz_files_tbl
    add index sz_files_tbl_type_id (type_id),
    add constraint sz_files_tbl_type_id
    foreign key (type_id)
    references common_file_types_tbl (id);

alter table sz_files_tbl
    add index FKC3A70A1EF6DE0A60 (file_to_download_id),
    add constraint FKC3A70A1EF6DE0A60
    foreign key (file_to_download_id)
    references common_files_tbl (id);

alter table sz_files_tbl
    add index FKC3A70A1E53508F07 (oszn_id),
    add constraint FKC3A70A1E53508F07
    foreign key (oszn_id)
    references sz_oszns_tbl (id);

-- Init Sz file module

INSERT INTO common_flexpay_modules_tbl (name) VALUES ('sz');
SELECT @module_sz:=last_insert_id();

-- Init Sz file types
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл тарифов', '(t|T)(a|A)(R|R)(i|I)(f|F)\\u002E(d|D)(b|B)(f|F)', 1, @module_sz);
SELECT @tariff_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл запроса характеристик жилья', '\\d{8}\\u002E(a|A)\\d{2}', 2, @module_sz);
SELECT @characteristics_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл запроса жилищно-коммунальных услуг и услуг связи', '\\d{8}\\u002E(b|B)\\d{2}', 3, @module_sz);
SELECT @srv_types_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Форма «2-льгота»', '\\d{8}\\u002E(e|E)\\d{2}', 4, @module_sz);
SELECT @form2_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('sz.file_type.characteristics_response', '\\d{8}\\u002E(c|C)\\d{2}', 5, @module_sz);
SELECT @characteristics_response_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('sz.file_type.srv_types_response', '\\d{8}\\u002E(d|D)\\d{2}', 6, @module_sz);
SELECT @srv_types_response_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл запроса субсидий', '.*\\d{4}\\u002E(d|D)(b|B)(f|F)', 7, @module_sz);
SELECT @subsidy_type_id:=last_insert_id();
INSERT INTO common_file_types_tbl (name, file_mask, code, module_id)
	VALUES ('Файл запроса субсидий', '\\d{8}\\u002E(a|A)\\d{2}', 8, @module_sz);
SELECT @subsidy_type_id2:=last_insert_id();

-- Init Sz file statuses
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Импортирован в систему', 1, @module_sz);
SELECT @imported_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Помечен на обработку', 2, @module_sz);
SELECT @marked_for_processing_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Обрабатывается', 3, @module_sz);
SELECT @processing_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Обработан без ошибок и замечаний', 4, @module_sz);
SELECT @processed_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Обработан с ошибками (замечаниями)', 5, @module_sz);
SELECT @processed_with_warnings_status_id:=last_insert_id();
INSERT INTO common_file_statuses_tbl (name, code, module_id)
	VALUES ('Помечен на удаление', 6, @module_sz);
SELECT @marked_as_deleted_status_id:=last_insert_id();

update common_version_tbl set last_modified_date='2008-12-26', date_version=3;
