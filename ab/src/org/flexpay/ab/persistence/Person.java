package org.flexpay.ab.persistence;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

/**
 * Person
 */
public class Person extends DomainObjectWithStatus {

	private Set<PersonAttribute> personAttributes = Collections.emptySet();
	private Set<PersonIdentity> personIdentities = Collections.emptySet();
	private Set<PersonRegistration> personRegistrations = Collections.emptySet();

	public Person() {
	}

	public Person(Long id) {
		super(id);
	}

	public Person(@NotNull Stub<Person> stub) {
		super(stub.getId());
	}

	public Set<PersonAttribute> getPersonAttributes() {
		return personAttributes;
	}

	public void setPersonAttributes(Set<PersonAttribute> personAttributes) {
		this.personAttributes = personAttributes;
	}

	public Set<PersonIdentity> getPersonIdentities() {
		return personIdentities;
	}

	public void setPersonIdentities(Set<PersonIdentity> personIdentities) {
		this.personIdentities = personIdentities;
	}


	public void setPersonAttributes(List<PersonAttribute> personAttributes) {
		this.personAttributes = set(personAttributes);
	}

	/**
	 * Find person default identity
	 *
	 * @return PersonIdentity
	 */
	@Nullable
	public PersonIdentity getDefaultIdentity() {
		for (PersonIdentity identity : personIdentities) {
			if (identity.isDefault() && identity.isActive()) {
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
				personAttributes = set();
			}

			personAttributes.add(attribute);
		}
	}

	public void setIdentity(PersonIdentity personIdentity) {

		PersonIdentity candidate = null;
		for (PersonIdentity identity : personIdentities) {
			if (identity.equals(personIdentity) || identity.sameNumber(personIdentity)) {
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

		addIdentity(personIdentity);
	}

	public void addIdentity(PersonIdentity identity) {
		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (personIdentities == Collections.EMPTY_SET) {
			personIdentities = set();
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

	public void setPersonRegistration(Apartment apartment, Date beginDate) {
		setPersonRegistration(apartment, beginDate, getFutureInfinite());
	}

	public void setPersonRegistration(@Nullable Apartment apartment, @Nullable Date beginDate, @Nullable Date endDate) {

		if (beginDate == null || beginDate.before(getPastInfinite())) {
			beginDate = getPastInfinite();
		}
		if (endDate == null || endDate.after(getFutureInfinite())) {
			endDate = getFutureInfinite();
		}

		beginDate = DateUtil.truncateDay(beginDate);
		endDate = DateUtil.truncateDay(endDate);

		List<PersonRegistration> toDelete = list();
		List<PersonRegistration> newRegistrations = list();
		for (PersonRegistration reg : personRegistrations) {
			// if registration intervals are intersecting update old intervals bound
			// possibly adds one new interval if new interval is inside the old
			Date beg = reg.getBeginDate();
			Date end = reg.getEndDate();
			if (DateIntervalUtil.areIntersecting(beg, end, beginDate, endDate)) {
				toDelete.add(reg);
				if (beg.before(beginDate)) {
					PersonRegistration reg1 = reg.copy();
					reg1.setEndDate(DateUtil.previous(beginDate));
					newRegistrations.add(reg1);
				}
				if (end.after(endDate)) {
					PersonRegistration reg1 = reg.copy();
					reg1.setBeginDate(DateUtil.next(endDate));
					newRegistrations.add(reg1);
				}
			}
		}
		personRegistrations.removeAll(toDelete);
		addRegistrations(newRegistrations);

		if (apartment != null) {
			PersonRegistration reg = new PersonRegistration();
			reg.setApartment(apartment);
			reg.setPerson(this);
			reg.setBeginDate(beginDate);
			reg.setEndDate(endDate);
			addRegistration(reg);
		}
	}

	private void addRegistration(PersonRegistration registration) {

		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (personRegistrations == Collections.EMPTY_SET) {
			personRegistrations = set();
		}

		personRegistrations.add(registration);
	}

	private void addRegistrations(Collection<PersonRegistration> registrations) {

		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (personRegistrations == Collections.EMPTY_SET) {
			personRegistrations = set();
		}

		personRegistrations.addAll(registrations);
	}

	public Set<PersonRegistration> getPersonRegistrations() {
		return personRegistrations;
	}

	public void setPersonRegistrations(Set<PersonRegistration> personRegistrations) {
		this.personRegistrations = personRegistrations;
	}

	public void setPersonRegistrations(List<PersonRegistration> personRegistrations) {
		this.personRegistrations = set(personRegistrations);
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
			if (type == null) {
				throw new NullPointerException("Person identity type is null: " + candidate);
			}
			if (type.isFIO() && isValidForDate(candidate, date)) {
				return candidate;
			}
		}

		return null;
	}

	private boolean isValidForDate(PersonIdentity id, Date dt) {
		return DateIntervalUtil.includes(dt, id.getBeginDate(), id.getEndDate());
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
			personIdentities = set();
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

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Person && super.equals(obj);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).
				append("id", getId()).
				append("status", getStatus()).
				toString();
	}

}
