    alter table eirc_service_type_name_translations_tbl
        drop foreign key FKA057A044A83C068F;

    alter table eirc_service_type_name_translations_tbl
        drop
        foreign key FKA057A0445A549E10;

ALTER TABLE eirc_service_type_name_translations_tbl
	CHANGE COLUMN lang language_id BIGINT(20)  NOT NULL,
	CHANGE COLUMN type_id service_type_id BIGINT(20)  NOT NULL;

ALTER TABLE eirc_service_type_name_translations_tbl
        add index FKA057A0442C648686 (service_type_id),
        add constraint FKA057A0442C648686
        foreign key (service_type_id)
        references eirc_service_types_tbl (id);

ALTER TABLE eirc_service_type_name_translations_tbl 
        add index FKA057A04461F37403 (language_id),
        add constraint FKA057A04461F37403
        foreign key (language_id)
        references languages_tbl (id);

ALTER TABLE eirc_personal_accounts_tbl ADD COLUMN account_number VARCHAR(255)  NOT NULL UNIQUE;
