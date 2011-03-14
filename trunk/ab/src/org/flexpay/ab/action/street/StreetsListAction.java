package org.flexpay.ab.action.street;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.filters.StreetSearchFilter;
import org.flexpay.ab.persistence.sorter.StreetSorterByName;
import org.flexpay.ab.persistence.sorter.StreetSorterByType;
import org.flexpay.ab.service.StreetService;
import org.flexpay.ab.service.TownService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

public class StreetsListAction extends FPActionWithPagerSupport<Street> {

	private Long townFilter;
	private StreetSearchFilter streetFilter = new StreetSearchFilter();
	private List<Street> streets = list();

	private StreetSorterByName streetSorterByName = new StreetSorterByName();
	private StreetSorterByType streetSorterByType = new StreetSorterByType();

	private StreetService streetService;
	private TownService townService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (!doValidate()) {
			return SUCCESS;
		}

		if (streetSorterByName == null) {
			log.warn("StreetSorterByName is null");
			streetSorterByName = new StreetSorterByName();
		}

		if (streetSorterByType == null) {
			log.warn("StreetSorterByType is null");
			streetSorterByType = new StreetSorterByType();
		}

		streetSorterByName.setLang(getLanguage());
		streetSorterByType.setLang(getLanguage());

		if (streetFilter == null) {
			log.debug("StreetFilter is null");
			streetFilter = new StreetSearchFilter();
		}

		String searchStr = streetFilter.getSearchString() == null ? "" : streetFilter.getSearchString();

		List<Street> streetsStubs = streetService.findByParentAndQuery(new Stub<Town>(townFilter),
				list(streetSorterByName, streetSorterByType), "%" + searchStr + "%", getLanguage(), getPager());

		if (log.isDebugEnabled()) {
			log.info("Total streets found: {}", streetsStubs.size());
		}

		for (Street street : streetsStubs) {
			streets.add(streetService.readFull(stub(street)));
		}

		return SUCCESS;
	}

	private boolean doValidate() {

		if (townFilter == null || townFilter <= 0) {
			log.warn("Incorrect town id in filter ({})", townFilter);
			addActionError(getText("ab.error.town.incorrect_town_id"));
			townFilter = 0L;
		} else {
			Town town = townService.readFull(new Stub<Town>(townFilter));
			if (town == null) {
				log.warn("Can't get town with id {} from DB", townFilter);
				addActionError(getText("ab.error.town.cant_get_town"));
				townFilter = 0L;
			} else if (town.isNotActive()) {
				log.warn("Town with id {} is disabled", townFilter);
				addActionError(getText("ab.error.town.cant_get_town"));
				townFilter = 0L;
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

	public void setStreetFilter(StreetSearchFilter streetFilter) {
		this.streetFilter = streetFilter;
	}

	public void setTownFilter(Long townFilter) {
		this.townFilter = townFilter;
	}

	public List<Street> getStreets() {
		return streets;
	}

	public StreetSorterByName getStreetSorterByName() {
		return streetSorterByName;
	}

	public void setStreetSorterByName(StreetSorterByName streetSorterByName) {
		this.streetSorterByName = streetSorterByName;
	}

	public StreetSorterByType getStreetSorterByType() {
		return streetSorterByType;
	}

	public void setStreetSorterByType(StreetSorterByType streetSorterByType) {
		this.streetSorterByType = streetSorterByType;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setTownService(TownService townService) {
		this.townService = townService;
	}

}
