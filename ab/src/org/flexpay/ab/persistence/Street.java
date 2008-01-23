package org.flexpay.ab.persistence;

import org.flexpay.ab.persistence.temp.Buildings;
import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TimeLine;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;

/**
 * Street
 */
public class Street extends NameTimeDependentChild<StreetName, StreetNameTemporal> {

	private Set<District> districts = Collections.emptySet();
	private TimeLine<TownType, TownTypeTemporal> typesTimeLine;
	private Set<Buildings> buildingses = new HashSet<Buildings>(0);

	public Street() {
	}

	/**
	 * Getter for property 'districts'.
	 *
	 * @return Value for property 'districts'.
	 */
	public Set<District> getDistricts() {
		return districts;
	}

	/**
	 * Setter for property 'districts'.
	 *
	 * @param districts Value to set for property 'districts'.
	 */
	public void setDistricts(Set<District> districts) {
		this.districts = districts;
	}
}
