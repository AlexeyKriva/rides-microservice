package com.software.modsen.ridesmicroservice.services;

import com.software.modsen.ridesmicroservice.exceptions.ReportSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String from;
    private final String reportSubject = "Ride report!";

    @SneakyThrows
    public void sendReport(String to, byte[] report) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);

        messageHelper.setTo(to);
        messageHelper.setSubject(reportSubject);
        messageHelper.setFrom(from);
        messageHelper.setText("Text");

        messageHelper.addAttachment("report.xlsx", () -> new ByteArrayInputStream(report));

        mailSender.send(mimeMessage);
    }
}