package org.flexpay.eirc.actions.eircaccount;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.ApartmentService;
import org.flexpay.common.actions.FPActionSupport;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.service.ConsumerInfoService;
import org.flexpay.eirc.service.EircAccountService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.util.config.ApplicationConfig.getDefaultLanguage;

public class EircAccountCreateAction extends FPActionSupport {

    private String consumerFio;
    private Long apartmentFilter;
    private EircAccount eircAccount = new EircAccount();

    private EircAccountService eircAccountService;
    private ConsumerInfoService consumerInfoService;
    private ApartmentService apartmentService;
    
    @NotNull
    @Override
    public String doExecute() throws FlexPayExceptionContainer {

        if (isNotSubmit()) {
            return INPUT;
        }

        if (isEmpty(consumerFio)) {
            log.debug("Consumer FIO can't be empty");
            addActionError(getText("eirc.error.account.create.no_person"));
            return INPUT;
        }

        if (apartmentFilter == null || apartmentFilter <= 0) {
            log.debug("Apartment not set, returning to input form");
            addActionError(getText("eirc.error.account.create.no_apartment"));
            return INPUT;
        }

        Stub<Apartment> apStub = new Stub<Apartment>(apartmentFilter);
        
        Apartment apartment = apartmentService.readFullWithHierarchy(apStub);
        if (apartment == null) {
            log.debug("Can't get apartment with id {}", apartmentFilter);
            addActionError(getText("eirc.error.account.create.no_apartment"));
            return INPUT;
        }

        EircAccount ac = eircAccountService.findAccount(apStub);
        if (ac != null) {
            log.debug("Can't create EIRC account: for this apartment it already exists (id = {})", apartmentFilter);
            addActionError(getText("eirc.error.account.create.duplicate2"));
            return INPUT;
        }

        eircAccount.setApartment(apartment);

        Language lang = getDefaultLanguage();

        ConsumerInfo info = new ConsumerInfo();
        info.setFirstName("");
        info.setMiddleName("");
        info.setLastName(consumerFio.toUpperCase());
        info.setTownName(apartment.getTown().getCurrentName().getTranslation(lang).getName());
        info.setStreetTypeName(apartment.getDefaultStreet().getCurrentType().getTranslation(lang).getName());
        info.setStreetName(apartment.getDefaultStreet().getCurrentName().getTranslation(lang).getName());
        info.setBuildingNumber(apartment.getDefaultBuildings().getNumber());
        info.setBuildingBulk(apartment.getDefaultBuildings().getBulk());
        info.setApartmentNumber(apartment.getNumber());

        consumerInfoService.save(info);

        eircAccount.setConsumerInfo(info);

        eircAccountService.create(eircAccount);

        log.info("Eirc account with id {} creatred", eircAccount.getId());

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
    @Override
    protected String getErrorResult() {
        return INPUT;
    }

    public Long getApartmentFilter() {
        return apartmentFilter;
    }

    public void setApartmentFilter(Long apartmentFilter) {
        this.apartmentFilter = apartmentFilter;
    }

    public String getConsumerFio() {
        return consumerFio;
    }

    public void setConsumerFio(String consumerFio) {
        this.consumerFio = consumerFio;
    }

    public EircAccount getEircAccount() {
        return eircAccount;
    }

    @Required
    public void setEircAccountService(EircAccountService eircAccountService) {
        this.eircAccountService = eircAccountService;
    }

    @Required
    public void setConsumerInfoService(ConsumerInfoService consumerInfoService) {
        this.consumerInfoService = consumerInfoService;
    }

    @Required
    public void setApartmentService(ApartmentService apartmentService) {
        this.apartmentService = apartmentService;
    }
}
