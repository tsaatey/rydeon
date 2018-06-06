/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.teasoft.rydeon.service;

import com.teasoft.rydeon.model.OTP;
import com.teasoft.rydeon.model.Person;
import com.teasoft.rydeon.repository.OtpRepo;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Theodore Elikem Attigah
 */
@Service
public class OTPService {
    @Autowired
    OtpRepo otpRepo;
    
    public String generatePassword() {
        String chars = "0123456789";
        StringBuilder salt = new StringBuilder();
        Random rand = new Random();
        while(salt.length() < 6) {
            int index = (int) (rand.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        String otp = salt.toString();
        return otp;
    }
    
    public OTP save(OTP otp) {
        return otpRepo.save(otp);
    }
    
    public OTP findByPhoneAndOtp(String phone, String otp) {
        return otpRepo.findByPhoneAndOtp(phone, otp);
    }

}
