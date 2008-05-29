package org.flexpay.ab.persistence;

import org.apache.commons.lang.time.DateUtils;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.util.DateIntervalUtil;
import org.flexpay.common.util.DateUtil;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

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

	/**
	 * Getter for property 'personAttributes'.
	 *
	 * @return Value for property 'personAttributes'.
	 */
	public Set<PersonAttribute> getPersonAttributes() {
		return personAttributes;
	}

	/**
	 * Setter for property 'personAttributes'.
	 *
	 * @param personAttributes Value to set for property 'personAttributes'.
	 */
	public void setPersonAttributes(Set<PersonAttribute> personAttributes) {
		this.personAttributes = personAttributes;
	}

	/**
	 * Getter for property 'personIdentities'.
	 *
	 * @return Value for property 'personIdentities'.
	 */
	public Set<PersonIdentity> getPersonIdentities() {
		return personIdentities;
	}

	/**
	 * Setter for property 'personIdentities'.
	 *
	 * @param personIdentities Value to set for property 'personIdentities'.
	 */
	public void setPersonIdentities(Set<PersonIdentity> personIdentities) {
		this.personIdentities = personIdentities;
	}

	/**
	 * Find person default identity
	 *
	 * @return PersonIdentity
	 */
	public PersonIdentity getDefaultIdentity() {
		for (PersonIdentity identity : getPersonIdentities()) {
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
	public void setAttribute(PersonAttribute attribute) {
		boolean needRemove = false;
		for (PersonAttribute attr : personAttributes) {
			if (attr.getName().equals(attribute.getName()) && attr.getLang().equals(attribute.getLang())) {
				// new value is null, need to remove it
				if (attribute.getValue() == null) {
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
			if (personAttributes == Collections.EMPTY_SET) {
				personAttributes = new HashSet<PersonAttribute>();
			}

			personAttributes.add(attribute);
		}
	}

	public void addIdentity(PersonIdentity identity) {
		if (personIdentities == Collections.EMPTY_SET) {
			personIdentities = new HashSet<PersonIdentity>();
		}

		personIdentities.add(identity);
	}
	
	public Apartment getApartment() {
		return getApartment(DateIntervalUtil.now());
	}
	
	public Apartment getApartment(Date date) {
		if(personRegistrations.isEmpty()) {
			return null;
		}
		
		for(PersonRegistration reg : personRegistrations) {
			if(reg.isValid(date)) {
				return reg.getApartment();
			}
		}
		
		return null;
	}
	
	public void setApartment(Apartment apartment) throws FlexPayException {
		setPersonRegistration(apartment, null, null);
	}
	
	public void setPersonRegistration(Apartment apartment, Date beginDate, Date endDate) throws FlexPayException {
		if(beginDate == null || beginDate.before(ApplicationConfig.getInstance().getPastInfinite())) {
			beginDate = ApplicationConfig.getInstance().getPastInfinite();
		}
		if(endDate == null || endDate.after(ApplicationConfig.getInstance().getFutureInfinite())) {
			endDate = ApplicationConfig.getInstance().getFutureInfinite();
		}
		
		beginDate = DateUtils.truncate(beginDate, Calendar.DAY_OF_MONTH);
		endDate = DateUtils.truncate(endDate, Calendar.DAY_OF_MONTH);
		
		if(beginDate.after(endDate)) {
			throw new FlexPayException("beginDate can not be after endDate", "ab.person.registration.error.begin_after_end");
		}
		
		Date[] dateInterval = getBeginValidInterval();
		if(beginDate.before(dateInterval[0]) || beginDate.after(dateInterval[1])) {
			throw new FlexPayException("beginDate valid interval error", "ab.person.registration.error.begin_date_interval_error");
		}
		
		for(PersonRegistration reg : personRegistrations) {
			if(reg.getEndDate().after(beginDate)) {
				reg.setEndDate(beginDate);
			}
		}
		
		PersonRegistration reg = new PersonRegistration();
		reg.setApartment(apartment);
		reg.setPerson(this);
		reg.setBeginDate(beginDate);
		reg.setEndDate(endDate);
		personRegistrations.add(reg);
	}
	
	public Date[] getBeginValidInterval() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DateIntervalUtil.now());
		cal.add(Calendar.MONTH, -3);
		Date date1 = cal.getTime();
		
		cal.add(Calendar.MONTH, 4);
		Date date2 = cal.getTime();
		
		for(PersonRegistration reg : personRegistrations) {
			if(reg.getBeginDate().after(date1)) {
				date1 = reg.getBeginDate();
			}
		}
		
		return new Date[] {date1, date2};
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
	
	
}
