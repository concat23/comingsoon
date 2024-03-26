package com.dev.comingsoon.adminuser;

import com.dev.comingsoon.entity.AdminUser;
import com.dev.comingsoon.repository.IAdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class InMemoryInitialUserInfo implements CommandLineRunner {

    private final IAdminUserRepository iAdminUserRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        AdminUser manager = new AdminUser();
        manager.setUserName("Manager");
        manager.setPassword(passwordEncoder.encode("password"));
        manager.setRoles("ROLE_MANAGER");
        manager.setEmail("manager@manager.com");

        AdminUser admin = new AdminUser();
        admin.setUserName("Admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRoles("ROLE_ADMIN");
        admin.setEmail("admin@admin.com");

        AdminUser user = new AdminUser();
        user.setUserName("User");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles("ROLE_USER");
        user.setEmail("user@user.com");

        iAdminUserRepository.saveAll(List.of(manager,admin,user));
    }
}
