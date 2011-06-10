package org.flexpay.payments.service.registry;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.persistence.Organization;
import org.jetbrains.annotations.NotNull;

import java.security.Signature;

public interface PaymentsRegistryMBGenerator {

	Registry exportToMegaBank(@NotNull Registry registry, @NotNull FPFile file, @NotNull Organization organization, Signature signature)
								throws FlexPayException;
}
