/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.controller;

import com.teasoft.auth.model.Users;
import com.teasoft.auth.service.UsersService;
import com.teasoft.rydeon.exception.MissingParameterException;
import com.teasoft.rydeon.model.Car;
import com.teasoft.rydeon.model.Make;
import com.teasoft.rydeon.model.Model;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.repository.CarRepo;
import com.teasoft.rydeon.repository.MakeRepo;
import com.teasoft.rydeon.repository.ModelRepo;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.service.CarService;
import com.teasoft.rydeon.service.PersonService;
import com.teasoft.rydeon.util.Enums;
import com.teasoft.rydeon.util.JSONResponse;
import io.jsonwebtoken.ExpiredJwtException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
public class CarController {

    @Autowired
    PersonRepo personRepo;
    @Autowired
    PersonService personService;
    @Autowired
    CarService carService;
    @Autowired
    MakeRepo makeRepo;
    @Autowired
    ModelRepo modelRepo;
    @Autowired
    UsersService userService;

    /**
     * Returns cars for a specified owner
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "api/rydeon/car", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getByOwner() throws Exception {
        Users user = userService.getCurrentUsers();
        Person person = personRepo.findByUser(user);
        if (person == null) {
            return new JSONResponse(false, 0, null, "UNKNOWN username");
        }
        List<Car> cars = carService.findByOwner(person);
        return new JSONResponse(true, cars.size(), cars, "SUCCESS");
    }

    /**
     * Saves a car object to the db
     * @param response the response object
     * @param request the request object
     * @param make the make of the car
     * @param model the model of the car
     * @param year the year the car was manufactured
     * @param regNumber the car's registration number
     * @param image the image of the car
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "api/rydeon/car", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveCar(HttpServletResponse response, HttpServletRequest request, 
            @RequestParam("make") Integer make, @RequestParam("model") Integer model,
            @RequestParam("year") Integer year, @RequestParam("regNumber") String regNumber,
            @RequestParam(value = "image", required = false) MultipartFile image) throws Exception {
        
        Users currentUser = userService.getCurrentUsers();
        Person person = personService.findByUser(currentUser);
        
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
        
        if (image != null && !image.isEmpty()) {
            BufferedImage img = ImageIO.read(image.getInputStream());
            Integer width = img.getWidth();
            Integer height = img.getHeight();
            //Math.abs(width - height) > MAX_IMAGE_DIM_DIFF
            if (false) {
                return new JSONResponse(false, 0, null, "Invalid Image dimensions");
            } else {
                //Resize img
                BufferedImage originalImage = ImageIO.read(image.getInputStream());
                int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

//                BufferedImage resizeImagePng = imageService.resizeImage(originalImage, IMG_WIDTH, IMG_HEIGHT, type);
                BufferedImage resizeImagePng = originalImage;

                String rootPath = request.getSession().getServletContext().getRealPath("/");
                File dir = new File(rootPath + File.separator + "image");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File serverFile = new File(dir.getAbsolutePath() + File.separator + image.getOriginalFilename());

                switch (image.getContentType()) {
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
                    switch (image.getContentType()) {
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
                car.setImage(imageInByte);
                serverFile.delete();
            }
        }
        
        car = carService.save(car);
        return new JSONResponse(true, 0, car, "SUCCESS");
    }

    @RequestMapping(value = "admin/rydeon/car", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse addCarForUser(HttpServletResponse response, HttpServletRequest request, 
            @RequestParam("make") Integer make, @RequestParam("model") Integer model,
            @RequestParam("year") Integer year, @RequestParam("regNumber") String regNumber,
            @RequestParam(value = "image", required = false) MultipartFile image, @RequestParam Integer userId) throws Exception {
        
        Users owner = userService.findOne(userId.longValue());
        Users addedBy = userService.getCurrentUsers();
        Person admin = personService.findByUser(addedBy);
        Person carOwner = personService.findByUser(owner);
        
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
        car.setOwner(carOwner);
        car.setAddedBy(admin);
        
        if (image != null && !image.isEmpty()) {
            BufferedImage img = ImageIO.read(image.getInputStream());
            Integer width = img.getWidth();
            Integer height = img.getHeight();
            //Math.abs(width - height) > MAX_IMAGE_DIM_DIFF
            if (false) {
                return new JSONResponse(false, 0, null, "Invalid Image dimensions");
            } else {
                //Resize img
                BufferedImage originalImage = ImageIO.read(image.getInputStream());
                int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

//                BufferedImage resizeImagePng = imageService.resizeImage(originalImage, IMG_WIDTH, IMG_HEIGHT, type);
                BufferedImage resizeImagePng = originalImage;

                String rootPath = request.getSession().getServletContext().getRealPath("/");
                File dir = new File(rootPath + File.separator + "image");
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                File serverFile = new File(dir.getAbsolutePath() + File.separator + image.getOriginalFilename());

                switch (image.getContentType()) {
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
                    switch (image.getContentType()) {
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
                car.setImage(imageInByte);
                serverFile.delete();
            }
        }
        
        car = carService.save(car);
        return new JSONResponse(true, 0, car, "SUCCESS");
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
