package org.flexpay.payments.process.export.handler2;

import org.apache.commons.lang.StringUtils;
import org.flexpay.common.exception.FlexPayException;
import org.flexpay.common.persistence.Stub;
import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.process.handler.TaskHandler;
import org.flexpay.common.service.FPFileService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

import static org.flexpay.common.persistence.Stub.stub;
import static org.flexpay.payments.process.export.ExportJobParameterNames.*;

public class SendFileHandler extends TaskHandler {

	private String emailPassword;
	private String emailUserName;
	private String smtpHost;
	private String emailFrom;

	// required services
	private FPFileService fpFileService;

	@Override
	public String execute(Map<String, Object> parameters) throws FlexPayException {
		FPFile file = getFile(parameters);
		if (file == null) {
			log.error("File was not found as a job parameter");
			return RESULT_ERROR;
		}

		String email = getEmail(parameters);
		if (email == null) {
			log.error("Email was not found as a job parameter");
		}

		try {
			send(emailFrom, email, "", emailUserName, emailPassword, smtpHost, file, file.getOriginalName());
		} catch (FlexPayException e) {
			log.error("Error sending file", e);
			return RESULT_ERROR;
		}

		return RESULT_NEXT;
	}

	protected FPFile getFile(Map<String, Object> parameters) {

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

	protected String getEmail(Map<String, Object> parameters) {

		String email = null;

		if (parameters.containsKey(EMAIL)) {
			email = (String) parameters.get(EMAIL);
		}

		return email;
	}

	private void send(final String emailFrom, final String emailTo, final String subject,
					  final String userName, final String userPassword, final String smptHost,
					  final FPFile attachment, final String attachmentName)
			throws FlexPayException {

		if (log.isDebugEnabled()) {
			log.debug("Sending mail from {} to {} with subject {} userName {} password **** smtpHost {} file {}",
					new Object[]{emailFrom, emailTo, subject, userName, smptHost, attachment.getNameOnServer()});
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
			helper.setText("");
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
