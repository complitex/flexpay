alter table ab_person_identities_tbl
	add column sex smallint not null comment 'Person sex type';

select @person_id:=i.person_id
from ab_person_identities_tbl i
	inner join ab_identity_types_tbl t on i.identity_type_id=t.id
where t.type_enum=2 and first_name='Михаил' and middle_name='Анатольевич' and last_name='Федько';

select @identity_type_fio_id:=id
from ab_identity_types_tbl
where type_enum=1;	

INSERT INTO ab_person_identities_tbl (begin_date, end_date, birth_date, serial_number,
	document_number, first_name, middle_name, last_name, organization,
	is_default, identity_type_id, person_id, status)
	VALUES ('1983-01-25', '2100-12-31', '1983-01-25', 0,
	0, 'Михаил', 'Анатольевич', 'Федько', '',
	0, @identity_type_fio_id, @person_id, 0);


update common_version_tbl set last_modified_date='2008-07-08', date_version=0;
