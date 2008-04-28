ALTER TABLE eirc_account_records_tbl
 ADD COLUMN source_registry_record_id bigint;


    alter table eirc_account_records_tbl
        add index FK_registry_record (source_registry_record_id),
        add constraint FK_registry_record
        foreign key (source_registry_record_id)
        references eirc_sp_registry_records_tbl (id);
