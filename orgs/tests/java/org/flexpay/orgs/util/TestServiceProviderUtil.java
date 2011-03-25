package org.flexpay.orgs.util;

import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestServiceProviderUtil {

    @Nullable
    ServiceProvider create(@NotNull Organization recipientOrganization);

    void delete(@NotNull ServiceProvider serviceProvider);
}
