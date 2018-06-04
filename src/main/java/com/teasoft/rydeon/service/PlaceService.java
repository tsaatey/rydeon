/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.service;

import com.teasoft.rydeon.model.Place;
import com.teasoft.rydeon.repository.PlaceRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class PlaceService {
    @Autowired
    PlaceRepo placeRepo;
    
    public List<Place> findAll() {
        return placeRepo.findAll();
    }
    
    public List<Place> search(String place) {
        return placeRepo.findPlaces(place);
    }
    
    public Place save(Place place) {
        return placeRepo.save(place);
    }
    
    public Iterable<Place> save(Iterable<Place> places) {
        return placeRepo.save(places);
    }
    
    public void delete(Place place) {
        placeRepo.delete(place);
    }
    
    public void delete(Long place) {
        placeRepo.delete(place);
    }
    
    public void delete(Iterable<Place> places) {
        placeRepo.delete(places);
    }
}
