/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.exception;

import com.teasoft.rydeon.util.Enums;

/**
 *
 * @author Elikem
 */
public class MissingParameterException extends Exception{
    private String param;
    public MissingParameterException(String param) {
        this.param = param;
    }
    @Override
    public String getMessage() {
        return Enums.JSONResponseMessage.MISSING_PARAMETERS.toString() + ": " + this.param + " is required";
    }
}
