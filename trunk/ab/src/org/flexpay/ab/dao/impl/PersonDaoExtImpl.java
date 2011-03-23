package org.flexpay.ab.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.Collections;
import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;

@SuppressWarnings ({"unchecked"})
public class PersonDaoExtImpl extends HibernateDaoSupport implements PersonDaoExt {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Find persistent person by identity
	 *
	 * @param person Identity data
	 * @return Person stub if persistent person matches specified identity
	 */
	@Override
	public Stub<Person> findPersonStub(final Person person) {

		final PersonIdentity identity = person.getDefaultIdentity();
		List<?> identities = getHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
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
				Apartment registrationApartment = person.getRegistrationApartment();
				if (registrationApartment != null) {
					crit
							.createAlias("person", "p")
							.createAlias("p.personRegistrations", "r")
							.createAlias("r.apartment", "a")
							.add(Restrictions.eq("a.id", registrationApartment.getId()));
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

	@Override
	public List<Person> listPersonsWithIdentities(FetchRange range) {

		if (!range.wasInitialized()) {
			Object[] stats = (Object[]) DataAccessUtils.uniqueResult(
					getHibernateTemplate().findByNamedQuery("Person.listPersonsWithIdentities.stats"));
			range.setMinId((Long) stats[0]);
			range.setMaxId((Long) stats[1]);
			range.setCount(((Long) stats[2]).intValue());
			range.setLowerBound(range.getMinId());
			range.setUpperBound(range.getLowerBound() != null ? range.getLowerBound() + range.getPageSize() : null);

			log.debug("initialized range: {}", range);
		}

		if (!range.wasInitialized()) {
			log.debug("No records in range");
			return Collections.emptyList();
		}

		Object[] params = {range.getLowerBound(), range.getUpperBound()};
		return getHibernateTemplate().findByNamedQuery("Person.listPersonsWithIdentities", params);
	}

	@Override
	public List<Person> listPersonsWithRegistrations(FetchRange range) {

		if (!range.wasInitialized()) {
			Object[] stats = (Object[]) DataAccessUtils.uniqueResult(
					getHibernateTemplate().findByNamedQuery("Person.listPersonsWithRegistrations.stats"));
			range.setMinId((Long) stats[0]);
			range.setMaxId((Long) stats[1]);
			range.setCount(((Long) stats[2]).intValue());
			range.setLowerBound(range.getMinId());
			range.setUpperBound(range.getLowerBound() != null ? range.getLowerBound() + range.getPageSize() : null);

			log.debug("initialized range: {}", range);
		}

		if (!range.wasInitialized()) {
			log.debug("No records in range");
			return Collections.emptyList();
		}

		Object[] params = {range.getLowerBound(), range.getUpperBound()};
		return getHibernateTemplate().findByNamedQuery("Person.listPersonsWithRegistrations", params);
	}

	@Override
	public void deletePerson(final Person person) {
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				Long personId = person.getId();
				if (personId == null || personId <= 0) {
					return null;
				}
				session.getNamedQuery("Person.deleteRegistration")
						.setLong(0, personId).executeUpdate();
				session.getNamedQuery("Person.deleteIdentity")
						.setLong(0, personId).executeUpdate();
				session.getNamedQuery("Person.deletePerson")
						.setLong(0, personId).executeUpdate();
				return null;
			}
		});
	}

}
