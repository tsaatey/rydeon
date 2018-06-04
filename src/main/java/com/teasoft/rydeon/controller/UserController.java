/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.service.UsersService;
import com.teasoft.rydeon.util.JSONResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

/**
 *
 * @author Theodore Elikem Attigah
 */
@RestController
public class UserController {

    @Autowired
    UsersService userService;

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    /**
     * Creates an account which can be used to user. Account types can be 1, 2
     * or 3. 1 = administrator, 2 = registration officer, 3 = electoral officer
     *
     * @param data the post request body
     * @param response response object
     * @param request request object
     * @return a JSONResponse object that contains the created account details
     * @throws Exception
     */
//    @RequestMapping(value = "admin/api/rydeon/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public JSONResponse saveUser(@RequestBody Object data,
//            HttpServletResponse response, HttpServletRequest request) throws Exception {
//        JSONResponse jSONResponse = new JSONResponse();
//        Map<String, Object> dataHash = (Map<String, Object>) data;
//
//        User user = new User();
//
//        if (dataHash.containsKey("username")) {
//            if (userService.findOne((String) dataHash.get("username")) != null) {
//                return new JSONResponse(false, 0, null, "USER ALREADY EXISTS");
//            }
//            user.setUsername((String) dataHash.get("username"));
//        } else {
//            return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//        }
//
//        if (dataHash.containsKey("password")) {
//            user.setPassword(PasswordHash.createHash((String) dataHash.get("password")));
//        } else {
//            return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//        }
//
//        if (dataHash.containsKey("role")) {
//            if (Pattern.matches("[12345]", String.valueOf((Integer) dataHash.get("role")))) {
//                user.setRole((Integer) dataHash.get("role"));
//            } else {
//                return new JSONResponse(false, 0, null, "INVALID ACCOUNT TYPE");
//            }
//        } else {
//            return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//        }
//
//        if (dataHash.containsKey("name")) {
//            user.setName((String) dataHash.get("name"));
//        } else {
//            return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//        }
//
//        userService.saveUser(user);
//
//        jSONResponse.setStatus(true);
//        jSONResponse.setMessage(Enums.JSONResponseMessage.SUCCESS.toString());
//        return jSONResponse;
//    }
    
    @RequestMapping(value="resources/api/rydeon/user-status", method=RequestMethod.GET)
    @ResponseBody
    public JSONResponse getUserStatus() {
        boolean status = userService.count() > 0;
        return new JSONResponse(status, 0, null, "");
    }
    
//    @RequestMapping(value = "api/rydeon/authenticate", method=RequestMethod.POST)
//    @ResponseBody
//    public JSONResponse auth(@RequestBody Object data,
//            HttpServletResponse response, HttpServletRequest request) throws Exception{
//        JSONResponse jSONResponse = new JSONResponse();
//        Map<String, String> dataHash = (Map<String, String>) data;
//        String username, password;
//        
//        if(dataHash.containsKey("password")) {
//            password = dataHash.get("password");
//        } else {
//            throw new MissingParameterException("password");
//        }
//        
//        if(dataHash.containsKey("username")) {
//            username = dataHash.get("username");
//        } else {
//            throw new MissingParameterException("username");
//        }
//        
//        Users user = userService.authenticateUser(username, password);
//        
//        if(user != null) {
//            return new JSONResponse(true, 0, null, "SUCCESS");
//        }
//        
//        return new JSONResponse(false, 0, null, "SUCCESS");
//        
//    }
    
//    @RequestMapping(value="api/rydeon/password/change", method=RequestMethod.POST)
//    @ResponseBody
//    public JSONResponse changePassword(@RequestBody Object data,
//            HttpServletResponse response, HttpServletRequest request) throws Exception {
//        JSONResponse jSONResponse = new JSONResponse();
//        Map<String, String> dataHash = (Map<String, String>) data;
//        String username, oldPassword, newPassword;
//        if(dataHash.containsKey("username")) {
//            username = dataHash.get("username");
//        } else {
//            throw new MissingParameterException("username");
//        }
//        
//        if(dataHash.containsKey("oldPassword")) {
//            oldPassword = dataHash.get("oldPassword");
//        } else {
//            throw new MissingParameterException("old password");
//        }
//        
//        if(dataHash.containsKey("newPassword")) {
//            newPassword = dataHash.get("newPassword");
//        } else {
//            throw new MissingParameterException("new password");
//        }
//        
//        User user = userService.findOne(username);
//        
//        if(user == null) {
//            return new JSONResponse(false, 0, null, "User does not exist");
//        }
//        
//        if(userService.authenticate(username, oldPassword) == null) {
//            return new JSONResponse(false, 0, null, "Wrong Password");
//        }
//        
//        user.setPassword(PasswordHash.createHash(newPassword));
//        
//        userService.saveUser(user);
//        
//        user.setPassword("xxx");
//        
//        return new JSONResponse(true, 1, user, "SUCCESS");
//    }

//    @RequestMapping(value = "resources/api/rydeon/user/signup", method = RequestMethod.POST)
//    @ResponseBody
//    public JSONResponse addUser(@RequestBody Object data,
//            HttpServletResponse response, HttpServletRequest request) throws Exception {
//        JSONResponse jSONResponse = new JSONResponse();
//        Map<String, Object> dataHash = (Map<String, Object>) data;
//
//        Users user = new Users();
//
//        if (userService.count() == 0) {
//            if (dataHash.containsKey("username")) {
//                user.setUsername((String) dataHash.get("username"));
//            } else {
//                return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//            }
//
//            if (dataHash.containsKey("password")) {
//                user.setPassword(PasswordHash.createHash((String) dataHash.get("password")));
//            } else {
//                return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//            }
//
//            if (dataHash.containsKey("role")) {
//                if (Pattern.matches("[12345]", String.valueOf((Integer) dataHash.get("role")))) {
//                    user.setRole((Integer) dataHash.get("role"));
//                } else {
//                    return new JSONResponse(false, 0, null, "INVALID ACCOUNT TYPE");
//                }
//            } else {
//                return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//            }
//
//            if (dataHash.containsKey("name")) {
//                user.setName((String) dataHash.get("name"));
//            } else {
//                return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//            }
//
//            userService.saveUser(user);
//
//            jSONResponse.setStatus(true);
//            jSONResponse.setMessage(Enums.JSONResponseMessage.SUCCESS.toString());
//            return jSONResponse;
//        } else {
//            return new JSONResponse(false, 0, null, "This resource can not be used");
//        }
//
//    }

//    @RequestMapping(value = "admin/api/rydeon/user/reset", method = RequestMethod.POST)
//    @ResponseBody
//    public JSONResponse getAllCandidates(@RequestBody Object data) throws Exception {
//        JSONResponse jSONResponse = new JSONResponse();
//        Map<String, String> dataHash = (Map<String, String>) data;
//        String adminUsername, adminPassword, username;
//
//        if (dataHash.containsKey("adminUsername")) {
//            adminUsername = dataHash.get("adminUsername");
//        } else {
//            throw new MissingParameterException("admin username");
//        }
//        if (dataHash.containsKey("adminPassword")) {
//            adminPassword = dataHash.get("adminPassword");
//        } else {
//            throw new MissingParameterException("admin password");
//        }
//        if (dataHash.containsKey("username")) {
//            username = dataHash.get("username");
//        } else {
//            throw new MissingParameterException("username");
//        }
//
//        User userObject = userService.authenticate(adminUsername, adminPassword);
//
//        if (userObject != null && userObject.getRole() == User.ADMIN) {
//            User user;
//            if ((user = userService.findOne(username)) != null) {
//                user.setPassword(PasswordHash.createHash("123456"));
//                
//                userService.saveUser(user);
//                user.setPassword("xxx");
//                jSONResponse.setStatus(true);
//                jSONResponse.setCount(1);
//                jSONResponse.setMessage(Enums.JSONResponseMessage.SUCCESS.toString());
//                jSONResponse.setResult(user);
//                return jSONResponse;
//            } else {
//                return new JSONResponse(false, 0, null, "User does not exist");
//            }
//        } else {
//            return new JSONResponse(false, 0, null, "You don't have permission to do this");
//        }
//    }

//    @RequestMapping(value = "admin/api/rydeon/user", method = RequestMethod.GET)
//    @ResponseBody
//    public JSONResponse getAllUsers(/*@RequestParam Object data*/) throws Exception {
//        JSONResponse jSONResponse = new JSONResponse();
////        Map<String, String> dataHash = (Map<String, String>) data;
////        String username, password;
////        if (dataHash.containsKey("username")) {
////            username = (String) dataHash.get("username");
////        } else {
////            throw new MissingParameterException();
////        }
////        if (dataHash.containsKey("password")) {
////            password = (String) dataHash.get("password");
////        } else {
////            throw new MissingParameterException();
////        }
//
////        User userObject = userService.authenticate(username, password);
////        if (userObject != null) {
//        Iterable<User> users = userService.findAll();
//
//        users.forEach(user -> {
//            user.setPassword("xxx");
//        });
//        return new JSONResponse(true, userService.count(), users, Enums.JSONResponseMessage.SUCCESS.toString());
////        }
//
////        jSONResponse.setStatus(false);
////        jSONResponse.setCount(0);
////        jSONResponse.setMessage(Enums.JSONResponseMessage.ACCESS_DENIED.toString());
////        jSONResponse.setResult(null);
////        return jSONResponse;
//    }

//    @RequestMapping(value = "admin/api/rydeon/user", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public JSONResponse modifyUser(@RequestBody Object data,
//            HttpServletResponse response, HttpServletRequest request) throws Exception {
//        JSONResponse jSONResponse = new JSONResponse();
//        Map<String, Object> dataHash = (Map<String, Object>) data;
//
////        String adminUsername, adminPassword;
////
////        if (dataHash.containsKey("adminUsername")) {
////            adminUsername = (String) dataHash.get("adminUsername");
////        } else {
////            throw new MissingParameterException();
////        }
////        if (dataHash.containsKey("adminPassword")) {
////            adminPassword = (String) dataHash.get("adminPassword");
////        } else {
////            throw new MissingParameterException();
////        }
////
////        User userObject = userService.authenticate(adminUsername, adminPassword);
////        if (userObject == null) {
////            return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.ACCESS_DENIED.toString());
////        }
//        User user;
//        if (dataHash.containsKey("username")) {
//            user = userService.findOne((String) dataHash.get("username"));
//        } else {
//            return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//        }
//
//        if (user == null) {
//            return new JSONResponse(false, 0, null, "USER DOES NOT EXIST");
//        }
//
//        if (dataHash.containsKey("password")) {
//            user.setPassword(PasswordHash.createHash((String) dataHash.get("password")));
//        } else {
////            return new JSONResponse(false, 0, null, "Password is required");
//        }
//
//        if (dataHash.containsKey("role")) {
//            if (Pattern.matches("[12345]", String.valueOf((Integer) dataHash.get("role")))) {
//                user.setRole((Integer) dataHash.get("role"));
//            } else {
//                return new JSONResponse(false, 0, null, "INVALID ACCOUNT TYPE");
//            }
//        } else {
//            return new JSONResponse(false, 0, null, "Account type is required");
//        }
//
//        if (dataHash.containsKey("name")) {
//            user.setName((String) dataHash.get("name"));
//        } else {
//            return new JSONResponse(false, 0, null, "Name is required");
//        }
//
//        userService.saveUser(user);
//
//        user.setPassword("xxx");
//
//        jSONResponse.setStatus(true);
//        jSONResponse.setMessage(Enums.JSONResponseMessage.SUCCESS.toString());
//        jSONResponse.setResult(user);
//        return jSONResponse;
//    }

//    @RequestMapping(value = "admin/api/rydeon/user", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public JSONResponse deleteUser(@RequestBody Object data, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        JSONResponse jSONResponse = new JSONResponse();
//        Map<String, String> dataHash = (Map<String, String>) data;
//        String adminUsername, adminPassword;
//
//        if (dataHash.containsKey("adminUsername")) {
//            adminUsername = dataHash.get("adminUsername");
//        } else {
//            throw new MissingParameterException("admin username");
//        }
//        if (dataHash.containsKey("adminPassword")) {
//            adminPassword = dataHash.get("adminPassword");
//        } else {
//            throw new MissingParameterException("admin password");
//        }
//
//        User userObject = userService.authenticate(adminUsername, adminPassword);
//
//        if (userObject != null && userObject.getRole() == User.ADMIN) {
//            User user;
//            String username;
//            if (dataHash.containsKey("username")) {
//                username = (String) dataHash.get("username");
//                user = userService.findOne(username);
//                userService.deleteUser(username);
//            } else {
//                return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.MISSING_PARAMETERS.toString());
//            }
//            jSONResponse.setStatus(true);
//            jSONResponse.setCount(1);
//            jSONResponse.setMessage(Enums.JSONResponseMessage.SUCCESS.toString());
//            user.setPassword("xxx");
//            jSONResponse.setResult(user);
//            return jSONResponse;
//        }
//        return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.ACCESS_DENIED.toString());
//    }
    
//    @RequestMapping(value = "admin/api/rydeon/user/delete", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseBody
//    public JSONResponse deleteAllUsers(@RequestBody Object data, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        JSONResponse jSONResponse = new JSONResponse();
//        Map<String, String> dataHash = (Map<String, String>) data;
//        String adminUsername, adminPassword;
//
//        if (dataHash.containsKey("username")) {
//            adminUsername = dataHash.get("username");
//        } else {
//            throw new MissingParameterException("username");
//        }
//        if (dataHash.containsKey("password")) {
//            adminPassword = dataHash.get("password");
//        } else {
//            throw new MissingParameterException("password");
//        }
//
//        User userObject = userService.authenticate(adminUsername, adminPassword);
//
//        if (userObject != null && userObject.getRole() == User.ADMIN) {
//            long count = userService.count();
//            userService.deleteAllUsers();
//           
//            jSONResponse.setStatus(true);
//            jSONResponse.setCount(count);
//            jSONResponse.setMessage(Enums.JSONResponseMessage.SUCCESS.toString());
//            jSONResponse.setResult(null);
//            return jSONResponse;
//        }
//        return new JSONResponse(false, 0, null, Enums.JSONResponseMessage.ACCESS_DENIED.toString());
//    }

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
