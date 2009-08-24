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
import org.flexpay.payments.process.export.util.GeneratePaymentsDBRegistry;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class GeneratePaymentsDBRegistryJob extends Job {

	// required services
	private FPFileService fpFileService;
	private GeneratePaymentsDBRegistry generatePaymentsDBRegistry;
	private OrganizationService organizationService;
	private ServiceProviderService serviceProviderService;

	// parameter names
	private static final String FILE_PARAMETER_NAME = "File";
	private static final String FILE_ID_PARAMETER_NAME = "FileId";
	private static final String REGISTERED_ORGANIZATION_PARAMETER_NAME = "RegisteredOrganization";
	private static final String REGISTERED_ORGANIZATION_ID_PARAMETER_NAME = "RegisteredOrganizationId";
	private static final String SERVICE_PROVIDER_ID_PARAMETER_NAME = "ServiceProviderId";
	private static final String SERVICE_PROVIDER_PARAMETER_NAME = "ServiceProvider";
	private static final String FINISH_DATE_PARAMETER_NAME = "finishDate";
	private static final String LAST_PROCESSED_DATE_PARAMETER_NAME = "lastProcessedDate";
	private static final String REGISTRY_ID_PARAMETER_NAME = "RegistryId";

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile file = getFileParameter(parameters);
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

		Registry registry = generatePaymentsDBRegistry.createDBRegestry(file, serviceProvider, registeredOrganization,
																		lastProcessedDateOldValue, lastProcessedDate);
		parameters.put(LAST_PROCESSED_DATE_PARAMETER_NAME, String.valueOf(lastProcessedDate.getTime()));

		if (registry == null) {
			log.error("Empty registry created. Returning error result.");
			return RESULT_ERROR;
		}

		parameters.put(REGISTRY_ID_PARAMETER_NAME, registry.getId());

		return RESULT_NEXT;
	}

	private FPFile getFileParameter(Map<Serializable, Serializable> parameters) {

		FPFile file = null;

		if (parameters.containsKey(FILE_PARAMETER_NAME)) {
			Object o = parameters.get(FILE_PARAMETER_NAME);
			if (o instanceof FPFile) {
				file = (FPFile) o;
			} else {
				log.error("Invalid file parameter class");
				return null;
			}
		} else if (parameters.containsKey(FILE_ID_PARAMETER_NAME)) {
			Long fileId = (Long) parameters.get(FILE_ID_PARAMETER_NAME);
			file = fpFileService.read(new Stub<FPFile>(fileId));
		}

		return file;
	}

	private Organization getRegisteredOrganization(Map<Serializable, Serializable> parameters) {

		Organization registeredOrganization = null;

		if (parameters.containsKey(REGISTERED_ORGANIZATION_PARAMETER_NAME)) {
			Object o = parameters.get(REGISTERED_ORGANIZATION_PARAMETER_NAME);
			if (o instanceof Organization) {
				registeredOrganization = (Organization) o;
			} else {
				log.error("Invalid registered organization parameter class");
				return null;
			}
		} else if (parameters.containsKey(REGISTERED_ORGANIZATION_ID_PARAMETER_NAME)) {
			Long organizationId = (Long) parameters.get(REGISTERED_ORGANIZATION_ID_PARAMETER_NAME);
			registeredOrganization = organizationService.readFull(new Stub<Organization>(organizationId));
		}

		return registeredOrganization;
	}

	private ServiceProvider getServiceProvider(Map<Serializable, Serializable> parameters) {

		ServiceProvider serviceProvider = null;

		if (parameters.containsKey(SERVICE_PROVIDER_PARAMETER_NAME)) {
			Object o = parameters.get(SERVICE_PROVIDER_PARAMETER_NAME);
			if (o instanceof ServiceProvider) {
				serviceProvider = (ServiceProvider) o;
			} else {
				log.error("Invalid service provider parameter class");
				return null;
			}
		} else if (parameters.containsKey(SERVICE_PROVIDER_ID_PARAMETER_NAME)) {
			Long serviceProviderId = (Long) parameters.get(SERVICE_PROVIDER_ID_PARAMETER_NAME);
			serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(serviceProviderId));
		}

		return serviceProvider;
	}

	private Date getLastProcessedDate(Map<Serializable, Serializable> parameters) {

		Date lastProcessedDate = new Date();

		if (parameters.containsKey(FINISH_DATE_PARAMETER_NAME)) {
			lastProcessedDate = (Date) parameters.get(FINISH_DATE_PARAMETER_NAME);
		}

		return lastProcessedDate;
	}

	private Date getLastProcessedDateOldValue(Map<Serializable, Serializable> parameters) {

		Date oldLastProcessedDate = new Date(0);

		String oldLastProcessedDateParamValue = (String) parameters.get(LAST_PROCESSED_DATE_PARAMETER_NAME);
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
	public void setGeneratePaymentsDBRegistry(GeneratePaymentsDBRegistry generatePaymentsDBRegistry) {
		this.generatePaymentsDBRegistry = generatePaymentsDBRegistry;
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
