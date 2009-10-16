package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.LanguageService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.ServiceProviderDescription;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.util.TestServiceProviderUtil;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.service.SPService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

public class PaymentsTestServiceProviderUtil implements TestServiceProviderUtil {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Resource(name="languageService")
	private LanguageService languageService;

    @Autowired
    @Resource(name="serviceProviderService")
    private ServiceProviderService tProviderService;

    @Autowired
    @Resource(name="spService")
	private SPService spService;

    @Autowired
    @Resource(name="paymentsTestServiceUtil")
    private PaymentsTestServiceUtil serviceUtil;

    @Override
    public ServiceProvider create(@NotNull Organization recipientOrganization) {
        Language lang = languageService.getLanguage("ru");
        if (lang == null) {
            log.error("Languge did not find");
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
        tProviderService.delete(serviceProvider);
    }
}
