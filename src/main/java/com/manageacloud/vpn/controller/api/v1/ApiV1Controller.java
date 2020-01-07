

package com.manageacloud.vpn.controller.api.v1;

import com.manageacloud.vpn.model.ClientCertificate;
import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.api.v1.*;
import com.manageacloud.vpn.model.cloud.Server;
import com.manageacloud.vpn.service.CloudService;
import com.manageacloud.vpn.service.ConfigurationService;
import com.manageacloud.vpn.service.PlanService;
import com.manageacloud.vpn.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * V1 API is just for internal usage. The security layer needs to be created
 * via http(s) server (basic auth, authorization via IP, etc)
 */
@RestController
public class ApiV1Controller {

    protected Log log = LogFactory.getLog(this.getClass());

    private final CloudService cloudService;

    private final UserService userService;

    private final ConfigurationService configurationService;

    private final PlanService planService;

    public ApiV1Controller(CloudService cloudService, UserService userService, ConfigurationService configurationService, PlanService planService) {
        this.cloudService = cloudService;
        this.userService = userService;
        this.configurationService = configurationService;
        this.planService = planService;
    }

    /**
     * Servers actions
     *  1 - CreationRequest: Executes the actions required when the server build has been requested
     *  2 - CreationReady: Executes the actions required when the server is ready to use
     *  3 - CreationFailed: Executes the actions required when the server failed to be created
     *
     * TODO This method should be returning the object Server.
     *
     * @return {"response": 0} if executed correctly, {"response": 1, "error": "Error Message"} if failed.
     */
    @PostMapping(value = "/api/v1/server/{server_id}/action")
    public ResponseEntity actions(
            @PathVariable(name="server_id") Long serverId,
            final @Valid ApiV1ServerActionDTO apiV1ServerActionDTO,
            final BindingResult result

) {

        final ResponseEntity response;

        log.info("Processing server action");

        if (!result.hasErrors()) {
            Server server = cloudService.processAction(serverId, apiV1ServerActionDTO);
            if (server != null) {
                log.info("Action processed successfully");
                ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO();
                response = new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                log.error("Failed while processing action. Server not found.");
                response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else {
            ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO(1, result.toString());
            log.error("Error while processing server action: " + responseDto.getError());
            response = new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }


        return response;
    }


    /**
     * Update User Certificates
     *
     * Update the client certificate, saves it in the client's profile and generates the devices configurations.
     *
     * - client_private
     * - client_public
     * - ca
     * - static_key
     *
     * TODO This method should be returning the whole object.
     *
     * @return {"response": 0} if executed correctly, {"response": 1, "error": "Error Message"} if failed.
     */
    @PutMapping(value = "/api/v1/user/{userId}/certs")
    public ResponseEntity updateUserCerts(
            @PathVariable(name="userId") Long userId,
            final @Valid ApiV1UserDTO apiV1UserDTO,
            final BindingResult result

    ) {

        final ResponseEntity response;

        log.info("Processing saving client certificate");

        User user = userService.findUserById(userId);

        if ( user == null ) {
            ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO(1, "User " + userId + "does not exist");
            log.error("Error while saving client certificate: " + responseDto.getError());
            response = new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        } else if (!result.hasErrors()) {

            ClientCertificate clientCertificate = configurationService.updateCertificates(user, apiV1UserDTO);
            configurationService.generateDeviceConfiguration(user);

            if (clientCertificate != null) {
                log.info("Certificate Saved Successfully");
                ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO();
                response = new ResponseEntity<>(responseDto, HttpStatus.OK);
            } else {
                ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO(1, "An error has occurred while updating the certificate.");
                log.error("Error while saving client certificate: " + responseDto.getError());
                response = new ResponseEntity<>(responseDto, HttpStatus.PRECONDITION_FAILED);
            }
        } else {
            ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO(1, result.toString());
            log.error("Error while saving client certificate: " + responseDto.getError());
            response = new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }


        return response;
    }


    /**
     * Create User Certificate
     *
     * Sends the request to generate the user certs.
     *
\    * @return {"response": 0} if executed correctly, {"response": 1, "error": "Error Message"} if failed.
     */
    @PostMapping(value = "/api/v1/user/{userId}/certs")
    public ResponseEntity createUserCerts(
            @PathVariable(name="userId") Long userId
    ) {

        final ResponseEntity response;

        log.info("Creating client certificate");

        User user = userService.findUserById(userId);

        if ( user == null ) {
            ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO(1, "User " + userId + "does not exist");
            log.error("Error while creating client certificate: " + responseDto.getError());
            response = new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        } else {
            configurationService.generateConfigurationForClient(user);
            ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO();
            response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        }


        return response;
    }


    /**
     * Create Device Configurations
     *
     * Sends the request to generate the configuration for the devices.
     *
     * @return {"response": 0} if executed correctly, {"response": 1, "error": "Error Message"} if failed.
     */
    @PostMapping(value = "/api/v1/user/{userId}/conf")
    public ResponseEntity createUserDeviceConfigurations(
            @PathVariable(name="userId") Long userId
    ) {

        final ResponseEntity response;

        log.info("Creating device configurations certificate");

        User user = userService.findUserById(userId);

        if ( user == null ) {
            ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO(1,  "User " + userId + "does not exist");
            log.error("Error while creating device configurations: " + responseDto.getError());
            response = new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        } else {
            configurationService.generateDeviceConfiguration(user);
            log.info("Device Configuration successfully generated");
            ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO();
            response = new ResponseEntity<>(responseDto, HttpStatus.OK);
        }


        return response;
    }


    /**
     * Gets the user information. The information that we are currently sharing is if the user can
     * connect to a VPN Server.
     *
     * @return { "access": 0 } if the user can access to the VPN, and { "access": 1 }  the user cannot access to the VPN
     */
    @GetMapping(value = "/api/v1/user/{userId}")
    public ResponseEntity getUserData(
            @PathVariable(name="userId") Long userId
    ) {

        final ResponseEntity response;

        log.info("Creating device configurations certificate");

        User user = userService.findUserById(userId);

        if ( user == null ) {
            log.error("The user does not exists");
            response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {

            boolean isActive = planService.isUserActive(user);
            final ApiV1ResponseUserDTO apiV1ResponseUserDTO;
            if ( isActive ) {
                apiV1ResponseUserDTO = new ApiV1ResponseUserDTO(0);
            } else {
                apiV1ResponseUserDTO = new ApiV1ResponseUserDTO(1);
            }

            response = new ResponseEntity<>(apiV1ResponseUserDTO, HttpStatus.OK);
        }


        return response;
    }


    /**
     * Receives notifications about the bandwidth usage and performs actions accordingly.
     * - Bandwidth notification usage for 500Mb, if the user is "free" account marks the
     * column "used" in subscription table.
     * - Bandwidth notification usage for 0, if the user is "free" account sets the column
     * "used" in the subscription table to null.
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/api/v1/user/{userId}/bandwidth")
    public ResponseEntity updateUserStatus(
            @PathVariable(name="userId") Long userId,
            final @Valid ApiV1UserBandwidthDTO apiV1UserBandwidthDTO,
            final BindingResult result

    ) {

        final ResponseEntity response;

        if ( ! result.hasErrors() ) {

            log.info("Assigning to user " + userId + " bandwidth notification " + apiV1UserBandwidthDTO.getMegabits());

            User user = userService.findUserById(userId);

            if (user == null) {
                ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO(1,  "User " + userId + "does not exist");
                response = new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);
            } else {
                planService.processBandwidthNotification(user, apiV1UserBandwidthDTO);
                final ApiV1ResponseDTO apiV1ResponseDTO;
                apiV1ResponseDTO = new ApiV1ResponseDTO();

                response = new ResponseEntity<>(apiV1ResponseDTO, HttpStatus.OK);
            }

        } else {
            ApiV1ResponseDTO responseDto = new ApiV1ResponseDTO(1,  "User " + userId + ": " + result.getAllErrors());
            log.error("Error while creating device configurations: " + result.getAllErrors());
            response = new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
        }


        return response;
    }

}
