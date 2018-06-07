/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.OTP;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.Users;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.repository.UsersRepo;
import com.teasoft.rydeon.service.OTPService;
import com.teasoft.rydeon.service.PersonService;
import com.teasoft.rydeon.service.SMSService;
import com.teasoft.rydeon.service.UsersService;
import com.teasoft.rydeon.util.JSONResponse;
import com.teasoft.rydeon.util.PasswordHash;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Autowired
    OTPService otpService;
    @Autowired
    SMSService smsService;
    @Autowired
    UsersRepo userRepo;
    
    @RequestMapping("resources/rydeon/testotp")
    @ResponseBody
    public String testOtp() {
        return otpService.generatePassword();
    }
    
    /**
     * Signup API. Call this API with user details 
     * @param data
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "resources/rydeon/signup", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse signup(@RequestBody Object data) throws Exception {
        Map<String, Object> dataHash = (HashMap<String, Object>) data;
        Person person = new Person();
        String password, confirmPassword;
        if (dataHash.containsKey("firstname")) {
            person.setFirstname((String) dataHash.get("firstname"));
        } else {
            return new JSONResponse(false, 0, "firstname", "firstname is required");
        }
        if (dataHash.containsKey("lastname")) {
            person.setLastname((String) dataHash.get("lastname"));
        } else {
            return new JSONResponse(false, 0, "lastname", "lastname is required");
        }
        if (dataHash.containsKey("gender")) {
            person.setGender((String) dataHash.get("gender"));
        } else {
            return new JSONResponse(false, 0, "gender", "gender is required");
        }
        if (dataHash.containsKey("phoneNumber")) {
            person.setPhone((String) dataHash.get("phoneNumber"));
        } else {
            return new JSONResponse(false, 0, "Phone Number", "Phone Number is required");
        }
        if (dataHash.containsKey("password")) {
            password = (String) dataHash.get("password");
        } else {
            return new JSONResponse(false, 0, "Password", "Password is required");
        }
        if (dataHash.containsKey("confirmPassword")) {
            confirmPassword = (String) dataHash.get("confirmPassword");
        } else {
            return new JSONResponse(false, 0, "Confirm Password", "Confirm Password is required");
        }
        
        if(!password.equals(confirmPassword)) {
            return new JSONResponse(false, 0, "Password Mismatch", "Passwords do not match");
        }
        
        //Check if user is already registered
        if(personService.findByPhone(person.getPhone()) != null) {
            return new JSONResponse(false, 0, person.getPhone()+" is already registered", "Already Registered");
        }
        
        //Persist person and use the persisted person to persist user
        person = personService.save(person);
        Users users = new Users();
        users.setPerson(person);
        users.setPassword(PasswordHash.createHash((String) dataHash.get("password")));
        users.setIsActive(false);
        userService.saveUsers(users);
        
        //Send a one-time-password to verify that the user's phone is his or hers
        String otp = otpService.generatePassword();
        OTP otpObj = new OTP();
        otpObj.setIsUsed(false);
        otpObj.setOtp(otp);
        otpObj.setPhone(person.getPhone());
        otpObj = otpService.save(otpObj);
        
        //Send SMS to user
        smsService.sendSMS(person.getPhone(), "Your RydeOn code is "+ otp);
        return new JSONResponse(true, 1, "", "SUCCESS");

    }
    
    /**
     * Resend verification code API. It resends the code to the user's provided phone number
     * @param data
     * @return 
     */
    @RequestMapping(value="/resources/rydeon/signup/resendcode", method=RequestMethod.POST)
    @ResponseBody
    public JSONResponse resendCode(@RequestBody Object data) {
        Map<String, Object> dataHash = (HashMap<String, Object>) data;
        String phoneNumber;
        if(dataHash.containsKey("phoneNumber")) {
            phoneNumber = (String) dataHash.get("phoneNumber");
        } else {
            return new JSONResponse(false, 0, "Phone number is required", "Missing Parameter");
        }
        
        String otp = otpService.generatePassword();
        OTP otpObj = new OTP();
        otpObj.setIsUsed(false);
        otpObj.setOtp(otp);
        otpObj.setPhone(phoneNumber);
        otpObj = otpService.save(otpObj);
        
        //Send SMS to user
        smsService.sendSMS(phoneNumber, "Your RydeOn code is "+ otp);
        return new JSONResponse(true, 1, "", "SUCCESS");
    }
    
    /**
     * API to verify user's phone number
     * @param data
     * @return
     * @throws MissingParameterException 
     */
    @RequestMapping(value="resources/rydeon/signup/verifycode", method=RequestMethod.POST)
    @ResponseBody
    public JSONResponse verifyMember(@RequestBody Object data) throws MissingParameterException {
        Map<String, String> dataHash = (HashMap<String, String>) data;
        String phoneNumber, code;
        Person person;
        OTP otp;
        if(dataHash.containsKey("code")) {
            phoneNumber = dataHash.get("phoneNumber");
        } else {
            throw new MissingParameterException("Code");
        }
        
        if(dataHash.containsKey("phoneNumber")) {
            code = dataHash.get("code");
        } else {
            throw new MissingParameterException("Phone Number");
        }
        
        if((person = personService.findByPhone(phoneNumber)) == null) {
            return new JSONResponse(false, 0, null, "User with phone number "+phoneNumber+" not found!");
        } 
        
        if((otp = otpService.findByPhoneAndOtp(phoneNumber, code)) == null) {
            return new JSONResponse(false, 0, null, "Invalid code");
        }
        
        if(otp.getIsUsed()) {
            return new JSONResponse(false, 0, null, "Code already used");
        }
        
        otp.setIsUsed(true);
        otp = otpService.save(otp);
        
        //Set user as active
        Users user = userService.findByPerson(person);
        user.setIsActive(true);
        userService.saveUsers(user);
        
        //Set person as verified
        person.setVerified(Boolean.TRUE);
        //Persist person
        personService.save(person);
        return new JSONResponse(true, 0, otp, "SUCCESS");
  
    }
    
    @RequestMapping("resources/rydeon/testuser")
    @ResponseBody
    public JSONResponse getUser(@RequestParam("username") String username) {
        Person person = personRepo.findByEmailOrPhone(username, username);
        final Users user = userRepo.findByPerson(person);
        return new JSONResponse(true, 0, user, "SC");
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
