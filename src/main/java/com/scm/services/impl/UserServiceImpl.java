package com.scm.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entities.User;
import com.scm.exceptionHelpers.AppConstants;
import com.scm.exceptionHelpers.Helper;
import com.scm.exceptionHelpers.ResourceNotFoundException;
import com.scm.repositories.UserRepository;
import com.scm.services.EmailService;
import com.scm.services.UserService;

// select UserServiceImpl and [ ctrl + dot] => select all unimplemented all methods
// niche sare methods apne aap ban jayenge kyuki humne implements UserService kia hai toh ye waha se yaha methods bane
// UserService ke methods ka implementation yaha likhenge ...

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User saveUser(User user) {
        // before save data into database , have to generate user id 
        String userId = UUID.randomUUID().toString();
        user.setUserId(userId);
        //password encode
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //set the user Role
        user.setRoleList(List.of(AppConstants.ROLE_USER));

        
        // generate emailLink and token to verify account
        
        String emailToken = UUID.randomUUID().toString();
        
        user.setEmailToken(emailToken);
        User savedUser = userRepository.save(user);

         String emailLink = Helper.getLinkForEmailVerification(emailToken);

         emailService.sendEmail(savedUser.getEmail(), "Verify Account : Smart Contact Manager", emailLink);

         return savedUser;
    }

    @Override
    public Optional<User> getUserById(String id) {
       return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepository.findById(user.getUserId()).orElseThrow(()-> new ResourceNotFoundException("user not found"));
        user2.setName(user.getName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePicLink(user.getProfilePicLink());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        //save the updated user in database
       User save =  userRepository.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(String id) {
        User user2 = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("user not found"));
        userRepository.delete(user2);
    }

    @Override
    public boolean isUserExist(String userId) {
        User user2 = userRepository.findById(userId).orElse(null);
        return user2 != null ? true : false;
    }
 
    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
       return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
       return userRepository.findByEmail(email).orElse(null);
    }

}
