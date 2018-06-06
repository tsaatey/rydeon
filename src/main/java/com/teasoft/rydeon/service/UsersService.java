/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teasoft.rydeon.service;

import com.teasoft.rydeon.auth.TokenUser;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.UserRole;
import com.teasoft.rydeon.model.Users;
import com.teasoft.rydeon.repository.UserRoleRepo;
import com.teasoft.rydeon.repository.UsersRepo;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class UsersService {

    @Autowired
    UsersRepo usersRepo;
    @Autowired
    UserRoleRepo userRoleRepo;

    public String userRoles(Users user) {
        String roles = "";
        List<UserRole> userRoles = userRoleRepo.findByUser(user);
        for(UserRole userRole: userRoles) {
            roles += userRole.getRole().getRoleName()+",";
        }
        return roles;
    }
    
    public Users getCurrentUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((TokenUser) authentication.getPrincipal()).getUser();
    }
    
    public Users findByPerson(Person person) {
        return usersRepo.findByPerson(person);
    }

    /**
     * Returns a user object if authentication succeeds otherwise null
     *
     * @param username
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
//    public Users authenticate(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        Users user = findOne(username);
//        if (user == null) {
//            return null;
//        }
//        if (!user.getRole().equals(Users.ADMIN)) {
//            return null;
//        }
//        String passwordHash = user.getPassword();
//        boolean status = PasswordHash.validatePassword(password, passwordHash);
//        if (status) {
//            return user;
//        }
//        return null;
//    }

//    public Users authenticateUsers(String username, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        Users user = findOne(username);
//        if (user == null) {
//            return null;
//        }
//        if (!user.getRole().equals(Users.USER)) {
//            return null;
//        }
//        String passwordHash = user.getPassword();
//        boolean status = PasswordHash.validatePassword(password, passwordHash);
//        if (status) {
//            return user;
//        }
//        return null;
//    }

    public Iterable<Users> findAll() {
        return usersRepo.findAll();
    }

    public Users findOne(Long id) {
        return usersRepo.findOne(id);
    }

    public Users saveUsers(Users user) {
        return usersRepo.save(user);
    }

    public Iterable<Users> saveUserss(Iterable<Users> itrbl) {
        return usersRepo.save(itrbl);
    }

    public void deleteUsers(Users user) {
        usersRepo.delete(user);
    }

    public void deleteUsers(Long id) {
        usersRepo.delete(id);
    }

    public void deleteAllUserss() {
        usersRepo.deleteAll();
    }

    public void deleteListOfUserss(Iterable<Users> users) {
        usersRepo.delete(users);
    }

    public long count() {
        return usersRepo.count();
    }

//    public Iterable<Users> search(String searchTerm) {
//        return usersRepo.search(searchTerm);
//    }
//
//    public Long countSearch(String searchTerm) {
//        return usersRepo.countSearch(searchTerm);
//    }
}
