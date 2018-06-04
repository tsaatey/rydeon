/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.repository;

import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.Users;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Theodore Elikem Attigah
 */
public interface UsersRepo extends CrudRepository<Users, Long> {
    Users findByPerson(Person person);
}
