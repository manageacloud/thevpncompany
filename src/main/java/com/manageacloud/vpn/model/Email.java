/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model;

import com.manageacloud.vpn.utils.CoreUtils;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Email {

    public enum Account {

        SUPPORT("support@thevpncompany.com.au"),
        INFO("info@thevpncompany.com.au"),
        TECH("ruben@thevpncompany.com.au");

        private String email;

        Account(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

    }

    public enum Type {
        RECOVER_PASSWORD("Password Recovery"),
        NEW_USER("Welcome to TheVPNCompany"),
        INVOICE("Your Invoice");

        private String subject;

        Type(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
    private String ip;
    private String cookie;

    private String from;
    private String to;
    private String subject;
    private Type type;
    private String textBody;
    private String htmlBody;
    private Map model;

    public static TraceStep builder() {
        return new Builder();
    }

    public interface TraceStep {
        FromStep with(String ip, String cookie);
    }
    public interface FromStep {
        ToStep fromSupport();
        ToStep fromInfo();
        ToStep from(String email);
    }

    public interface ToStep {
        SubjectStep to(String to);
        SubjectStep to(Email.Account to);
    }
    public interface SubjectStep {
        Build withType(Type emailType);
        BodyStep subject(String subject);
    }
    public interface BodyStep{
        Build withBody(String textBody, String htmlBody);
        Build withTextBody(String textBody);
        Build withHTMLBody(String htmlBody);
    }

    public interface Build {
        Email build();
        Build withUser(User user);
        Build withSubscription(Subscription subscription);
        Build withPlainPassword(String plainPassword);
        Build withRecoverPassword(Token recoverPasswordToken);
    }

    public static class Builder implements TraceStep, FromStep, ToStep, SubjectStep, BodyStep, Build {

        private User user;
        private String ip;
        private String cookie;

        private String from;
        private String to;
        private String subject;
        private String textBody;
        private String htmlBody;

        // variables to build email templates
        private Type type;
        private String plainPassword;
        private Token recoverToken;
        private Subscription subscription;
        private Map model = new HashMap();

        @Override
        public Email build() {

            if ( this.type != null ) {

                subject = type.getSubject();

                model.put("domain", CoreUtils.getDomain());

                if (plainPassword != null) {
                    model.put("plain_password", plainPassword);
                }

                if (recoverToken != null) {
                    model.put("recover_token", recoverToken.getToken());
                }

                if (subscription != null) {
                    model.put("plan_name", subscription.getPlan().getDefaultName());
                    model.put("plan_price", subscription.getPlan().getPrice_discounted());
                    model.put("plan_total_price", subscription.getPlan().getTotalDiscountedPrice());

                    model.put("user_name", subscription.getUser().getName());
                    model.put("user_email", subscription.getUser().getEmail());
                } else if (user != null) {
                    model.put("user_name", user.getName());
                    model.put("user_email", user.getEmail());
                }
            }


            return new Email(this);
        }

        @Override
        public ToStep fromSupport() {
            this.from = Account.SUPPORT.getEmail();
            return this;
        }

        @Override
        public ToStep fromInfo() {
            this.from = Account.INFO.getEmail();
            return this;
        }

        @Override
        public ToStep from(String email) {
            this.from = email;
            return this;
        }

        @Override
        public FromStep with(String ip, String cookie) {
            this.ip = ip;
            this.cookie = cookie;
            return this;
        }

        @Override
        public Build withUser(User user) {
            this.user = user;
            return this;
        }

        @Override
        public Build withSubscription(Subscription subscription) {
            this.subscription = subscription;
            return this;
        }

        @Override
        public Build withPlainPassword(String plainPassword) {
            this.plainPassword = plainPassword;
            return this;
        }

        @Override
        public Build withRecoverPassword(Token recoverPasswordToken) {
            this.recoverToken = recoverPasswordToken;
            return this;
        }

        @Override
        public SubjectStep to(String to) {
            this.to = to;
            return this;
        }

        @Override
        public SubjectStep to(Account to) {
            this.to = to.getEmail();
            return this;

        }

        @Override
        public BodyStep subject(String subject) {
            this.subject = subject;
            return this;
        }

        @Override
        public Build withBody(String txtBody, String htmlBody) {
            this.textBody = txtBody;
            this.htmlBody = htmlBody;
            return this;
        }

        @Override
        public Build withTextBody(String textBody) {
            this.textBody = textBody;
            return this;
        }

        @Override
        public Build withHTMLBody(String htmlBody) {
            this.htmlBody = htmlBody;
            return this;
        }

        @Override
        public Build withType(Type emailType) {
            this.type = emailType;
            return this;
        }
    }

    public Email(Builder builder) {
        user     = builder.user;
        ip       = builder.ip;
        cookie   = builder.cookie;

        from     = builder.from;
        to       = builder.to;
        subject  = builder.subject;
        textBody = builder.textBody;
        htmlBody = builder.htmlBody;
        model    = builder.model;
        type     = builder.type;
    }

    private Email() {}

    public long getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getTextBody() {
        return textBody;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public Optional<User> getUser() {
        return Optional.of(user);
    }

    public String getIp() {
        return ip;
    }

    public String getCookie() {
        return cookie;
    }

    /**
     * Get the parameters and variables that has been generated with the input parameters
     *
     * @return
     */
    public Map getModel() {
        return model;
    }

    public Type getType() {
        return type;
    }

    public String getTypeTemplate() {
        return "email_" + this.type.toString().toLowerCase() + ".ftl";
    }
}
