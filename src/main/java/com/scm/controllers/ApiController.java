package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.entities.Contact;
import com.scm.services.ContactService;

//jb bhi hum kisi cheez koRestController se annotate krte hai iska mtlb
// JSON ko data return kr rahe hai .. 


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    // get contact
    @GetMapping("/contact/{contactId}")
    public Contact getContact(@PathVariable String contactId) {
        return contactService.getContactById(contactId);
    }

     // get contact
     @DeleteMapping("/contact/delete/{contactId}")
     public void deleteContact(@PathVariable String contactId) {
          contactService.delete(contactId);
     }

}
