/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.auth.model.Users;
import com.teasoft.auth.service.UsersService;
import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Car;
import com.teasoft.rydeon.model.Journey;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.Place;
import com.teasoft.rydeon.repository.CarRepo;
import com.teasoft.rydeon.repository.JourneyRepo;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.repository.PlaceRepo;
import com.teasoft.rydeon.service.JourneyService;
import com.teasoft.rydeon.service.PlaceService;
import com.teasoft.rydeon.util.Enums;
import com.teasoft.rydeon.util.JSONResponse;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
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
public class JourneyController {

    @Autowired
    JourneyService journeyService;
    @Autowired
    PlaceService placeService;
    @Autowired
    PlaceRepo placeRepo;
    @Autowired
    PersonRepo personRepo;
    @Autowired
    JourneyRepo journeyRepo;
    @Autowired
    CarRepo carRepo;
    @Autowired
    UsersService userService;

    /**
     * Creates a journey
     *
     * @param source where the journey will start from
     * @param destination where the journey will end
     * @param journeyDate the date journey will be embarked on
     * @param status the status of the journey. A journey can can STARTED, CANCELED, PENDING  or COMPLETED
     * @param maxRiders the maximum number of riders for the journey
     * @param sourceCoords geographical coordinates of the journey source
     * @param destCoords geographical coordinates of the journey destination
     * @param amount maximum amount that will be charged
     * @param carId a numerical id of the car that will be used for the journey
     * @param startTime time the journey will start
     * @return the saved journey
     * @throws Exception
     */
    @RequestMapping(value = "api/rydeon/journey", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse createJourney(@RequestParam(value = "source") String source,
            @RequestParam("destination") String destination, @RequestParam("journeyDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date journeyDate,
            @RequestParam("status") String status, @RequestParam("maxRiders") Integer maxRiders,
            @RequestParam("startTime") @DateTimeFormat(pattern = "HH:mm") Date startTime,
            @RequestParam(value = "sourceCoord", required = false) String sourceCoords,
            @RequestParam(value = "destCoord", required = false) String destCoords,
            @RequestParam(value = "amount") Double amount, @RequestParam(value = "carId") Long carId) throws Exception {

        Journey journey = new Journey();
        Users user = userService.getCurrentUsers();
        Person person = personRepo.findByUser(user);
        journey.setDestination(destination);
        journey.setSource(source);
        journey.setJourneyDate(journeyDate);
        journey.setStartTime(startTime);
        journey.setJourneyStatus(status);
        journey.setPerson(person);
        journey.setMaxRiders(maxRiders);
        journey.setAmount(amount);
        journey.setDestCoord(destCoords);
        Car car = carRepo.findOne(carId);
        journey.setCar(car);
        if (sourceCoords != null) {
            journey.setSourceCoord(sourceCoords);
        }

        return new JSONResponse(true, 1, journeyService.save(journey), "SUCCESS");

    }

    /**
     * Searches for a journey given the journey status and either the source or destination
     * @param source where the journey will start from
     * @param destination where the journey will end
     * @param status the status of the journey. A journey can can STARTED, CANCELED, PENDING  or COMPLETED
     * @return a list journeys that match the search criteria
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/journey", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getJourney(@RequestParam(value = "source", required = false) String source,
            @RequestParam(value = "destination", required = false) String destination,
            @RequestParam(value = "status") String status) throws Exception {

        if (destination == null) {
            if (source != null) {
                List<Journey> journeys = journeyService.findBySourceAndStatus(source, status);
                return new JSONResponse(true, journeys.size(), journeys, Enums.JSONResponseMessage.SUCCESS.toString());
            }
            List<Journey> journeys = journeyService.findAll();
            return new JSONResponse(true, journeys.size(), journeys, Enums.JSONResponseMessage.SUCCESS.toString());
        } else if (source == null) {
            List<Journey> journeys = journeyService.findByDestinationAndStatus(destination, status);
            return new JSONResponse(true, journeys.size(), journeys, Enums.JSONResponseMessage.SUCCESS.toString());
        } else {
            List<Journey> journeys = journeyService.findBySourceAndDestinationAndStatus(source, destination, status);
            return new JSONResponse(true, journeys.size(), journeys, Enums.JSONResponseMessage.SUCCESS.toString());
        }

    }
    
    /**
     * Returns all journeys in the db
     * @return all journeys in the db
     */
    @RequestMapping(value="api/rydeon/my/journey/all", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse myJourneys() {
        Users user = userService.getCurrentUsers();
        Person person = personRepo.findByUser(user);
        List<Journey> journeys = journeyRepo.findByPersonOrderByJourneyDateDesc(person);
        return new JSONResponse(true, journeys.size(), journeys, Enums.JSONResponseMessage.SUCCESS.toString());
    }

    /**
     * Searches given a specified key for journeys that match the search key.
     * @param key the search term. e.g. Aflao to Accra
     * @return a list of journeys that match the specified key
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/journey/search", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse searchJourney(@RequestParam(value = "key") String key) throws Exception {
        String[] words = key.split("\\s+");
        String source = null;
        String destination = null;
        
        if (words.length > 0) {
            source = words[0];
            destination = words[words.length - 1];
        }
        
        if (destination == null) {
            if (source != null) {
                List<Journey> journeys = journeyRepo.searchSource(source);
                return new JSONResponse(true, journeys.size(), journeys, Enums.JSONResponseMessage.SUCCESS.toString());
            }
            return new JSONResponse(true, 0, new ArrayList(), Enums.JSONResponseMessage.SUCCESS.toString());
        } else if (source == null) {
            List<Journey> journeys = journeyRepo.searchDestination(destination);
            return new JSONResponse(true, journeys.size(), journeys, Enums.JSONResponseMessage.SUCCESS.toString());
        } else {
            List<Journey> journeys = journeyRepo.search(source, destination);
            return new JSONResponse(true, journeys.size(), journeys, Enums.JSONResponseMessage.SUCCESS.toString());
        }

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
