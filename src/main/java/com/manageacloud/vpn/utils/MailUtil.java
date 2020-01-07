/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.utils;

import com.manageacloud.vpn.model.Email;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MailUtil {

    protected Log log = LogFactory.getLog(this.getClass());

    private final JavaMailSender emailSender;

    private final Configuration freemarkerConfig;

    public MailUtil(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") JavaMailSender emailSender, Configuration freemarkerConfig) {
        this.emailSender = emailSender;
        this.freemarkerConfig = freemarkerConfig;
    }

    /**
     * Attemps to send an email
     *
     * @param email
     * @return boolean confirming in the email has been sent
     */
    public boolean sendEmail(Email email) {

        boolean success = true;

        try {

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            if (email.getType() != null) {
                Template template = freemarkerConfig.getTemplate(email.getTypeTemplate());
                String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, email.getModel());
                helper.setText(html, true);
            } else if (email.getHtmlBody() != null && email.getTextBody() != null) {
                helper.setText(email.getTextBody());
                helper.setText(email.getHtmlBody(), true);
            } else if (email.getHtmlBody() != null) {
                helper.setText(email.getHtmlBody(), true);
            } else if (email.getTextBody() != null) {
                helper.setText(email.getTextBody());
            }

            helper.setTo(email.getTo());
            helper.setSubject(email.getSubject());
            helper.setFrom(email.getFrom());

            if (  email.getHtmlBody() != null && !email.getHtmlBody().isEmpty() ) {
                helper.addInline("logo", new ClassPathResource("/static/the-vpn-company-logo.png"));
            }

            if ( CoreUtils.isJUnitTest() ) {
                return true;
            } else {
                emailSender.send(message);
            }

        } catch (IOException | TemplateException | MessagingException e) {
            log.error("Email could not be sent because: " + e.getMessage(), e);
            success = false;
        }

        return success;
    }


}