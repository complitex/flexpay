package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentPointName;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.util.TestPaymentPointUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

public class PaymentsTestPaymentPointUtil implements TestPaymentPointUtil {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Resource(name="paymentsTestPaymentCollectorUtil")
    private PaymentsTestPaymentCollectorUtil paymentCollectorUtil;
    @Autowired
    @Resource(name="paymentPointService")
    private PaymentPointService paymentPointService;
    @Autowired
    @Resource(name="languageService")
	private LanguageService languageService;

    @Nullable
    @Override
    public PaymentPoint create(@NotNull Organization registerOrganization) {
        Language lang = languageService.getLanguage("ru");
        if (lang == null) {
            log.error("Language did not find");
            return null;
        }

        PaymentCollector paymentCollector = paymentCollectorUtil.create(registerOrganization);
        if (paymentCollector == null) {
            return null;
        }

        PaymentPoint paymentPoint = new PaymentPoint();
        paymentPoint.setStatus(PaymentPoint.STATUS_ACTIVE);
        paymentPoint.setAddress("address");

        PaymentPointName paymentPointName = new PaymentPointName();
        paymentPointName.setLang(lang);
        paymentPointName.setName("payment point name");

        paymentPoint.setName(paymentPointName);

        paymentPoint.setCollector(paymentCollector);
        try {
            return paymentPointService.create(paymentPoint);
        } catch (FlexPayExceptionContainer flexPayExceptionContainer) {
            log.error("Did not create payment point. Remove payment collector", flexPayExceptionContainer);
            paymentCollectorUtil.delete(paymentCollector);
        }
        return null;
    }

    @Override
    public void delete(@NotNull PaymentPoint paymentPoint) {
        paymentPointService.delete(paymentPoint);
        paymentCollectorUtil.delete(paymentPoint.getCollector());
    }
}