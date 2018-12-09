package com.teasoft.rydeon.model;

import com.fasterxml.jackson.annotation.JsonView;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Entity
@Table
public class Posting implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long postingId;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    private Person person;
    
    @NotNull
    @OneToOne
    private Journal journal;
    
    @NotNull
    private String journalType;
    
    @NotNull
    private Double amount;
    
    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    private AccountingPeriod accountPeriod;
    
    @Column(insertable=true, updatable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTimeCreated;
    
    @PrePersist
    void createdAt() {
        this.dateTimeCreated = new Date();
    }

    public Long getPostingId() {
        return postingId;
    }

    public String getJournalType() {
        return journalType;
    }

    public Double getAmount() {
        return amount;
    }

    public AccountingPeriod getAccountPeriod() {
        return accountPeriod;
    }

    public Date getDateTimeCreated() {
        return dateTimeCreated;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }


    public void setPostingId(Long postingId) {
        this.postingId = postingId;
    }

    public void setJournalType(String journalType) {
        this.journalType = journalType;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setAccountPeriod(AccountingPeriod accountPeriod) {
        this.accountPeriod = accountPeriod;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
}
