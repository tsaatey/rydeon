/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.model.fb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author Theodore Elikem Attigah
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FbAccount {

    private String id;
    private FbPhone phone;
    private FbApp application;

    public FbAccount() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FbPhone getPhone() {
        return phone;
    }

    public void setPhone(FbPhone phone) {
        this.phone = phone;
    }

    public FbApp getApplication() {
        return application;
    }

    public void setApplication(FbApp application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + ", phone=" + phone
                + ", application=" + application
                + '}';
    }
}
