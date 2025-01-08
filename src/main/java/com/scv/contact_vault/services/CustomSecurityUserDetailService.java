package com.scv.contact_vault.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.scv.contact_vault.repositories.UserRepo;

@Service
public class CustomSecurityUserDetailService implements UserDetailsService {

    private UserRepo userRepo;

    public CustomSecurityUserDetailService(UserRepo theUserRepo) {
        userRepo = theUserRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));

    }
}