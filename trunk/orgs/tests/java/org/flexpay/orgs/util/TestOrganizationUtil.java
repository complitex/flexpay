package org.flexpay.orgs.util;

import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestOrganizationUtil {

    @Nullable
    Organization create(@NotNull String individualTaxNumber);

    void delete(@NotNull Organization organization);
}
