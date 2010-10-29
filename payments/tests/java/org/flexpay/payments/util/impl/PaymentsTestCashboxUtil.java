package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayExceptionContainer;
import org.flexpay.common.persistence.Language;
import org.flexpay.common.service.LanguageService;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.CashboxNameTranslation;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.CashboxService;
import org.flexpay.orgs.util.TestCashboxUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class PaymentsTestCashboxUtil implements TestCashboxUtil {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier ("cashboxService")
    private CashboxService cashboxService;

    @Autowired
    @Qualifier ("languageService")
	private LanguageService languageService;

    @Nullable
    @Override
    public Cashbox create(@NotNull PaymentPoint paymentPoint, @NotNull String name) {
        Language lang = languageService.getLanguage("ru");
        if (lang == null) {
            log.error("Language did not find");
            return null;
        }

        Cashbox cashbox = new Cashbox();
        cashbox.setPaymentPoint(paymentPoint);
        cashbox.setName(new CashboxNameTranslation(name, lang));
        try {
            return cashboxService.create(cashbox);
        } catch (FlexPayExceptionContainer flexPayExceptionContainer) {
            log.error("Did not create cashbox", flexPayExceptionContainer);
        }
        return null;
    }

    @Override
    public void delete(@NotNull Cashbox cashbox) {
        cashboxService.delete(cashbox);
    }
}
