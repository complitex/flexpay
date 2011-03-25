package org.flexpay.orgs.util;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestCashboxUtil {

    @Nullable
    Cashbox create(@NotNull PaymentPoint paymentPoint, @NotNull String name);
    
    void delete(@NotNull Cashbox cashbox);
}
