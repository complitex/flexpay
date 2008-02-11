package org.flexpay.ab.persistence;

import org.flexpay.common.persistence.NameTimeDependentChild;
import org.flexpay.common.persistence.TimeLine;

import java.util.Collections;
import java.util.Set;

/**
 * Street
 */
public class Street extends NameTimeDependentChild<StreetName, StreetNameTemporal> {

	private Set<District> districts = Collections.emptySet();
	private TimeLine<TownType, TownTypeTemporal> typesTimeLine;
	private Set<Buildings> buildingses = Collections.emptySet();

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

	/**
	 * Getter for property 'buildingses'.
	 *
	 * @return Value for property 'buildingses'.
	 */
	public Set<Buildings> getBuildingses() {
		return buildingses;
	}

	/**
	 * Setter for property 'buildingses'.
	 *
	 * @param buildingses Value to set for property 'buildingses'.
	 */
	public void setBuildingses(Set<Buildings> buildingses) {
		this.buildingses = buildingses;
	}
}
