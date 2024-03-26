package com.dev.comingsoon.adminuser;

import com.dev.comingsoon.repository.IAdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdminUserManagerConfig implements UserDetailsService {

    private final IAdminUserRepository iAdminUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return iAdminUserRepository
                .findByEmail(email)
                .map(AdminUserConfig::new)
                .orElseThrow(()-> new UsernameNotFoundException("UserEmail: "+email+" does not exist"));
    }
}
