package org.flexpay.payments.process.export.job;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.process.export.util.GeneratePaymentsDBRegistry;

import java.io.Serializable;
import java.util.Map;
import java.util.Date;

public class GeneratePaymentsDBRegistryJob extends Job {
    private FPFileService fpFileService;
    private GeneratePaymentsDBRegistry generatePaymentsDBRegistry;
    private OrganizationService organizationService;

    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
        Long fileId = (Long) parameters.get("FileId");
        Long organizationId = (Long) parameters.get("OrganizationId");

        FPFile spFile = fpFileService.read(new Stub<FPFile>(fileId));
        if (spFile == null) {
            log.warn("Invalid File Id");
            return RESULT_ERROR;
        }

        Organization organization = organizationService.readFull(new Stub<Organization>(organizationId));
        if (organization == null) {
            log.warn("Invalid Organization Id");
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
        Registry registry = generatePaymentsDBRegistry.createDBRegestry(spFile, organization, oldLastProcessedDate, lastProcessedDate);
        parameters.put("lastProcessedDate", String.valueOf(lastProcessedDate.getTime()));
        parameters.put("RegistryId", registry.getId());
        return RESULT_NEXT;
    }

    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }

    public void setGeneratePaymentsDBRegistry(GeneratePaymentsDBRegistry generatePaymentsDBRegistry) {
        this.generatePaymentsDBRegistry = generatePaymentsDBRegistry;
    }

    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }
}
