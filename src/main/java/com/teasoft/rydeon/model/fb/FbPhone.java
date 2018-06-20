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
public class FbPhone {

    private String number;
    private String country_prefix;
    private String national_number;

    public FbPhone() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCountry_prefix() {
        return country_prefix;
    }

    public void setCountry_prefix(String country_prefix) {
        this.country_prefix = country_prefix;
    }

    public String getNational_number() {
        return national_number;
    }

    public void setNational_number(String national_number) {
        this.national_number = national_number;
    }

    @Override
    public String toString() {
        return "{"
                + "number=" + number
                + ", country_prefix=" + country_prefix
                + ", national_number=" + national_number
                + '}';
    }
}
