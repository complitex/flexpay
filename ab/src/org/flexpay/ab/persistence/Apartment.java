package org.flexpay.ab.persistence;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.StringUtils;
import org.flexpay.ab.util.config.ApplicationConfig;
import org.flexpay.common.persistence.DomainObjectWithStatus;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateUtil;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Apartment
 */
public class Apartment extends DomainObjectWithStatus {

	private Building building;
	private Set<ApartmentNumber> apartmentNumbers = Collections.emptySet();
	private Set<PersonRegistration> personRegistrations = Collections.emptySet();

	public Apartment() {
	}

	public Apartment(@NotNull Long id) {
		super(id);
	}

	public Apartment(@NotNull Stub<Apartment> stub) {
		super(stub.getId());
	}

	@NotNull
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(@NotNull Building building) {
		this.building = building;
	}

	@NotNull
	public Set<ApartmentNumber> getApartmentNumbers() {
		return this.apartmentNumbers;
	}

	public void setApartmentNumbers(Set<ApartmentNumber> apartmentNumbers) {
		this.apartmentNumbers = apartmentNumbers;
	}

	/**
	 * Get apartment number for particular date
	 *
	 * @param dt Date to get apartment number for
	 * @return apartment number
	 */
	@Nullable
	public String getNumberForDate(@NotNull Date dt) {
		for (ApartmentNumber number : apartmentNumbers) {
			Date begin = number.getBegin();
			Date end = number.getEnd();
			if (begin.compareTo(dt) <= 0 && dt.compareTo(end) < 0) {
				return number.getValue();
			}
		}

		return null;
	}

	/**
	 * Get current apartment number
	 *
	 * @return apartment number
	 */
	@Nullable
	public String getNumber() {
		return getNumberForDate(DateUtil.now());
	}

	public void setNumber(@Nullable String number) {

		if (number == null && getNumber() == null) {
			return;
		}
		if (number != null && number.equals(getNumber())) {
			return;
		}

		// Check if apartment numbers is not empty (Collections.EMPTY_SET)
		if (apartmentNumbers == Collections.EMPTY_SET) {
			apartmentNumbers = set();
		}

		Date nowDate = DateUtil.now();
		// set up previous numbers to end at the record's operation date
		for (ApartmentNumber apartmentNumber : getApartmentNumbers()) {
			if (apartmentNumber.getEnd().after(nowDate)) {
				apartmentNumber.setEnd(nowDate);
			}
		}

		if (number == null) {
			return;
		}

		// Create a new apartment number and setup its properties
		ApartmentNumber apartmentNumber = new ApartmentNumber();
		apartmentNumber.setBegin(nowDate);
		apartmentNumber.setEnd(ApplicationConfig.getFutureInfinite());
		apartmentNumber.setValue(number);
		apartmentNumber.setApartment(this);

		// Add number to apartment numbers set
		apartmentNumbers.add(apartmentNumber);
	}


	public Set<Person> getPersons() {
		return getPersons(DateUtil.now());
	}

	public Set<Person> getPersons(Date date) {
		Set<Person> persons = new HashSet<Person>();
		for (PersonRegistration reg : personRegistrations) {
			if (reg.isValid(date)) {
				persons.add(reg.getPerson());
			}
		}

		return persons;
	}

	public Set<PersonRegistration> getValidPersonRegistrations() {
		return getValidPersonRegistrations(DateUtil.now());
	}

	public Set<PersonRegistration> getValidPersonRegistrations(Date date) {
		Set<PersonRegistration> result = new HashSet<PersonRegistration>();
		for (PersonRegistration reg : personRegistrations) {
			if (reg.isValid(date)) {
				result.add(reg);
			}
		}

		return result;
	}

	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("id", getId())
				.append("numbers", apartmentNumbers.toArray())
				.toString();
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

	/**
	 * Get default address buildings
	 * <p/>
	 * Transaction aware
	 *
	 * @return Buildings
	 */
	public Buildings getDefaultBuildings() {
		return building.getDefaultBuildings();
	}

	/**
	 * Get default address street
	 * <p/>
	 * Transaction aware
	 *
	 * @return Street
	 */
	public Street getDefaultStreet() {
		return getDefaultBuildings().getStreet();
	}

	/**
	 * Get District
	 * <p/>
	 * Transaction aware
	 *
	 * @return District
	 */
	public District getDistrict() {
		return building.getDistrict();
	}

	/**
	 * Get Town
	 * <p/>
	 * Transaction aware
	 *
	 * @return Town
	 */
	public Town getTown() {
		return (Town) getDistrict().getParent();
	}

	/**
	 * Get Region
	 * <p/>
	 * Transaction aware
	 *
	 * @return Region
	 */
	public Region getRegion() {
		return (Region) getTown().getParent();
	}

	/**
	 * Get country
	 * <p/>
	 * Transaction aware
	 *
	 * @return Country
	 */
	public Country getCountry() {
		return (Country) getRegion().getParent();
	}

	public boolean hasBuilding() {
		return building != null;
	}

	public boolean hasNoBuilding() {
		return !hasBuilding();
	}

	public boolean hasNumber() {
		return StringUtils.isNotBlank(getNumber());
	}

	public boolean hasNoNumber() {
		return !hasNumber();
	}
}
