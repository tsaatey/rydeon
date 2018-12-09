/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.util;

import org.springframework.http.MediaType;

/**
 *
 * @author TOSHIBA
 */
public class MyMediaType extends MediaType {
    
    public MyMediaType(String type) {
        super(type);
    }
    
    public static MediaType parseMediaType(String s) {
        return new MediaType("1");
    }
    
}
