package org.flexpay.payments.util;

import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestOperationUtil {
    @Nullable
    Operation create(@NotNull PaymentPoint paymentPoint, long summ);

    @Nullable
    Operation create(@NotNull Cashbox cashbox, long summ);

    @Nullable
    Document addDocument(@NotNull Operation operation,
                         @NotNull Service service,
                         long summ);

    void delete(@NotNull Operation operation);
}
