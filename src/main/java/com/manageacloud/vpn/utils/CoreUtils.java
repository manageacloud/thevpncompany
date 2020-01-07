package com.manageacloud.vpn.utils;


import com.manageacloud.vpn.config.VPNIps;
import com.manageacloud.vpn.model.vpn.Process;
import freemarker.core.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class CoreUtils {

    private static final Logger log = LoggerFactory.getLogger(CoreUtils.class);

    private CoreUtils() {}

    /**
     *
     * Returns if given the remote IP the VPN is UP
     *
     * @return
     */
    public static boolean isVPN() {
        String remoteIP = CoreUtils.getRemoteIp();
        return VPNIps.isVPN(remoteIP);
    }

    public static VPNIps getVpnIp() {
        String remoteIP = CoreUtils.getRemoteIp();
        return VPNIps.getVpnIUp(remoteIP);
    }


    public static String bcrypt(String message) {
        return new BCryptPasswordEncoder(11).encode(message);
    }

    public static String sha256(String message) {

        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(message.getBytes());
            byte byteData[] = md.digest();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }

        return sb.toString();

    }



    public static String md5(String message) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] bytesOfMessage = message.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte byteData[] = md.digest(bytesOfMessage);
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    public static String generateCode() {
        final int PASSWORD_LENGTH = 5;
        final Random RANDOM = new SecureRandom();

        String letters = "1234567890";

        String pw = "";
        for (int i=0; i<PASSWORD_LENGTH; i++)
        {
            int index = RANDOM.nextInt()*letters.length();
            pw += letters.substring(index, index+1);
        }
        return pw;
    }

    public static String generatePassword() {
        final int PASSWORD_LENGTH = 8;
        final Random RANDOM = new SecureRandom();

        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789";

        String pw = "";
        for (int i=0; i<PASSWORD_LENGTH; i++)
        {
            int index = (int)(RANDOM.nextDouble()*letters.length());
            pw += letters.substring(index, index+1);
        }

//        if ( AbodeUtils.isJUnitTest() ) {
//            pw = "12345678";
//        }
        return pw;
    }


    public static HttpServletRequest getServerRequest() {
        if ( RequestContextHolder.getRequestAttributes() != null ) { // integration / unit tests
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } else {
            return null;
        }

    }

    public static HttpServletResponse getServerResponse() {
        if ( RequestContextHolder.getRequestAttributes() != null ) { // integration / unit tests
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        } else {
            return null;
        }
    }

    public static String getRemoteIp() {
        HttpServletRequest request = CoreUtils.getServerRequest();
        String ip = null;
        if ( request != null) {
            ip = request.getHeader("X-Real-IP");
            if (ip == null) {
                ip = request.getHeader("X-Forwarded-For");
                if ( ip == null ) {
                    ip = request.getRemoteAddr();
                }
            }
        }
        return ip;
    }

    public static void setCookieIfEmpty() {

        if ( !CoreUtils.isJUnitTest() ) {
            HttpServletRequest request = CoreUtils.getServerRequest();
            if (request != null) {
                String key = "id";
                Cookie[] cookies = request.getCookies();
                if ( cookies != null ) {
                    Optional<String> idCookie = Arrays.stream(cookies)
                            .filter(c -> key.equals(c.getName()))
                            .map(Cookie::getValue)
                            .findAny();

                    final int YEAR_SECONDS = 31536000;
                    String value;
                    if (idCookie.isEmpty()) {
                        HttpServletResponse response = CoreUtils.getServerResponse();
                        if (response != null) {
                            value = CoreUtils.nextSessionId();
                            Cookie cookie = new Cookie("id", value);
                            cookie.setMaxAge(YEAR_SECONDS);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    }
                }
            }
        }
    }

    public static Optional<String> getCookieId() {

        if ( !CoreUtils.isJUnitTest() ) {
            HttpServletRequest request = CoreUtils.getServerRequest();
            if (request != null && request.getCookies() != null) {
                String key = "id";
                return Arrays.stream(request.getCookies())
                        .filter(c -> key.equals(c.getName()))
                        .map(Cookie::getValue)
                        .findAny();
            }
        }
        return Optional.of("");
    }

    public static String nextSessionId() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }

    public static boolean isJUnitTest() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        List<StackTraceElement> list = Arrays.asList(stackTrace);
        for (StackTraceElement element : list) {
            if (element.getClassName().startsWith("org.junit.")) {
                return true;
            }
        }
        return false;
    }


    public static boolean isDevelopment() {
        String env = CoreUtils.getEnvironemnt();
        return env.equals("dev");
    }

    public static boolean isProduction() {
        String env = CoreUtils.getEnvironemnt();
        return  env.equals("prod");
    }

    public static String getEnvironemnt() {
        if ( isJUnitTest() ) {
            return "tests";
        } else {
            String environment = System.getenv().get("ENV");
            if ( environment == null ) {
                throw new IllegalArgumentException("Environment variable ENV cannot be null");
            }
            log.debug("Environment:" + environment);
            return environment;
        }
    }

    public static String getDomain() {
        if ( CoreUtils.isProduction() ) {
            return "https://thevpncompany.com.au";
        } else {
            return "http://localhost:8111";
        }
    }

    public static String getEnvironemnt(String envName) {
        return System.getenv().get(envName);
    }

    public static Process execute(String cmd, List<String> envs, File dir) {


        try {
            log.info("Executing command: " + cmd);
            log.info("ENV: " + envs);

            String[] array = envs.toArray(new String[0]);

            java.lang.Process p = Runtime.getRuntime()
                    .exec(cmd, array, dir);
            int result = p.waitFor();

            log.info("Process exit code: " + result);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader readerError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            String line = "";
            StringBuilder stdout = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stdout.append(line);
            }

            StringBuilder stderr = new StringBuilder();
            while ((line = readerError.readLine()) != null) {
                stderr.append(line);
            }

            return new Process(stdout.toString(), stderr.toString(), result);

        } catch (Exception e) {
            e.printStackTrace();
            return new Process("", e.getMessage(), -1);
        }

    }


    /**
     * Gets all the information related to the context to allow debugging
     *
     * @param e Exception (if any)
     * @return
     */
    public static String getDebugBody(Exception e) {

        HttpServletRequest request = CoreUtils.getServerRequest();

        final StringBuffer body = new StringBuffer();
        String line;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                body.append(line);
        } catch (Exception ex) {
            body.append("Exception when reading body");
            body.append(ex);
        }

        final StringBuilder headers = new StringBuilder();
        try {
            Enumeration headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = request.getHeader(key);
                headers.append(key + "=" + value + "\n");
            }
        } catch (Exception ex) {
            headers.append("Exception when reading headers");
            headers.append(ex);
        }

        final StringBuilder posts = new StringBuilder();
        try {
            Enumeration postNames = request.getParameterNames();
            while (postNames.hasMoreElements()) {
                String key = (String) postNames.nextElement();
                String value = request.getHeader(key);
                posts.append(key + "=" + value + "\n");
            }
        } catch (Exception ex) {
            posts.append("Exception when reading headers");
            posts.append(ex);
        }

        final StringBuilder env = new StringBuilder();
        try {
            Map<String, String> variables = System.getenv();
            for (Map.Entry<String, String> entry : variables.entrySet())
            {
                String name = entry.getKey();
                String value = entry.getValue();
                env.append(name + "=" + value + "\n");
            }
        } catch (Exception ex) {
            env.append("Exception when reading env");
            env.append(ex);
        }

        StringBuilder emailBody = new StringBuilder();

        emailBody.append("Debug data at: " + request.getRequestURI() + "?" + request.getQueryString() + "\n" +
                "\n" +
                "----------------\n" +
                "Headers of the request\n" +
                "----------------\n" +
                headers + "\n" +
                "\n" +
                "\n" +
                "----------------\n" +
                "Post params\n" +
                "----------------\n" +
                posts + "\n" +
                "\n" +
                "\n" +
                "----------------\n" +
                "Body of the request\n" +
                "----------------\n" +
                body + "\n" +
                "\n" +
                "\n" +
                "----------------\n" +
                "Environment variables\n" +
                "----------------\n" +
                env + "\n" +
                "\n" +
                "\n");

        if (e != null) {
            emailBody.append("----------------\n" +
                    "Exception\n" +
                    "----------------\n" +
                    e + "\n" +
                    "\n" +
                    "\n" +
                    "----------------\n" +
                    "Exception\n" +
                    "----------------\n" +
                    Arrays.toString(e.getStackTrace()) + "\n");
        }

        return emailBody.toString();
    }
}
