package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.process.export.util.GeneratePaymentsDBRegistry;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class GeneratePaymentsDBRegistryJob extends Job {

    private FPFileService fpFileService;
    private GeneratePaymentsDBRegistry generatePaymentsDBRegistry;
    private OrganizationService organizationService;

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

        Organization organization = null;
        if (parameters.containsKey("Organization")) {
            Object o = parameters.get("Organization");
            if (o instanceof Organization) {
                organization = (Organization) o;
            } else {
                log.warn("Invalid organization`s instance class");
            }
        } else if (parameters.containsKey("OrganizationId")) {
            Long organizationId = (Long) parameters.get("OrganizationId");
            organization = organizationService.readFull(new Stub<Organization>(organizationId));
        }
        if (organization == null) {
            log.warn("Did not find organization in job parameters");
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
        Registry registry = generatePaymentsDBRegistry.createDBRegestry(file, organization, oldLastProcessedDate, lastProcessedDate);
        parameters.put("lastProcessedDate", String.valueOf(lastProcessedDate.getTime()));
        parameters.put("Registry", registry);
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

}
