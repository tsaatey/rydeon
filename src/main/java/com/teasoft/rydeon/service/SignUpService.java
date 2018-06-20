/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.teasoft.auth.model.Role;
import com.teasoft.auth.model.UserRole;
import com.teasoft.auth.model.Users;
import com.teasoft.auth.repo.RoleRepo;
import com.teasoft.auth.repo.UserRoleRepo;
import com.teasoft.auth.repo.UsersRepo;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.repository.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class SignUpService {

    @Autowired
    UsersRepo userRepo;
    @Autowired
    PersonRepo personRepo;
    @Autowired
    UserRoleRepo userRoleRepo;
    @Autowired
    RoleRepo roleRepo;


    public UserRole saveUserAndRole(String phoneNumber) {
        Users user = new Users();
        user.setEnabled(Boolean.TRUE);
        user.setAccountNonExpired(Boolean.TRUE);
        user.setAccountNonLocked(Boolean.TRUE);
        user.setCredentialNonExpired(Boolean.TRUE);
        user.setPhone(phoneNumber);
        user = userRepo.save(user);
        UserRole userRole = new UserRole();
        Role role = roleRepo.findByRoleName("USER");
        if (userRoleRepo.findByUserAndRole(user, role) == null) {
            userRole.setUser(user);
            userRole.setRole(role);
            return userRoleRepo.save(userRole);
        }
        return userRole;
    }

}
