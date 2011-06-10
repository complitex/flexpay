package org.flexpay.payments.util.impl;

import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.util.impl.CommonTestRegistryUtil;
import org.flexpay.payments.service.RegistryDeliveryHistoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentsTestRegistryUtil extends CommonTestRegistryUtil {

    @Autowired
    private RegistryDeliveryHistoryService registryDeliveryHistoryService;

    @Override
    protected void deleteRegistryDependences(@NotNull Stub<Registry> stub) {
        registryDeliveryHistoryService.delete(stub);
        super.deleteRegistryDependences(stub);
    }
}
