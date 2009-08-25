package org.flexpay.payments.process.export.job;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import static org.flexpay.payments.process.export.job.GeneratePaymentsRegistryParameterNames.*;
import org.flexpay.payments.service.RegistryDeliveryHistoryService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class SendRegistryJob extends Job {

	private String emailPassword;
	private String emailUserName;
	private String smtpHost;
	private String emailFrom;

	// required services
	private FPFileService fpFileService;
    private RegistryService registryService;
    private ServiceProviderService providerService;
    private RegistryDeliveryHistoryService registryDeliveryHistoryService;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile file = getFile(parameters);
		if (file == null) {
			log.error("File was not found as a job parameter");
			return RESULT_ERROR;
		}

        Registry registry = getRegistry(parameters);
        if (registry == null) {
            log.error("Registry was not found as a job parameter");
            return RESULT_ERROR;
        }

        if (registry.getProperties() == null || !(registry.getProperties() instanceof EircRegistryProperties)) {
            log.error("Registry {} doesn`t contain properties", registry.getId());
            return RESULT_ERROR;
        }

        EircRegistryProperties properties = (EircRegistryProperties)registry.getProperties();
        ServiceProvider serviceProvider = providerService.read(properties.getServiceProviderStub());
        if (serviceProvider == null) {
            log.error("Registry {} properties don`t content service provider", registry.getId());
            return RESULT_ERROR;
        }
        if (StringUtils.isEmpty(serviceProvider.getEmail())) {
            log.error("Service provider {} of registry {} does not have an e-mail", new Object[]{serviceProvider.getId(), registry.getId()});
            return RESULT_ERROR;
        }

		try {
			send(emailFrom, serviceProvider.getEmail(), "", emailUserName, emailPassword, smtpHost, file, file.getOriginalName());
			addToDeliveryHistory(file, registry, serviceProvider);
		} catch (FlexPayException e) {
			log.error("Error sending file", e);
			return RESULT_ERROR;
		}

		return RESULT_NEXT;
	}

	private void addToDeliveryHistory(FPFile file, Registry registry, ServiceProvider serviceProvider) {

		RegistryDeliveryHistory registryDeliveryHistory = new RegistryDeliveryHistory();
		registryDeliveryHistory.setDeliveryDate(new Date());
		registryDeliveryHistory.setEmail(serviceProvider.getEmail());
		registryDeliveryHistory.setSpFile(file);
		registryDeliveryHistory.setRegistry(registry);
		registryDeliveryHistoryService.create(registryDeliveryHistory);
	}

	private void send(String emailFrom, String emailTo, String subject, String userName, String userPassword, String smptHost, FPFile attachment, String attachmentName)
			throws FlexPayException {

		if (log.isDebugEnabled()) {
			log.debug("Sending mail from {} to {} with subject {} userName {} password **** smtpHost {} ",
					new Object[]{emailFrom, emailTo, subject, userName, smptHost});
		}

		JavaMailSenderImpl sender = new JavaMailSenderImpl();

		if (!StringUtils.isEmpty(userName)) {
			sender.setUsername(userName);
			sender.setPassword(userPassword);
		}

		sender.setHost(smptHost);

		MimeMessage message = sender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(emailTo);
			helper.setFrom(emailFrom);
			helper.setSubject(subject);

			helper.addAttachment(attachmentName, attachment);

			sender.send(message);
		} catch (MessagingException m) {
			log.debug("Can't send email.", m);
			throw new FlexPayException(m);
		}

	}

	private FPFile getFile(Map<Serializable, Serializable> parameters) {

		FPFile spFile = null;

		if (parameters.containsKey(FILE)) {
			Object o = parameters.get(FILE);
			if (o instanceof FPFile && ((FPFile) o).getId() != null) {
				spFile = (FPFile) o;
				spFile = fpFileService.read(stub(spFile));
			} else {
				log.error("Invalid file`s instance class");
			}
		} else if (parameters.containsKey(FILE_ID)) {
			Long fileId = (Long) parameters.get(FILE_ID);
			spFile = fpFileService.read(new Stub<FPFile>(fileId));
		}

		return spFile;
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
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Required
    public void setProviderService(ServiceProviderService providerService) {
        this.providerService = providerService;
    }

    @Required
    public void setRegistryDeliveryHistoryService(RegistryDeliveryHistoryService registryDeliveryHistoryService) {
        this.registryDeliveryHistoryService = registryDeliveryHistoryService;
    }

    @Required
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	@Required
	public void setEmailUserName(String emailUserName) {
		this.emailUserName = emailUserName;
	}

	@Required
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	@Required
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
}
