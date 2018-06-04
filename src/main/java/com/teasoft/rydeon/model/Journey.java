/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.model;

import com.teasoft.rydeon.util.Enums;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Entity
@Table
public class Journey implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Person person;   
    private String source;
    private String destination;
    private String sourceCoord;
    private String destCoord;
    
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date startTime;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date journeyDate;
    private Double amount;
    private String status;
    private Integer maxRiders;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateTimeCreated;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateTimeUpdated;
    
    @PrePersist
    void createdAt() {
        this.dateTimeCreated = this.dateTimeUpdated = new Date();
        this.status = Enums.JourneyStatus.PENDING.toString();
    }

    @PreUpdate
    void updatedAt() {
        this.dateTimeUpdated = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getJourneyStatus() {
        return status;
    }

    public void setJourneyStatus(String status) {
        this.status = status;
    }

    public Integer getMaxRiders() {
        return maxRiders;
    }

    public void setMaxRiders(Integer maxRiders) {
        this.maxRiders = maxRiders;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Date dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public Date getDateTimeUpdated() {
        return dateTimeUpdated;
    }

    public void setDateTimeUpdated(Date dateTimeUpdated) {
        this.dateTimeUpdated = dateTimeUpdated;
    }

    public String getSourceCoord() {
        return sourceCoord;
    }

    public void setSourceCoord(String sourceCoord) {
        this.sourceCoord = sourceCoord;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestCoord() {
        return destCoord;
    }

    public void setDestCoord(String destCoord) {
        this.destCoord = destCoord;
    }
    
    
}
