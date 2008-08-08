package org.flexpay.common.service;

import org.flexpay.common.test.SpringBeanAwareTestCase;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.junit.Test;

public class TestSendEmail extends SpringBeanAwareTestCase {

	@Autowired
	protected MailSender mailSender;
	private SimpleMailMessage message;

	@Autowired
	public void setMessage(@Qualifier ("templateMessage") SimpleMailMessage message) {
		this.message = message;
	}

	@Test
	public void voidSendMail() {
		message.setText("Hello, I'm a test");
		mailSender.send(message);
	}
}
