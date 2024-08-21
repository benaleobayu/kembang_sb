package com.bca.byc.service.impl;

import com.bca.byc.entity.User;
import com.bca.byc.repository.UserRepository;
import com.bca.byc.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // Assuming user roles or permissions are handled similarly
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN")); // Example

        return new UserPrincipal(user, grantedAuthorities);
    }

}
