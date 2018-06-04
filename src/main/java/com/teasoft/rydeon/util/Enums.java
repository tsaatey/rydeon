/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.util;

/**
 *
 * @author Theodore Elikem Attigah
 */
public class Enums {

    public enum JSONResponseMessage {
        SUCCESS,
        DATA_NOT_FOUND,
        SERVER_ERROR,
        RESOURCE_NOT_FOUND,
        LOGIC_ERROR,
        UNAUTHORIZED_ACCESS,
        USER_DOES_NOT_EXIST,
        ACCESS_DENIED,
        MISSING_PARAMETERS;
    }
    
    public enum JourneyStatus {
        PENDING,
        STARTED,
        CANCELLED,
        COMPLETED
    }

}
