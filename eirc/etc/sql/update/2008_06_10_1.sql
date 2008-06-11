    alter table ab_person_registrations_tbl
        drop foreign key FK2BD18CD7095AEAD;

    alter table ab_person_registrations_tbl
        drop foreign key FK2BD18CDDEF75687;


    alter table ab_person_registrations_tbl
        add index FP_ab_person_registrations_person (person_id),
        add constraint FP_ab_person_registrations_person
        foreign key (person_id)
        references persons_tbl (id);

    alter table ab_person_registrations_tbl
        add index FP_ab_person_registrations_apartment (apartment_id),
        add constraint FP_ab_person_registrations_apartment
        foreign key (apartment_id)
        references apartments_tbl (id);

    alter table eirc_consumers_tbl
        drop foreign key FK_eirc_eirc_consumers_eirc_account_id;


    alter table eirc_consumers_tbl
        add index FK_eirc_consumer_eirc_account (eirc_account_id),
        add constraint FK_eirc_consumer_eirc_account
        foreign key (eirc_account_id)
        references eirc_eirc_accounts_tbl (id);

