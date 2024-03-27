package com.dev.comingsoon.auth;

import com.dev.comingsoon.dto.AdminUserLoginDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;


public class AuthenticationServiceTest {

    private AdminUserLoginDTO adminUserLoginDTO;

    public AdminUserLoginDTO getAdminUserLoginDTO() {
        return adminUserLoginDTO;
    }

    public void setAdminUserLoginDTO(AdminUserLoginDTO adminUserLoginDTO) {
        this.adminUserLoginDTO = adminUserLoginDTO;
    }

    @BeforeEach
    public void setUp() {
        String usernameOrEmail = "test";
        String password = "test";
        this.adminUserLoginDTO = new AdminUserLoginDTO(usernameOrEmail,password);
    }

    @Test
    public void testInfo_Username_Email_Password(){
        // Arrange
        String expectedUsernameOrEmail = "test";
        String expectedPassword = "test";

        // Act
        AdminUserLoginDTO actualAdminUserLoginDTO = getAdminUserLoginDTO();

        // Assert
        assertEquals(expectedUsernameOrEmail, actualAdminUserLoginDTO.getUsernameOrEmail());
        assertEquals(expectedPassword, actualAdminUserLoginDTO.getPassword());
    }


}