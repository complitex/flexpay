package org.flexpay.ab.persistence.filters;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BuildingsFilter extends PrimaryKeyFilter<BuildingAddress> {

	private List<BuildingAddress> buildingses = Collections.emptyList();

	public BuildingsFilter() {
	}

	public BuildingsFilter(Long selectedId) {
		super(selectedId);
	}

	public BuildingsFilter(@NotNull Stub<BuildingAddress> stub) {
		super(stub);
	}

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
				if (getSelectedStub().sameId(buildingAddress)) {
					return buildingAddress;
				}
			}
		}

		return null;
	}
}
