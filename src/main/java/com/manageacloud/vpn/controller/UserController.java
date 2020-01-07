package com.manageacloud.vpn.controller;

import com.manageacloud.vpn.model.Email;
import com.manageacloud.vpn.model.Subscription;
import com.manageacloud.vpn.model.Token;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.dto.*;
import com.manageacloud.vpn.model.payment.PayableResult;
import com.manageacloud.vpn.service.ConfigurationService;
import com.manageacloud.vpn.service.PlanService;
import com.manageacloud.vpn.service.UserService;
import com.manageacloud.vpn.utils.AccountUtils;
import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.MailUtil;
import com.manageacloud.vpn.utils.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.manageacloud.vpn.utils.CoreUtils.getRemoteIp;

@RestController
public class UserController {

    protected Log log = LogFactory.getLog(this.getClass());

    private final UserService userService;

    private final PlanService planService;

    private final ConfigurationService configurationService;

    private final MailUtil mailUtil;

    public UserController(UserService userService, PlanService planService, MailUtil mailUtil, ConfigurationService configurationService) {
        this.userService = userService;
        this.planService = planService;
        this.mailUtil = mailUtil;
        this.configurationService = configurationService;
    }


    @PostMapping(value = "/get-started/xhr")
    public ResponseEntity createClient(final @Valid ClientDTO clientDTO,
                                       final BindingResult result) {

        final ResponseEntity response;

        String emailBody = CoreUtils.getDebugBody(null);
        String cookie = CoreUtils.getCookieId().orElse("NO_COOKIE");

        log.info("Starting get-started request");
        Email debugEmail = Email.builder()
                .with(CoreUtils.getRemoteIp(), cookie)
                .fromSupport()
                .to(Email.Account.TECH)
                .subject(CoreUtils.getEnvironemnt() + " - DEBUG get-started request")
                .withTextBody(emailBody)
                .build();
        mailUtil.sendEmail(debugEmail);

        if (!(result.hasErrors() || clientDTO.isEmailEmpty() || clientDTO.isClientDetailsEmpty() || clientDTO.isCreditCardEmpty())) {

            log.info("Looks good !");
            User user = userService.createUser(clientDTO);

            if ( user != null ) {
                log.info("User created");

                Subscription subscription = planService.createSubscription(user, clientDTO);

                if ( subscription.isEnabled() ) {

                    debugEmail = Email.builder()
                            .with(CoreUtils.getRemoteIp(), cookie)
                            .fromSupport()
                            .to(Email.Account.TECH)
                            .subject(CoreUtils.getEnvironemnt() + " - DEBUG all good! creating user")
                            .withTextBody(emailBody)
                            .build();
                    mailUtil.sendEmail(debugEmail);

                    userService.authenticateUser(user);
                    configurationService.generateConfigurationForClient(user);

                    response = new ResponseEntity(HttpStatus.OK);

                    WebUtils.ampRedirect("/dashboard");

                } else {
                    log.info("FAILED to create the plan");
                    debugEmail = Email.builder()
                            .with(CoreUtils.getRemoteIp(), cookie)
                            .fromSupport()
                            .to(Email.Account.TECH)
                            .subject(CoreUtils.getEnvironemnt() + " - DEBUG createSubscription failed")
                            .withTextBody(emailBody)
                            .build();
                    mailUtil.sendEmail(debugEmail);

                    PayableResult payableResult = planService.findLatestPayableResult(subscription);

                    AmpErrorResponseDto error = new AmpErrorResponseDto(payableResult.getText());
                    return new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
                }
            } else {

                log.info("FAILED to the user");
                debugEmail = Email.builder()
                        .with(CoreUtils.getRemoteIp(), cookie)
                        .fromSupport()
                        .to(Email.Account.TECH)
                        .subject(CoreUtils.getEnvironemnt() + " - DEBUG The user already exists. Please try to log-in into your account.")
                        .withTextBody(emailBody)
                        .build();
                mailUtil.sendEmail(debugEmail);


                AmpErrorResponseDto error = new AmpErrorResponseDto("The user already exists. Please try to log-in into your account.");
                return new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
            }



        } else {

            log.info("The validation FAILED: all errors: " + result.getAllErrors());
            log.info("The validation FAILED: isClientDetailsEmpty: " + clientDTO.isClientDetailsEmpty());
            log.info("The validation FAILED: isCreditCardEmpty: " + clientDTO.isCreditCardEmpty());
            debugEmail = Email.builder()
                    .with(CoreUtils.getRemoteIp(), cookie)
                    .fromSupport()
                    .to(Email.Account.TECH)
                    .subject(CoreUtils.getEnvironemnt() + " - DEBUG the validation Failed.")
                    .withTextBody(emailBody)
                    .build();
            mailUtil.sendEmail(debugEmail);

            StringBuilder errorMessage = AccountUtils.processCCerrors(clientDTO, result.getAllErrors());

            if ( clientDTO.getEmail() == null || clientDTO.getEmail().isEmpty()) {
                errorMessage.append("Email is mandatory. ");
            }

            AmpErrorResponseDto error = new AmpErrorResponseDto(errorMessage.toString());
            return new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);

        }

        return response;

    }


    @PostMapping(value = "/reset-password/xhr")
    public ResponseEntity resetPassword(final @Valid ResetPasswordDTO resetPasswordDTO,
                                       final BindingResult result,
                                       @CookieValue(value = "id", defaultValue = "") String idCookie) {

        final ResponseEntity response;

        if ( !result.hasErrors() ) {


            Token token = userService.generateToken(resetPasswordDTO);

            if ( token != null ) {


                String ip = getRemoteIp();

                Email email = Email.builder()
                        .with(ip, idCookie)
                        .fromSupport()
                        .to(resetPasswordDTO.getEmail())
                        .withType(Email.Type.RECOVER_PASSWORD)
                        .withRecoverPassword(token)
                        .build();

                mailUtil.sendEmail(email);

                return new ResponseEntity<>("{}", HttpStatus.OK);

            } else {
                AmpErrorResponseDto error = new AmpErrorResponseDto("We can't find this email in our system");
                return new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
            }

        } else {

            response = new ResponseEntity(HttpStatus.BAD_REQUEST);

        }

        return response;

    }


    @PostMapping(value = "/update-password/xhr")
    public ResponseEntity updatePassword(final @Valid UpdatePasswordTokenDTO updatePasswordDTO,
                                        final BindingResult result,
                                        @CookieValue(value = "id", defaultValue = "") String idCookie) {

        final ResponseEntity response;

        if ( !result.hasErrors() ) {

            boolean succesful = false;
            boolean validToken = userService.validateToken(Token.Type.RESET_PASSWORD, updatePasswordDTO);
            if ( validToken ) {
                User user = userService.findUserByToken(updatePasswordDTO);
                succesful = userService.resetPassword(user, updatePasswordDTO);
            }

            if ( succesful ) {
                return new ResponseEntity<>("{}", HttpStatus.OK);

            } else {
                AmpErrorResponseDto error = new AmpErrorResponseDto("Token not valid");
                return new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
            }

        } else {

            response = new ResponseEntity(HttpStatus.BAD_REQUEST);

        }

        return response;

    }



}
