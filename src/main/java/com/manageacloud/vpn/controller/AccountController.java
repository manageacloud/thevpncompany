/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.controller;

import com.manageacloud.vpn.model.Plan;
import com.manageacloud.vpn.model.Subscription;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.dto.AmpErrorResponseDto;
import com.manageacloud.vpn.model.dto.ClientDTO;
import com.manageacloud.vpn.model.dto.UpdatePasswordDTO;
import com.manageacloud.vpn.model.payment.PayableResult;
import com.manageacloud.vpn.service.PlanService;
import com.manageacloud.vpn.service.UserService;
import com.manageacloud.vpn.utils.AccountUtils;
import com.manageacloud.vpn.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class AccountController {

    private final UserService userService;

    private final PlanService planService;

    public AccountController(UserService userService, PlanService planService) {
        this.userService = userService;
        this.planService = planService;
    }

    /**
     * Update the plan
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/subscription")
    public String updateSubscription(Model model) {
        User user = userService.getAuthenticatedUser();
        Subscription subscription = planService.findSubscriptionByUser(user).orElseThrow();
        Plan.Type planType = Plan.Type.valueOf(subscription.getPlan().getId());
        if ( planType != null ) {
            model.addAttribute("currentPlan", planType);
        } else {
            model.addAttribute("currentPlan", Plan.Type.FREE_500);
        }
        return "dashboard/account/changeSubscription";
    }

    @PostMapping(value = "/dashboard/account/subscription/xhr", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateSubscriptionAction(final @Valid ClientDTO clientDTO,
                                                   final BindingResult result,
                                                   HttpServletRequest request,
                                                   HttpServletResponse servletResponse) {

        final ResponseEntity response;

        User user = userService.getAuthenticatedUser();

        if (!clientDTO.isPlanIncorrect()) {

            planService.changeSubscription(user, clientDTO);

            response = new ResponseEntity(HttpStatus.OK);

            WebUtils.ampRedirect("/dashboard/account/subscription/done");

        } else {

            AmpErrorResponseDto error = new AmpErrorResponseDto("Subscription is not correct");
            response = new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
        }

        return response;

    }


    /**
     * Update plan confirmation screen
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/subscription/done")
    public String updateSubscriptionOK(Model model) {
        User user = userService.getAuthenticatedUser();
        Subscription subscription = planService.findSubscriptionByUser(user).orElseThrow();
        model.addAttribute("subscription", subscription);
        return "dashboard/account/changeSubscriptionDone";
    }


    /**
     * Cancel existing subscription form
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/cancel")
    public String cancelSubscription() {

        return "dashboard/account/cancelSubscription";
    }

    /**
     * Cancel an existing subscription
     *
     * @return
     */
    @PostMapping(value = "/dashboard/account/cancel/xhr", produces = "application/json")
    @ResponseBody
    public ResponseEntity cancelSubscriptionAction() {

        final ResponseEntity response;

        User user = userService.getAuthenticatedUser();

        Subscription  subscription = planService.cancelSubscription(user);

        // check errors
        StringBuilder errorMessage = new StringBuilder();
        if ( subscription == null ) {
            errorMessage.append("You don't have an active subscription");
        }

        if ( errorMessage.length() == 0 ) { // an error has occurred
            WebUtils.ampRedirect("/dashboard/account/cancel/done");
            response = new ResponseEntity(HttpStatus.OK);
        } else {
            AmpErrorResponseDto error = new AmpErrorResponseDto(errorMessage.toString());
            response = new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }


        return response;

    }


    /**
     * Cancel existing plan Confirmation
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/cancel/done")
    public String cancelSubscriptionOK() {
        return "dashboard/account/cancelSubscriptionDone";
    }


    /**
     * Change Credit Card
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/cc")
    public String updateCC() {

        return "dashboard/account/updateCc";
    }

    @PostMapping(value = "/dashboard/account/cc/xhr", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateCC(final @Valid ClientDTO clientDTO,
                                       final BindingResult result) {

        final ResponseEntity response;

        if (!(result.hasErrors() || clientDTO.isClientDetailsEmpty() || clientDTO.isCreditCardEmpty())) {

            User user = userService.getAuthenticatedUser();
            PayableResult payableResult = planService.updateCreditCard(user, clientDTO);

            if( payableResult.is2xxCode() ) {
                WebUtils.ampRedirect("/dashboard/account/cc/done");
                response = new ResponseEntity(HttpStatus.OK);
            } else {

                String message = "The credit card does not look valid. The message from the bank says: " +
                        payableResult.getText() +
                        " Please check the credit card information and try again.";
                AmpErrorResponseDto error = new AmpErrorResponseDto(message);
                response = new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
            }

        } else {

            StringBuilder errorMessage = AccountUtils.processCCerrors(clientDTO, result.getAllErrors());

            AmpErrorResponseDto error = new AmpErrorResponseDto(errorMessage.toString());
            return new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);

        }

        return response;

    }


    /**
     * Cancel existing plan Confirmation
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/cc/done")
    public String updateCCDone() {
        return "dashboard/account/updateCcDone";
    }


    /**
     * Add a new subscription
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/subscription/new")
    public String newSubscription() {

        return "dashboard/account/newSubscription";
    }


    /**
     * The user cancelled a subscription and wants to get a new one.
     *
     * @param clientDTO
     * @param result
     * @return
     */
    @PostMapping(value = "/dashboard/account/subscription/new/xhr", produces = "application/json")
    @ResponseBody
    public ResponseEntity newSubscriptionAction(final @Valid ClientDTO clientDTO,
                                     final BindingResult result) {

        final ResponseEntity response;

        User user = userService.getAuthenticatedUser();

        if (!clientDTO.isPlanIncorrect()) {

            // cancel current free subscription before creating the paid subscription
            Subscription subscription = planService.createSubscription(user, clientDTO);

            if ( subscription.isEnabled() ) {

                response = new ResponseEntity(HttpStatus.OK);

                WebUtils.ampRedirect( "/dashboard/account/subscription/new/done");

            } else {
                // the latest payableresult record includes the details of the payment error
                PayableResult payableResult = planService.findLatestPayableResult(subscription);
                AmpErrorResponseDto error = new AmpErrorResponseDto(payableResult.getText());
                response = new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
            }

        } else {

            AmpErrorResponseDto error = new AmpErrorResponseDto("Subscription is not correct");
            response = new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
        }

        return response;

    }


    /**
     * Create new subscription successful message
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/subscription/new/done")
    public String newSubscriptionDone(Model model) {
        User user = userService.getAuthenticatedUser();
        Subscription subscription = planService.findSubscriptionByUser(user).orElseThrow();
        model.addAttribute("subscription", subscription);
        return "dashboard/account/newSubscriptionDone";
    }


    /**
     * Add a new subscription
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/password")
    public String updatePassword() {

        return "dashboard/account/updatePassword";
    }

    @PostMapping(value = "/dashboard/account/password/xhr", produces = "application/json")
    @ResponseBody
    public ResponseEntity updatePasswordXhr(final @Valid UpdatePasswordDTO updatePasswordDTO,
                                         final BindingResult result,
                                         @CookieValue(value = "id", defaultValue = "") String idCookie) {

        final ResponseEntity response;

        if ( !result.hasErrors() ) {

            User user = userService.getAuthenticatedUser();
            boolean succesful = userService.resetPassword(user, updatePasswordDTO);

            if ( succesful ) {

                response = new ResponseEntity(HttpStatus.OK);

                WebUtils.ampRedirect("/dashboard/account/password/done");

            } else {
                AmpErrorResponseDto error = new AmpErrorResponseDto("Token not valid");
                return new ResponseEntity<> (error, HttpStatus.BAD_REQUEST);
            }

        } else {

            response = new ResponseEntity(HttpStatus.BAD_REQUEST);

        }

        return response;

    }


    /**
     * Update password from user account
     *
     * @return
     */
    @GetMapping(value = "/dashboard/account/password/done")
    public String updatePasswordDone(Model model) {
        User user = userService.getAuthenticatedUser();
        return "dashboard/account/updatePasswordDone";
    }

}
