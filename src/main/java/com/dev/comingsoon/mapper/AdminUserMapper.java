package com.dev.comingsoon.mapper;

import com.dev.comingsoon.dto.AdminUserRegistrationDTO;
import com.dev.comingsoon.entity.AdminUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminUserMapper {
    private final PasswordEncoder passwordEncoder;

    public AdminUser convertToEntity(AdminUserRegistrationDTO adminUserRegistrationDTO) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUserName(adminUserRegistrationDTO.getUserName());
        adminUser.setEmail(adminUserRegistrationDTO.getUserEmail());
        adminUser.setMobileNumber(adminUserRegistrationDTO.getUserMobileNo());
        adminUser.setRoles(adminUserRegistrationDTO.getUserRole());
        adminUser.setPassword(passwordEncoder.encode(adminUserRegistrationDTO.getUserPassword()));
        return adminUser;
    }
}
