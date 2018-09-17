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
import com.teasoft.rydeon.util.Enums;
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
        return jrRepo.findByPersonOrderByDateTimeCreatedDesc(person);
    }
    
    public List<JourneyRider> findByJourney(Journey journey) {
        return jrRepo.findByJourney(journey);
    }
    
    public JourneyRider save(JourneyRider journeyRider) {
        return jrRepo.save(journeyRider);
    }
    
    public Integer countByJourneyAndStatus(Journey journey, String status) {
        return jrRepo.countByJourneyAndStatus(journey, status);
    }
    
    public Boolean isJourneyFull(Journey journey) {
        return journey.getMaxRiders() >= jrRepo.countByJourneyAndStatus(journey, Enums.RideRequestStatus.APPROVED.toString());
    }
    
    public JourneyRider findByJournerAndPerson(Journey journey, Person person) {
        return jrRepo.findByJourneyAndPerson(journey, person);
    }
}
