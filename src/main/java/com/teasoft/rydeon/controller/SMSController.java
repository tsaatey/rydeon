/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.service.SMSService;
import com.teasoft.rydeon.util.JSONResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author TOSHIBA
 */
@RestController
public class SMSController {
    
    @Autowired
    SMSService smsService;
    
//    @RequestMapping(value="api/vma/sms", method=RequestMethod.GET)
//    @ResponseBody
//    public JSONResponse sendVMAMessages() {
//        Map<String, String> contacts = new HashMap<>();
//        List<String> contact = new ArrayList<>();
//        contact.add("233241444116");
//        contact.add("233552718393");
//        contact.add("233246106248");
//        contact.add("233556286408");
//        contact.add("233500388902");
//        contact.add("233246849717");
//        contact.add("233548311462");
//        contact.add("233247799791");
//        contact.add("233542665785");
//        contact.add("233269155089");
//        contact.add("233244059604");
//        contact.add("233540388960");
//        contact.add("233246426816");
//        contact.add("233543026990");
//        contact.add("233545313985");
//        contact.add("233243982253");
//        contact.add("233243260880");
//        contact.add("233242724203");
//        
//        String message = "Volta Music Awards officially invites you to the 2nd edition of VMA Plaque Presentation at Freedom Hotel, Ho @ Exactly 10am. Winners of the Maiden Edition are entreated to bring their managers for industry education and road map to VMA 2019. Call 0243260880,0246129375 for more info. Thank you";
//        for(int i = 0; i < contact.size(); i++) {
//            smsService.sendSMS(contact.get(i), message);
//        }
//        
//        return new JSONResponse(true, contact.size(), message, "Success");
//    }
    
    
}
