package com.dev.comingsoon.config.jwtauth;

import com.dev.comingsoon.adminuser.AdminUserConfig;
import com.dev.comingsoon.repository.IAdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    public String getUserName(Jwt jwtToken){
        return jwtToken.getSubject();
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){
        final String userName = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired  && isTokenUserSameAsDatabase;

    }

    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    private final IAdminUserRepository iAdminUserRepository;
    public UserDetails userDetails(String email){
        return iAdminUserRepository
                .findByEmail(email)
                .map(AdminUserConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+email+" does not exist"));
    }
}
