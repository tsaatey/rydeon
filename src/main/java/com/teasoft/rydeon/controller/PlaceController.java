/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Place;
import com.teasoft.rydeon.model.Region;
import com.teasoft.rydeon.service.PlaceService;
import com.teasoft.rydeon.service.RegionService;
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
public class PlaceController {

    @Autowired
    PlaceService placeService;

    @Autowired
    RegionService regionService;

    /**
     * Returns all places in the db
     * @return a list of all places in the db
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/place", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlaces() throws Exception {
        List<Place> places = placeService.findAll();
        return new JSONResponse(true, places.size(), places, "SUCCESS");
    }

    /**
     * Returns a list of places based on a search term
     * @param search the search term to search with
     * @return a list of places based on a search term
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/place/search", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse searchPlaces(@RequestParam("search") String search) throws Exception {
        List<Place> places = placeService.search(search);
        return new JSONResponse(true, places.size(), places, "SUCCESS");
    }

    /**
     * Adds a place to the db
     * @param name name of the place or location to add
     * @param region region or state of the location
     * @return the added object
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/place", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getPlaces(@RequestParam("name") String name,
            @RequestParam("region") Integer region) throws Exception {

        Place place = new Place();
        place.setPlaceName(name);
        Region reg = regionService.findOne(region.longValue());
        place.setRegion(reg);

        return new JSONResponse(true, 1, placeService.save(place), "SUCCESS");

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
