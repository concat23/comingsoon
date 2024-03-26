package com.dev.comingsoon.adminuser;

import com.dev.comingsoon.entity.AdminUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@RequiredArgsConstructor
public class AdminUserConfig implements UserDetails {
    private final AdminUser adminUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays
                .stream(adminUser
                        .getRoles()
                        .split(","))
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return adminUser.getPassword();
    }

    @Override
    public String getUsername() {
        return adminUser.getEmail();
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
    public boolean isEnabled() {
        return true;
    }
}
