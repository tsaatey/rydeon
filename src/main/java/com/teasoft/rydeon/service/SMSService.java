/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class SMSService {
    
    public Map sendSMS(String to, String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic a3p3cmdiY2s6YWF3eWF0Z3U=");
        
        Map<String, Object> dat = new HashMap();
        
//        dat.put("From", "rydeOn GH");
        dat.put("From", "VMA");
        dat.put("To", to);
        dat.put("Content", content);
        dat.put("RegisteredDelivery", true);
        
        HttpEntity req = new HttpEntity(dat, headers);
        
        RestTemplate restTemplate = new RestTemplate();
        
        Map<String, Object> account = restTemplate.postForObject("https://api.smsgh.com/v3/messages", req, Map.class);
        return account;
    }

}
