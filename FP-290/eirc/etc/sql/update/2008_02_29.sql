ALTER TABLE eirc_personal_account_records_tbl 
 CHANGE COLUMN `billBeginDate` `bill_begin_date` DATE NOT NULL,
 CHANGE COLUMN `billEndDate` `bill_end_date` DATE NOT NULL;

ALTER TABLE eirc_personal_account_records_tbl
 CHANGE COLUMN `creationDate` `creation_date` DATE NOT NULL;

ALTER TABLE `flexpay_db`.`eirc_personal_account_records_tbl` CHANGE COLUMN `account` `account_id` BIGINT(20) NOT NULL,
 CHANGE COLUMN `service` `service_id` BIGINT(20) NOT NULL,
 DROP FOREIGN KEY `FK4883F2DA36052C2B`,
 DROP FOREIGN KEY `FK4883F2DAF6C14BBB`;

ALTER TABLE eirc_personal_account_records_tbl
 DROP INDEX `FK4883F2DA36052C2B`,
 DROP INDEX `FK4883F2DAF6C14BBB`;

    alter table eirc_personal_account_records_tbl
        add index FK4883F2DAD0BDDFB (account_id),
        add constraint FK4883F2DAD0BDDFB
        foreign key (account_id)
        references eirc_personal_accounts_tbl (id);

    alter table eirc_personal_account_records_tbl
        add index FK4883F2DA58F3985B (service_id),
        add constraint FK4883F2DA58F3985B
        foreign key (service_id)
        references eirc_services_tbl (id);
