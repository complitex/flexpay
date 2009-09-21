package org.flexpay.payments.service.registry;

import org.jetbrains.annotations.NotNull;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.orgs.persistence.Organization;

import java.security.Signature;

public interface PaymentsRegistryMBGenerator {

	void exportToMegaBank(@NotNull Registry registry, @NotNull FPFile file, @NotNull Organization organization)
								throws FlexPayException;

	void setSignature(Signature signature);
}