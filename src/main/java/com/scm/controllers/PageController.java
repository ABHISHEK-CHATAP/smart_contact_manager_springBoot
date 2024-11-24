package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.exceptionHelpers.Message;
import com.scm.exceptionHelpers.MessageType;
import com.scm.forms.UserForm;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index(){
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model){
        System.out.println("Home page handlers");

        // sending data to home.html page 
        model.addAttribute("name", "Welcome to Home Page - ABHISHEK");
        model.addAttribute("GitHubRepo", "https://github.com/ABHISHEK-CHATAP");

        return "home";
    }

    //about route
    @RequestMapping("/about")
    public String aboutPage(){
        System.out.println("About page loading ...");
        return "about";
    }

    // services route
    @RequestMapping("/services")
    public String servicesPage(){
        System.out.println("services page loading ...");
        return "services";
    }

     // contact us route
     @RequestMapping("/contact")
     public String contactPage(){
         System.out.println("contact page loading ...");
         return "contact";
     }

      // login route
      @GetMapping("/login")
      public String loginPage(){
          System.out.println("login page loading ...");
          return "login";
      }

       // signup route
       @GetMapping("/signup")
       public String signUpPage(Model model){
        UserForm userForm = new UserForm();
        // userForm.setName("Abhishek");
        model.addAttribute("userForm", userForm);
        //    System.out.println("signup page loading ...");
           return "register";
       }

       //processing register
       @PostMapping("/do-register")
       public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        System.out.println("processing register");
        System.out.println("signup userForm :-"+ userForm);

        //valid form data
        if(rBindingResult.hasErrors()){
            return "register";
        }

        // User user = User.builder()
        //         .name(userForm.getName())
        //         .email(userForm.getEmail())
        //         .password(userForm.getPassword())
        //         .about(userForm.getAbout())
        //         .phoneNumber(userForm.getPhoneNumber())
        //         .profilePicLink("https://static-00.iconduck.com/assets.00/profile-default-icon-2048x2045-u3j7s5nj.png")
        //         .build();

        // builder se nhi ho raha tha toh object banake send kr rahe

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setAbout(userForm.getAbout());
        user.setEnabled(false);
        user.setProfilePicLink("https://static-00.iconduck.com/assets.00/profile-default-icon-2048x2045-u3j7s5nj.png");


        User savedUser = userService.saveUser(user);
        System.out.println("signup saved user :-"+ savedUser);

        // message = "Registration Successful";
        //add the message from ExceptionHelpers
        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message", message);

        return "register";
       }

}
