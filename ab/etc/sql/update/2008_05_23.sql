create table ab_person_registrations_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        person_id bigint not null,
        apartment_id bigint not null,
        primary key (id)
    );
	
alter table ab_person_registrations_tbl 
        add index INDX_ab_person_registrations#apartment_id (apartment_id), 
        add constraint FP_ab_person_registrations#apartment_id
        foreign key (apartment_id) 
        references apartments_tbl (id);

alter table ab_person_registrations_tbl 
        add index INDX_ab_person_registrations#person_id (person_id), 
        add constraint FP_ab_person_registrations#person_id 
        foreign key (person_id) 
        references persons_tbl (id);