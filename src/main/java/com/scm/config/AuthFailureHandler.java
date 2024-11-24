package com.scm.config;

import java.io.IOException;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.scm.exceptionHelpers.Message;
import com.scm.exceptionHelpers.MessageType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

                if(exception instanceof DisabledException){
                    // means user is disabled - enabled email is false

                    HttpSession session = request.getSession();
                    session.setAttribute("message", 
                        Message.builder().content("user is disabled, verification link sent on your Email Id").type(MessageType.red).build());

                        response.sendRedirect("/login");
                }else{
                    response.sendRedirect("/login?error=true");
                }

    }

}
