package org.flexpay.ab.persistence;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.persistence.EsbXmlSyncObject;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.util.DateIntervalUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.*;
import static org.flexpay.common.util.DateUtil.*;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;
import static org.flexpay.common.util.config.ApplicationConfig.getPastInfinite;

/**
 * Apartment
 */
public class Apartment extends EsbXmlSyncObject {

	private Building building;
	private Set<ApartmentNumber> apartmentNumbers = set();
	private Set<PersonRegistration> personRegistrations = set();

	protected Apartment() {
	}

	public Apartment(Long id) {
		super(id);
	}

    public Apartment(@NotNull Stub<Apartment> stub) {
		super(stub.getId());
	}

	public static Apartment newInstance() {
		return new Apartment();
	}

    @Override
    public String getXmlString() {
        StringBuilder builder = new StringBuilder();

        builder.append("    <apartment>\n");

        if (ACTION_UPDATE.equals(action)) {
            builder.append("        <id>").append(id).append("</id>\n");
        }

        if (ACTION_INSERT.equals(action) || ACTION_UPDATE.equals(action)) {
            builder.append("        <buildingId>").append(getDefaultBuildings().getId()).append("</buildingId>\n").
                    append("        <number>").append(getNumber()).append("</number>\n");
        } else if (ACTION_DELETE.equals(action)) {
            builder.append("        <ids>\n");
            for (Long id : ids) {
                builder.append("            <long>").append(id).append("</long>\n");
            }
            builder.append("        </ids>\n");
        }

        builder.append("    </apartment>\n");

        return builder.toString();
    }

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

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
			if (begin.compareTo(dt) <= 0 && dt.compareTo(end) <= 0) {
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
		return getNumberForDate(now());
	}

	public void setNumber(@Nullable String number) {
		setNumberForDate(number, now());
	}


	public Set<Person> getPersons() {
		return getPersons(now());
	}

	public Set<Person> getPersons(Date date) {
		Set<Person> persons = set();
		for (PersonRegistration reg : personRegistrations) {
			if (reg.isValid(date)) {
				persons.add(reg.getPerson());
			}
		}

		return persons;
	}

	public Set<PersonRegistration> getValidPersonRegistrations() {
		return getValidPersonRegistrations(now());
	}

	public Set<PersonRegistration> getValidPersonRegistrations(Date date) {
		Set<PersonRegistration> result = set();
		for (PersonRegistration reg : personRegistrations) {
			if (reg.isValid(date)) {
				result.add(reg);
			}
		}

		return result;
	}

	public Set<PersonRegistration> getPersonRegistrations() {
		return personRegistrations;
	}

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
	public BuildingAddress getDefaultBuildings() {
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

	public String format(Locale locale, boolean shortMode) {
		return getNumber();
	}

	public Stub<Building> getBuildingStub() {
		return stub(building);
	}

	public void setNumberForDate(String value, Date begin) {
		setNumberForDates(value, begin, getFutureInfinite());
	}

	public void setNumberForDates(String value, Date begin, Date end) {

		if (begin == null || begin.before(getPastInfinite())) {
			begin = getPastInfinite();
		}
		begin = truncateDay(begin);
		if (end == null || end.after(getFutureInfinite())) {
			end = getFutureInfinite();
		}
		end = truncateDay(end);

		SortedSet<ApartmentNumber> numbers = treeSet(getApartmentNumbers());
		List<ApartmentNumber> intersectingNumbers = list();
		for (ApartmentNumber number : apartmentNumbers) {
			if (DateIntervalUtil.areIntersecting(number.getBegin(), number.getEnd(), begin, end)) {
				intersectingNumbers.add(number);
			}
		}

		if (numbers.isEmpty()) {
			addNumber(begin, end, value);
			return;
		}

		// check first intersecting interval and add new apartment number with old value
		ApartmentNumber first = intersectingNumbers.get(0);
		if (first.getBegin().before(begin)) {
			addNumber(first.getBegin(), previous(begin), first.getValue());
		}
		// check last intersecting interval and add new apartment number with old value
		ApartmentNumber last = intersectingNumbers.get(intersectingNumbers.size() - 1);
		if (last.getEnd().after(end)) {
			addNumber(next(end), last.getEnd(), last.getValue());
		}

		// add interval with required boundaries
		addNumber(begin, end, value);

		// remove old intervals
		apartmentNumbers.removeAll(intersectingNumbers);
	}

	private void addNumber(Date begin, Date end, String value) {

		if (StringUtils.isBlank(value)) {
			return;
		}

		//noinspection CollectionsFieldAccessReplaceableByMethodCall
		if (apartmentNumbers == null) {
			apartmentNumbers = set();
		}

		ApartmentNumber number = new ApartmentNumber();
		number.setBegin(begin);
		number.setEnd(end);
		number.setValue(value);
		number.setApartment(this);
		apartmentNumbers.add(number);
	}

}
