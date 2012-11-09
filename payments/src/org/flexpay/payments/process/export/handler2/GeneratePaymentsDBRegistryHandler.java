package org.flexpay.payments.process.export.handler2;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.DateRange;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.handler.ProcessInstanceExecuteHandler;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.service.registry.PaymentsRegistryDBGenerator;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.*;

public class GeneratePaymentsDBRegistryHandler extends ProcessInstanceExecuteHandler {

	// required services
	private PaymentsRegistryDBGenerator paymentsRegistryDBGenerator;
	private OrganizationService organizationService;
	private ServiceProviderService serviceProviderService;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {

		Organization registeredOrganization = getRegisteredOrganization(parameters);
		if (registeredOrganization == null) {
			log.error("Registered organization was not found as a job parameter");
			return RESULT_ERROR;
		}

		ServiceProvider serviceProvider = getServiceProvider(parameters);
		if (serviceProvider == null) {
			log.error("Service provider was not found as a job parameter");
			return RESULT_ERROR;
		}

		Date lastProcessedDateOldValue = getLastProcessedDateOldValue(parameters);
		Date lastProcessedDate = getLastProcessedDate(parameters);

		DateRange range = new DateRange(lastProcessedDateOldValue, lastProcessedDate);
		Registry registry = paymentsRegistryDBGenerator.createRegistry(
				serviceProvider, registeredOrganization, range);
		parameters.put(LAST_PROCESSED_DATE, String.valueOf(lastProcessedDate.getTime()));

		if (registry == null) {
			log.error("Empty registry created. Returning error result.");
			return RESULT_ERROR;
		}

		parameters.put(REGISTRY_ID, registry.getId());

		 return RESULT_NEXT;
	}

	private Organization getRegisteredOrganization(Map<String, Object> parameters) {

		Organization registeredOrganization = null;

		if (parameters.containsKey(REGISTERED_ORGANIZATION)) {
			Object o = parameters.get(REGISTERED_ORGANIZATION);
			if (o instanceof Organization) {
				registeredOrganization = (Organization) o;
			} else {
				log.error("Invalid registered organization parameter class");
				return null;
			}
		} else if (parameters.containsKey(REGISTERED_ORGANIZATION_ID)) {
			Long organizationId = (Long) parameters.get(REGISTERED_ORGANIZATION_ID);
			registeredOrganization = organizationService.readFull(new Stub<Organization>(organizationId));
			log.error("Get registered organization id: {}, persistent object: {}", organizationId, registeredOrganization);
		}

		return registeredOrganization;
	}

	private ServiceProvider getServiceProvider(Map<String, Object> parameters) {

		ServiceProvider serviceProvider = null;

		if (parameters.containsKey(SERVICE_PROVIDER)) {
			Object o = parameters.get(SERVICE_PROVIDER);
			if (o instanceof ServiceProvider) {
				serviceProvider = (ServiceProvider) o;
			} else {
				log.error("Invalid service provider parameter class");
				return null;
			}
		} else if (parameters.containsKey(SERVICE_PROVIDER_ID)) {
			Long serviceProviderId = (Long) parameters.get(SERVICE_PROVIDER_ID);
			serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(serviceProviderId));
		}

		return serviceProvider;
	}

	private Date getLastProcessedDate(Map<String, Object> parameters) {

		Date lastProcessedDate = new Date();

		if (parameters.containsKey(FINISH_DATE)) {
			lastProcessedDate = (Date) parameters.get(FINISH_DATE);
		}

		return lastProcessedDate;
	}

	private Date getLastProcessedDateOldValue(Map<String, Object> parameters) {

		Date oldLastProcessedDate = new Date(0);

		String oldLastProcessedDateParamValue = (String) parameters.get(LAST_PROCESSED_DATE);
		if (oldLastProcessedDateParamValue != null) {
			try {
				oldLastProcessedDate = new Date(Long.parseLong(oldLastProcessedDateParamValue));
			} catch (NumberFormatException ex) {
				log.warn("Invalid value of parameter 'lastProcessedDate'", ex);
			}
		}

		return oldLastProcessedDate;
	}

	@Required
	public void setPaymentsRegistryDBGenerator(PaymentsRegistryDBGenerator paymentsRegistryDBGenerator) {
		this.paymentsRegistryDBGenerator = paymentsRegistryDBGenerator;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setServiceProviderService(ServiceProviderService serviceProviderService) {
		this.serviceProviderService = serviceProviderService;
	}
}
