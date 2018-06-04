/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.service;

import com.teasoft.rydeon.model.Journey;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.repository.JourneyRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class JourneyService {
    @Autowired
    JourneyRepo journeyRepo;
    
    public Journey save(Journey journey) {
        return journeyRepo.save(journey);
    }
    
    public Iterable<Journey> save(Iterable<Journey> journey) {
        return journeyRepo.save(journey);
    }
    
    public void delete(Journey journey) {
        journeyRepo.delete(journey);
    }
    
    public void delete(Iterable<Journey> journey) {
        journeyRepo.delete(journey);
    }
    
    public List<Journey> findAll() {
        return journeyRepo.findAll();
    }
    
    public List<Journey> findByPersonAndStatus(Person person, String status) {
        return journeyRepo.findByPersonAndStatusOrderByJourneyDateDesc(person, status);
    }
    
    public List<Journey> findBySourceAndDestinationAndStatus(String source, String Destination, String status) {
        return journeyRepo.findBySourceAndDestinationAndStatusOrderByJourneyDateDesc(source, Destination, status);
    }
    
    public List<Journey> findBySourceAndStatus(String source, String status) {
        return journeyRepo.findBySourceAndStatusOrderByJourneyDateDesc(source, status);
    }
    
    public List<Journey> findByDestinationAndStatus(String destination, String status) {
        return journeyRepo.findByDestinationAndStatus(destination, status);
    }
    
    
}
