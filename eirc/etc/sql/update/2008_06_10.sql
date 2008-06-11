ALTER TABLE eirc_consumers_tbl
    ADD COLUMN eirc_account_id BIGINT COMMENT 'Eirc account reference';

alter table eirc_consumers_tbl 
        add index FK_eirc_eirc_consumers_eirc_account_id (apartment_id),
        add constraint FK_eirc_eirc_consumers_eirc_account_id
        foreign key (eirc_account_id) 
        references eirc_eirc_accounts_tbl (id);
