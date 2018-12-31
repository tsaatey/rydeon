/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Make;
import com.teasoft.rydeon.model.Model;
import com.teasoft.rydeon.repository.MakeRepo;
import com.teasoft.rydeon.repository.ModelRepo;
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
public class ModelController {
    @Autowired
    ModelRepo modelRepo;
    @Autowired
    MakeRepo makeRepo;
    
    /**
     * Returns a list of models of a particular make
     * @param makeId the make id
     * @return a list of models of a particular make
     */
    @RequestMapping(value="api/rydeon/model", method=RequestMethod.GET)
    @ResponseBody
    public JSONResponse getModel(@RequestParam Integer makeId) {
        Make make = makeRepo.findOne(makeId.longValue());
        if(make == null) {
            return new JSONResponse(false, 0, null, "Unknown make id");
        }
        
        List<Model> models =  modelRepo.findByMake(make);
        return new JSONResponse(true, models.size(), models, Enums.JSONResponseMessage.SUCCESS.toString());
    }
    
    /**
     * Adds a model to a make
     * @param makeId the make id
     * @param modelName the name of the model
     * @return the saved model object
     */
    @RequestMapping(value="admin/rydeon/model", method=RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveModel(@RequestParam Integer makeId, @RequestParam String modelName) {
        Make make = makeRepo.findOne(makeId.longValue());
        if(make == null) {
            return new JSONResponse(false, 0, null, "Unknown make id");
        }
        Model model = new Model();
        model.setMake(make);
        model.setModelName(modelName);
        model = modelRepo.save(model);
        return new JSONResponse(true, 1, model, Enums.JSONResponseMessage.SUCCESS.toString());
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
