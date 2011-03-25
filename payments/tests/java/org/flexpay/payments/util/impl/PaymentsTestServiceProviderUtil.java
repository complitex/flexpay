package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.LanguageService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.flexpay.orgs.service.ServiceProviderAttributeService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.util.TestServiceProviderUtil;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

public class PaymentsTestServiceProviderUtil implements TestServiceProviderUtil {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier ("languageService")
	private LanguageService languageService;

    @Autowired
    @Qualifier ("serviceProviderService")
    private ServiceProviderService tProviderService;

    @Autowired
    @Qualifier ("spService")
	private SPService spService;

    @Autowired
    @Qualifier ("paymentsTestServiceUtil")
    private PaymentsTestServiceUtil serviceUtil;

    @Autowired
    @Qualifier ("serviceProviderServiceAttribute")
    private ServiceProviderAttributeService serviceProviderAttributeService;

    @Override
    public ServiceProvider create(@NotNull Organization recipientOrganization) {
        Language lang = languageService.getLanguage("ru");
        if (lang == null) {
            log.error("Language did not find");
            return null;
        }

        ServiceProvider serviceProvider = new ServiceProvider();
		serviceProvider.setOrganization(recipientOrganization);
		serviceProvider.setStatus(ServiceProvider.STATUS_ACTIVE);
		serviceProvider.setEmail("test@test.ru");

		ServiceProviderDescription serviceProviderDescription = new ServiceProviderDescription();
		serviceProviderDescription.setLang(lang);
		serviceProviderDescription.setName("test service1 provider description");
		serviceProvider.setDescription(serviceProviderDescription);

        try {
            return tProviderService.create(serviceProvider);
        } catch (FlexPayExceptionContainer flexPayExceptionContainer) {
            log.error("Did not create service provider");
        }

        return null;
    }

    @Override
    public void delete(@NotNull ServiceProvider serviceProvider) {
        List<Service> services = spService.findServices(Stub.stub(serviceProvider));
        for (Service service : services) {
            serviceUtil.delete(service);
        }
        serviceProviderAttributeService.delete(Stub.stub(serviceProvider));
        tProviderService.delete(serviceProvider);
    }
}
