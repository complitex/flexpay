package org.flexpay.orgs.util;

import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestPaymentPointUtil {
    @Nullable
    PaymentPoint create(@NotNull Organization registerOrganization);

    void delete(@NotNull PaymentPoint paymentPoint);
}
