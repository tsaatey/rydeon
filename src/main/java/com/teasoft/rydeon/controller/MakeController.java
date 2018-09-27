/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Make;
import com.teasoft.rydeon.repository.MakeRepo;
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
public class MakeController {
    @Autowired
    MakeRepo makeRepo;
    
    /**
     * Returns the list of all car makes in the db
     * @return the list of all car makes in the db
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/make", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getMakes() throws Exception {      
        List<Make> makes = makeRepo.findAll();
        return new JSONResponse(true, makes.size(), makes, "SUCCESS");
    }
    
    /**
     * Adds a car make to the db
     * @param make a make or a car manufacturer
     * @return the make object added
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/make", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveMake(@RequestParam("make") String make) throws Exception {      
        Make m = new Make();
        m.setMake(make);
        m = makeRepo.save(m);
        return new JSONResponse(true, 1, m, "SUCCESS");
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
