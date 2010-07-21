package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.PaymentCollector;
import org.flexpay.orgs.persistence.PaymentPoint;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.PaymentCollectorService;
import org.flexpay.orgs.service.PaymentPointService;
import org.flexpay.payments.util.registries.EndOperationDayRegistryGenerator;
import org.flexpay.payments.util.registries.ExportBankPaymentsRegistry;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import static org.flexpay.payments.process.export.job.ExportJobParameterNames.*;

public class GenerateEndOperationDayRegistryJob extends Job {

	public final static String RESULT_NO_REGISTRY_CREATED = "No registry created";

	// required services
	private OrganizationService organizationService;
	private PaymentCollectorService paymentCollectorService;
	private PaymentPointService paymentPointService;


	private EndOperationDayRegistryGenerator registryGenerator;
	private ExportBankPaymentsRegistry exportBankPaymentsRegistry;
    private RegistryFPFileTypeService registryFPFileTypeService;

	@Override
	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		log.info("Start process generating end operation day registry and save it to file...");

		PaymentPoint paymentPoint = getPaymentPoint(parameters);
		if (paymentPoint == null) {
			log.error("Payment point was not found (searching by id {})", parameters.get(PAYMENT_POINT_ID));
			return RESULT_ERROR;
		}

		log.debug("Payment point found: {}", paymentPoint);

		Organization organization = getOrganization(parameters);
		if (organization == null) {
			log.error("Organization was not found (searching by id {})", parameters.get(ORGANIZATION_ID));
			return RESULT_ERROR;
		}

		log.debug("Organization found: {}", organization);

		Date beginDate = (Date) parameters.get(BEGIN_DATE);
		Date endDate = (Date) parameters.get(END_DATE);

		Registry registry = registryGenerator.generate(paymentPoint, organization, beginDate, endDate);
		if (registry == null) {
			return RESULT_NO_REGISTRY_CREATED;
		}

		FPFile file = exportBankPaymentsRegistry.generateAndAttachFile(registry);
		parameters.put(FILE_ID, file.getId());

		PaymentCollector paymentCollector = getPaymentCollector(paymentPoint);
		if (paymentCollector != null) {
			parameters.put(EMAIL, paymentCollector.getEmail());
		}

		log.info("Process end operation day registry and save it to file finished...");

		return RESULT_NEXT;
	}

	private PaymentCollector getPaymentCollector(PaymentPoint paymentPoint) {
		return paymentCollectorService.read(paymentPoint.collectorStub());
	}

	private PaymentPoint getPaymentPoint(Map<Serializable, Serializable> parameters) {
		Long pointId = (Long) parameters.get(PAYMENT_POINT_ID);
		return paymentPointService.read(new Stub<PaymentPoint>(pointId));
	}

	private Organization getOrganization(Map<Serializable, Serializable> parameters) {
		Long organizationId = (Long) parameters.get(ORGANIZATION_ID);
		return organizationService.readFull(new Stub<Organization>(organizationId));
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
	public void setPaymentCollectorService(PaymentCollectorService paymentCollectorService) {
		this.paymentCollectorService = paymentCollectorService;
	}

    @Required
    public void setRegistryFPFileTypeService(RegistryFPFileTypeService registryFPFileTypeService) {
        this.registryFPFileTypeService = registryFPFileTypeService;
    }
}
