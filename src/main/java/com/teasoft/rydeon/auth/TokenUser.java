package com.teasoft.rydeon.auth;

import com.teasoft.rydeon.model.Users;
import org.springframework.security.core.authority.AuthorityUtils;


public class TokenUser extends org.springframework.security.core.userdetails.User {
    private Users user;

    public TokenUser(Users user) {
        super(user.getPerson().getEmail(), user.getPassword(), user.getIsActive(), true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
        this.user = user;
    }

    public Users getUser() {
        return user;
    }

    public String getId() {
        return user.getPerson().getEmail();
    }

    public String getRole() {
        return "ROLE_USER";
    }
}
