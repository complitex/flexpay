package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryFPFileType;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryFPFileTypeService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.service.SignatureService;
import org.flexpay.payments.service.registry.PaymentsRegistryMBGenerator;
import org.springframework.beans.factory.annotation.Required;

import java.io.Serializable;
import java.security.Signature;
import java.util.Map;

import static org.flexpay.payments.process.export.ExportJobParameterNames.*;

public class GeneratePaymentsMBRegistryJob extends Job {

	private static final int BUFFER_SIZE = 1024;

	private FPFileService fpFileService;
	private PaymentsRegistryMBGenerator paymentsRegistryMBGenerator;
	private OrganizationService organizationService;
	private RegistryService registryService;
    private RegistryFPFileTypeService registryFPFileTypeService;
	private SignatureService signatureService;


	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile file = getFile(parameters);
		if (file == null) {
			log.error("File was not found as a job parameter");
			return RESULT_ERROR;
		}

		Organization organization = getOrganization(parameters);
		if (organization == null) {
			log.error("Organization was not found as a job parameter");
			return RESULT_ERROR;
		}

		Registry registry = getRegistry(parameters);
		if (registry == null) {
			log.error("Registry was not found as a job parameter");
			return RESULT_ERROR;
		}

		String privateKey = getPrivateKey(parameters);
		Signature signature = null;
		if (privateKey != null && ApplicationConfig.isResourceAvailable(privateKey)) {
			signature = getSignature(privateKey);
		} else {
			log.warn("Private key resource {} not found", privateKey);
		}

        try {
		    paymentsRegistryMBGenerator.exportToMegaBank(registry, file, organization, signature);
        } catch (FlexPayException ex) {
            log.error("Registry generation failed", ex);
            return RESULT_ERROR;
        }

		file.updateSize();
		fpFileService.update(file);
        registry.getFiles().put(registryFPFileTypeService.findByCode(RegistryFPFileType.MB_FORMAT), file);
        registryService.update(registry);
		
		return RESULT_NEXT;
	}

	private Signature getSignature(String privateKey) {
		try {
			return signatureService.readPrivateSignature(privateKey);
		} catch (Exception e) {
			log.error("Error creating signature '{}': {}", privateKey, e);
		}
		return null;
	}

	private FPFile getFile(Map<Serializable, Serializable> parameters) {

		FPFile file = null;

		if (parameters.containsKey(FILE)) {
			Object o = parameters.get(FILE);
			if (o instanceof FPFile) {
				file = (FPFile) o;
			} else {
				log.warn("Invalid file`s instance class");
			}
		} else if (parameters.containsKey(FILE_ID)) {
			Long fileId = (Long) parameters.get(FILE_ID);
			file = fpFileService.read(new Stub<FPFile>(fileId));
		}

		return file;
	}

	private Organization getOrganization(Map<Serializable, Serializable> parameters) {

		Organization organization = null;

		if (parameters.containsKey(ORGANIZATION)) {
			Object o = parameters.get(ORGANIZATION);
			if (o instanceof Organization) {
				organization = (Organization) o;
			} else {
				log.error("Invalid organization`s instance class");
			}
		} else if (parameters.containsKey(ORGANIZATION_ID)) {
			Long organizationId = (Long) parameters.get(ORGANIZATION_ID);
			organization = organizationService.readFull(new Stub<Organization>(organizationId));
		}

		return organization;
	}

	private String getPrivateKey(Map<Serializable, Serializable> parameters) {

		if (parameters.containsKey(PRIVATE_KEY)) {
            return (String) parameters.get(PRIVATE_KEY);
		}

		return null;
	}

	private Registry getRegistry(Map<Serializable, Serializable> parameters) {

		Registry registry = null;

		if (parameters.containsKey(REGISTRY)) {
			Object o = parameters.get(REGISTRY);
			if (o instanceof Registry) {
				registry = (Registry) o;
			} else {
				log.error("Invalid registry`s instance class");
			}
		} else if (parameters.containsKey(REGISTRY_ID)) {
			Long registryId = (Long) parameters.get(REGISTRY_ID);
			registry = registryService.read(new Stub<Registry>(registryId));
		}

		return registry;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setRegistryPaymentsMBGenerator(PaymentsRegistryMBGenerator paymentsRegistryMBGenerator) {
		this.paymentsRegistryMBGenerator = paymentsRegistryMBGenerator;
	}

	@Required
	public void setOrganizationService(OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Required
	public void setRegistryService(RegistryService registryService) {
		this.registryService = registryService;
	}

    @Required
    public void setRegistryFPFileTypeService(RegistryFPFileTypeService registryFPFileTypeService) {
        this.registryFPFileTypeService = registryFPFileTypeService;
    }

	@Required
	public void setSignatureService(SignatureService signatureService) {
		this.signatureService = signatureService;
	}
}
