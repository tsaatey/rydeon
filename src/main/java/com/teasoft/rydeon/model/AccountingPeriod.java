package com.teasoft.rydeon.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Entity
@Table
public class AccountingPeriod implements Serializable {
    @Id
    private Integer accPeriod;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable=true, updatable=false)
    private Date startDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(insertable=true, updatable=false)
    private Date endDate;

    public AccountingPeriod() {
    }

    public AccountingPeriod(Integer accPeriod) {
        this.accPeriod = accPeriod;
    }

    
    public Integer getAccPeriod() {
        return accPeriod;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setAccPeriod(Integer accPeriod) {
        this.accPeriod = accPeriod;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    @PrePersist
    void createdAt() {
        this.startDate = new Date();
    }
    
}
