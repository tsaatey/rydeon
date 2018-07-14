/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.repository;

import com.teasoft.rydeon.model.Make;
import com.teasoft.rydeon.model.Model;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Theodore Elikem Attigah
 */
public interface ModelRepo extends CrudRepository<Model, Long>{
    @Override
    List<Model> findAll();
    
    List<Model> findByMake(Make make);
}
