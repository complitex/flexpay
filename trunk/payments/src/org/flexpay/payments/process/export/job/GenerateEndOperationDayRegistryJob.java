package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.persistence.PaymentsCollector;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.orgs.service.PaymentsCollectorService;
import static org.flexpay.payments.process.export.job.GeneratePaymentsRegistryParameterNames.*;
import org.flexpay.payments.util.registries.EndOperationDayRegistryGenerator;
import org.flexpay.payments.util.registries.ExportBankPaymentsRegistry;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class GenerateEndOperationDayRegistryJob extends Job {

	public final static String RESULT_NO_REGISTRY_CREATED = "No registry created";
	private PaymentPointService paymentPointService;
	private OrganizationService organizationService;
    private EndOperationDayRegistryGenerator registryGenerator;
    private ExportBankPaymentsRegistry exportBankPaymentsRegistry;
	private PaymentsCollectorService paymentsCollectorService;

	@Override
    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		log.info("Start process generating end operation day registry and save it to file...");

		Long pointId = (Long) parameters.get(PAYMENT_POINT_ID);
		PaymentPoint paymentPoint = paymentPointService.read(new Stub<PaymentPoint>(pointId));
		if (paymentPoint == null) {
			log.error("Payment point with id - {} does not exist", pointId);
			return RESULT_ERROR;
		}
		log.debug("Found paymentPoint - {}", paymentPoint);

		Long organizationId = (Long) parameters.get(ORGANIZATION_ID);
		Organization organization = organizationService.readFull(new Stub<Organization>(organizationId));
		if (organization == null) {
			log.error("Organization with id - {} does not exist", organizationId);
			return RESULT_ERROR;
		}
		log.debug("Found organization - {}", organization);

		Date beginDate = (Date) parameters.get(BEGIN_DATE);
		Date endDate = (Date) parameters.get(END_DATE);

		Registry registry = registryGenerator.generate(paymentPoint, organization, beginDate, endDate);

		if (registry != null) {
			registry = exportBankPaymentsRegistry.generateAndAttachFile(registry);
			parameters.put(FILE_ID, registry.getSpFile().getId());
			PaymentsCollector paymentsCollector = paymentsCollectorService.read(new Stub<PaymentsCollector>(paymentPoint.getCollector().getId()));
			if (paymentsCollector != null ) {
				parameters.put(EMAIL, paymentsCollector.getEmail());
			}			

			log.info("Process end operation day registry and save it to file finished...");

			return RESULT_NEXT;
		} else {
			return RESULT_NO_REGISTRY_CREATED;
		}
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

	@Required
	public void setPaymentsCollectorService(PaymentsCollectorService paymentsCollectorService) {
		this.paymentsCollectorService = paymentsCollectorService;
	}

}
