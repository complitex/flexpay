package org.flexpay.ab.action.building;

import org.flexpay.ab.persistence.BuildingAddress;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.persistence.sorter.BuildingsSorter;
import org.flexpay.ab.service.BuildingService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.util.TranslationUtil;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.DomainObject.collectionIds;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class BuildingsListAction extends FPActionWithPagerSupport<BuildingAddress> {

	private Long streetFilter;
	private BuildingsSorter buildingsSorter = new BuildingsSorter();
	private List<BuildingAddress> buildings = list();

	private BuildingService buildingService;
	private StreetService streetService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (!doValidate()) {
			return SUCCESS;
		}

		if (buildingsSorter == null) {
			log.warn("BuildingsSorter is null");
			buildingsSorter = new BuildingsSorter();
		}

		buildings = buildingService.findAddresses(arrayStack(new StreetFilter(streetFilter)), list(buildingsSorter), getPager());
		if (log.isDebugEnabled()) {
			log.debug("Total buildings found: {}", buildings.size());
		}

		buildings = buildingService.readFullAddresses(collectionIds(buildings), true);
		if (log.isDebugEnabled()) {
			log.debug("Total full buildings found: {}", buildings.size());
		}

		return SUCCESS;
	}

	private boolean doValidate() {

		if (streetFilter == null || streetFilter <= 0) {
			log.warn("Incorrect street id in filter ({})", streetFilter);
			addActionError(getText("ab.error.street.incorrect_street_id"));
			streetFilter = 0L;
		} else {
			Street street = streetService.readFull(new Stub<Street>(streetFilter));
			if (street == null) {
				log.warn("Can't get street with id {} from DB", streetFilter);
				addActionError(getText("ab.error.street.cant_get_street"));
				streetFilter = 0L;
			} else if (street.isNotActive()) {
				log.warn("Street with id {} is disabled", streetFilter);
				addActionError(getText("ab.error.street.cant_get_street"));
				streetFilter = 0L;
			}
		}

		return !hasActionErrors();
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

    public String getBuildingNumber(BuildingAddress address) throws FlexPayException {
        return TranslationUtil.getBuildingNumber(address, getUserPreferences().getLocale());
    }

	@Required
	public void setBuildingService(BuildingService buildingService) {
		this.buildingService = buildingService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

}
