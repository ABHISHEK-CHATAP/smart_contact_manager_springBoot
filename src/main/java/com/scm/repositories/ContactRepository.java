package com.scm.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entities.Contact;
import com.scm.entities.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact,String> {
    // Contact entity me id String type ki hai toh, JpaRepository me 2nd parameter String jayegi, 
    // agar contact entity me id Long hoti toh , JpaRepository me Long id jati...
    // as simple as that

    // ***** custom finder method *****
    // find the contact by user
    Page<Contact> findByUser(User user, Pageable pageable);


    // ***** custom query method  *****
    @Query("SELECT c FROM Contact c WHERE c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") String userId);

    //basically hume ek query chalayi 
    // agar
    //hamare contact entity ke under user hai uski id == above 2nd parameter userId se match krti hai toh 
    // woh contacts mil jaye


    // for searching on contacts methods
    // agar pagination data bhi chaiye to instead of List<Contact> => Page<Contact> krooo ...

    Page<Contact> findByNameContaining(String name, Pageable pageable);
    Page<Contact> findByEmailContaining(String email, Pageable pageable);
    Page<Contact> findByPhoneNumberContaining(String phoneNumber, Pageable pageable);


}
