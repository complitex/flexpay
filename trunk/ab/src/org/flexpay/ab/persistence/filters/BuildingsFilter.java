package org.flexpay.ab.persistence.filters;

import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.ab.persistence.BuildingAddress;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Collections;

public class BuildingsFilter extends PrimaryKeyFilter<BuildingAddress> {

	private List<BuildingAddress> buildingses = Collections.emptyList();

	/**
	 * Getter for property 'buildingses'.
	 *
	 * @return Value for property 'buildingses'.
	 */
	@NotNull
	public List<BuildingAddress> getBuildingses() {
		return buildingses;
	}

	/**
	 * Setter for property 'buildingses'.
	 *
	 * @param buildingses Value to set for property 'buildingses'.
	 */
	public void setBuildingses(@NotNull List<BuildingAddress> buildingses) {
		this.buildingses = buildingses;
	}

	@Nullable
	public BuildingAddress getSelected() {
		if (needFilter()) {
			for (BuildingAddress buildingAddress : buildingses) {
				if (getSelectedId().equals(buildingAddress.getId())) {
					return buildingAddress;
				}
			}
		}

		return null;
	}
}
