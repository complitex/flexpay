package org.flexpay.ab.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.ab.persistence.Buildings;

import java.util.List;
import java.util.Collections;

public class BuildingsFilter extends PrimaryKeyFilter<Buildings> {

	private List<Buildings> buildingses = Collections.emptyList();

	/**
	 * Getter for property 'buildingses'.
	 *
	 * @return Value for property 'buildingses'.
	 */
	public List<Buildings> getBuildingses() {
		return buildingses;
	}

	/**
	 * Setter for property 'buildingses'.
	 *
	 * @param buildingses Value to set for property 'buildingses'.
	 */
	public void setBuildingses(List<Buildings> buildingses) {
		this.buildingses = buildingses;
	}
}
