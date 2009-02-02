package org.flexpay.eirc.actions.organization;

import org.flexpay.ab.persistence.Building;
import org.flexpay.ab.service.AddressService;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServiceOrganizationListServedBuildingsAction extends FPActionWithPagerSupport<ServedBuilding> {

	private ServiceOrganization serviceOrganization = new ServiceOrganization();
	private List<ServedBuilding> buildings = Collections.emptyList();
    private List<String> addresses = Collections.emptyList();

	private AddressService addressService;
	private ServiceOrganizationService serviceOrganizationService;

	@NotNull
	public String doExecute() throws Exception {

        if (serviceOrganization.getId() == null) {
            addActionError(getText("error.no_id"));
            return SUCCESS;
        }

        serviceOrganization = serviceOrganizationService.read(serviceOrganization);
        if (serviceOrganization == null) {
            addActionError(getText("error.invalid_id"));
            return SUCCESS;
        }

		buildings = serviceOrganizationService.findServedBuildings(new Stub<ServiceOrganization>(serviceOrganization.getId()), getPager());

        if (buildings == null || buildings.size() == 0) {
            return SUCCESS;
        }

        addresses = new ArrayList<String>();

        for (Building b : buildings) {
            addresses.add(addressService.getBuildingAddress(new Stub<Building>(b), userPreferences.getLocale()));
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

    public List<String> getAddresses() {
        return addresses;
    }

    public List<ServedBuilding> getBuildings() {
        return buildings;
    }

    public ServiceOrganization getServiceOrganization() {
        return serviceOrganization;
    }

    public void setServiceOrganization(ServiceOrganization serviceOrganization) {
        this.serviceOrganization = serviceOrganization;
    }

	@Required
    public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
        this.serviceOrganizationService = serviceOrganizationService;
    }

	@Required
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

}
