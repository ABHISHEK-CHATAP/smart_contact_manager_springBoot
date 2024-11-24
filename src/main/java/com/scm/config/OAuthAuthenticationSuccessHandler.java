package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.exceptionHelpers.AppConstants;
import com.scm.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    // ctrl + dot [.] se add unimplemented methods pr click

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
      
                logger.info("OAuthenticationSuccessHandler");
                // after login before redirect to protected page, 

               DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();

            //    logger.info(user.getName());

            //    user.getAttributes().forEach((key, value) ->{
            //     logger.info("{} => {}", key, value);
            //    });

            //    logger.info(user.getAuthorities().toString());

                //save data into database
                String email = user.getAttribute("email").toString();
                String name = user.getAttribute("name").toString();
                String picture = user.getAttribute("picture").toString();

                //create user and save in databse
                User user1 = new User();
                user1.setName(name); 
                user1.setEmail(email);
                user1.setProfilePicLink(picture);
                user1.setPassword("password"); 
                user1.setUserId(UUID.randomUUID().toString());
                user1.setProvider(Providers.GOOGLE);
                user1.setEnabled(true); 
                user1.setEmailVerified(true); 
                user1.setProviderUserId(user1.getName());
                user1.setRoleList(List.of(AppConstants.ROLE_USER));
                user1.setAbout("This account is created using Google ...");

                User isUserPresent = userRepository.findByEmail(email).orElse(null);

                if(isUserPresent == null) {
                    userRepository.save(user1);
                    logger.info("User saved :-" , email);
                }

                new DefaultRedirectStrategy().sendRedirect(request, response,"/user/profile");
    }

}
