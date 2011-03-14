package org.flexpay.eirc.action.organization;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.persistence.Street;
import org.flexpay.ab.persistence.filters.StreetFilter;
import org.flexpay.ab.service.AddressService;
import org.flexpay.ab.service.StreetService;
import org.flexpay.common.action.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.EircServiceOrganization;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.CollectionUtils.arrayStack;
import static org.flexpay.common.util.CollectionUtils.list;

public class ServiceOrganizationBuildingsListAction extends FPActionWithPagerSupport<ServedBuilding> {

	private Long streetFilter;
	private EircServiceOrganization serviceOrganization = EircServiceOrganization.newInstance();
	private List<ServedBuilding> buildings = list();
	private List<String> addresses = list();

	private AddressService addressService;
	private StreetService streetService;
	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	@Override
	public String doExecute() throws Exception {

		if (!doValidate()) {
			return SUCCESS;
		}

		buildings = serviceOrganizationService.findServedBuildingsAddressesForOtherOrgs(arrayStack(new StreetFilter(streetFilter)), stub(serviceOrganization), getPager());
		if (log.isDebugEnabled()) {
			log.debug("Total buildings found: {}", buildings.size());
		}

		if (buildings == null || buildings.isEmpty()) {
			return SUCCESS;
		}

		for (Building b : buildings) {
			addresses.add(addressService.getBuildingAddressOnStreet(stub(b), new Stub<Street>(streetFilter), getUserPreferences().getLocale()));
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

		if (serviceOrganization == null || serviceOrganization.isNew()) {
			log.warn("Incorrect service organization id");
			addActionError(getText("common.error.invalid_id"));
		}

		Stub<EircServiceOrganization> stub = stub(serviceOrganization);
		serviceOrganization = serviceOrganizationService.read(stub);

		if (serviceOrganization == null) {
			log.warn("Can't get service organization with id {} from DB", stub.getId());
			addActionError(getText("eirc.error.service_organization.cant_get_service_organization"));
		} else if (serviceOrganization.isNotActive()) {
			log.warn("Service organization with id {} is disabled", stub.getId());
			addActionError(getText("eirc.error.service_organization.cant_get_service_organization"));
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

	public void setServiceOrganization(EircServiceOrganization serviceOrganization) {
		this.serviceOrganization = serviceOrganization;
	}

	public List<String> getAddresses() {
		return addresses;
	}

	public List<ServedBuilding> getBuildings() {
		return buildings;
	}

	@Required
	public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
		this.serviceOrganizationService = serviceOrganizationService;
	}

	@Required
	public void setAddressService(AddressService addressService) {
		this.addressService = addressService;
	}

	@Required
	public void setStreetService(StreetService streetService) {
		this.streetService = streetService;
	}
}
