package org.flexpay.ab.action.street;

import org.flexpay.ab.persistence.DistrictName;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.StreetDistrictRelation;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.ab.service.DistrictService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.action.FPActionSupport;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.list;
import static org.flexpay.common.util.CollectionUtils.set;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

public class StreetDistrictEditAction extends FPActionSupport {

	private Street street = new Street();
	private Set<Long> objectIds = set();
	private List<DistrictName> districtNames = list();

	private StreetService streetService;
	private DistrictService districtService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (street == null || street.isNew()) {
			log.warn("Incorrect street id");
			addActionError(getText("ab.error.street.incorrect_street_id"));
			return REDIRECT_SUCCESS;
		}

		Stub<Street> stub = stub(street);
		street = streetService.readFull(stub);

		if (street == null) {
			log.warn("Can't get street with id {} from DB", stub.getId());
			addActionError(getText("ab.error.street.cant_get_street"));
			return REDIRECT_SUCCESS;
		} else if (street.isNotActive()) {
			log.warn("Street with id {} is disabled", stub.getId());
			addActionError(getText("ab.error.street.cant_get_street"));
			return REDIRECT_SUCCESS;
		}

		log.debug("Street loaded: {}", street.getCurrentName());

		if (objectIds == null) {
			log.warn("ObjectIds parameter is null");
			objectIds = set();
		}

		// save street districts
		if (isSubmit()) {
			street = streetService.saveDistricts(street, objectIds);
			return REDIRECT_SUCCESS;
		}

		for (StreetDistrictRelation streetDistrict : street.getStreetDistricts()) {
			objectIds.add(streetDistrict.getDistrict().getId());
		}

		districtNames = districtService.findNames(new TownFilter(street.getParent().getId()));
        log.debug("Found district names: {}, for town with id {}", districtNames, street.getParent().getId());

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

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}

	@Required
	public void setDistrictService(DistrictService districtService) {
		this.districtService = districtService;
	}

}
