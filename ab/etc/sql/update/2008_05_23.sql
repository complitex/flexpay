create table person_registrations_tbl (
        id bigint not null auto_increment,
        begin_date date not null,
        end_date date not null,
        person_id bigint not null,
        apartment_id bigint not null,
        primary key (id)
    );
	
alter table person_registrations_tbl 
        add index FK87E75A6FDEF75687 (apartment_id), 
        add constraint FK87E75A6FDEF75687 
        foreign key (apartment_id) 
        references apartments_tbl (id);

alter table person_registrations_tbl 
        add index FK87E75A6F7095AEAD (person_id), 
        add constraint FK87E75A6F7095AEAD 
        foreign key (person_id) 
        references persons_tbl (id);