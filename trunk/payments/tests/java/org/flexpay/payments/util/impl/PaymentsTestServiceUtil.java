package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceDescription;
import org.flexpay.payments.persistence.ServiceType;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.service.ServiceTypeService;
import org.flexpay.payments.util.TestServiceUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;

public class PaymentsTestServiceUtil implements TestServiceUtil {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier ("languageService")
	private LanguageService languageService;

    @Autowired
    @Qualifier ("serviceTypeService")
	private ServiceTypeService serviceTypeService;

    @Autowired
    @Qualifier ("spService")
	private SPService spService;

    @Override
    public Service create(@NotNull ServiceProvider serviceProvider, int serviceTypeCode) {
        Language lang = languageService.getLanguage("ru");
        if (lang == null) {
            log.error("Language did not find");
            return null;
        }

        ServiceType serviceType = serviceTypeService.getServiceType(serviceTypeCode);

		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setLang(lang);
		serviceDescription.setName("description");

		Service service = new Service();
		service.setBeginDate(new Date());
		service.setEndDate(new Date(service.getBeginDate().getTime() + 100000));
		service.setServiceType(serviceType);
		service.setExternalCode(String.valueOf(serviceTypeCode));
		service.setDescription(serviceDescription);
		service.setServiceProvider(serviceProvider);
        try {
            return spService.create(service);
        } catch (FlexPayExceptionContainer flexPayExceptionContainer) {
            log.error("Did not create service", flexPayExceptionContainer);
        }
        return null;
    }

    @Override
    public void delete(@NotNull Service service) {
        spService.delete(service);
    }
}
