package org.flexpay.eirc.sp.impl.validation;

import org.flexpay.common.persistence.Stub;
import org.flexpay.eirc.sp.impl.MessageValidatorWithContext;
import org.flexpay.eirc.sp.impl.Messenger;
import org.flexpay.eirc.sp.impl.ValidationConstants;
import org.flexpay.eirc.sp.impl.ValidationContext;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.payments.persistence.Service;
import org.flexpay.payments.persistence.ServiceType;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class ServiceCodeValidator extends MessageValidatorWithContext<String> {
    private static final String SERVICE_CODES_SEPARATOR = ";";

    public ServiceCodeValidator(@NotNull Messenger mess, @NotNull ValidationContext context) {
        super(mess, context);
    }

    @Override
    public boolean validate(@NotNull String o) {
        String[] serviceCodes = o.split(SERVICE_CODES_SEPARATOR);
		for (String code : serviceCodes) {
			if ("0".equals(code)) {
				if (serviceCodes.length > 1) {
					addErrorMessage("Service code 0 should be the only one: {}", o);
                    return false;
				}
			} else if (context.getServiceTypesMapper().getInternalType(code) == null) {
				addErrorMessage("Cannot map service type code {} in list {}", new Object[]{code, o});
			} else {
				// ensure services can be found
                Long serviceProviderId = (Long)context.getParam().get(ServiceProviderValidator.SERVICE_PROVIDER_ID);
                Date beginDate = (Date)context.getParam().get(ValidationConstants.MODIFICATIONS_BEGIN_DATE);
				return isInternalServiceExist(serviceProviderId, code, beginDate);
			}
		}
        return true;
    }

    private boolean isInternalServiceExist(Long serviceProviderId, String mbCode, Date date) {
        Stub<ServiceProvider> providerStub = new Stub<ServiceProvider>(serviceProviderId);

		Stub<ServiceType> typeStub = context.getServiceTypesMapper().getInternalType(mbCode);
		List<Service> services = context.getSpService().findServices(providerStub, typeStub);

        if (services.isEmpty()) {
            addErrorMessage("No service found by internal type {}, mb code={}", new Object[]{typeStub, mbCode});
            return false;
        }
        return true;
	}
}
