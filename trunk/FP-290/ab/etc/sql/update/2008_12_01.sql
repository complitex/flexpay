alter table ab_person_identities_tbl
    modify column organization varchar(4000) not null comment 'Organization gave document';

update common_version_tbl set last_modified_date='2008-12-01', date_version=0;