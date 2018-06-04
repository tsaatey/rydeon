/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.auth;


import com.teasoft.rydeon.util.PasswordHash;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Theodore Elikem Attigah
 */
public class MyPasswordEncoder implements PasswordEncoder{

    @Override
    public String encode(CharSequence cs) {
        try {
            return PasswordHash.createHash(cs.toString());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(MyPasswordEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    @Override
    public boolean matches(CharSequence cs, String string) {
        try {
            return PasswordHash.validatePassword(cs.toString(), string);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(MyPasswordEncoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
