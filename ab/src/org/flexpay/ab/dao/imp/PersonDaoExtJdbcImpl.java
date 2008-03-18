package org.flexpay.ab.dao.imp;

import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @deprecated Use PersonDaoExtImpl instead as it uses Hibernate (and is a bit less efficient)
 */
public class PersonDaoExtJdbcImpl extends SimpleJdbcDaoSupport implements PersonDaoExt {

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persistent person matches specified identity
	 */
	public Person findPersonStub(Person person) {
		final PersonIdentity identity = person.getDefaultIdentity();
		String sql = "     select" +
					 "         this_.id as id37_1_," +
					 "         this_.begin_date as begin2_37_1_," +
					 "         this_.end_date as end3_37_1_," +
					 "         this_.birth_date as birth4_37_1_," +
					 "         this_.serial_number as serial5_37_1_," +
					 "         this_.document_number as document6_37_1_," +
					 "         this_.first_name as first7_37_1_," +
					 "         this_.middle_name as middle8_37_1_," +
					 "         this_.last_name as last9_37_1_," +
					 "         this_.organization as organiz10_37_1_," +
					 "         this_.is_default as is11_37_1_," +
					 "         this_.identity_type_id as identity12_37_1_," +
					 "         this_.person_id as person13_37_1_," +
					 "         identityty1_.id as id33_0_," +
					 "         identityty1_.status as status33_0_," +
					 "         identityty1_.type_enum as type3_33_0_" +
					 "     from" +
					 "         person_identities_tbl this_" +
					 "     inner join" +
					 "         identity_types_tbl identityty1_" +
					 "             on this_.identity_type_id=identityty1_.id" +
					 "     where" +
					 "         this_.first_name=?" +
					 "         and this_.middle_name=?" +
					 "         and this_.last_name=?" +
					 "         and identityty1_.type_enum=? limit 2";
		List<PersonIdentity> identities = getSimpleJdbcTemplate().query(sql,
				new ParameterizedRowMapper<PersonIdentity>() {
					public PersonIdentity mapRow(ResultSet rs, int rowNum) throws SQLException {
						int n = 0;
						PersonIdentity i = new PersonIdentity();
						i.setId(rs.getLong(++n));
						i.setBeginDate(rs.getDate(++n));
						i.setEndDate(rs.getDate(++n));
						i.setBirthDate(rs.getDate(++n));
						i.setSerialNumber(rs.getString(++n));
						i.setDocumentNumber(rs.getString(++n));
						i.setFirstName(rs.getString(++n));
						i.setMiddleName(rs.getString(++n));
						i.setLastName(rs.getString(++n));
						i.setOrganization(rs.getString(++n));
						i.setDefault(rs.getBoolean(++n));

						IdentityType t = new IdentityType(rs.getLong(++n));
						i.setIdentityType(t);

						Person stub = new Person(rs.getLong(++n));
						i.setPerson(stub);
						Set<PersonIdentity> personIdentities = new HashSet<PersonIdentity>();
						personIdentities.add(i);
						stub.setPersonIdentities(personIdentities);

						++n;
						t.setStatus(rs.getInt(++n));
						t.setTypeId(rs.getInt(++n));

						return i;
					}
				},
				identity.getFirstName(), identity.getMiddleName(),
				identity.getLastName(), identity.getIdentityType().getTypeId());

		if (identities.isEmpty()) {
			return null;
		}

		PersonIdentity res = identities.get(0);
		return res.getPerson();
	}
}