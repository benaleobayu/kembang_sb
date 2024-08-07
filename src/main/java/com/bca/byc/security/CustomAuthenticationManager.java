package com.bca.byc.security;

import com.bca.byc.service.CustomAdminDetailsService;
import com.bca.byc.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Primary
public class CustomAuthenticationManager implements AuthenticationManager {

    private final CustomAdminDetailsService adminDetailsService;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomAuthenticationManager(CustomAdminDetailsService adminDetailsService, UserDetailsServiceImpl userDetailsService, PasswordEncoder passwordEncoder) {
        this.adminDetailsService = adminDetailsService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            UserDetails adminDetails = adminDetailsService.loadUserByUsername(email);
            if (passwordEncoder.matches(password, adminDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(adminDetails, password, adminDetails.getAuthorities());
            }
        } catch (UsernameNotFoundException ex) {
            // Admin not found, continue to check users
        }

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("Invalid email or password");
            }
        } catch (UsernameNotFoundException ex) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
