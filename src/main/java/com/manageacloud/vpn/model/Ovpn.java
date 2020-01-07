/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model;

import org.apache.commons.lang3.text.StrSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ovpn {

    private User user;
    private String configuration;

    public static UserStep builder() {
        return new Builder();
    }

    public interface UserStep {
        DomainStep withUser(User user);
    }

    public interface DomainStep {
        OsStep withDomain(String domain);
    }


    public interface OsStep {
        KeyStep withDevice(Device device);
    }

    public interface KeyStep {
        Build withKeys(ClientCertificate clientCertificate);
    }


    public interface Build {
        Ovpn build();
        Build withRemote(String remote);
        Build withCompress(String compress);
        Build add(String configurationDirective);
    }

    public static class Builder implements UserStep, OsStep, KeyStep, DomainStep, Build {

        private String template = "client\n" +
                "nobind\n" +
                "dev tun\n" +
                "remote-cert-tls server\n" +
                "\n" +
                "%(remote)\n" +
                "\n" +
                "<key>\n" +
                "%(privateKey)\n" +
                "</key>\n" +
                "<cert>\n" +
                "%(cert)\n" +
                "</cert>\n" +
                "<ca>\n" +
                "%(ca)\n" +
                "</ca>\n" +
                "key-direction 1\n" +
                "<tls-auth>\n" +
                "%(staticKey)\n" +
                "</tls-auth>\n" +
                "\n" +
                "redirect-gateway def1\n" +
                "\n" +
                "%(compress)\n" +
                "\n" +
                "cipher AES-256-CBC\n" +
                "%(additionalConfiguration)\n";


        private User user;
        private String configuration;


        /**
         * Domain used to connect to the vpn
         */
        private String domain;

        /**
         *  configuration directives
         *  remote au.cloud.thevpncompany.com.au 2294 udp
         */
        private static String remoteTemplate = "remote %s%s 2296 udp";
        private String remote;

        /**
         *  configuration directives
         *  comp-lzo yes / comp-lzo no / compress
         */
        private String compress = "comp-lzo no";

        /**
         *  additional directives
         */
        private Device device;
        private List<String> additionalConfiguration = new ArrayList<>();

        /**
         * Keys
         */
        private String clientPrivate;
        private String clientPublic;
        private String ca;
        private String staticKey;

        @Override
        public Ovpn build() {

            Map<String, String> values = new HashMap<>();
            if ( remote == null ) {
                values.put("remote", String.format(remoteTemplate, user.getHostname(), domain));
            } else {
                values.put("remote", remote);
            }
            values.put("privateKey", clientPrivate);
            values.put("cert", clientPublic);
            values.put("ca", ca);
            values.put("staticKey", staticKey);
            values.put("compress", compress);

            StringBuilder additionals = new StringBuilder();
            for (String s : additionalConfiguration)
            {
                additionals.append(s);
                additionals.append("\n");
            }

            values.put("additionalConfiguration", additionals.toString());

            this.configuration = StrSubstitutor.replace(template, values, "%(", ")");

            return new Ovpn(this);
        }

        @Override
        public DomainStep withUser(User user) {
            this.user = user;
            return this;
        }

        @Override
        public OsStep withDomain(String domain) {
            this.domain = domain;
            return this;
        }

        @Override
        public KeyStep withDevice(Device device) {
            this.device = device;
            return this;
        }

        @Override
        public Build withRemote(String remote) {
            this.remote = remote;
            return this;
        }

        @Override
        public Build add(String configurationDirective) {
            additionalConfiguration.add(configurationDirective);
            return this;
        }

        @Override
        public Build withKeys(ClientCertificate clientCertificate) {
            this.clientPrivate = clientCertificate.getClientPrivate();
            this.clientPublic= clientCertificate.getClientPublic();
            this.ca = clientCertificate.getCa();
            this.staticKey = clientCertificate.getStaticKey();
            return this;
        }

        @Override
        public Build withCompress(String compress) {
            this.compress = compress;
            return this;
        }
    }

    public Ovpn(Builder builder) {
        user = builder.user;
        configuration = builder.configuration;
    }

    public User getUser() {
        return user;
    }

    public String getConfiguration() {
        return configuration;
    }
}
