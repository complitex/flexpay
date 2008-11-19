package org.flexpay.ab.actions.street;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.persistence.District;
import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.dao.paging.Page;
import static org.flexpay.common.persistence.Stub.stub;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StreetDistrictEditAction extends FPActionSupport {

	private Street street = new Street();
	private Set<Long> objectIds = new HashSet<Long>();
	private List<DistrictName> districtNames = new ArrayList<DistrictName>();

	private StreetService streetService;
	private DistrictService districtService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (street.getId() == null) {
			addActionError(getText("error.no_id"));
			return INPUT;
		}

		street = streetService.readFull(stub(street));
		if (street == null) {
			addActionError(getText("error.street_invalid_id"));
			return INPUT;
		}
		if (log.isInfoEnabled()) {
			log.info("Street loaded: " + street.getCurrentName());
		}

		// save street districts
		if (isPost()) {
			street = streetService.saveDistricts(street, objectIds);
			return SUCCESS;
		}

		for (District district : street.getDistricts()) {
			objectIds.add(district.getId());
		}

		ArrayStack filters = new ArrayStack();
		TownFilter townFilter = new TownFilter();
		townFilter.setSelectedId(street.getParent().getId());
		filters.push(townFilter);

		Page pager = new Page(1000, 1);
		districtNames = districtService.findNames(filters, pager);
		if (log.isInfoEnabled()) {
			log.info("Found " + districtNames);
		}

		return INPUT;
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
		return INPUT;
	}

	public boolean doesDistrictContainsStreet(Long id) {
		return objectIds.contains(id);
	}

	/**
	 * Setter for property 'streetService'.
	 *
	 * @param streetService Value to set for property 'streetService'.
	 */
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	/**
	 * Setter for property 'districtService'.
	 *
	 * @param districtService Value to set for property 'districtService'.
	 */
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

	public Street getStreet() {
		return street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

	public List<DistrictName> getDistrictNames() {
		return districtNames;
	}

	public void setObjectIds(Set<Long> objectIds) {
		this.objectIds = objectIds;
	}
}
