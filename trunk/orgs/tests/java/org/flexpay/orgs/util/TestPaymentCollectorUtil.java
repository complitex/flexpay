package org.flexpay.orgs.util;

import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestPaymentCollectorUtil {

    @Nullable
    PaymentCollector create(@NotNull Organization registerOrganization);

    void delete(@NotNull PaymentCollector paymentCollector);
}
