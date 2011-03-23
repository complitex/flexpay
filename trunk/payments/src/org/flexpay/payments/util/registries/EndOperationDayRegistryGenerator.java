package org.flexpay.payments.util.registries;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public interface EndOperationDayRegistryGenerator {

	Registry generate(@NotNull PaymentCollector paymentCollector, @NotNull Organization organization,
							 @NotNull Date beginDate, @NotNull Date endDate) throws FlexPayException;

}
