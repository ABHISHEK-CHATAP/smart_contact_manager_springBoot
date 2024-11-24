package com.scm.controllers;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.exceptionHelpers.AppConstants;
import com.scm.exceptionHelpers.Helper;
import com.scm.exceptionHelpers.Message;
import com.scm.exceptionHelpers.MessageType;
import com.scm.forms.ContactForm;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;




@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    // add contact page/view
    @GetMapping("/add")
    public String addContactView(Model model){
        ContactForm contactForm = new ContactForm();
        // contactForm.setFavourite(true);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @PostMapping("/add")
    public String SaveContactFrom(@Valid  @ModelAttribute ContactForm contactFormData, BindingResult result, Authentication authentication , HttpSession httpSession){
        if (result.hasErrors()) {
            httpSession.setAttribute("message", Message.builder()
            .content("please correct the following errors")
            .type(MessageType.red)
            .build());
            return "user/add_contact";
        }

        //process the form data
        System.out.println("from of contact - " + contactFormData);

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        // System.out.println("contact form image - " + contactFormData.getPicture().getOriginalFilename());
        //image process

        //upload krne ka code
         String fileNameId = UUID.randomUUID().toString();
        String fileURL = imageService.uploadImage(contactFormData.getPicture(), fileNameId);


        // form data --> Contact (entity)
        Contact contact = new Contact();

        contact.setName(contactFormData.getName());
        contact.setEmail(contactFormData.getEmail());
        contact.setPhoneNumber(contactFormData.getPhoneNumber());
        //yaha pe faurite boolean type hai.., to getFavourite() nhi isFavourite() aayega;
        contact.setFavourite(contactFormData.isFavourite());
        contact.setAddress(contactFormData.getAddress());
        contact.setDescription(contactFormData.getDescription());
        contact.setLinkedInLink(contactFormData.getLinkedInLink());
        contact.setWebsiteLink(contactFormData.getWebsiteLink());
        contact.setPicture(fileURL);
        contact.setCloudinaryPicturePublicId(fileNameId);
        // to get current user => we need Authentication from spring security
        contact.setUser(user);


        contactService.saveContact(contact);
        System.out.println("contact saves in database - " + contact);

        httpSession.setAttribute("message", Message.builder()
        .content("You have successfully Added a new contact")
        .type(MessageType.green)
        .build());

        return "redirect:/user/contacts/add";
    }

    //view all contacts
    @GetMapping
    public String viewContacts(
        @RequestParam(value = "page", defaultValue="0") int page,
        @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,    
        Model model ,Authentication authentication){

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);


        return "user/contacts";
    }

    //search Handler

    @GetMapping("/search")
    public String searchHandler(
        @RequestParam("field") String field,
        @RequestParam("keyword") String keyword,
        @RequestParam(value = "page", defaultValue="5") int page,
        @RequestParam(value = "size" , defaultValue =  AppConstants.PAGE_SIZE + "") int size,
        @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
        @RequestParam(value = "direction", defaultValue = "asc") String direction,    
        Model model
    ){
        // System.out.println(field);
        // System.out.println(keyword);

        Page<Contact> pageContact = null ;

        if(field.equalsIgnoreCase("name")){
            pageContact = contactService.searchByName(keyword, page, size, sortBy, direction);
        }else if(field.equalsIgnoreCase("email")){
            pageContact = contactService.searchByEmail(keyword, page, size, sortBy, direction);
        }else if (field.equalsIgnoreCase("phoneNumber")) {
            pageContact = contactService.searchByPhoneNumber(keyword, page, size, sortBy, direction);
        }

        model.addAttribute("pageContact", pageContact);

        return "user/search";
    }

}
