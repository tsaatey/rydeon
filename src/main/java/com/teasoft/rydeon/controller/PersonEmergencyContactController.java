/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.PersonEmergencyContact;
import com.teasoft.rydeon.repository.PersonEmergencyContactRepo;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.service.SMSService;
import com.teasoft.rydeon.util.JSONResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Theodore Elikem Attigah
 */
@RestController
public class PersonEmergencyContactController {

    @Autowired
    PersonEmergencyContactRepo pecRepo;
    @Autowired
    PersonRepo personRepo;
    @Autowired
    SMSService smsService;
    
    /**
     * Returns the emergency contact of a person
     * @param person the email or phone number of the person
     * @return the emergency contact of a person
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/emergency", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getContact(@RequestParam("person") String person) throws Exception {
        Person p = personRepo.findByEmailOrPhone(person, person);
        List<PersonEmergencyContact> pec = pecRepo.findByPerson(p);
        
        return new JSONResponse(true, pec.size(), pec, "SUCCESS");
    }
    
    /**
     * Sends SMS to a specified contact
     * @param message the content of the SMS
     * @param to destination contact
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/emergency/sms", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse sendMessage(@RequestParam("message") String message,
            @RequestParam("to") String to) throws Exception {
        return new JSONResponse(true, 0, smsService.sendSMS(to, message), "SUCCESS");
    }
    
    /**
     * Adds emergency contact to a person
     * @param person the person to add emergency contact for
     * @param contactName name of the emergency contact
     * @param phone phone number of the emergency contact
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/emergency", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveContact(@RequestParam("person") String person, @RequestParam("contactName") String contactName,
            @RequestParam("phone") String phone) throws Exception {
        Person p = personRepo.findByEmailOrPhone(person, person);
        PersonEmergencyContact pec = new PersonEmergencyContact();
        pec.setContactName(contactName);
        pec.setPerson(p);
        pec.setPhone(phone);
        pec = pecRepo.save(pec);
        return new JSONResponse(true, 0, pec, "SUCCESS");
    }
}
