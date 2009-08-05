package org.flexpay.payments.process.export.job;

import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.orgs.persistence.Organization;
import org.flexpay.orgs.service.OrganizationService;
import org.flexpay.payments.process.export.util.GeneratePaymentsMBRegistry;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.Map;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

public class GeneratePaymentsMBRegistryJob extends Job {

	private FPFileService fpFileService;
	private GeneratePaymentsMBRegistry generatePaymentsMBRegistry;
	private OrganizationService organizationService;
	private RegistryService registryService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile spFile = null;

		if (parameters.containsKey("File")) {
			Object o = parameters.get("File");
			if (o instanceof FPFile) {
				spFile = (FPFile) o;
			} else {
				log.warn("Invalid file`s instance class");
			}
		} else if (parameters.containsKey("FileId")) {
			Long fileId = (Long) parameters.get("FileId");
			spFile = fpFileService.read(new Stub<FPFile>(fileId));
		}

		if (spFile == null) {
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

		Registry registry = null;

		if (parameters.containsKey("Registry")) {
			Object o = parameters.get("Registry");
			if (o instanceof Registry) {
				registry = (Registry) o;
			} else {
				log.warn("Invalid registry`s instance class");
			}
		} else if (parameters.containsKey("RegistryId")) {
			Long registryId = (Long) parameters.get("RegistryId");
			registry = registryService.read(new Stub<Registry>(registryId));
		}

		if (registry == null) {
			log.warn("Did not find registry in job parameters");
			return RESULT_ERROR;
		}

        if (parameters.containsKey("PrivateKey")) {
            File privateKeyFile = (File)parameters.get("PrivateKey");
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(privateKeyFile));
                byte[] privKeyBytes = new byte[(int)privateKeyFile.length()];
                dis.read(privKeyBytes);
                dis.close();
                dis = null;

                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                // decode private key
                PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(privKeyBytes);
                PrivateKey privKey = keyFactory.generatePrivate(privSpec);

                Signature instance = Signature.getInstance("SHA1withRSA");
                instance.initSign(privKey);

                generatePaymentsMBRegistry.setSignature(instance);                
            } catch (Exception e) {
                log.error("Error create signature '{}': {}", privateKeyFile.getAbsolutePath(), e);
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        log.warn("Error close stream of private key", e);
                    }
                }
            }
        }

		generatePaymentsMBRegistry.exportToMegaBank(registry, spFile, organization);

		spFile.updateSize();
		fpFileService.update(spFile);
		return RESULT_NEXT;
	}

	@Required
	public void setFpFileService(FPFileService fpFileService) {
		this.fpFileService = fpFileService;
	}

	@Required
	public void setGeneratePaymentsMBRegistry(GeneratePaymentsMBRegistry generatePaymentsMBRegistry) {
		this.generatePaymentsMBRegistry = generatePaymentsMBRegistry;
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
