package org.flexpay.eirc.sp.impl;

import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.payments.util.ServiceTypesMapper;
import org.flexpay.payments.service.SPService;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;

public class ValidationContext {

	private Map<String, Object> param = org.flexpay.common.util.CollectionUtils.treeMap();
    private ServiceValidationFactory serviceValidationFactory;

    public ValidationContext(ServiceValidationFactory serviceValidationFactory) {
        this.serviceValidationFactory = serviceValidationFactory;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public ServiceTypesMapper getServiceTypesMapper() {
        return serviceValidationFactory.getServiceTypesMapper();
    }

    public SPService getSpService() {
        return serviceValidationFactory.getSpService();
    }

    public CorrectionsService getCorrectionsService() {
        return serviceValidationFactory.getCorrectionsService();
    }

    public Stub<DataSourceDescription> getMegabankSD() {
        return stub(serviceValidationFactory.getMegabankSD());
    }
}
