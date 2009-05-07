package org.flexpay.ab.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.CollectionUtils;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Person
 */
public class Person extends DomainObjectWithStatus {

	private Set<PersonAttribute> personAttributes = Collections.emptySet();
	private Set<PersonIdentity> personIdentities = Collections.emptySet();
	private Set<PersonRegistration> personRegistrations = Collections.emptySet();

	/**
	 * Constructs a new Person.
	 */
	public Person() {
	}

	public Person(Long id) {
		super(id);
	}

	public Person(@NotNull Stub<Person> stub) {
		super(stub.getId());
	}

	/**
	 * Getter for property 'personAttributes'.
	 *
	 * @return Value for property 'personAttributes'.
	 */
	@NotNull
	public Set<PersonAttribute> getPersonAttributes() {
		return personAttributes;
	}

	/**
	 * Setter for property 'personAttributes'.
	 *
	 * @param personAttributes Value to set for property 'personAttributes'.
	 */
	public void setPersonAttributes(@NotNull Set<PersonAttribute> personAttributes) {
		this.personAttributes = personAttributes;
	}

	/**
	 * Getter for property 'personIdentities'.
	 *
	 * @return Value for property 'personIdentities'.
	 */
	@NotNull
	public Set<PersonIdentity> getPersonIdentities() {
		return personIdentities;
	}

	/**
	 * Setter for property 'personIdentities'.
	 *
	 * @param personIdentities Value to set for property 'personIdentities'.
	 */
	public void setPersonIdentities(@NotNull Set<PersonIdentity> personIdentities) {
		this.personIdentities = personIdentities;
	}


	public void setPersonAttributes(@NotNull List<PersonAttribute> personAttributes) {
		this.personAttributes = CollectionUtils.set(personAttributes);
	}

	/**
	 * Find person default identity
	 *
	 * @return PersonIdentity
	 */
	@Nullable
	public PersonIdentity getDefaultIdentity() {
		for (PersonIdentity identity : personIdentities) {
			if (identity.isDefault()) {
				return identity;
			}
		}

		return null;
	}

	/**
	 * Set person attribute, create, update or delete attribute operation.
	 *
	 * @param attribute PersonAttribute to set up
	 */
	public void setAttribute(@NotNull PersonAttribute attribute) {
		boolean needRemove = false;
		for (PersonAttribute attr : personAttributes) {
			if (attr.getName().equals(attribute.getName()) && attr.getLang().equals(attribute.getLang())) {
				// new value is empty, need to remove it
				if (StringUtils.isEmpty(attribute.getValue())) {
					needRemove = true;
					attribute = attr;
					break;
				}
				attr.setValue(attribute.getValue());
				return;
			}
		}

		if (needRemove) {
			personAttributes.remove(attribute);
		} else {
			if (Collections.emptySet().equals(personAttributes)) {
				personAttributes = CollectionUtils.set();
			}

			personAttributes.add(attribute);
		}
	}

	public void setIdentity(PersonIdentity personIdentity) {
		if (Collections.emptySet().equals(personIdentities)) {
			personIdentities = CollectionUtils.set();
		}

		PersonIdentity candidate = null;
		for (PersonIdentity identity : personIdentities) {
			if (identity.equals(personIdentity)) {
				candidate = identity;
				break;
			}
		}

		if (candidate != null) {
			if (personIdentity.isBlank()) {
				personIdentities.remove(candidate);
				return;
			}
			candidate.copy(personIdentity);
			return;
		}

		if (personIdentity.isBlank()) {
			return;
		}

		personIdentity.setPerson(this);
		personIdentities.add(personIdentity);
	}

	public void addIdentity(PersonIdentity identity) {
		if (Collections.emptySet().equals(personIdentities)) {
			personIdentities = CollectionUtils.set();
		}

		identity.setPerson(this);
		personIdentities.add(identity);
	}

	@Nullable
	public Apartment getRegistrationApartment() {
		return getRegistrationApartment(DateUtil.now());
	}

	@Nullable
	public Apartment getRegistrationApartment(@NotNull Date date) {
		PersonRegistration registration = getRegistrationForDate(date);
		if (registration != null) {
			return registration.getApartment();
		}

		return null;
	}

	@Nullable
	public PersonRegistration getCurrentRegistration() {
		return getRegistrationForDate(DateUtil.now());
	}

	@Nullable
	public PersonRegistration getRegistrationForDate(@NotNull Date date) {
		if (personRegistrations.isEmpty()) {
			return null;
		}

		for (PersonRegistration reg : personRegistrations) {
			if (reg.isValid(date)) {
				return reg;
			}
		}

		return null;
	}

	public void setRegistrationApartment(Apartment apartment) throws FlexPayException {
		setPersonRegistration(apartment, null, null);
	}

	public void setPersonRegistration(Apartment apartment, Date beginDate, Date endDate) throws FlexPayException {
		if (beginDate == null || beginDate.before(ApplicationConfig.getPastInfinite())) {
			beginDate = ApplicationConfig.getPastInfinite();
		}
		if (endDate == null || endDate.after(ApplicationConfig.getFutureInfinite())) {
			endDate = ApplicationConfig.getFutureInfinite();
		}

		beginDate = DateUtils.truncate(beginDate, Calendar.DAY_OF_MONTH);
		endDate = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);

		if (beginDate.after(endDate)) {
			throw new FlexPayException("beginDate after endDate", "ab.person.registration.error.begin_after_end");
		}

		//todo move it to registration action instead
//		Date[] dateInterval = getBeginValidInterval();
//		if (beginDate.before(dateInterval[0]) || beginDate.after(dateInterval[1])) {
//			throw new FlexPayException("beginDate valid interval error",
//					"ab.person.registration.error.begin_date_interval_error", dateInterval[0], dateInterval[1]);
//		}

		for (PersonRegistration reg : personRegistrations) {
			if (reg.getEndDate().after(beginDate)) {
				reg.setEndDate(beginDate);
			}
		}

		if (Collections.emptySet().equals(personRegistrations)) {
			personRegistrations = CollectionUtils.set();
		}

		PersonRegistration reg = new PersonRegistration();
		reg.setApartment(apartment);
		reg.setPerson(this);
		reg.setBeginDate(beginDate);
		reg.setEndDate(endDate);
		personRegistrations.add(reg);
	}

	private Date[] getBeginValidInterval() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateUtil.now());
		cal.add(Calendar.MONTH, -3);
		Date date1 = cal.getTime();

		cal.add(Calendar.MONTH, 4);
		Date date2 = cal.getTime();

		for (PersonRegistration reg : personRegistrations) {
			if (reg.getBeginDate().after(date1)) {
				date1 = reg.getBeginDate();
			}
		}

		return new Date[]{date1, date2};
	}

	/**
	 * @return the personRegistrations
	 */
	public Set<PersonRegistration> getPersonRegistrations() {
		return personRegistrations;
	}

	/**
	 * @param personRegistrations the personRegistrations to set
	 */
	public void setPersonRegistrations(Set<PersonRegistration> personRegistrations) {
		this.personRegistrations = personRegistrations;
	}

	public void setPersonRegistrations(@NotNull List<PersonRegistration> personRegistrations) {
		this.personRegistrations = CollectionUtils.set(personRegistrations);
	}

	public PersonIdentity getPassportIdentity() {
		for (PersonIdentity personIdentity : personIdentities) {
			if (personIdentity.getIdentityType().getTypeId() == IdentityType.TYPE_PASSPORT) {
				return personIdentity;
			}
		}

		return null;
	}

	public PersonIdentity getForeignPassportIdentity() {
		for (PersonIdentity personIdentity : personIdentities) {
			if (personIdentity.getIdentityType().getTypeId() == IdentityType.TYPE_FOREIGN_PASSPORT) {
				return personIdentity;
			}
		}

		return null;
	}

	/**
	 * Get current FIO identity
	 *
	 * @return PersonIdentity with First-Middle-Last names if available, or <code>null</code> otherwise
	 */
	public PersonIdentity getFIOIdentity() {
		return getFIOIdentity(DateUtil.now());
	}

	/**
	 * Get FIO identity for date
	 *
	 * @param date Date to get identity for
	 * @return PersonIdentity with First-Middle-Last names if available, or <code>null</code> otherwise
	 */
	public PersonIdentity getFIOIdentity(Date date) {
		for (PersonIdentity candidate : personIdentities) {
			IdentityType type = candidate.getIdentityType();
			if (type.isFIO() && isValidForDate(candidate, date)) {
				return candidate;
			}
		}

		return null;
	}

	private boolean isValidForDate(PersonIdentity id, Date dt) {
		return id.getBeginDate().compareTo(dt) <= 0 && dt.compareTo(id.getEndDate()) <= 0;
	}

	/**
	 * Set current FIO identity
	 *
	 * @param identity PersonIdentity to set
	 * @return <code>true</code> if identity was updated, or <code>false</code> otherwise
	 */
	public boolean setFIOIdentity(PersonIdentity identity) {
		PersonIdentity current = getFIOIdentity();
		if (current != null && current.isSameFIO(identity)) {
			return false;
		}

		// need to update identity
		if (current != null) {
			current.disable();
			current.setEndDate(identity.getBeginDate());
		}

		if (Collections.emptySet().equals(personIdentities)) {
			personIdentities = CollectionUtils.set();
		}

		personIdentities.add(identity);
		identity.setPerson(this);
		return true;
	}

	/**
	 * Get person First-middle-last name group
	 *
	 * @return person fio
	 */
	@NotNull
	public String getFIO() {
		PersonIdentity pi = getDefaultIdentity();
		if (pi == null) {
			if (personIdentities.isEmpty()) {
				return "--------No FIO-------";
			}
			pi = personIdentities.iterator().next();
		}

		return pi.getLastName() + " " + pi.getFirstName() + " " + pi.getMiddleName();
	}

}
