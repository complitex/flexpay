package org.flexpay.payments.util;

import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestServiceUtil {
    @Nullable
    Service create(@NotNull ServiceProvider serviceProvider, int serviceTypeCode);

    void delete(@NotNull Service service);
}
