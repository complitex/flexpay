package org.flexpay.ab.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.dao.PersonDaoExt;
import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.persistence.IdentityType;
import org.flexpay.ab.persistence.Person;
import org.flexpay.ab.persistence.PersonIdentity;
import org.flexpay.common.dao.paging.FetchRange;
import org.flexpay.common.persistence.Stub;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.map;

@SuppressWarnings ({"unchecked"})
public class PersonDaoExtImpl extends JpaDaoSupport implements PersonDaoExt {

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

		StringBuilder queryBuilder = new StringBuilder("FROM PersonIdentity pi");

		Map<String, Object> parameters = map();

		Apartment registrationApartment = person.getRegistrationApartment();
		if (registrationApartment != null) {
			queryBuilder.append(" INNER JOIN pi.person.personRegistrations pr WHERE pr.apartment.id=:apartment and ");
			parameters.put("apartment", registrationApartment.getId());
		} else {
			queryBuilder.append(" WHERE ");
		}

		queryBuilder.append("pi.firstName=:firstName and pi.lastName=:lastName");
		parameters.put("firstName", identity.getFirstName());
		parameters.put("lastName", identity.getLastName());

		if (StringUtils.isNotEmpty(identity.getMiddleName())) {
			queryBuilder.append(" and pi.middleName=:middleName");
			parameters.put("middleName", identity.getMiddleName());
		} else {
			queryBuilder.append(" and pi.middleName IS NULL");
		}

		if (identity.getIdentityType().getTypeId() != IdentityType.TYPE_UNKNOWN) {
			queryBuilder.append(" and pi.identityType.id=:identityTypeId");
			parameters.put("identityTypeId", identity.getIdentityType().getId());
		}

		if (StringUtils.isNotEmpty(identity.getOrganization())) {
			queryBuilder.append(" and pi.organization=:organization");
			parameters.put("organization", identity.getOrganization());
		}
		if (StringUtils.isNotEmpty(identity.getSerialNumber())) {
			queryBuilder.append(" and pi.serialNumber=:serialNumber");
			parameters.put("serialNumber", identity.getSerialNumber());
		}
		if (StringUtils.isNotEmpty(identity.getDocumentNumber())) {
			queryBuilder.append(" and pi.documentNumber=:documentNumber");
			parameters.put("documentNumber", identity.getDocumentNumber());
		}

		List<?> identities = getJpaTemplate().findByNamedParams(queryBuilder.toString(), parameters);
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
					getJpaTemplate().findByNamedQuery("Person.listPersonsWithIdentities.stats"));
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
		return getJpaTemplate().findByNamedQuery("Person.listPersonsWithIdentities", params);
	}

	@Override
	public List<Person> listPersonsWithRegistrations(FetchRange range) {

		if (!range.wasInitialized()) {
			Object[] stats = (Object[]) DataAccessUtils.uniqueResult(
					getJpaTemplate().findByNamedQuery("Person.listPersonsWithRegistrations.stats"));
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
		return getJpaTemplate().findByNamedQuery("Person.listPersonsWithRegistrations", params);
	}

	@Override
	public void deletePerson(final Person person) {
		getJpaTemplate().execute(new JpaCallback() {
			@Override
			public Object doInJpa(EntityManager entityManager) throws HibernateException {
				Long personId = person.getId();
				if (personId == null || personId <= 0) {
					return null;
				}
				entityManager.createNamedQuery("Person.deleteRegistration")
						.setParameter(1, personId).executeUpdate();
				entityManager.createNamedQuery("Person.deleteIdentity")
						.setParameter(1, personId).executeUpdate();
				entityManager.createNamedQuery("Person.deletePerson")
						.setParameter(1, personId).executeUpdate();
				return null;
			}
		});
	}

}
