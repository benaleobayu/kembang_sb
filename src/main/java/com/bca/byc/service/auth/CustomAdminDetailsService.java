package com.bca.byc.service.auth;

import com.bca.byc.entity.Admin;
import com.bca.byc.entity.RoleHasPermissions;
import com.bca.byc.repository.AdminRepository;
import com.bca.byc.repository.RoleHasPermissionsRepository;
import com.bca.byc.security.AdminPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RoleHasPermissionsRepository roleHasPermissionsRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found with email: " + email));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        
        List<RoleHasPermissions> rolePermissions = roleHasPermissionsRepository.findByRoleId(admin.getRole().getId());
        for (RoleHasPermissions rolePermission : rolePermissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(rolePermission.getPermission().getName()));
        }
        
        return new AdminPrincipal(admin, grantedAuthorities);
    }
}
