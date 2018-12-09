/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class Journal implements Serializable {
    
    public final static String BENEFIT = "BENEFIT";
    public final static String CONTRIBUTION = "CONTRIBUTION";
    public final static String EXPENDITURE = "EXPENDITURE";
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long journalId;
    
    private String journalType;
    
    @Column(insertable=true, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeCreated;
    
    public Journal() {
    }
    
    public Journal(String journalType) {
        this.journalType = journalType;
    }
    
    @PrePersist
    void createdAt() {
        this.dateTimeCreated = new Date();
    }

    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public String getJournalType() {
        return journalType;
    }

    public void setJournalType(String journalType) {
        this.journalType = journalType;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(Date dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }
    
}
