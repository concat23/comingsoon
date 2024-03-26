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
        createAndSaveUser("Manager", "manager@manager.com", "ROLE_MANAGER");
        createAndSaveUser("Admin", "admin@admin.com", "ROLE_ADMIN");
        createAndSaveUser("User", "user@user.com", "ROLE_USER");
        createAndSaveUser("Host", "host@host.com", "ROLE_MASTER");
    }

    private void createAndSaveUser(String username, String email, String roles) {
        if (!userExists(email)) {
            AdminUser user = new AdminUser();
            user.setUserName(username);
            user.setPassword(passwordEncoder.encode("password"));
            user.setRoles(roles);
            user.setEmail(email);
            iAdminUserRepository.save(user);
        }
    }

    private boolean userExists(String email) {
        return iAdminUserRepository.findByEmail(email).isPresent();
    }
}
