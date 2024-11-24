package com.scm.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entities.User;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User,String> {

    // If want to add more extra methods db related operations
    // custom query methods
    //custom finder methods

    Optional<User> findByEmail(String email);

    User findByEmailToken(String emailToken);
    
}
