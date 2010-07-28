package org.flexpay.payments.util.impl;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.persistence.*;
import org.flexpay.payments.service.DocumentService;
import org.flexpay.payments.service.DocumentStatusService;
import org.flexpay.payments.service.DocumentTypeService;
import org.flexpay.payments.util.TestDocumentUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.math.BigDecimal;

public class PaymentsTestCashPaymentDocumentUtil implements TestDocumentUtil {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    @Resource(name="documentTypeService")
	private DocumentTypeService documentTypeService;
	@Autowired
    @Resource(name="documentStatusService")
	private DocumentStatusService documentStatusService;
	@Autowired
	private DocumentService documentService;

    @Nullable
    @Override
    public Document create(@NotNull Organization serviceProviderOrganization,
                           @NotNull Organization collectorOrganization,
                           @NotNull Operation operation,
                           @NotNull Service service,
                           long sum) {
        //get document type
        DocumentType documentType = null;
        try {
            documentType = documentTypeService.read(DocumentType.CASH_PAYMENT);
        } catch (FlexPayException e) {
            log.error("Can not get document type", e);
        }
        if (documentType == null) {
            log.error("Did not find document type");
            return null;
        }
        //get document status
        DocumentStatus documentStatus = null;
        try {
            documentStatus = documentStatusService.read(DocumentStatus.REGISTERED);
        } catch (FlexPayException e) {
            log.error("Can not get document status", e);
        }
        if (documentStatus == null) {
            log.error("Did not find document status");
            return null;
        }

        Document document = new Document();
		document.setSum(new BigDecimal(sum));
		document.setDocumentStatus(documentStatus);
		document.setOperation(operation);
		document.setCreditorOrganization(serviceProviderOrganization);
		document.setDebtorOrganization(collectorOrganization);
		document.setService(service);
		document.setAddress("Test address");
		document.setLastName("Test Last Name");
		document.setMiddleName("Test Middle Name");
		document.setFirstName("Test First Name");
		document.setTownName("Test Town Name");
        document.setTownType("Test Town Type");
		document.setStreetType("Test Street Type");
		document.setStreetName("Test Street Name");
		document.setBuildingNumber("00");
		document.setBuildingBulk("1234567890");
		document.setApartmentNumber("1");

		document.setDocumentType(documentType);
		document.setOperation(operation);

        documentService.create(document);

        return document;
    }

    @Override
    public void delete(@NotNull Document document) {
        documentService.delete(Stub.stub(document));
    }
}
