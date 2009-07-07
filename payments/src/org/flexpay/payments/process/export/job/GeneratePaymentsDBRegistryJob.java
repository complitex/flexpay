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

    private FPFileService fpFileService;
    private GeneratePaymentsDBRegistry generatePaymentsDBRegistry;
    private OrganizationService organizationService;
    private ServiceProviderService serviceProviderService;

    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
        FPFile file = null;
        if (parameters.containsKey("File")) {
            Object o = parameters.get("File");
            if (o instanceof FPFile) {
                file = (FPFile) o;
            } else {
                log.warn("Invalid file`s instance class");
            }
        } else if (parameters.containsKey("FileId")) {
            Long fileId = (Long) parameters.get("FileId");
            file = fpFileService.read(new Stub<FPFile>(fileId));
        }
        if (file == null) {
            log.warn("Did not find file in job parameters");
            return RESULT_ERROR;
        }

        Organization registeredOrganization = null;
        if (parameters.containsKey("RegisteredOrganization")) {
            Object o = parameters.get("RegisteredOrganization");
            if (o instanceof Organization) {
                registeredOrganization = (Organization) o;
            } else {
                log.warn("Invalid registered organization`s instance class");
            }
        } else if (parameters.containsKey("RegisteredOrganizationId")) {
            Long organizationId = (Long) parameters.get("RegisteredOrganizationId");
            registeredOrganization = organizationService.readFull(new Stub<Organization>(organizationId));
        }
        if (registeredOrganization == null) {
            log.warn("Did not find registered organization in job parameters");
            return RESULT_ERROR;
        }

        ServiceProvider serviceProvider = null;
        if (parameters.containsKey("ServiceProvider")) {
            Object o = parameters.get("ServiceProvider");
            if (o instanceof ServiceProvider) {
                serviceProvider = (ServiceProvider) o;
            } else {
                log.warn("Invalid service provider`s instance class");
            }
        } else if (parameters.containsKey("ServiceProviderId")) {
            Long serviceProviderId = (Long) parameters.get("ServiceProviderId");
            serviceProvider = serviceProviderService.read(new Stub<ServiceProvider>(serviceProviderId));
        }
        if (serviceProvider == null) {
            log.warn("Did not find service provider in job parameters");
            return RESULT_ERROR;
        }

        String oldLastProcessedDateSt = (String) parameters.get("lastProcessedDate");
        Date oldLastProcessedDate = new Date(0);
        if (oldLastProcessedDateSt != null) {
            try {
                oldLastProcessedDate = new Date(Long.parseLong(oldLastProcessedDateSt));
            } catch (NumberFormatException ex) {
                log.warn("Invalid parameter lastSatartDate", ex);
            }
        }
        Date lastProcessedDate = new Date();
        if (parameters.containsKey("finishDate")) {
            lastProcessedDate = (Date) parameters.get("finishDate");
        }
        Registry registry = generatePaymentsDBRegistry.createDBRegestry(file, serviceProvider, registeredOrganization, oldLastProcessedDate, lastProcessedDate);
        parameters.put("lastProcessedDate", String.valueOf(lastProcessedDate.getTime()));
        if (registry == null) {
            log.debug("Empty registry nothing do");
            return RESULT_ERROR;
        }
        //parameters.put("Registry", registry);
        parameters.put("RegistryId", registry.getId());
        return RESULT_NEXT;
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
