/*
 * Copyright (c) 2020 TheVPNCompany.com.au
 */

package com.manageacloud.vpn.model.cloud;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "suppliers")
public class Supplier {

    public enum Type {

        DIGITAL_OCEAN(1L),
        VULTR(1L);

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

    @Column(insertable = false, updatable = false)
    private Timestamp deleted;

    protected Supplier() {}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getDeleted() {
        return deleted;
    }
}
