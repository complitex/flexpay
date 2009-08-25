package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import static org.flexpay.payments.process.export.job.GeneratePaymentsRegistryParameterNames.*;
import org.flexpay.payments.service.registry.PaymentsRegistryMBGenerator;
import org.springframework.beans.factory.annotation.Required;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Map;

public class GeneratePaymentsMBRegistryJob extends Job {
    private static final int BUFFER_SIZE = 1024;

	private FPFileService fpFileService;
	private PaymentsRegistryMBGenerator paymentsRegistryMBGenerator;
	private OrganizationService organizationService;
	private RegistryService registryService;
	

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile spFile = null;

		if (parameters.containsKey(FILE)) {
			Object o = parameters.get(FILE);
			if (o instanceof FPFile) {
				spFile = (FPFile) o;
			} else {
				log.warn("Invalid file`s instance class");
			}
		} else if (parameters.containsKey(FILE_ID)) {
			Long fileId = (Long) parameters.get(FILE_ID);
			spFile = fpFileService.read(new Stub<FPFile>(fileId));
		}

		if (spFile == null) {
			log.warn("Did not find file in job parameters");
			return RESULT_ERROR;
		}

		Organization organization = null;

		if (parameters.containsKey(ORGANIZATION)) {
			Object o = parameters.get(ORGANIZATION);
			if (o instanceof Organization) {
				organization = (Organization) o;
			} else {
				log.warn("Invalid organization`s instance class");
			}
		} else if (parameters.containsKey(ORGANIZATION_ID)) {
			Long organizationId = (Long) parameters.get(ORGANIZATION_ID);
			organization = organizationService.readFull(new Stub<Organization>(organizationId));
		}

		if (organization == null) {
			log.warn("Did not find organization in job parameters");
			return RESULT_ERROR;
		}

		Registry registry = null;

		if (parameters.containsKey(REGISTRY)) {
			Object o = parameters.get(REGISTRY);
			if (o instanceof Registry) {
				registry = (Registry) o;
			} else {
				log.warn("Invalid registry`s instance class");
			}
		} else if (parameters.containsKey(REGISTRY_ID)) {
			Long registryId = (Long) parameters.get(REGISTRY_ID);
			registry = registryService.read(new Stub<Registry>(registryId));
		}

		if (registry == null) {
			log.warn("Did not find registry in job parameters");
			return RESULT_ERROR;
		}

        if (parameters.containsKey(PRIVATE_KEY)) {
            String privateKey = (String)parameters.get(PRIVATE_KEY);
            if (ApplicationConfig.isResourceAvailable(privateKey)) {
                DataInputStream dis = null;
                try {
                    dis = new DataInputStream(ApplicationConfig.getResourceAsStream(privateKey));
                    byte[] privKeyBytes = new byte[BUFFER_SIZE];
                    int off = 0;
                    int countRead;
                    while ((countRead = dis.read(privKeyBytes, off, BUFFER_SIZE)) > 0) {
                        off += countRead;
                        privKeyBytes = Arrays.copyOf(privKeyBytes, off + BUFFER_SIZE);
                    }
                    dis.close();
                    dis = null;

                    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                    // decode private key
                    PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
                    PrivateKey privKey = keyFactory.generatePrivate(privSpec);

                    Signature instance = Signature.getInstance("SHA1withRSA");
                    instance.initSign(privKey);

                    paymentsRegistryMBGenerator.setSignature(instance);
                } catch (Exception e) {
                    log.error("Error create signature '{}': {}", privateKey, e);
                } finally {
                    if (dis != null) {
                        try {
                            dis.close();
                        } catch (IOException e) {
                            log.warn("Error close stream of private key", e);
                        }
                    }
                }
            } else {
                log.error("Resource {} not found", privateKey);
            }
        }
        try {
		    paymentsRegistryMBGenerator.exportToMegaBank(registry, spFile, organization);
        } catch (FlexPayException ex) {
            log.error("Failture generation registry", ex);
            return RESULT_ERROR;
        }

		spFile.updateSize();
		fpFileService.update(spFile);
		return RESULT_NEXT;
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

}
