package com.manageacloud.vpn.model.dto;

import com.manageacloud.vpn.model.Plan;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

public class ClientDTO implements PlanDTO {

    @Email
    private String email;

    private String name;
    private String address;
    private String city;
    private String country;

    @Pattern(regexp = "^$|[0-9]{4}")
    private String cc1;

    @Pattern(regexp = "^$|[0-9]{4}")
    private String cc2;

    @Pattern(regexp = "^$|[0-9]{4}")
    private String cc3;

    @Pattern(regexp = "^$|[0-9]{4}")
    private String cc4;

    @Pattern(regexp = "^$|[0|1][0-9]")
    private String month;

    @Pattern(regexp = "^$|[1|2][0-9]")
    private String year;

    @Pattern(regexp = "^$|[0-9]+")
    private String cvv;

    //@Pattern(regexp = "[0-9]+")
    //@NotBlank
    private Long plan;

    public String getName() {
        return name;
    }

    @Override
    public Long getPlan() {
        return plan;
    }

    @Override
    public void setPlan(Long plan) {
        this.plan = plan;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailEmpty() {
        return this.email.isEmpty();
    }

    public String getCc1() {
        return cc1;
    }

    public String getCc2() {
        return cc2;
    }

    public String getCc3() {
        return cc3;
    }

    public String getCc4() {
        return cc4;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getCvv() {
        return cvv;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCc1(String cc1) {
        this.cc1 = cc1;
    }

    public void setCc2(String cc2) {
        this.cc2 = cc2;
    }

    public void setCc3(String cc3) {
        this.cc3 = cc3;
    }

    public void setCc4(String cc4) {
        this.cc4 = cc4;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public boolean isClientDetailsEmpty() {
        if ( plan != null && ! Plan.Type.isFree(plan)) {
            return name.isEmpty() || address.isEmpty() || city.isEmpty() || country.isEmpty();
        } else {
            return false;
        }

    }

    public boolean isPlanIncorrect() {
        return plan == null || !(5 >= plan && plan >= 1);
    }

    public boolean isCreditCardEmpty() {
        if ( plan != null && ! Plan.Type.isFree(plan)) {
                return cc1.isEmpty() || cc2.isEmpty() || cc3.isEmpty()
                        || cc4.isEmpty() || month.isEmpty() || year.isEmpty() || cvv.isEmpty();
        } else {
            return false;
        }
    }
}
