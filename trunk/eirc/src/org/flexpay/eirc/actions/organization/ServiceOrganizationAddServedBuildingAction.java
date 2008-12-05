package org.flexpay.eirc.actions.organization;

import org.apache.commons.collections.ArrayStack;
import org.flexpay.ab.actions.buildings.BuildingsActionsBase;
import org.flexpay.ab.persistence.Buildings;
import org.flexpay.ab.persistence.filters.CountryFilter;
import org.flexpay.ab.persistence.filters.RegionFilter;
import org.flexpay.ab.persistence.filters.StreetNameFilter;
import org.flexpay.ab.persistence.filters.TownFilter;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.persistence.filter.PrimaryKeyFilter;
import org.flexpay.common.service.ParentService;
import static org.flexpay.common.util.CollectionUtils.list;
import org.flexpay.eirc.persistence.ServedBuilding;
import org.flexpay.eirc.persistence.ServiceOrganization;
import org.flexpay.eirc.service.ServiceOrganizationService;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceOrganizationAddServedBuildingAction extends BuildingsActionsBase {

    private ServiceOrganizationService serviceOrganizationService;
    private ParentService parentService;

    private CountryFilter countryFilter = new CountryFilter();
    private RegionFilter regionFilter = new RegionFilter();
    private TownFilter townFilter = new TownFilter();
    private StreetNameFilter streetNameFilter = new StreetNameFilter();
    private Page pager = new Page();

    private ServiceOrganization serviceOrganization = new ServiceOrganization();
    private List<Buildings> buildingsList = list();
    private Set<Long> objectIds = new HashSet<Long>();

    public ServiceOrganizationAddServedBuildingAction() {
        streetNameFilter.setShowSearchString(true);
    }

    @NotNull
    @Override
	public String doExecute() throws Exception {

        if (serviceOrganization.getId() == null) {
            addActionError(getText("error.no_id"));
            return REDIRECT_SUCCESS;
        }

        serviceOrganization = serviceOrganizationService.read(serviceOrganization);
        if (serviceOrganization == null) {
            addActionError(getText("error.invalid_id"));
            return REDIRECT_SUCCESS;
        }

        // prepare initial setup
        if (!isSubmit()) {
            ArrayStack filterArrayStack = getFilters();
            for (Object filter : filterArrayStack) {
                ((PrimaryKeyFilter) filter).initFilter(session);
            }

            ArrayStack filters = parentService.initFilters(filterArrayStack, userPreferences.getLocale());
            setFilters(filters);

            buildingsList = serviceOrganizationService.getBuildings(filters, serviceOrganization, pager);

            return INPUT;
        }

        log.info("Served building ids: " + objectIds);

        List<ServedBuilding> servedBuildingList = serviceOrganizationService.findServedBuildings(objectIds);

        for (ServedBuilding sb : servedBuildingList) {
            sb.setServiceOrganization(serviceOrganization);
            serviceOrganizationService.saveServedBuilding(sb);
        }

		return REDIRECT_SUCCESS;
	}

    /**
     * Get default error execution result
     * <p/>
     * If return code starts with a {@link #PREFIX_REDIRECT} all error messages are stored in a session
     *
     * @return {@link #ERROR} by default
     */
    @NotNull
    protected String getErrorResult() {
        return INPUT;
    }

    /**
     * Getter for property 'filters'.
     *
     * @return Value for property 'filters'.
     */
    public ArrayStack getFilters() {

        ArrayStack filters = new ArrayStack();
        filters.push(countryFilter);
        filters.push(regionFilter);
        filters.push(townFilter);
        filters.push(streetNameFilter);

        return filters;
    }

    /**
     * Setter for property 'filters'.
     *
     * @param filters Value to set for property 'filters'.
     */
    public void setFilters(ArrayStack filters) {
        countryFilter = (CountryFilter) filters.peek(3);
        regionFilter = (RegionFilter) filters.peek(2);
        townFilter = (TownFilter) filters.peek(1);
        streetNameFilter = (StreetNameFilter) filters.peek(0);
    }

    public ServiceOrganization getServiceOrganization() {
        return serviceOrganization;
    }

    public void setServiceOrganization(ServiceOrganization serviceOrganization) {
        this.serviceOrganization = serviceOrganization;
    }

    public Set<Long> getObjectIds() {
        return objectIds;
    }

    public void setObjectIds(Set<Long> objectIds) {
        this.objectIds = objectIds;
    }

    public List<Buildings> getBuildingsList() {
        return buildingsList;
    }

    public CountryFilter getCountryFilter() {
        return countryFilter;
    }

    public void setCountryFilter(CountryFilter countryFilter) {
        this.countryFilter = countryFilter;
    }

    public RegionFilter getRegionFilter() {
        return regionFilter;
    }

    public void setRegionFilter(RegionFilter regionFilter) {
        this.regionFilter = regionFilter;
    }

    public TownFilter getTownFilter() {
        return townFilter;
    }

    public void setTownFilter(TownFilter townFilter) {
        this.townFilter = townFilter;
    }

    public StreetNameFilter getStreetNameFilter() {
        return streetNameFilter;
    }

    public void setStreetNameFilter(StreetNameFilter streetNameFilter) {
        this.streetNameFilter = streetNameFilter;
    }

    public Page getPager() {
        return pager;
    }

    public void setPager(Page pager) {
        this.pager = pager;
    }

    public void setParentService(ParentService parentService) {
        this.parentService = parentService;
    }

    public void setServiceOrganizationService(ServiceOrganizationService serviceOrganizationService) {
        this.serviceOrganizationService = serviceOrganizationService;
    }

}
