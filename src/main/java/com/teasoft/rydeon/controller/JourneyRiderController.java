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
import com.teasoft.rydeon.service.FirebaseNotifier;
import com.teasoft.rydeon.service.JourneyRiderService;
import com.teasoft.rydeon.util.Enums;
import com.teasoft.rydeon.util.JSONResponse;
import com.teasoft.rydeon.util.MappingAnyJsonHttpMessageConverter;
import io.jsonwebtoken.ExpiredJwtException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    @Autowired
    FirebaseNotifier fn;

    /**
     * Returns a journeyRider object given an email or phone.
     *
     * @param emailOrPhone the email address or phone of the rider
     * @return a journeyRider object
     * @throws Exception
     */
    @RequestMapping(value = "api/rydeon/journeyrider/rider", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getJourneyRider(@RequestParam("emailOrPhone") String emailOrPhone) throws Exception {
        Person p = personRepo.findByEmailOrPhone(emailOrPhone, emailOrPhone);
        if (p == null) {
            return new JSONResponse(false, 0, null, "Invalid Email or Phone");
        }
        List<JourneyRider> jr = jrService.findByPerson(p);
        return new JSONResponse(true, jr.size(), jr, "SUCCESS");
    }

    /**
     * Returns a list of JourneyRiders given a numerical journey id
     *
     * @param journey a numerical id that represents a journey
     * @return a list of JourneyRiders given a journey id
     * @throws Exception
     */
    @RequestMapping(value = "api/rydeon/journeyrider/journey", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getJourneyRider(@RequestParam("journey") Integer journey) throws Exception {
        Journey j = journeyRepo.findOne(journey.longValue());
        if (j == null) {
            return new JSONResponse(false, 0, null, "Journey ID");
        }
        List<JourneyRider> jr = jrService.findByJourney(j);
        return new JSONResponse(true, jr.size(), jr, "SUCCESS");
    }

    /**
     * Adds a rider to a Journey
     *
     * @param person email or phone number of rider
     * @param journey journey id of the journey
     * @return the JourneyRider object
     * @throws Exception
     */
    @RequestMapping(value = "api/rydeon/journey/rider", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getJourneyRider(@RequestParam("person") String person,
            @RequestParam("journey") Integer journey) throws Exception {

        Person p = personRepo.findByEmailOrPhone(person, person);
        Journey j = journeyRepo.findOne(journey.longValue());
        if (p == null || j == null) {
            return new JSONResponse(false, 0, null, "Invalid Email or Journey");
        }

        if (jrService.isJourneyFull(j)) {
            return new JSONResponse(false, 0, null, "Journey is full");
        }

        JourneyRider jRider = new JourneyRider();
        jRider.setJourney(j);
        jRider.setPerson(p);

        return new JSONResponse(true, 1, jrService.save(jRider), "SUCCESS");

    }

    /**
     * Approves or rejects a join ride request from a rider. A notification is
     * sent to the user on the status of the request
     *
     * @param status a boolean true to approve or false to reject
     * @param journey the journey
     * @param person the rider
     * @deprecated use approveJoinRideRequest instead
     * @return
     */
    @RequestMapping(value = "api/v1/rydeon/journey/approve", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    public JSONResponse approveRideRequest(@RequestParam("status") Boolean status,
            @RequestParam("journey") Integer journey, @RequestParam("person") String person) {

        Person p = personRepo.findByEmailOrPhone(person, person);
        Journey j = journeyRepo.findOne(journey.longValue());
        if (p == null || j == null) {
            return new JSONResponse(false, 0, null, "Invalid Email or Journey");
        }

        JourneyRider jRider = jrService.findByJournerAndPerson(j, p);
        if (jRider == null) {
            return new JSONResponse(false, 0, null, "Journey not found");
        }

        if (status) {
            jRider.setStatus(Enums.RideRequestStatus.APPROVED.toString());
        } else {
            jRider.setStatus(Enums.RideRequestStatus.REJECTED.toString());
        }

        return new JSONResponse(true, 1, jrService.save(jRider), Enums.JSONResponseMessage.SUCCESS.toString());

    }

    /**
     * Approves or rejects a ride joining request from a rider. A notification
     * is sent to the user on the status of his request. This replaces the
     * deprecated approveRideRequest
     *
     * @param status a boolean true to approve or false to reject a request
     * @param journeyRiderId a numerical id that represents a journeyRider
     * @return the saved journeyRider object
     */
    @RequestMapping(value = "api/rydeon/journey/approve", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse approveJoinRideRequest(@RequestParam("status") Boolean status,
            @RequestParam("journeyRider") Long journeyRiderId) {

        JourneyRider jRider = jrService.findOne(journeyRiderId);

        if (status) {
            jRider.setStatus(Enums.RideRequestStatus.APPROVED.toString());

        } else {
            jRider.setStatus(Enums.RideRequestStatus.REJECTED.toString());
        }
        JourneyRider journeyRider = jrService.save(jRider);

        //Send notification to the user
        String title = "Ride Request Status";
        String message = "";
        if (status) {
            message = "Your request to join ride is approved";
            fn.publish(title, message, jRider.getPerson().getDeviceToken(), Enums.NotificationType.CUSTOMER.toString());
        } else {
            message = "Your request to join ride is disapproved";
            fn.publish(title, message, jRider.getPerson().getDeviceToken(), Enums.NotificationType.CUSTOMER.toString());
        }

        return new JSONResponse(true, 1, journeyRider, Enums.JSONResponseMessage.SUCCESS.toString());

    }

    @RequestMapping(value = "resources/rydeon/gpa", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getGPAs(@RequestParam("startId") String startId, @RequestParam("endId") String endId) throws Exception {
//        RestTemplate restTemplate = new RestTemplate();
        String loginUrl = "http://uew.edu.gh/graduation/backend/graduands/login";
        String dataUrl = "";

        JSONObject json = new JSONObject();
        json.put("index_number", "5141040011");
        json.put("password", "5141040011");
        Map<String, String> data = new HashMap();
        data.put("index_number", "5141040011");
        data.put("password", "5141040011");
//        HttpEntity<String> httpEntity = new HttpEntity<String>("{}", httpHeaders);

        RestTemplate rest = new RestTemplate();
        List<HttpMessageConverter<?>> msgConverters
                = new ArrayList<HttpMessageConverter<?>>(1);
        msgConverters.add(new MappingAnyJsonHttpMessageConverter());
        rest.setMessageConverters(msgConverters);

        ResponseEntity<String> login = rest.postForEntity(loginUrl, data, String.class);

        return new JSONResponse(true, 1, login, "SUCCESS");
    }

//    @ExceptionHandler(NullPointerException.class)
//    @ResponseBody
//    public JSONResponse nullPointerException(NullPointerException e) {
//        return new JSONResponse(false, 0, null, e.getMessage());
//    }
//
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public JSONResponse exception(Exception e) {
//        return new JSONResponse(false, 0, null, e.getMessage());
//    }
//
//    @ExceptionHandler(EmptyResultDataAccessException.class)
//    @ResponseBody
//    public JSONResponse exception(EmptyResultDataAccessException e) {
//        return new JSONResponse(false, 0, null, e.getMessage());
//    }
//
//    @ExceptionHandler(MissingParameterException.class)
//    @ResponseBody
//    public JSONResponse exception(MissingParameterException e) {
//        return new JSONResponse(false, 0, null, e.getMessage());
//    }
//
//    @ExceptionHandler(ExpiredJwtException.class)
//    @ResponseBody
//    public JSONResponse expiredJwtException(Exception e) {
//        return new JSONResponse(false, 0, e.getMessage(), "ExpiredJwt");
//    }
}
