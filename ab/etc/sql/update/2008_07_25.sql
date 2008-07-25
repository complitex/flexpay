alter table ab_person_registrations_tbl
	drop foreign key FK2BD18CD22797B84;

update common_version_tbl set last_modified_date='2008-07-25', date_version=0;
