package org.flexpay.ab.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.ab.persistence.Buildings;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Collections;

public class BuildingsFilter extends PrimaryKeyFilter<Buildings> {

	private List<Buildings> buildingses = Collections.emptyList();

	/**
	 * Getter for property 'buildingses'.
	 *
	 * @return Value for property 'buildingses'.
	 */
	@NotNull
	public List<Buildings> getBuildingses() {
		return buildingses;
	}

	/**
	 * Setter for property 'buildingses'.
	 *
	 * @param buildingses Value to set for property 'buildingses'.
	 */
	public void setBuildingses(@NotNull List<Buildings> buildingses) {
		this.buildingses = buildingses;
	}

	@Nullable
	public Buildings getSelected() {
		if (needFilter()) {
			for (Buildings buildings : buildingses) {
				if (getSelectedId().equals(buildings.getId())) {
					return buildings;
				}
			}
		}

		return null;
	}
}
