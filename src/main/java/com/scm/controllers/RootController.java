package com.scm.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.exceptionHelpers.Helper;
import com.scm.services.UserService;

// kisi class ko ControllerAdvice annotate kia toh, is class ke under jo methods honge woh har ek request ke liye execute honge ... 
//means hr ek page pe execute honge, 
// to prevent that , and we want only authenticated pages below model response ..
// so add add condition authentication == null ? then simply return ho jaoo.

@ControllerAdvice
public class RootController {

    @Autowired
    private UserService userService;

     @ModelAttribute
    public void addLoggedInUserInformation(Authentication authentication, Model model){


        if(authentication == null){
            // agar user authenticated nhi hai to below code model attribute response will not run
            return;
        }


        System.out.println("Adding loggedIn user information to the model...");
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        System.out.println(user.getName());
        System.out.println(user.getEmail());
        model.addAttribute("loggedInUser", user);

    }

}
