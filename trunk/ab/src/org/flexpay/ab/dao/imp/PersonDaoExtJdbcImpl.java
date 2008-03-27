package org.flexpay.ab.dao.imp;

import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PersonDaoExtJdbcImpl extends SimpleJdbcDaoSupport implements PersonDaoExt {

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persistent person matches specified identity
	 */
	public Person findPersonStub(Person person) {
		final PersonIdentity identity = person.getDefaultIdentity();
		List<Person> persons = getSimpleJdbcTemplate().query(
				"select distinct person_id from person_identities_tbl where first_name=? and middle_name=? and last_name=?",
				new ParameterizedRowMapper<Person>() {
					public Person mapRow(ResultSet rs, int i) throws SQLException {
						return new Person(rs.getLong("person_id"));
					}
				}, identity.getFirstName(), identity.getMiddleName(), identity.getLastName()
		);

		if (persons.isEmpty()) {
			return null;
		}

		return persons.get(0);
	}
}