package org.flexpay.ab.actions.buildings;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.sorter.BuildingsSorter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.DomainObject;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class BuildingsListAction extends FPActionWithPagerSupport<BuildingAddress> {

	private Long streetFilter;
	private List<BuildingAddress> buildings = list();

	private BuildingsSorter buildingsSorter = new BuildingsSorter();
	private BuildingService buildingService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (streetFilter == null || streetFilter <= 0) {
			log.warn("Incorrect street id in filter ({})", streetFilter);
			return SUCCESS;
		}

		List<BuildingAddress> addresses = buildingService.getBuildings(
				arrayStack(new StreetFilter(streetFilter)), list(buildingsSorter), getPager());

		if (log.isDebugEnabled()) {
			log.debug("Total buildings found: {}", buildings.size());
		}

		buildings = buildingService.readFullAddresses(DomainObject.collectionIds(addresses), true);

		return SUCCESS;
	}

	/**
	 * Get default error execution result
	 * <p/>
	 * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
	 *
	 * @return {@link #ERROR} by default
	 */
	@NotNull
	@Override
	protected String getErrorResult() {
		return SUCCESS;
	}

	public void setStreetFilter(Long streetFilter) {
		this.streetFilter = streetFilter;
	}

	public List<BuildingAddress> getBuildings() {
		return buildings;
	}

	public BuildingsSorter getBuildingsSorter() {
		return buildingsSorter;
	}

	public void setBuildingsSorter(BuildingsSorter buildingsSorter) {
		this.buildingsSorter = buildingsSorter;
	}

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

}
