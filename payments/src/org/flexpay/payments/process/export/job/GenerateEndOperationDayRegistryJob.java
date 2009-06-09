package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.util.FPFileUtil;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.util.registries.EndOperationDayRegistryGenerator;
import org.flexpay.payments.util.registries.ExportBankPaymentsRegistry;
import org.springframework.beans.factory.annotation.Required;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class GenerateEndOperationDayRegistryJob extends Job {

	private PaymentPointService paymentPointService;
	private OrganizationService organizationService;
    private EndOperationDayRegistryGenerator registryGenerator;
    private ExportBankPaymentsRegistry exportBankPaymentsRegistry;

    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		log.info("Start generating end operation day registry and save it to file process...");

		Long pointId = (Long) parameters.get("paymentPointId");
		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(pointId));
		if (paymentPoint == null) {
			log.error("Payment point with id - {} does not exist", pointId);
			return RESULT_ERROR;
		}
		log.debug("Found paymentPoint - {}", paymentPoint);

		Long organizationId = (Long) parameters.get("organizationId");
		Organization organization = organizationService.readFull(new Stub<Organization>(organizationId));
		if (organization == null) {
			log.error("Organization with id - {} does not exist", organizationId);
			return RESULT_ERROR;
		}
		log.debug("Found organization - {}", organization);

		Date beginDate = (Date) parameters.get("beginDate");
		Date endDate = (Date) parameters.get("endDate");

		Registry registry = registryGenerator.generate(paymentPoint, organization, beginDate, endDate);

		registry = exportBankPaymentsRegistry.export(registry);
        parameters.put("File", registry.getSpFile());
        parameters.put("Email", paymentPoint.getEmail());

		log.info("End operation day registry and save it to file process finished...");

        return RESULT_NEXT;
    }

	@Required
	public void setPaymentPointService(PaymentPointService paymentPointService) {
		this.paymentPointService = paymentPointService;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setRegistryGenerator(EndOperationDayRegistryGenerator registryGenerator) {
		this.registryGenerator = registryGenerator;
	}

	@Required
	public void setExportBankPaymentsRegistry(ExportBankPaymentsRegistry exportBankPaymentsRegistry) {
		this.exportBankPaymentsRegistry = exportBankPaymentsRegistry;
	}

}
