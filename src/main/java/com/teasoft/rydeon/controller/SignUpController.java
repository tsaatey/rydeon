/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.Users;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.service.PersonService;
import com.teasoft.rydeon.service.UsersService;
import com.teasoft.rydeon.util.Enums;
import com.teasoft.rydeon.util.JSONResponse;
import com.teasoft.rydeon.util.PasswordHash;
import io.jsonwebtoken.ExpiredJwtException;
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
public class SignUpController {

    @Autowired
    PersonService personService;
    @Autowired
    UsersService userService;
    @Autowired
    PersonRepo personRepo;

    @RequestMapping(value = "resources/rydeon/signup", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse signUp(@RequestParam("lastname") String lastname,
            @RequestParam("firstname") String firstname, @RequestParam(value = "othernames", required = false) String othernames,
            @RequestParam("email") String email, @RequestParam("phone") String phone,
            @RequestParam("gender") String gender, @RequestParam(value = "digitalAddress", required = false) String digitalAddress,
            @RequestParam("password") String password) throws Exception {

        if (personRepo.findByEmailOrPhone(email, phone) != null) {
             return new JSONResponse(false, 1, null, "Duplicate Phone or Email address");
        }
        Person person = new Person();
        if (digitalAddress != null) {
            person.setDigitalAddress(digitalAddress);
        }
        person.setEmail(email);
        person.setFirstname(firstname);
        person.setGender(gender);
        person.setLastname(lastname);
        person.setPhone(phone);
        if (othernames != null) {
            person.setOthernames(othernames);
        }

        //Persist person
        person = personService.save(person);
        //Create a user and persist
        Users user = new Users();
        user.setIsActive(false);
        user.setPassword(PasswordHash.createHash(password));
        user.setPerson(person);
        user = userService.saveUsers(user);
        return new JSONResponse(true, 1, person, Enums.JSONResponseMessage.SUCCESS.toString());
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
