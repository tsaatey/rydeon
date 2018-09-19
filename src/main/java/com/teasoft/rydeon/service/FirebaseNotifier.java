/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class FirebaseNotifier {

    @Value("${com.teasoft.canteen.fcm-url}")
    String fcmUrl;
    @Value("${com.teasoft.canteen.fcm-key}")
    String fcmKey;

    public String publishToTopic(String title, String message, String topic, String type) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + fcmKey);
        httpHeaders.set("Content-Type", "application/json");
        JSONObject msg = new JSONObject();
        JSONObject json = new JSONObject();

        msg.put("title", title);
        msg.put("body", message);
        msg.put("notificationType", type);
        
        json.put("data", msg);
        json.put("to", "/topics/"+topic);

        HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(fcmUrl, httpEntity, String.class);
    }
    
    public String publish(String title, String message, String deviceToken, String type) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + fcmKey);
        httpHeaders.set("Content-Type", "application/json");
        JSONObject msg = new JSONObject();
        JSONObject json = new JSONObject();

        msg.put("title", title);
        msg.put("body", message);
        msg.put("notificationType", type);
        
        json.put("data", msg);
        json.put("to", deviceToken);

        HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(fcmUrl, httpEntity, String.class);
    }

}
