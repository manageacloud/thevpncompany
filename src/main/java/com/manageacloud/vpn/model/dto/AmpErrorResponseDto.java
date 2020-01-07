package com.manageacloud.vpn.model.dto;

public class AmpErrorResponseDto {

    private String message;

    /**
     *
     * @param message Error message to show to the user
     */
    public AmpErrorResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
