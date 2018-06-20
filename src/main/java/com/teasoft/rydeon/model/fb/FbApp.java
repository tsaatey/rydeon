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
public class FbApp {

    private String id;

    public FbApp() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{"
                + "id=" + id
                + '}';
    }

}
