package com.scm.entities;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements  UserDetails{
                 // implements userDetails from spring security ...

    @Id
    private String userId;
    @Column(name="user_name", nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;

    // Getter AccessLevel.NONE se perticalar feild ka getter nhi banega ...
    @Getter(AccessLevel.NONE)
    private String password;
    @Column(length = 1000)
    private String about;
    private String profilePicLink;
    private String phoneNumber;

    //information additional
    @Getter(AccessLevel.NONE)
    private boolean enabled = false;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    // login Info - SELF, GOOGLE< LINKDIN, GITHUB, FACEBOOK, TWITTER
    //getting from enum Providers
    @Enumerated(value = EnumType.STRING)
    private Providers provider = Providers.SELF;
    private String providerUserId;


    // Mapping 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();


    @ElementCollection(fetch= FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    private String emailToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // list og roles hai [USER, ADMIN]
        //collection of SimpleGrantedAuthority [roles{ADMIN,USER}]
        Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role-> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }


    @Override
    public String getUsername() {
        // for this project oue email-Id is username, thats why retunring in userName 
       return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
       return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
       return true;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public  boolean isEnabled() {
		return this.enabled;
	}

}