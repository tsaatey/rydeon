/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.auth.model.UserRole;
import com.teasoft.auth.model.Users;
import com.teasoft.auth.repo.UsersRepo;
import com.teasoft.auth.sec.PasswordHash;
import com.teasoft.auth.sec.TokenAuthService;
import com.teasoft.auth.service.RoleService;
import com.teasoft.auth.service.UserRoleService;
import com.teasoft.auth.service.UsersService;
import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.OTP;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.fb.FbAccount;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.service.OTPService;
import com.teasoft.rydeon.service.PersonService;
import com.teasoft.rydeon.service.SMSService;
import com.teasoft.rydeon.service.SignUpService;
import com.teasoft.rydeon.util.Enums;
import com.teasoft.rydeon.util.JSONResponse;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
    @Autowired
    TokenAuthService tokenAuthService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    SignUpService signUpService;

    @Value("${com.teasoft.auth.fb-graph-api}")
    private String fbApi;

    @Value("${com.teasoft.auth.fb-app-id}")
    private String fbAppId;

    @RequestMapping("resources/rydeon/testotp")
    @ResponseBody
    public String testOtp() {
        return otpService.generatePassword();
    }

    /**
     * Signs a user up using the Facebook graph API
     * @param response
     * @param data a map that should contain Facebook token
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "resources/fbsignin", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse fbSignup(HttpServletResponse response, @RequestBody Object data) throws Exception {
        Map<String, Object> dataHash = (HashMap<String, Object>) data;
        Map<String, Boolean> registered = new HashMap<String, Boolean>();
        String token, deviceToken;
        if (dataHash.containsKey("token")) {
            token = (String) dataHash.get("token");
        } else {
            return new JSONResponse(false, 0, "token", "token is required");
        }
        
        if(dataHash.containsKey("deviceToken")) {
            deviceToken = (String) dataHash.get("deviceToken");
        } else {
            throw new MissingParameterException("deviceToken");
        }

        //Make a call to facebook to verify token
        RestTemplate restTemplate = new RestTemplate();
        FbAccount fbAccount = restTemplate.getForObject(fbApi + "/me/?access_token=" + token, FbAccount.class);
        if (!fbAccount.getApplication().getId().equals(fbAppId)) {
            return new JSONResponse(false, 0, null, "Invalid App ID");
        }
        //Get user's phone number
        String phoneNumber = fbAccount.getPhone().getCountry_prefix() + fbAccount.getPhone().getNational_number();
        if (userService.findByPhone(phoneNumber) == null) {
            UserRole userRole = signUpService.saveUserAndRole(phoneNumber);
            Person person = new Person();
            person.setPhone(phoneNumber);
            person.setVerified(Boolean.TRUE);
            person.setUser(userRole.getUser());
            person.setDeviceToken(deviceToken);
            personRepo.save(person);
            //Log user in and return token
            Users uUser = userRepo.queryUser(phoneNumber);
            List<UserRole> uRole = new ArrayList<UserRole>();
            uRole.add(userRole);
            uUser.setRoles(uRole);
            String sToken = tokenAuthService.createTokenForUser(uUser);
            response.addHeader("x-auth-token", sToken);
            response.addHeader("Access-Control-Expose-Headers", "x-auth-token");

            //Flag as not registered
            registered.put("registered", Boolean.FALSE);
        } else {
            //user exists. log user in and return token
            Users user = userService.findByPhone(phoneNumber);
            String sToken = tokenAuthService.createTokenForUser(user);
            response.addHeader("x-auth-token", sToken);
            response.addHeader("Access-Control-Expose-Headers", "x-auth-token");
//            flag as registered
            registered.put("registered", Boolean.TRUE);
        }
        return new JSONResponse(true, 1, registered, Enums.JSONResponseMessage.SUCCESS.toString());
    }

    /**
     * Signup API. Call this API with user details
     *
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "resources/rydeon/signup", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse signup(@RequestBody Object data) throws Exception {
        Map<String, Object> dataHash = (HashMap<String, Object>) data;
        Person person = new Person();
        Users users = new Users();
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
            users.setPhone((String) dataHash.get("phoneNumber"));
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

        if (!password.equals(confirmPassword)) {
            return new JSONResponse(false, 0, "Password Mismatch", "Passwords do not match");
        }

        //Check if user is already registered
        if (personService.findByPhone(person.getPhone()) != null) {
            return new JSONResponse(false, 0, person.getPhone() + " is already registered", "Already Registered");
        }

        //Persist user and use the persisted user to persist person
        users.setPassword(PasswordHash.createHash((String) dataHash.get("password")));
        users.setEnabled(false);
        users = userService.save(users);
        person.setUser(users);
        person = personService.save(person);

        //Send a one-time-password to verify that the user's phone is his or hers
        String otp = otpService.generatePassword();
        OTP otpObj = new OTP();
        otpObj.setIsUsed(false);
        otpObj.setOtp(otp);
        otpObj.setPhone(person.getPhone());
        otpObj = otpService.save(otpObj);

        //Send SMS to user
        smsService.sendSMS(person.getPhone(), "Your RydeOn code is " + otp);
        return new JSONResponse(true, 1, "", "SUCCESS");

    }
    
    /**
     * API for creating employee accounts
     * @param data
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "admin/rydeon/create/employee/account", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse createEmployeeAccount(@RequestBody Object data) throws Exception {
        Map<String, Object> dataHash = (HashMap<String, Object>) data;
        Person person = new Person();
        Users users = new Users();
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
        if (dataHash.containsKey("email")) {
            person.setEmail((String) dataHash.get("email"));
            users.setEmail((String) dataHash.get("email"));
        } else {
            return new JSONResponse(false, 0, "email", "email is required");
        }
        if (dataHash.containsKey("username")) {
            users.setUsername((String) dataHash.get("username"));
        } else {
            return new JSONResponse(false, 0, "username", "username is required");
        }
        if (dataHash.containsKey("phoneNumber")) {
            person.setPhone((String) dataHash.get("phoneNumber"));
            users.setPhone((String) dataHash.get("phoneNumber"));
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

        if (!password.equals(confirmPassword)) {
            return new JSONResponse(false, 0, "Password Mismatch", "Passwords do not match");
        }

        //Check if user is already registered
        if (personService.findByPhone(person.getPhone()) != null) {
            return new JSONResponse(false, 0, person.getPhone() + " is already registered", "Already Registered");
        }

        //Persist user and use the persisted user to persist person
        users.setPassword(PasswordHash.createHash((String) dataHash.get("password")));
        users.setEnabled(true);
        users = userService.save(users);
        person.setUser(users);
        person = personService.save(person);

        return new JSONResponse(true, 1, "", "SUCCESS");

    }

    /**
     * Resend verification code API. It resends the code to the user's provided
     * phone number
     *
     * @param data
     * @return
     */
    @RequestMapping(value = "/resources/rydeon/signup/resendcode", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse resendCode(@RequestBody Object data) {
        Map<String, Object> dataHash = (HashMap<String, Object>) data;
        String phoneNumber;
        if (dataHash.containsKey("phoneNumber")) {
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
        smsService.sendSMS(phoneNumber, "Your RydeOn code is " + otp);
        return new JSONResponse(true, 1, "", "SUCCESS");
    }

    /**
     * API to verify user's phone number
     *
     * @param response
     * @param data
     * @return
     * @throws MissingParameterException
     */
    @RequestMapping(value = "resources/rydeon/signup/verifycode", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse verifyMember(HttpServletResponse response, @RequestBody Object data) throws MissingParameterException {
        Map<String, String> dataHash = (HashMap<String, String>) data;
        String phoneNumber, code;
        Person person;
        OTP otp;
        if (dataHash.containsKey("code")) {
            code = dataHash.get("code");
        } else {
            throw new MissingParameterException("Code");
        }

        if (dataHash.containsKey("phoneNumber")) {
            phoneNumber = dataHash.get("phoneNumber");
        } else {
            throw new MissingParameterException("Phone Number");
        }

        if ((person = personService.findByPhone(phoneNumber)) == null) {
            return new JSONResponse(false, 0, null, "User with phone number " + phoneNumber + " not found!");
        }

        if ((otp = otpService.findByPhoneAndOtp(phoneNumber, code)) == null) {
            return new JSONResponse(false, 0, null, "Invalid code");
        }

        if (otp.getIsUsed()) {
            return new JSONResponse(false, 0, null, "Code already used");
        }

        otp.setIsUsed(true);
        otpService.save(otp);

        //Set user as active
        Users user = userService.findByPhone(phoneNumber);
        user.setEnabled(true);
        userService.save(user);

        //Set person as verified
        person.setVerified(Boolean.TRUE);
        //Persist person
        person = personService.save(person);

        //Log user in and return token
        String token = tokenAuthService.createTokenForUser(user);
        response.addHeader("x-auth-token", token);
        response.addHeader("Access-Control-Expose-Headers", "x-auth-token");

        return new JSONResponse(true, 0, person, "SUCCESS");
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
