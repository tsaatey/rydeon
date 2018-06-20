/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.service;

import com.teasoft.auth.model.Users;
import com.teasoft.auth.repo.UsersRepo;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class PersonService {
    @Autowired
    PersonRepo personRepo;
    @Autowired
    UsersRepo userRepo;
    
    public Person save(Person person) {
        return personRepo.save(person);
    }
    
    public Person findByPhone(String phone) {
        return personRepo.findByPhone(phone);
    }
    
    public Long count() {
        return personRepo.count();
    }
    
    public Person findByUser(Users user) {
        return personRepo.findByUser(user);
    }
    
    @Transactional
    public Person savePerson(Person person, Users user) {
        user = userRepo.save(user);
        person.setUser(user);
        return personRepo.save(person);
    }
    
}
