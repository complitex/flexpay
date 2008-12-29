select @person_id:=i.person_id
from ab_person_identities_tbl i
	inner join ab_identity_types_tbl t on i.identity_type_id=t.id
where t.type_enum=2 and first_name='Михаил' and middle_name='Анатольевич' and last_name='Федько';

-- TODO insert some apartment ids here
--SELECT @apartment_ivanova_27_id:=27;
-- SELECT @apartment_ivanova_330_id:=330;

-- Init person registrations
INSERT INTO ab_person_registrations_tbl (begin_date, end_date, person_id, apartment_id)
	VALUES ('2007-01-12', '2008-01-13', @person_id, @apartment_ivanova_27_id);

INSERT INTO ab_person_registrations_tbl (begin_date, end_date, person_id, apartment_id)
	VALUES ('2008-04-12', '2100-12-31', @person_id, @apartment_ivanova_330_id);
