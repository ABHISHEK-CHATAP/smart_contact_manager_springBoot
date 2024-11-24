package com.scm.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scm.entities.Contact;
import com.scm.entities.User;

public interface ContactService {

    //Save contats
    Contact saveContact(Contact contact);

    //update contact
    Contact updateContact(Contact contact);

    //get contacts
    List<Contact> getAll();

    //get Contact by id
    Contact getContactById(String id);

    //delete contact
    void delete(String id);


    // get Contact by userId
    List<Contact> getByUserId(String userId);

    Page<Contact> getByUser(User user, int page, int size, String sortField, String sortDirection);

    // Searching contacts methods
    //search by name
    Page<Contact> searchByName(String name, int page, int size, String sortBy, String order);

    //search by phone
    Page<Contact> searchByPhoneNumber( String phoneNumber, int page, int size, String sortBy, String order);

    //search by email
    Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String order);


}
