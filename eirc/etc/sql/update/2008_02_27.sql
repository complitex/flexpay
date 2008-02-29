    create table eirc_account_status_translations_tbl (
        id bigint not null auto_increment,
        name varchar(255) not null,
        account_status_id bigint not null,
        language_id bigint not null,
        primary key (id),
        unique (account_status_id, language_id)
    );

    alter table eirc_account_statuses_tbl drop column value;
    alter table eirc_account_statuses_tbl add column status integer not null;

    ALTER TABLE eirc_account_statuses_tbl DROP COLUMN language,
        DROP INDEX `FKB610981B47DC07F9`,
        DROP FOREIGN KEY `FKB610981B47DC07F9`;

