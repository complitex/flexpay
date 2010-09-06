package org.flexpay.eirc.actions.eircaccount;

import org.flexpay.ab.persistence.filters.PersonSearchFilter;
import org.flexpay.common.actions.FPActionWithPagerSupport;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.sorter.EircAccountSorterByAccountNumber;
import org.flexpay.eirc.persistence.sorter.EircAccountSorterByAddress;

public abstract class EircAccountAction extends FPActionWithPagerSupport<EircAccount> {

    protected Long apartmentFilter;
    protected Long buildingFilter;
    protected Long streetFilter;
    protected Long townFilter;
    protected Long regionFilter;
    protected Long countryFilter;
    protected PersonSearchFilter personSearchFilter = new PersonSearchFilter();
    protected Integer output = 1;
    protected EircAccount eircAccount = new EircAccount();

    protected EircAccountSorterByAccountNumber eircAccountSorterByAccountNumber = new EircAccountSorterByAccountNumber();
    protected EircAccountSorterByAddress eircAccountSorterByAddress = new EircAccountSorterByAddress();

    public Long getApartmentFilter() {
        return apartmentFilter;
    }

    public void setApartmentFilter(Long apartmentFilter) {
        this.apartmentFilter = apartmentFilter;
    }

    public Long getBuildingFilter() {
        return buildingFilter;
    }

    public void setBuildingFilter(Long buildingFilter) {
        this.buildingFilter = buildingFilter;
    }

    public Long getStreetFilter() {
        return streetFilter;
    }

    public void setStreetFilter(Long streetFilter) {
        this.streetFilter = streetFilter;
    }

    public Long getTownFilter() {
        return townFilter;
    }

    public void setTownFilter(Long townFilter) {
        this.townFilter = townFilter;
    }

    public Long getRegionFilter() {
        return regionFilter;
    }

    public void setRegionFilter(Long regionFilter) {
        this.regionFilter = regionFilter;
    }

    public Long getCountryFilter() {
        return countryFilter;
    }

    public void setCountryFilter(Long countryFilter) {
        this.countryFilter = countryFilter;
    }

    public PersonSearchFilter getPersonSearchFilter() {
        return personSearchFilter;
    }

    public void setPersonSearchFilter(PersonSearchFilter personSearchFilter) {
        this.personSearchFilter = personSearchFilter;
    }

    public Integer getOutput() {
        return output;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }

    public EircAccount getEircAccount() {
        return eircAccount;
    }

    public void setEircAccount(EircAccount eircAccount) {
        this.eircAccount = eircAccount;
    }

    public EircAccountSorterByAccountNumber getEircAccountSorterByAccountNumber() {
        return eircAccountSorterByAccountNumber;
    }

    public void setEircAccountSorterByAccountNumber(EircAccountSorterByAccountNumber eircAccountSorterByAccountNumber) {
        this.eircAccountSorterByAccountNumber = eircAccountSorterByAccountNumber;
    }

    public EircAccountSorterByAddress getEircAccountSorterByAddress() {
        return eircAccountSorterByAddress;
    }

    public void setEircAccountSorterByAddress(EircAccountSorterByAddress eircAccountSorterByAddress) {
        this.eircAccountSorterByAddress = eircAccountSorterByAddress;
    }

}
