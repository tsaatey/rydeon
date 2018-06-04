/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.repository;

import com.teasoft.rydeon.model.Journey;
import com.teasoft.rydeon.model.Person;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Theodore Elikem Attigah
 */
public interface JourneyRepo extends CrudRepository<Journey, Long> {
    @Override
    List<Journey> findAll();
    List<Journey> findBySourceAndDestinationAndStatusOrderByJourneyDateDesc(String source, String destination, String status);
    List<Journey> findByPersonAndStatusOrderByJourneyDateDesc(Person person, String status);
    List<Journey> findBySourceAndStatusOrderByJourneyDateDesc(String source, String status);
    List<Journey> findByDestinationAndStatus(String destination, String status);
    
    @Query(value = "SELECT p FROM Journey p WHERE p.source LIKE %:searchTerm1% or p.destination LIKE %:searchTerm2%")
    List<Journey> search(@Param("searchTerm1") String searchTerm1, @Param("searchTerm2") String searchTerm2);
    
    @Query(value = "SELECT p FROM Journey p WHERE p.source LIKE %:searchTerm1%")
    List<Journey> searchSource(@Param("searchTerm1") String searchTerm1);
    
    @Query(value = "SELECT p FROM Journey p WHERE p.destination LIKE %:searchTerm2%")
    List<Journey> searchDestination(@Param("searchTerm2") String searchTerm2);
    
}
