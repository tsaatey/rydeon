/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.auth.model.Users;
import com.teasoft.auth.sec.PasswordHash;
import com.teasoft.auth.service.UsersService;
import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.service.PersonService;
import com.teasoft.rydeon.util.Enums;
import com.teasoft.rydeon.util.ImageService;
import com.teasoft.rydeon.util.JSONResponse;
import io.jsonwebtoken.ExpiredJwtException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Theodore Elikem Attigah
 */
@RestController
public class PersonController {

    final int MAX_IMAGE_DIM_DIFF = 70;
    final static int IMG_WIDTH = 250;
    final static int IMG_HEIGHT = 250;

    @Autowired
    PersonService personService;
    @Autowired
    UsersService userService;
    @Autowired
    ImageService imageService;

    /**
     * Uploads image for a person. The image is uploaded for the current user so
     * does not require any person id
     *
     * @param response
     * @param request
     * @param file the image to upload
     * @return the updated person object
     * @throws Exception
     */
    @RequestMapping(value = "api/rydeon/image/upload", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse uploadImage(HttpServletResponse response, HttpServletRequest request,
            @RequestParam(value = "image", required = true) MultipartFile file) throws Exception {

        Users currentUser = userService.getCurrentUsers();
        Person person = personService.findByUser(currentUser);
        if (!file.isEmpty()) {
            BufferedImage image = ImageIO.read(file.getInputStream());
            Integer width = image.getWidth();
            Integer height = image.getHeight();

            if (Math.abs(width - height) > MAX_IMAGE_DIM_DIFF) {
                return new JSONResponse(false, 0, null, "Invalid Image dimensions");
            } else {
                //Resize image
                BufferedImage originalImage = ImageIO.read(file.getInputStream());
                int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

                BufferedImage resizeImagePng = imageService.resizeImage(originalImage, IMG_WIDTH, IMG_HEIGHT, type);

                String rootPath = request.getSession().getServletContext().getRealPath("/");
                File dir = new File(rootPath + File.separator + "image");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File serverFile = new File(dir.getAbsolutePath() + File.separator + file.getOriginalFilename());

                switch (file.getContentType()) {
                    case "image/png":
                        ImageIO.write(resizeImagePng, "png", serverFile);
                        break;
                    case "image/jpeg":
                        ImageIO.write(resizeImagePng, "jpg", serverFile);
                        break;
                    default:
                        ImageIO.write(resizeImagePng, "png", serverFile);
                        break;
                }

                BufferedImage resizedImage = ImageIO.read(serverFile);

                byte[] imageInByte;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    switch (file.getContentType()) {
                        case "image/png":
                            ImageIO.write(resizedImage, "png", baos);
                            break;
                        case "image/jpeg":
                            ImageIO.write(resizedImage, "jpg", baos);
                            break;
                        default:
                            ImageIO.write(resizedImage, "png", baos);
                            break;
                    }

                    baos.flush();
                    imageInByte = baos.toByteArray();
                }
                person.setImage(imageInByte);
                serverFile.delete();
            }
        }
        return new JSONResponse(true, 1, personService.save(person), Enums.JSONResponseMessage.SUCCESS.toString());
    }

    /**
     * Queries the details of the current user.
     *
     * @return details of the current user.
     */
    @RequestMapping(value = "api/rydeon/person/details", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPersonDetails() {
        Users user = userService.getCurrentUsers();
        Person person = personService.findByUser(user);
        return new JSONResponse(true, 1, person, Enums.JSONResponseMessage.SUCCESS.toString());
    }

    @RequestMapping(value = "admin/rydeon/person", method = RequestMethod.PUT)
    @ResponseBody
    public JSONResponse updatePersonDetails(@RequestBody Object data) throws Exception {
        Map<String, Object> dataHash = (HashMap<String, Object>) data;
        Person person;
        Users users;
        String password, confirmPassword;
        if (dataHash.containsKey("userId")) {
            person = personService.findOne(((Integer) dataHash.get("userId")).longValue());
            users = person.getUser();
        } else {
            return new JSONResponse(false, 0, "userId", "userId is required");
        }

        if (dataHash.containsKey("firstname")) {
            person.setFirstname((String) dataHash.get("firstname"));
        }

        if (dataHash.containsKey("lastname")) {
            person.setLastname((String) dataHash.get("lastname"));
        }

        if (dataHash.containsKey("gender")) {
            person.setGender((String) dataHash.get("gender"));
        }
        
        if (dataHash.containsKey("othernames")) {
            person.setOthernames((String) dataHash.get("othernames"));
        }

        if (dataHash.containsKey("email")) {
            person.setEmail((String) dataHash.get("email"));
            users.setEmail((String) dataHash.get("email"));
        }

        if (dataHash.containsKey("phoneNumber")) {
            person.setPhone((String) dataHash.get("phoneNumber"));
            users.setPhone((String) dataHash.get("phoneNumber"));
        }
        
        
        if (dataHash.containsKey("deviceToken")) {
            person.setDeviceToken((String) dataHash.get("deviceToken"));
        }
        
        if (dataHash.containsKey("digitalAddress")) {
            person.setDigitalAddress((String) dataHash.get("digitalAddress"));
        }
        
        if(dataHash.containsKey("verified")) {
            person.setVerified((Boolean) dataHash.get("verified"));
        }

        if (dataHash.containsKey("password")) {
            password = (String) dataHash.get("password");
            if (dataHash.containsKey("confirmPassword")) {
                confirmPassword = (String) dataHash.get("confirmPassword");
            } else {
                return new JSONResponse(false, 0, "Confirm Password", "Confirm Password is required");
            }
            if (!password.equals(confirmPassword)) {
                return new JSONResponse(false, 0, "Password Mismatch", "Passwords do not match");
            }
            users.setPassword(PasswordHash.createHash((String) dataHash.get("password")));
        }

        if(dataHash.containsKey("enabled")) {
            users.setEnabled((Boolean) dataHash.get("enabled"));
        }
        
        if(dataHash.containsKey("accountNonExpired")) {
            users.setAccountNonExpired((Boolean) dataHash.get("accountNonExpired"));
        }
        
        if(dataHash.containsKey("accountNonLocked")) {
            users.setAccountNonLocked((Boolean) dataHash.get("accountNonLocked"));
        }

        //Persist user and use the persisted user to persist person
        users = userService.save(users);
        person.setUser(users);
        person = personService.save(person);
        return new JSONResponse(true, 1, person, "SUCCESS");
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
