package org.flexpay.ab.dao.imp;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.sql.SQLException;
import java.util.List;

public class PersonDaoExtImpl extends HibernateDaoSupport implements PersonDaoExt {

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persistent person matches specified identity
	 */
	public Stub<Person> findPersonStub(final Person person) {

		final PersonIdentity identity = person.getDefaultIdentity();
		List identities = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria crit = session.createCriteria(PersonIdentity.class)
						.setMaxResults(2)
						.add(Restrictions.eq("firstName", identity.getFirstName()))
						.add(Restrictions.eq("lastName", identity.getLastName()));

				if (StringUtils.isNotEmpty(identity.getMiddleName())) {
					crit.add(Restrictions.eq("middleName", identity.getMiddleName()));
				} else {
					crit.add(Restrictions.isNull("middleName"));
				}

				if (identity.getIdentityType().getTypeId() != IdentityType.TYPE_UNKNOWN) {
					crit.add(Restrictions.eq("identityType.id", identity.getIdentityType().getId()));
				}

				if (StringUtils.isNotEmpty(identity.getOrganization())) {
					crit.add(Restrictions.eq("organization", identity.getOrganization()));
				}
				if (StringUtils.isNotEmpty(identity.getSerialNumber())) {
					crit.add(Restrictions.eq("serialNumber", identity.getSerialNumber()));
				}
				if (StringUtils.isNotEmpty(identity.getDocumentNumber())) {
					crit.add(Restrictions.eq("documentNumber", identity.getDocumentNumber()));
				}
				if (person.getRegistrationApartment() != null) {
					crit
							.createAlias("person", "p")
							.createAlias("p.apartment", "a")
							.add(Restrictions.eq("a.id", person.getRegistrationApartment().getId()));
				}

				return crit.list();
			}
		});

		if (identities.isEmpty()) {
			return null;
		}

		PersonIdentity res = (PersonIdentity) identities.get(0);
		return stub(res.getPerson());
	}
}
