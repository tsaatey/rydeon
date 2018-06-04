/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.repository;

import com.teasoft.rydeon.model.Place;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Theodore Elikem Attigah
 */
public interface PlaceRepo extends CrudRepository<Place, Long> {
    @Override
    List<Place> findAll();
    
    
    @Query("Select u from Place u where u.placeName like %?1 order by u.placeName asc")
    List<Place> findPlaces(String placeName);
}
