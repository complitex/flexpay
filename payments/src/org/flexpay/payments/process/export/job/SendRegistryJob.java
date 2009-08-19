package org.flexpay.payments.process.export.job;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.registry.Registry;
import org.flexpay.common.persistence.registry.RegistryProperties;
import static org.flexpay.common.persistence.Stub.stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.job.Job;
import org.flexpay.common.service.FPFileService;
import org.flexpay.common.service.RegistryService;
import org.flexpay.common.util.StringUtil;
import org.flexpay.payments.persistence.EircRegistryProperties;
import org.flexpay.payments.persistence.RegistryDeliveryHistory;
import org.flexpay.payments.service.RegistryDeliveryHistoryService;
import org.flexpay.orgs.service.ServiceProviderService;
import org.flexpay.orgs.persistence.ServiceProvider;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.Map;
import java.util.Date;

public class SendRegistryJob extends Job {

	private FPFileService fpFileService;
    private RegistryService registryService;
    private ServiceProviderService providerService;
    private RegistryDeliveryHistoryService registryDeliveryHistoryService;
	private String emailPassword;
	private String emailUserName;
	private String smtpHost;
	private String emailFrom;

	public String execute(Map<Serializable, Serializable> parameters) throws FlexPayException {

		FPFile spFile = null;

		if (parameters.containsKey("File")) {
			Object o = parameters.get("File");
			if (o instanceof FPFile && ((FPFile) o).getId() != null) {
				spFile = (FPFile) o;
				spFile = fpFileService.read(stub(spFile));
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
        if (registry.getProperties() == null || !(registry.getProperties() instanceof EircRegistryProperties)) {
            log.warn("Registry {} don`t content registry properties", registry.getId());
            return RESULT_ERROR;
        }

        EircRegistryProperties properties = (EircRegistryProperties)registry.getProperties();
        ServiceProvider serviceProvider = providerService.read(properties.getServiceProviderStub());
        if (serviceProvider == null) {
            log.warn("Registry {} properties don`t content service provider", registry.getId());
            return RESULT_ERROR;
        }
        if (StringUtils.isEmpty(serviceProvider.getEmail())) {
            log.warn("Service provider {} of registry {} does not content e-mail", new Object[]{serviceProvider.getId(), registry.getId()});
            return RESULT_ERROR;
        }

		try {
			send(emailFrom, serviceProvider.getEmail(), "", emailUserName, emailPassword, smtpHost, spFile, spFile.getOriginalName());
            RegistryDeliveryHistory registryDeliveryHistory = new RegistryDeliveryHistory();
            registryDeliveryHistory.setDeliveryDate(new Date());
            registryDeliveryHistory.setEmail(serviceProvider.getEmail());
            registryDeliveryHistory.setSpFile(spFile);
            registryDeliveryHistory.setRegistry(registry);
            registryDeliveryHistoryService.create(registryDeliveryHistory);
		} catch (FlexPayException e) {
			log.warn("Send file exception", e);
			return RESULT_ERROR;
		}

		return RESULT_NEXT;
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
