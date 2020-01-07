/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.utils;

import com.manageacloud.vpn.model.dto.ClientDTO;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class AccountUtils {

    static public StringBuilder processCCerrors(ClientDTO clientDTO, List<ObjectError> objectErrors) {
        StringBuilder errorMessage = new StringBuilder();

        if ( clientDTO.isCreditCardEmpty() ) {
            errorMessage.append("You can't leave empty the fields for the credit card information");
        } else if ( clientDTO.isClientDetailsEmpty() ) {
            errorMessage.append("You need to add your customer details: ");
            List<String> missingFields = new ArrayList<>();
            if ( clientDTO.getName() == null || clientDTO.getName().isEmpty()) {
                missingFields.add("Name");
            }
            if ( clientDTO.getAddress() == null || clientDTO.getAddress().isEmpty()) {
                missingFields.add("Address");
            }

            if ( clientDTO.getCity() == null || clientDTO.getCity().isEmpty()) {
                missingFields.add("City");
            }

            if ( clientDTO.getCountry() == null || clientDTO.getCountry().isEmpty()) {
                missingFields.add("Country");
            }

            errorMessage.append(String.join(", ", missingFields));

            if ( !missingFields.isEmpty() ) {
                errorMessage.append(". ");
            }

        } else {
            for (ObjectError objectError : objectErrors) {
                errorMessage.append(objectError.getDefaultMessage());
            }
        }

        return errorMessage;
    }
}
