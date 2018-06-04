/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.service;

import com.teasoft.rydeon.model.Journey;
import com.teasoft.rydeon.model.JourneyRider;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.repository.JourneyRiderRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class JourneyRiderService {
    @Autowired
    JourneyRiderRepo jrRepo;
    
    public List<JourneyRider> findByPerson(Person person) {
        return jrRepo.findByPerson(person);
    }
    
    public List<JourneyRider> findByJourney(Journey journey) {
        return jrRepo.findByJourney(journey);
    }
    
    public JourneyRider save(JourneyRider journeyRider) {
        return jrRepo.save(journeyRider);
    }
}
