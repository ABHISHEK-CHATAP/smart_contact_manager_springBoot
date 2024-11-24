package com.scm.exceptionHelpers;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {


    public static String getEmailOfLoggedInUser(Authentication authentication) {


        //agar emailId and password se login kia hai toh : email kaise nikalenge


        if(authentication instanceof OAuth2AuthenticationToken){

            var aOAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
            var clientId = aOAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();
            String UserName = "";

            if(clientId.equalsIgnoreCase("google")){
                // agar signIn with Google kia hai toh
                System.out.println("Getting email from google ...");
                UserName = oauth2User.getAttribute("email").toString();

            }
            else if(clientId.equalsIgnoreCase("github")){
                // lly, GitHub se kia hai toh
                System.out.println("Getting email from GItHub ...");
                UserName = oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString() : oauth2User.getAttribute("login").toString() + "@gmail.com";

            }

            return UserName;

        }else{
            //means user login with emailId and password
            System.out.println("Getting email from local database ...");
            return authentication.getName();
        }


    }


    public static String getLinkForEmailVerification(String emailToken){

        String link = "http://localhost:8081/auth/verify-email?token=" + emailToken;

        return link;
    }



}
