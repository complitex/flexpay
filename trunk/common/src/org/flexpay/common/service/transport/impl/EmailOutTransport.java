package org.flexpay.common.service.transport.impl;

import org.flexpay.common.persistence.file.FPFile;
import org.flexpay.common.service.transport.OutTransport;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;

public class EmailOutTransport implements OutTransport {
    private JavaMailSender sender;
    private String email;

    public void send(FPFile file) throws Exception {
		
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);

        helper.addAttachment(file.getOriginalName(), file);

        sender.send(message);
    }

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
