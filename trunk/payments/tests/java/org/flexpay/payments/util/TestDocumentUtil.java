package org.flexpay.payments.util;

import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.Document;
import org.flexpay.payments.persistence.Operation;
import org.flexpay.payments.persistence.Service;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TestDocumentUtil {
    @Nullable
    Document create(@NotNull Organization serviceProviderOrganization,
                    @NotNull Organization collectorOrganization,
                    @NotNull Operation operation,
                    @NotNull Service service,
                    long sum);

    void delete(@NotNull Document document);
}
