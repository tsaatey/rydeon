/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.repository;

import com.teasoft.rydeon.model.Car;
import com.teasoft.rydeon.model.Person;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Theodore Elikem Attigah
 */
public interface CarRepo extends CrudRepository<Car, Long> {
    List<Car> findByOwner(Person owner);
    @Override
    List<Car> findAll();
}
