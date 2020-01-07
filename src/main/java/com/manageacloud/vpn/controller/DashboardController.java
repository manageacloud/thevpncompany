package com.manageacloud.vpn.controller;

import com.manageacloud.vpn.config.OS;
import com.manageacloud.vpn.model.*;
import com.manageacloud.vpn.model.cloud.Server;
import com.manageacloud.vpn.model.dto.AmpErrorResponseDto;
import com.manageacloud.vpn.model.dto.LocationDTO;
import com.manageacloud.vpn.service.ConfigurationService;
import com.manageacloud.vpn.service.LocationService;
import com.manageacloud.vpn.service.PlanService;
import com.manageacloud.vpn.service.UserService;
import com.manageacloud.vpn.utils.CoreUtils;
import com.manageacloud.vpn.utils.MailUtil;
import com.manageacloud.vpn.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(value = "/dashboard")
public class DashboardController {

    private final
    UserService userService;

    private final
    PlanService planService;

    private final
    LocationService locationService;

    private final
    ConfigurationService configurationService;

    private final
    MailUtil mailUtil;


    public DashboardController(UserService userService, PlanService planService, ConfigurationService configurationService, LocationService locationService, MailUtil mailUtil) {
        this.userService = userService;
        this.planService = planService;
        this.configurationService = configurationService;
        this.locationService = locationService;
        this.mailUtil = mailUtil;
    }

    @RequestMapping()
    public String dashboard(HttpServletRequest request, Model model) {
        OS os = OS.valueOf(request);
        model.addAttribute("os", os);
        return "dashboard/indexDashboard";
    }

    @RequestMapping(value = "/location")
    public String location(Model model,
                           @RequestParam(required = false, name="success") Integer statusSuccess,
                           @RequestParam(required = false, name="building") Integer statusBuilding,
                           @RequestParam(required = false, name="error") Integer statusError
    ) {

        User user = userService.getAuthenticatedUser();
        List<Location> locations = locationService.getAllLocations();
        UserLocation userLocation = locationService.findUserLocation(user);

        model.addAttribute("locations", locations);
        model.addAttribute("active", userLocation.getLocation());
        model.addAttribute("status_success", statusSuccess);
        model.addAttribute("status_building", statusBuilding);
        model.addAttribute("status_error", statusError);

        return "dashboard/location";
    }

    @RequestMapping(value = "/support")
    public String support() {

        return "dashboard/support";
    }

    @RequestMapping(value = "/account")
    public String account(Principal principal, Model model) {

        User user = userService.getAuthenticatedUser();
        Subscription subscription = planService.findSubscriptionByUser(user).orElse(null);
        model.addAttribute("subscription", subscription);

        return "dashboard/account";
    }


    /**
     * Downloads the configuration file customized for the device.
     *
     * if the configuration file is not generated you need to run the following API
     * curl -X POST http://localhost:8111/api/v1/user/17/conf
     *
     * @param device
     * @return
     */
    @RequestMapping(value = "/{device}/ovpn")
    @ResponseBody
    public ResponseEntity<String> configurationDownload(@PathVariable("device") String device) {

        final ResponseEntity response;

        User user = userService.getAuthenticatedUser();

        Device.Type deviceType = Device.Type.valueOf(device.toUpperCase());

        if ( deviceType != null ) {

            VpnDeviceConfiguration vpnDeviceConfiguration = configurationService.findDeviceConfiguration(user, Vpn.Type.OPEN_VPN, deviceType);

            if ( vpnDeviceConfiguration == null ) {
                Email debugEmail = Email.builder()
                        .with(CoreUtils.getRemoteIp(), CoreUtils.getCookieId().orElse("no cookie"))
                        .fromSupport()
                        .to(Email.Account.TECH)
                        .subject(CoreUtils.getEnvironemnt() + " - Error while trying to download documentation")
                        .withTextBody("The User " + user.getId() + " had problems downloading the configuration for the device " + device)
                        .build();

                mailUtil.sendEmail(debugEmail);

                String filename = "configuration_not_found.txt";
                response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("Content-Type", "text/plain")
                        .header("Content-Disposition", "attachment; filename=" + filename)
                        .body("Configuration file is not ready yet. Please wait a few seconds and refresh. ");

            } else {


                String filename = "thevpncompany-" + device.toLowerCase() + ".ovpn";
                String content = vpnDeviceConfiguration.getConfiguration();

                response = ResponseEntity.ok()
                        .header("Content-Type", "application/x-openvpn-profile")
                        .header("Content-Disposition", "attachment; filename=" + filename)
                        .body(content);

            }

        }  else {

            response = ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Device not found");

        }

        return response;
    }


    @PostMapping(value = "/location/xhr", produces = "application/json")
    @ResponseBody
    public ResponseEntity updateLocation(final @Valid LocationDTO locationDTO,
                                     final BindingResult result ) {

        ResponseEntity response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        User user = userService.getAuthenticatedUser();


        // check errors
        StringBuilder errorMessage = new StringBuilder();

        if ( !result.hasErrors() && errorMessage.length() == 0) {

            Server server = locationService.updateLocation(user, locationDTO);

            if ( server.isActive() ) {
                WebUtils.ampRedirect("/dashboard/location?success=1");
            } else {
                WebUtils.ampRedirect("/dashboard/location?building=1");
            }
            response = new ResponseEntity(HttpStatus.OK);
        } else {
            errorMessage.append("The location is not currently active. Please choose another location.");
        }

        if ( errorMessage.length() > 0 ) {
            AmpErrorResponseDto error = new AmpErrorResponseDto(errorMessage.toString());
            response = new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        return response;

    }


}