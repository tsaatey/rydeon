/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.repository;

import com.teasoft.rydeon.model.RideRequest;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author TOSHIBA
 */
public interface RideRequestRepo extends CrudRepository<RideRequest, Long>{
    
}
