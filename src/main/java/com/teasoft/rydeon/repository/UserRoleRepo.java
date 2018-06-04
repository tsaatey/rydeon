/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.repository;

import com.teasoft.rydeon.model.UserRole;
import com.teasoft.rydeon.model.Users;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Theodore Elikem Attigah
 */
public interface UserRoleRepo extends CrudRepository<UserRole, Long> {
    List<UserRole> findByUser(Users users);
}
