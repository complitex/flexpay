create table ab_person_registrations_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        person_id bigint not null,
        apartment_id bigint not null,
        primary key (id)
    );
	
alter table ab_person_registrations_tbl 
        add index INDX_ab_person_registrations$apartment_id (apartment_id),
        add constraint FP_ab_person_registrations$apartment_id
        foreign key (apartment_id) 
        references apartments_tbl (id);

alter table ab_person_registrations_tbl 
        add index INDX_ab_person_registrations$person_id (person_id),
        add constraint FP_ab_person_registrations$person_id
        foreign key (person_id) 
        references persons_tbl (id);

insert into ab_person_registrations_tbl (person_id,apartment_id, begin_date,end_date)
 (SELECT id, apartment_id, current_date AS BEGIN_DATE, DATE('2100-01-01') AS END_DATE FROM persons_tbl p where apartment_id is not null);

alter table persons_tbl drop index FKE7B2AFBDDEF75687, drop foreign key FKE7B2AFBDDEF75687;
alter table persons_tbl drop column apartment_id;