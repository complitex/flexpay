package org.flexpay.payments.service.registry;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.persistence.Organization;

import java.util.Date;

public interface PaymentsRegistryDBGenerator {

	@Nullable
	Registry createRegistry(@NotNull ServiceProvider serviceProvider,
							  @NotNull Organization registerOrganization, @NotNull DateRange range)
									 throws FlexPayException;
}
