package org.flexpay.orgs.util;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestPaymentPointUtil {

    @Nullable
    PaymentPoint create(@NotNull Organization registerOrganization);

    @Nullable
    Cashbox addCashbox(@NotNull PaymentPoint paymentPoint, @NotNull String cashboxName);

    void delete(@NotNull PaymentPoint paymentPoint);
}
