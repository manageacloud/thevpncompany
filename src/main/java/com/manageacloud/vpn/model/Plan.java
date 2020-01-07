package com.manageacloud.vpn.model;


import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        ),
        @TypeDef(
                name = "int-array",
                typeClass = IntArrayType.class
        )
})


@Entity
@Table(name = "plans")
public class Plan {

    public enum Type {

        MONTHLY(1L, 1L),
        SEMESTER(2L, 6L),
        YEAR(3L, 12L),
        FREE_NO_LIMITS(4L, 12L),
        FREE_500(5L, 1L);

        private long id;
        private long monthlyPeriod;

        Type(long id, long monthlyPeriod) {
            this.id = id;
            this.monthlyPeriod = monthlyPeriod;
        }

        public long  getId() {
            return id;
        }

        public long getMonthlyPeriod() {
            return monthlyPeriod;
        }

        public static Type valueOf(Long id) {
            for (Type type : Type.values()) {
                if ( id != null && type.getId() == id ) {
                    return type;
                }
            }
            return null;
        }

        public static boolean isFree(Long plan) {
            if ( plan == Plan.Type.MONTHLY.getId() || plan == Plan.Type.YEAR.getId() || plan == Plan.Type.SEMESTER.getId()) {
                return false;
            } else {
                return true;
            }
        }

        public boolean isFree() {
            return Type.isFree(this.getId());
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @org.hibernate.annotations.Type( type = "string-array" )
    @Column( name = "name", columnDefinition = "text[]" )
    private String[] name;

    private BigDecimal price;
    private BigDecimal price_discounted;

    private Integer data;

    @Column(insertable = false, updatable = false)
    private Timestamp created;

    @Column(insertable = false, updatable = false)
    private Timestamp deleted;

    protected Plan() {}

    public long getId() {
        return id;
    }

    public String[] getName() {
        return name;
    }

    // get the default name (en)
    public String getDefaultName() {
        return this.name[0];
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getPrice_discounted() {
        return price_discounted;
    }

    public BigDecimal getTotalDiscountedPrice () {

        Plan.Type type = Plan.Type.valueOf(id);

        if ( type != null ) {
            return price_discounted.multiply(BigDecimal.valueOf(type.getMonthlyPeriod()));
        } else {
            return null;
        }

    }

    public Integer getData() {
        return data;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getDeleted() {
        return deleted;
    }

    public Type getType() {
        return Type.valueOf(id);
    }
}
