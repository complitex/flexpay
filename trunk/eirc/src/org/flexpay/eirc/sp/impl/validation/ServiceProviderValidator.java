package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messager;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.jetbrains.annotations.NotNull;

public class ServiceProviderValidator extends MessageValidatorWithContext<String> {
    public static String SERVICE_PROVIDER_ID = "ServiceProviderId";

    public ServiceProviderValidator(@NotNull Messager mess, @NotNull ValidationContext context) {
        super(mess, context);
    }

    public boolean validate(@NotNull String externalId) {
        Stub<ServiceProvider> providerStub = context.getCorrectionsService().findCorrection(
				externalId, ServiceProvider.class, context.getMegabankSD());
		if (providerStub == null) {
			addErrorMessage("No service provider correction with id ", externalId);
            return false;
		}
        context.getParam().put(SERVICE_PROVIDER_ID, providerStub.getId());
		return true;
    }
}
