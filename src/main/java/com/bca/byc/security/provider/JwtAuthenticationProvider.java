package com.bca.byc.security.provider;

import com.bca.byc.entity.AppAdmin;
import com.bca.byc.entity.AppUser;
import com.bca.byc.response.AdminPermissionResponse;
import com.bca.byc.security.model.JwtAuthenticationToken;
import com.bca.byc.security.model.RawAccessJwtToken;
import com.bca.byc.service.AdminService;
import com.bca.byc.service.AppUserService;
import com.bca.byc.service.RoleService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final AppUserService appUserService;
    private final AdminService adminService;
    private final RoleService roleService;


    private final SecretKey key;
    private final RestTemplate restTemplate;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwtToken token = (RawAccessJwtToken) authentication.getCredentials();
        Jws<Claims> jwsClaims = token.parseClaims(key);
        String subject = jwsClaims.getPayload().getSubject();

//		List<String> scopes = (List<String>) jwsClaims.getBody().getOrDefault("scopes", Collections.emptyList());

//		List<GrantedAuthority> authorities = scopes.stream()
//				.map(SimpleGrantedAuthority::new)
//				.collect(Collectors.toList());

        AdminPermissionResponse permissions = adminService.getPermissionDetail(subject);
        List<GrantedAuthority> authorities = permissions.getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        List<String> adminRoles = roleService.getAdminRoles();  // Mendapatkan role admin CMS

        AppAdmin admins = adminService.findByEmail(subject);

        boolean isAdmin = adminRoles.stream()
                .anyMatch(role -> admins.getRole().getName().equalsIgnoreCase(role));

        // jika admin cms
        if (isAdmin) {
            UserDetails adminDetails = new UserDetails() {
                @Override
                public String getUsername() {
                    return subject;
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return authorities;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public String getPassword() {
                    return null;
                }
            };
            return new JwtAuthenticationToken(adminDetails, authorities);
        }

        // Untuk user app
        AppUser appUser = appUserService.findByUsername(subject);
        if (appUser == null) {
            throw new AuthenticationException("User not found") {
            };
        }
        return new JwtAuthenticationToken(appUser, null);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
