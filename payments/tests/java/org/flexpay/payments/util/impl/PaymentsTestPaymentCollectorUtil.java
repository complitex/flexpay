package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentCollectorDescription;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.util.TestPaymentCollectorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class PaymentsTestPaymentCollectorUtil implements TestPaymentCollectorUtil {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Resource(name="paymentCollectorService")
    private PaymentCollectorService paymentCollectorService;

    @Autowired
    @Resource(name="languageService")
	private LanguageService languageService;

    @Nullable
    @Override
    public PaymentCollector create(@NotNull Organization registerOrganization) {
        Language lang = languageService.getLanguage("ru");
        if (lang == null) {
            log.error("Language did not find");
            return null;
        }

        PaymentCollector paymentCollector = new PaymentCollector();
        paymentCollector.setOrganization(registerOrganization);

        PaymentCollectorDescription paymentCollectorDescription = new PaymentCollectorDescription();
        paymentCollectorDescription.setLang(lang);
        paymentCollectorDescription.setName("payments collector description");
        paymentCollector.setDescription(paymentCollectorDescription);

        try {
            return paymentCollectorService.create(paymentCollector);
        } catch (FlexPayExceptionContainer flexPayExceptionContainer) {
            log.error("Did not create payment collector", flexPayExceptionContainer);
        }
        return null;
    }

    @Override
    public void delete(@NotNull PaymentCollector paymentCollector) {
        paymentCollectorService.delete(paymentCollector);
    }
}
