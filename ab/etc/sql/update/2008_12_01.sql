alter table ab_person_identities_tbl
    modify column organization varchar(4000) not null comment 'Organization gave document';
