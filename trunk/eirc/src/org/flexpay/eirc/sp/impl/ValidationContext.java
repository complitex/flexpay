package org.flexpay.eirc.sp.impl;

import org.flexpay.common.persistence.DataSourceDescription;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.service.importexport.CorrectionsService;
import org.flexpay.payments.service.SPService;
import org.flexpay.payments.util.ServiceTypesMapper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;

public class ValidationContext {

	private Map<String, Object> param = org.flexpay.common.util.CollectionUtils.treeMap();
    private ServiceValidationFactory serviceValidationFactory;
    private LineParser lineParser;

    public ValidationContext(@NotNull ServiceValidationFactory serviceValidationFactory, @NotNull LineParser lineParser) {
        this.serviceValidationFactory = serviceValidationFactory;
        this.lineParser = lineParser;
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

    public LineParser getLineParser() {
        return lineParser;
    }
}
