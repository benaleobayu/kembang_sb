package com.dev.byc.service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.dev.byc.entity.Admin;
import com.dev.byc.repository.AdminRepository;
import com.dev.byc.security.AdminPrincipal;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public CustomUserDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        Admin admin = adminOptional.get();
        return new AdminPrincipal(admin);
    }
}
