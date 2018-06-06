/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Car;
import com.teasoft.rydeon.model.Make;
import com.teasoft.rydeon.model.Model;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.repository.MakeRepo;
import com.teasoft.rydeon.repository.ModelRepo;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.service.CarService;
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
public class CarController {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    CarService carService;

    @Autowired
    MakeRepo makeRepo;

    @Autowired
    ModelRepo modelRepo;

    /**
     * Returns cars for a specified owner
     *
     * @param username
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "api/rydeon/car", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getByOwner(@RequestParam("owner") String username) throws Exception {
        Person person = personRepo.findByEmailOrPhone(username, username);
        if (person == null) {
            return new JSONResponse(false, 0, null, "UNKNOWN username");
        }
        List<Car> cars = carService.findByOwner(person);
        return new JSONResponse(true, cars.size(), cars, "SUCCESS");
    }

    @RequestMapping(value = "api/rydeon/car", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveCar(@RequestParam("user") String username,
            @RequestParam("make") Integer make, @RequestParam("model") Integer model,
            @RequestParam("year") Integer year, @RequestParam("regNumber") String regNumber) throws Exception {
        Person person = personRepo.findByEmailOrPhone(username, username);
        if (person == null) {
            return new JSONResponse(false, 0, null, "UNKNOWN username");
        }
        Make carMake = makeRepo.findOne(make.longValue());
        Model carModel = modelRepo.findOne(model.longValue());

        if (carMake == null || carModel == null) {
            return new JSONResponse(false, 0, null, "Invalid model or make");
        }

        Car car = new Car();
        car.setMake(carMake);
        car.setModel(carModel);
        car.setYear(year);
        car.setRegNumber(regNumber);
        car.setOwner(person);
        car.setAddedBy(person);

        car = carService.save(car);
        return new JSONResponse(true, 0, car, "SUCCESS");
    }

    @RequestMapping(value = "resources/rydeon/hello", method = RequestMethod.GET)
    @ResponseBody
    public String printHello() throws Exception {
       return "Hello Worldl";
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
