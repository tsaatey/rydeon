/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.service;

import com.teasoft.rydeon.model.Region;
import com.teasoft.rydeon.repository.RegionRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class RegionService {
    @Autowired
    RegionRepo regionRepo;
    
    public List<Region> findAll() {
        return regionRepo.findAll();
    }
    
    public Region save(Region region) {
        return regionRepo.save(region);
    }
    
    public Region findOne(Long id) {
        return regionRepo.findOne(id);
    }
    
}
