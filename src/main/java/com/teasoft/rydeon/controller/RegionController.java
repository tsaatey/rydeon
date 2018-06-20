/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Region;
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
public class RegionController {

    @Autowired
    RegionService regionService;

    @RequestMapping(value = "api/rydeon/region", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRegions() throws Exception {
        List<Region> regions = regionService.findAll();
        return new JSONResponse(true, regions.size(), regions, "SUCCESS");
    }

    @RequestMapping(value = "api/rydeon/region", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveRegion(@RequestParam("name") String region) throws Exception {
        Region r = new Region();
        r.setRegionName(region);
        r = regionService.save(r);
        return new JSONResponse(true, 1, r, "SUCCESS");
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
