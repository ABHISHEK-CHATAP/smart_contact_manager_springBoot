package com.scm.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.services.impl.SecurityCustomUserDetailsService;


@Configuration
public class SecurityConfig {

    // user create and login using java code with in memory service

    // @Bean
    // public UserDetailsService userDetailsService() {

        // UserDetails user1 = User
        // .withUsername("admin123")
        // .password("admin123")
        // .roles("ADMIN")
        // .build(); 

        // var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1);
        // return inMemoryUserDetailsManager;
    // }
    // -------------------------------------- XXXX ----------------------------------------------------------------
    // ----> above code inMemoryUserDetailsManager is used to login with default user only ,,..,, now from below we are logging only from database stored user

    @Autowired
    private SecurityCustomUserDetailsService securityCustomUserDetailsService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    // configuration of authentication provider for spring security
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //user detail service ka object:
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailsService);
        // password encoder ka object:
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //url configurations which one is public and rest private
        httpSecurity.authorizeHttpRequests(authorize-> {
            // authorize.requestMatchers("/home", "/services").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();
        });

        // form default login page by spring security
        // agar hame kuch bhi change krna hua toh yaha aayenge : form login se related
        // httpSecurity.formLogin(Customizer.withDefaults()); // from these line only can access spring security by-default username and password inputs
        // but we want or custom so above line commented , below is same working flow with custom loginPage

        httpSecurity.formLogin(formLogin -> {

            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            // formLogin.successForwardUrl("/user/dashboard");
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.defaultSuccessUrl("/user/profile",true);
            formLogin.failureUrl("/login?error=true");
            // In above code [ successForwordUrl and failureForwardUrl ] is not working so used [ defaultSuccessUrl and failureUrl ]
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");

            formLogin.failureHandler(authFailureHandler);

            // formLogin.successHandler(new AuthenticationSuccessHandler(){

            //     @Override
            //     public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            //             Authentication authentication) throws IOException, ServletException {
            //         // TODO Auto-generated method stub
            //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
            //     }
                
            // });


            // write now I don't want failureHandler and successHandler so commented 




        });

        // toh logout krne ke liye hume CSRF diabled krna padega kyuki by-default CSRF enabled rehta hai
        // and CSRF enabled hai toh logout ki POST request send krni padegi,,, wahi fir form method post and logouturl "/do-logout"
        // toh iise banche ke liye direct CSRF disabled karenge in below code

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        // abhi csrf ki GET request jayegi and logout
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/do-logout")
            .logoutSuccessUrl("/login?logout=true");
        });

        //oauth2 configurations
        httpSecurity.oauth2Login(outh -> {
            outh.loginPage("/login");
            // OAuthAuthenticationSuccessHandler iss class me likha hai logic successHandler ka
            outh.successHandler(handler);
        });


        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




}
