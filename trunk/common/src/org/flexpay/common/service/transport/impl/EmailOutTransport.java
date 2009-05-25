package org.flexpay.common.service.transport.impl;

import org.flexpay.common.service.transport.OutTransport;
import org.flexpay.common.persistence.file.FPFile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.omg.CORBA_2_3.portable.InputStream;

import javax.mail.internet.MimeMessage;

public class EmailOutTransport implements OutTransport {
    private JavaMailSender sender;
    private String email;

    public void send(FPFile file) throws Exception {
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);

        helper.addAttachment(file.getOriginalName(), new InputStreamResource(file.getInputStream()));

        sender.send(message);
    }

    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

