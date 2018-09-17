/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Journey;
import com.teasoft.rydeon.model.JourneyRider;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.Place;
import com.teasoft.rydeon.model.Region;
import com.teasoft.rydeon.repository.JourneyRepo;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.service.JourneyRiderService;
import com.teasoft.rydeon.util.Enums;
import com.teasoft.rydeon.util.JSONResponse;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
public class JourneyRiderController {
    @Autowired
    JourneyRiderService jrService;
    @Autowired
    PersonRepo personRepo;
    @Autowired
    JourneyRepo journeyRepo;
    
    @RequestMapping(value = "api/rydeon/journeyrider/rider", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse searchPlaces(@RequestParam("person") String person) throws Exception {
        Person p = personRepo.findByEmailOrPhone(person, person);
        if(p == null) {
            return new JSONResponse(false, 0, null, "Invalid Email or Phone");
        }
        List<JourneyRider> jr = jrService.findByPerson(p);
        return new JSONResponse(true, jr.size(), jr, "SUCCESS");
    }
    
    @RequestMapping(value = "api/rydeon/journeyrider/journey", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse searchPlaces(@RequestParam("journey") Integer journey) throws Exception {
        Journey j = journeyRepo.findOne(journey.longValue());
        if(j == null) {
            return new JSONResponse(false, 0, null, "Journey ID");
        }
        List<JourneyRider> jr = jrService.findByJourney(j);
        return new JSONResponse(true, jr.size(), jr, "SUCCESS");
    }
    
    @RequestMapping(value = "api/rydeon/journey/rider", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getPlaces(@RequestParam("person") String person,
            @RequestParam("journey") Integer journey) throws Exception {
        
        Person p = personRepo.findByEmailOrPhone(person, person);
        Journey j = journeyRepo.findOne(journey.longValue());
        if(p == null || j == null) {
            return new JSONResponse(false, 0, null, "Invalid Email or Journey");
        }
        
        if(jrService.isJourneyFull(j)) {
            return new JSONResponse(false, 0, null, "Journey is full");
        }
        
        JourneyRider jRider = new JourneyRider();
        jRider.setJourney(j);
        jRider.setPerson(p);

        return new JSONResponse(true, 1, jrService.save(jRider), "SUCCESS");
        
    }
    
    @RequestMapping(value = "api/rydeon/journey/approve", method=RequestMethod.POST)
    @ResponseBody
    public JSONResponse approveRideRequest(@RequestParam("status") Boolean status, 
            @RequestParam("journey") Integer journey, @RequestParam("person") String person) {
        
        Person p = personRepo.findByEmailOrPhone(person, person);
        Journey j = journeyRepo.findOne(journey.longValue());
        if(p == null || j == null) {
            return new JSONResponse(false, 0, null, "Invalid Email or Journey");
        }
        
        JourneyRider jRider = jrService.findByJournerAndPerson(j, p);
        if(jRider == null) {
            return new JSONResponse(false, 0, null, "Journey not found");
        }
        
        if(status) {
            jRider.setStatus(Enums.RideRequestStatus.APPROVED.toString());
        } else {
            jRider.setStatus(Enums.RideRequestStatus.REJECTED.toString());
        }
        
        return new JSONResponse(true, 1, jrService.save(jRider), Enums.JSONResponseMessage.SUCCESS.toString());
        
    }
    
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public JSONResponse nullPointerException(NullPointerException e) {
        return new JSONResponse(false, 0, null, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public JSONResponse exception(Exception e) {
        return new JSONResponse(false, 0, null, e.getMessage());
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseBody
    public JSONResponse exception(EmptyResultDataAccessException e) {
        return new JSONResponse(false, 0, null, e.getMessage());
    }

    @ExceptionHandler(MissingParameterException.class)
    @ResponseBody
    public JSONResponse exception(MissingParameterException e) {
        return new JSONResponse(false, 0, null, e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public JSONResponse expiredJwtException(Exception e) {
        return new JSONResponse(false, 0, e.getMessage(), "ExpiredJwt");
    }
}
