package org.flexpay.ab.actions.street;

import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.Town;
import org.flexpay.ab.persistence.sorter.StreetSorterByName;
import org.flexpay.ab.persistence.sorter.StreetSorterByType;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.sorter.ObjectSorter;
import org.flexpay.common.util.CollectionUtils;
import static org.flexpay.common.util.CollectionUtils.list;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;

public class StreetsListAjaxAction extends FPActionWithPagerSupport<Street> {

	private Long streetFilter;
	private Long townFilter;
	private List<Street> streets = list();

	private StreetSorterByName streetSorterByName = new StreetSorterByName();
	private StreetSorterByType streetSorterByType = new StreetSorterByType();

	private StreetService streetService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (streetFilter != null && streetFilter > 0) {
			streets = new ArrayList<Street>();
			streets.add(streetService.readFull(new Stub<Street>(streetFilter)));
			return SUCCESS;
		}

		streetSorterByName.setLang(getLanguage());
		streetSorterByType.setLang(getLanguage());

		List<ObjectSorter> sorters = CollectionUtils.<ObjectSorter>list(streetSorterByName, streetSorterByType);

		if (townFilter == null || townFilter <= 0) {
			log.warn("Incorrect town id in filter ({})", townFilter);
			return SUCCESS;
		}

		List<Street> streetsStubs = streetService.getStreets(new Stub<Town>(townFilter), sorters, getPager());
		if (log.isDebugEnabled()) {
			log.info("Total streets found: {}", streetsStubs.size());
		}
		streets = new ArrayList<Street>();

		for (Street street : streetsStubs) {
			streets.add(streetService.readFull(stub(street)));
		}

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

}
