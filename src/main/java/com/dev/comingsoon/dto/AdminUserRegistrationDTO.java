package com.dev.comingsoon.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Builder
@Getter @Setter
public class AdminUserRegistrationDTO {
    @NotEmpty(message = "User Name must not be empty")
    private String userName;

    private String userMobileNo;

    @NotEmpty(message = "User email must not be empty")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotEmpty(message = "User password must not be empty")
    private String userPassword;

    @NotEmpty(message = "User role must not be empty")
    private String userRole;


}
