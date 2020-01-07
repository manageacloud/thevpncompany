package com.manageacloud.vpn.model.api.v1;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApiV1ResponseDTO {

    private int response;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;


    /**
     * Simple response interface for the API
     *
     * @param response Response code. Convention: 0 successful, Different than zero error.
     * @param error If the response code is an error, this must contain a description of the error.
     */
    public ApiV1ResponseDTO(int response, String error) {
        this.response = response;
        this.error = error;
    }

    /**
     * Default constructor, contains teh response for a successful action.
     */
    public ApiV1ResponseDTO() {
        this.response = 0;
    }

    public int getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }
}
