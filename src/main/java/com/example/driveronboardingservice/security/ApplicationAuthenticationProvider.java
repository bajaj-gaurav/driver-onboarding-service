package com.example.driveronboardingservice.security;

import com.example.driveronboardingservice.entity.DriverProfile;
import com.example.driveronboardingservice.entity.Role;
import com.example.driveronboardingservice.repository.DriverProfileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class ApplicationAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private DriverProfileRepository driverProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        DriverProfile driverProfile = driverProfileRepository.findByEmail(username);
        if (driverProfile != null) {
            if (passwordEncoder.matches(pwd, driverProfile.getPassword()) && driverProfile.getEnabled()) {
                return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthorities(Set.of(driverProfile.getRole())));
            } else {
                throw new BadCredentialsException("Invalid password/Verification Pending!");
            }
        } else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Set<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role authority : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
