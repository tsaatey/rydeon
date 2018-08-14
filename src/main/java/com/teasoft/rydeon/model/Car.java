/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Make make;
    private String regNumber;
    @ManyToOne
    private Model model;
    private Integer year;
    @Lob
    private byte[] image;
    @ManyToOne
    private Person addedBy;
    @ManyToOne
    private Person owner;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateTimeCreated;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateTimeUpdated;
    
    @PrePersist
    void createdAt() {
        this.dateTimeCreated = this.dateTimeUpdated = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.dateTimeUpdated = new Date();
    }

    

    public Make getMake() {
        return make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Person getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Person addedBy) {
        this.addedBy = addedBy;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    
    
}
