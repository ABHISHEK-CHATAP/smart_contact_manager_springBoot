package com.scm.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.repositories.UserRepository;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    //verify email
    @GetMapping("/verify-email")
    public String verifyEmail(
        @RequestParam("token") String token
    ){

        User user = userRepository.findByEmailToken(token);

        if(user != null){
            //means user fetch hua hai :: process krna hai
            if(user.getEmailToken().equals(token)){
                user.setEmailVerified(true);
                user.setEnabled(true);
                userRepository.save(user);
                return "success_page";
            }
            return "error_page";
        }
        return "error_page";
    }


}
