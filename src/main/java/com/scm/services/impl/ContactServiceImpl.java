package com.scm.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.exceptionHelpers.ResourceNotFoundException;
import com.scm.repositories.ContactRepository;
import com.scm.services.ContactService;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact saveContact(Contact contact) {

        // firstly we need COntact Id se creating using UUID
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);

        return contactRepository.save(contact);
       
    }

    @Override
    public Contact updateContact(Contact contact) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateContact'");
    }

    @Override
    public List<Contact> getAll() {
       return contactRepository.findAll();
    }

    @Override
    public Contact getContactById(String id) {
       return contactRepository.findById(id)
       .orElseThrow(()-> new ResourceNotFoundException("contact not found with given id "+ id) );
    }

    @Override
    public void delete(String id) {
        var contact = contactRepository.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("contact not found with given id "+ id) );

        contactRepository.delete(contact);
    }


    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepository.findByUserId(userId);
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);
       return contactRepository.findByUser(user, pageable);
    }


    // searching on contacts methods

    @Override
    public Page<Contact> searchByName(String name, int page, int size, String sortBy, String order) {

        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
       return contactRepository.findByNameContaining(name, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumber, int page, int size, String sortBy, String order) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
       return contactRepository.findByPhoneNumberContaining(phoneNumber, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String email, int page, int size, String sortBy, String order) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
       return contactRepository.findByEmailContaining(email, pageable);
    }


    

}
