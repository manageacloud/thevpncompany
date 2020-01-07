/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model;


import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "devices")
public class Device {

    public enum Type {

        LINUX(1L),
        WINDOWS(2L),
        MAC(3L),
        ANDROID(3L),
        IOS(5L);


        private long id;

        Type(long id) {
            this.id = id;
        }

        public long  getId() {
            return id;
        }


        public static Type valueOf(Long id) {
            for (Type type : Type.values()) {
                if ( id != null && type.getId() == id ) {
                    return type;
                }
            }
            return null;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    protected Device() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreated() {
        return created;
    }
}
