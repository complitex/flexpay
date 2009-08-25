package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.service.ServiceProviderService;
import static org.flexpay.payments.process.export.job.GeneratePaymentsRegistryParameterNames.*;
import org.flexpay.payments.service.registry.PaymentsRegistryDBGenerator;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class GeneratePaymentsDBRegistryJob extends Job {

	// required services
	private FPFileService fpFileService;
	private PaymentsRegistryDBGenerator paymentsRegistryDBGenerator;
	private OrganizationService organizationService;
	private ServiceProviderService serviceProviderService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile file = getFile(parameters);
		if (file == null) {
			log.error("File was not found as a job parameter");
			return RESULT_ERROR;
		}

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

		Registry registry = paymentsRegistryDBGenerator.createDBRegistry(file, serviceProvider, registeredOrganization,
																		lastProcessedDateOldValue, lastProcessedDate);
		parameters.put(LAST_PROCESSED_DATE, String.valueOf(lastProcessedDate.getTime()));

		if (registry == null) {
			log.error("Empty registry created. Returning error result.");
			return RESULT_ERROR;
		}

		parameters.put(REGISTRY_ID, registry.getId());

		return RESULT_NEXT;
	}

	private FPFile getFile(Map<Serializable, Serializable> parameters) {

		FPFile file = null;

		if (parameters.containsKey(FILE)) {
			Object o = parameters.get(FILE);
			if (o instanceof FPFile) {
				file = (FPFile) o;
			} else {
				log.error("Invalid file parameter class");
				return null;
			}
		} else if (parameters.containsKey(FILE_ID)) {
			Long fileId = (Long) parameters.get(FILE_ID);
			file = fpFileService.read(new Stub<FPFile>(fileId));
		}

		return file;
	}

	private Organization getRegisteredOrganization(Map<Serializable, Serializable> parameters) {

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
		}

		return registeredOrganization;
	}

	private ServiceProvider getServiceProvider(Map<Serializable, Serializable> parameters) {

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

	private Date getLastProcessedDate(Map<Serializable, Serializable> parameters) {

		Date lastProcessedDate = new Date();

		if (parameters.containsKey(FINISH_DATE)) {
			lastProcessedDate = (Date) parameters.get(FINISH_DATE);
		}

		return lastProcessedDate;
	}

	private Date getLastProcessedDateOldValue(Map<Serializable, Serializable> parameters) {

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
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
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
