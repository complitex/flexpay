package org.flexpay.payments.process.export.job;

import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.payments.process.export.util.GeneratePaymentsMBRegistry;

import java.io.Serializable;
import java.io.File;
import java.util.Map;

public class GeneratePaymentsMBRegistryJob extends Job {
    private FPFileService fpFileService;
    private GeneratePaymentsMBRegistry generatePaymentsMBRegistry;
    private OrganizationService organizationService;
    private RegistryService registryService;

    public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {
        Long fileId = (Long) parameters.get("FileId");
        Long organizationId = (Long) parameters.get("OrganizationId");
        Long registryId = (Long) parameters.get("RegistryId");

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

        Registry registry = registryService.read(new Stub<Registry>(registryId));
        if (registry == null) {
            log.warn("Invalid Registry Id");
            return RESULT_ERROR;
        }
        File file = spFile.getFile();
        long currentLength = file.length();
        generatePaymentsMBRegistry.exportToMegaBank(registry, file, organization);
        if (file.length() > currentLength) {
            spFile.setSize(file.length());
            fpFileService.update(spFile);
        }
        return RESULT_NEXT;
    }

    public void setFpFileService(FPFileService fpFileService) {
        this.fpFileService = fpFileService;
    }

    public void setGeneratePaymentsMBRegistry(GeneratePaymentsMBRegistry generatePaymentsMBRegistry) {
        this.generatePaymentsMBRegistry = generatePaymentsMBRegistry;
    }

    public void setOrganizationService(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }
}
