package com.manageacloud.vpn.model.payment;

import com.manageacloud.vpn.model.User;
import com.manageacloud.vpn.model.dto.ClientDTO;

import java.sql.Timestamp;

/**
 * Created by R3Systems Pty Ltd
 * User tk421 on 27/11/15.
 */
public class Cc {
    private int id;
    private User user;
    private String identifier;
    private String number;
    private String expire;
    private String cvv;
    private String description;
    private String code;
    private String txtid;
    private String ip;

    private String name;
    private String address;
    private String city;
    private String country;

    private Timestamp created;
    private Timestamp deleted;

    // for processing payment

    public Cc(User user, String identifier, ClientDTO clientDTO, String ip) {
        this.number = clientDTO.getCc1() + "-" + clientDTO.getCc2() + "-" + clientDTO.getCc3() + "-" + clientDTO.getCc4();
        this.expire = clientDTO.getMonth() + "/" + clientDTO.getYear();
        this.cvv = clientDTO.getCvv();

        this.user = user;
        this.identifier = identifier;
        this.ip = ip;

        this.name = user.getName();
        this.address = clientDTO.getAddress();
        this.city = clientDTO.getCity();
        this.country = clientDTO.getCountry();

    }



    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getNumber() {
        return number;
    }

    public String getExpire() {
        return expire;
    }
    public String getMonth() {
        return  expire.substring(0, Math.min(expire.length(), 2));
    }

    public String getYear() {
        return  "20" + expire.substring(3, Math.min(expire.length(), 5));
    }


    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getTxtid() {
        return txtid;
    }

    public String getIp() {
        return ip;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTxtid(String txtid) {
        this.txtid = txtid;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCvv() {
        return cvv;
    }

    public boolean isValid() {
        return this.code.equals("201");
    }

    public String getName() {
        return name;
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

}
