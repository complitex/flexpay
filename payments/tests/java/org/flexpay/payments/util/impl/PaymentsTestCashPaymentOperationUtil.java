package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Cashbox;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.OperationLevelService;
import org.flexpay.payments.service.OperationService;
import org.flexpay.payments.service.OperationStatusService;
import org.flexpay.payments.service.OperationTypeService;
import org.flexpay.payments.util.TestDocumentUtil;
import org.flexpay.payments.util.TestOperationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.Date;

public class PaymentsTestCashPaymentOperationUtil implements TestOperationUtil {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier ("operationTypeService")
	private OperationTypeService operationTypeService;
	@Autowired
    @Qualifier ("operationLevelService")
	private OperationLevelService operationLevelService;
	@Autowired
    @Qualifier ("operationStatusService")
	private OperationStatusService operationStatusService;
    @Autowired
    private OperationService operationService;
    @Autowired
    @Qualifier ("paymentsTestCashPaymentDocumentUtil")
    private TestDocumentUtil documentUtil;

    @Override
    public Operation create(@NotNull PaymentPoint paymentPoint, long sum) {
        OperationType operationType = null;
        OperationLevel operationLevel = null;
        OperationStatus operationStatus = null;
        try {
            operationType = operationTypeService.read(OperationType.SERVICE_CASH_PAYMENT);
            operationLevel = operationLevelService.read(OperationLevel.LOW);
            operationStatus = operationStatusService.read(OperationStatus.REGISTERED);
        } catch (FlexPayException e) {
            log.error("Can not get properties", e);
        }
        if (operationType == null || operationLevel == null || operationStatus == null) {
            log.error("Empty operation properties: type={}, level={}, status={}", new Object[]{operationType, operationLevel, operationStatus});
            return null;
        }

        Operation operation = new Operation();
		operation.setOperationSum(new BigDecimal(sum));
		operation.setCreatorUserName("test");
		operation.setCreationDate(new Date());
		operation.setOperationType(operationType);
		operation.setCreatorOrganization(paymentPoint.getCollector().getOrganization());
		operation.setOperationLevel(operationLevel);
		operation.setOperationStatus(operationStatus);
		operation.setPaymentPoint(paymentPoint);
		operation.setRegisterOrganization(paymentPoint.getCollector().getOrganization());
		operation.setRegisterDate(new Date());
		operation.setRegisterUserName("test");

        operationService.create(operation);

        return operation;
    }

    @Nullable
    @Override
    public Operation create(@NotNull Cashbox cashbox, long sum) {
        Operation operation = create(cashbox.getPaymentPoint(), sum);
        if (operation != null) {
            operation.setCashbox(cashbox);
            operationService.update(operation);
        }
        return operation;
    }

    @Nullable
    @Override
    public Document addDocument(@NotNull Operation operation, @NotNull Service service, long sum) {
        Document document = documentUtil.create(service.getServiceProvider().getOrganization(),
                            operation.getRegisterOrganization(), operation, service, sum);
        operation.addDocument(document);
        operationService.update(operation);

        return document;
    }

    @Override
    public void delete(@NotNull Operation operation) {
        for (Document document : operation.getDocuments()) {
            documentUtil.delete(document);
        }
        operationService.delete(Stub.stub(operation));
    }
}
