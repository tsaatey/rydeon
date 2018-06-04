package com.teasoft.rydeon.auth;


import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.model.Users;
import com.teasoft.rydeon.repository.PersonRepo;
import com.teasoft.rydeon.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UsersRepo userRepo;
    @Autowired PersonRepo personRepo;

    private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public final TokenUser loadUserByUsername(String username) throws UsernameNotFoundException {
        //find person and use person to find user
        Person person = personRepo.findByEmailOrPhone(username, username);
        final Users user = userRepo.findByPerson(person);
        if(user == null) {
            throw new UsernameNotFoundException("");
        }
        TokenUser currentUser = new TokenUser(user);
        detailsChecker.check(currentUser);
        return currentUser;
    }
    
}