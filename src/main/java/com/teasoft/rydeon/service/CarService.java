/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.service;

import com.teasoft.rydeon.model.Car;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.repository.CarRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class CarService {
    @Autowired
    CarRepo carRepo;
    
    public Car save(Car car) {
        return carRepo.save(car);
    }
    
    public Iterable<Car> save(Iterable<Car> cars) {
        return carRepo.save(cars);
    }
    
    public void delete(Car car) {
        carRepo.delete(car);
    }
    
    public List<Car> findByOwner(Person person) {
        return carRepo.findByOwner(person);
    }
}
