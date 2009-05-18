package org.flexpay.common.service.transport.impl;

import org.flexpay.common.service.transport.OutTransport;
import org.flexpay.common.persistence.file.FPFile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.FileSystemResource;

import javax.mail.internet.MimeMessage;

public class SendFileToEmail implements OutTransport {
    private JavaMailSender sender;
    private String email;

    public void send(FPFile file) throws Exception {
        sender = new JavaMailSenderImpl();

        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);

        FileSystemResource resourceFile = new FileSystemResource(file.getFile());
        helper.addAttachment(file.getOriginalName(), resourceFile);

        sender.send(message);
    }

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

