package org.flexpay.eirc.action.eircaccount;

import org.flexpay.ab.persistence.Apartment;
import org.flexpay.ab.service.AddressService;
import org.flexpay.common.dao.paging.Page;
import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.filter.ObjectFilter;
import org.flexpay.eirc.persistence.Consumer;
import org.flexpay.eirc.persistence.ConsumerInfo;
import org.flexpay.eirc.persistence.EircAccount;
import org.flexpay.eirc.persistence.consumer.ConsumerAttribute;
import org.flexpay.eirc.persistence.consumer.ConsumerAttributeTypeBase;
import org.flexpay.eirc.service.ConsumerAttributeTypeService;
import org.flexpay.eirc.service.ConsumerInfoService;
import org.flexpay.eirc.service.ConsumerService;
import org.flexpay.eirc.service.EircAccountService;
import org.flexpay.payments.action.outerrequest.request.response.data.ConsumerAttributes;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.filters.ServiceFilter;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.common.util.config.ApplicationConfig.getFutureInfinite;

public class EircAccountAddConsumerAction extends EircAccountAction {

    private ServiceFilter serviceFilter = new ServiceFilter();
    private String consumerFio;
    private String externalAccount;
    private String ercAccount;

    private Service service;

    private SPService spService;
    private ConsumerInfoService consumerInfoService;
    private ConsumerService consumerService;
	private EircAccountService eircAccountService;
    private AddressService addressService;
    private ConsumerAttributeTypeService consumerAttributeTypeService;

	@NotNull
	@Override
	public String doExecute() {

		if (eircAccount == null || eircAccount.isNew()) {
            log.error("Incorrect eircAccount id");
			addActionError(getText("common.error.invalid_id"));
			return REDIRECT_SUCCESS;
		}

        Stub<EircAccount> stub = stub(eircAccount);
        eircAccount = eircAccountService.read(stub);
        if (eircAccount == null) {
            addActionError(getText("eirc.error.account_not_found"));
            log.error("Can't get eirc account with id {} from DB", stub.getId());
            return REDIRECT_SUCCESS;
        }

        if (isSubmit()) {
            if (!doValidate()) {
                return INPUT;
            }

            saveConsumer();

            if (hasActionErrors()) {
                return INPUT;
            }
            addActionMessage(getText("eirc.eirc_account.consumer_added"));

            return REDIRECT_SUCCESS;
        }

        serviceFilter.setServices(spService.listServices(new ArrayList<ObjectFilter>(), new Page<Service>(10000, 1)));

		return INPUT;
	}

    private void saveConsumer() {

        ConsumerInfo eircInfo = eircAccount.getConsumerInfo();
        ConsumerInfo info = new ConsumerInfo();
        info.setFirstName("");
        info.setMiddleName("");
        info.setLastName(consumerFio.toUpperCase());
        info.setTownName(eircInfo.getTownName());
        info.setStreetTypeName(eircInfo.getStreetTypeName());
        info.setStreetName(eircInfo.getStreetName());
        info.setBuildingNumber(eircInfo.getBuildingNumber());
        info.setBuildingBulk(eircInfo.getBuildingBulk());
        info.setApartmentNumber(eircInfo.getApartmentNumber());

        consumerInfoService.save(info);

        Date creatingDate = new Date();

        Consumer consumer = new Consumer();
        consumer.setService(service);
        consumer.setExternalAccountNumber(externalAccount);
        consumer.setEircAccount(eircAccount);
        consumer.setBeginDate(creatingDate);
        consumer.setEndDate(getFutureInfinite());
        consumer.setApartment(eircAccount.getApartment());
        consumer.setResponsiblePerson(eircAccount.getPerson());
        consumer.setConsumerInfo(info);

        List<ConsumerAttributeTypeBase> attributeTypes = consumerAttributeTypeService.getByUniqueCode(ConsumerAttributes.EIRC_ATTRIBUTES);

        for (ConsumerAttributeTypeBase type : attributeTypes) {
            ConsumerAttribute attribute = new ConsumerAttribute();
            attribute.setType(type);
            attribute.setValue(ConsumerAttributes.CALCULATION_ATTRIBUTES.get(type.getUniqueCode()));
            consumer.setTmpAttributeForDate(attribute, creatingDate);
        }

        ConsumerAttributeTypeBase ercAccountAttrType = consumerAttributeTypeService.readByCode(ConsumerAttributes.ATTR_ERC_ACCOUNT);

        ConsumerAttribute attribute = new ConsumerAttribute();
        attribute.setType(ercAccountAttrType);
        attribute.setStringValue(ercAccount);
        consumer.setTmpAttributeForDate(attribute, creatingDate);

        try {
            consumerService.save(consumer);
        } catch (FlexPayExceptionContainer e) {
            log.error("Can't create consumer", e);
            addActionError(getText("eirc.error.account.cant_create_consumer"));
        }

    }

    private boolean doValidate() {

        if (serviceFilter == null || serviceFilter.getSelectedId() == null || serviceFilter.getSelectedId() <= 0) {
            log.warn("Incorrect service id in filter ({})", serviceFilter);
            addActionError(getText("eirc.error.account.service_not_set"));
            serviceFilter = new ServiceFilter();
        } else {
            service = spService.readFull(new Stub<Service>(serviceFilter.getSelectedId()));
            if (service == null) {
                log.warn("Can't get service with id {} from DB", serviceFilter.getSelectedId());
                addActionError(getText("eirc.error.account.service_not_found"));
                serviceFilter = new ServiceFilter();
            } else if (service.isNotActive()) {
                log.warn("Service with id {} is disabled", serviceFilter.getSelectedId());
                addActionError(getText("eirc.error.account.service_not_active"));
                serviceFilter = new ServiceFilter();
            }
        }

        if (isEmpty(externalAccount)) {
            log.warn("External account number not set");
            addActionError(getText("eirc.error.account.external_account_not_set"));
        }

        if (isEmpty(ercAccount)) {
            log.warn("ERC account number not set");
            addActionError(getText("eirc.error.account.erc_account_not_set"));
        }

        if (isEmpty(consumerFio)) {
            log.warn("Consumer FIO not set");
            addActionError(getText("eirc.error.account.consumer_fio_not_set"));
        }

        if (hasActionErrors()) {
            serviceFilter.setServices(spService.listServices(new ArrayList<ObjectFilter>(), new Page<Service>(10000, 1)));
            return false;
        }

        return true;
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
		return REDIRECT_SUCCESS;
	}

    public String getAddress(@NotNull Apartment apartment) throws Exception {
        return addressService.getAddress(stub(apartment), getLocale());
    }

    public ServiceFilter getServiceFilter() {
        return serviceFilter;
    }

    public void setServiceFilter(ServiceFilter serviceFilter) {
        this.serviceFilter = serviceFilter;
    }

    public String getConsumerFio() {
        return consumerFio;
    }

    public void setConsumerFio(String consumerFio) {
        this.consumerFio = consumerFio;
    }

    public String getExternalAccount() {
        return externalAccount;
    }

    public void setExternalAccount(String externalAccount) {
        this.externalAccount = externalAccount;
    }

    public String getErcAccount() {
        return ercAccount;
    }

    public void setErcAccount(String ercAccount) {
        this.ercAccount = ercAccount;
    }

    @Required
	public void setEircAccountService(EircAccountService eircAccountService) {
		this.eircAccountService = eircAccountService;
	}

	@Required
	public void setSpService(SPService spService) {
		this.spService = spService;
	}

    @Required
    public void setAddressService(AddressService addressService) {
        this.addressService = addressService;
    }

    @Required
    public void setConsumerService(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @Required
    public void setConsumerInfoService(ConsumerInfoService consumerInfoService) {
        this.consumerInfoService = consumerInfoService;
    }

    @Required
    public void setConsumerAttributeTypeService(ConsumerAttributeTypeService consumerAttributeTypeService) {
        this.consumerAttributeTypeService = consumerAttributeTypeService;
    }

}
