/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.repository;

import com.teasoft.rydeon.model.Journey;
import com.teasoft.rydeon.model.JourneyRider;
import com.teasoft.rydeon.model.Person;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Theodore Elikem Attigah
 */
public interface JourneyRiderRepo extends CrudRepository<JourneyRider, Long>{
    Integer countByJourney(Journey journey);
    List<JourneyRider> findByPerson(Person person);
    List<JourneyRider> findByJourney(Journey journey);
}
