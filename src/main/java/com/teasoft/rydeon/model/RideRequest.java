/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.model;

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
import javax.persistence.TemporalType;

/**
 *
 * @author TOSHIBA
 */
@Entity
@Table
public class RideRequest {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Journey journey;
    @ManyToOne
    private Person person;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeCreated;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeUpdated;

    public RideRequest() {
    }
    
    @PrePersist
    void createdAt() {
        this.dateTimeCreated = this.dateTimeUpdated = new Date();
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

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public Date getDateTiimeUpdated() {
        return dateTimeUpdated;
    }
    
    
}
