package com.dev.comingsoon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="admin_users")
public class AdminUser {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username")
    private String userName;


    @Column(nullable = false, name = "email_id", unique = true)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(nullable = false, name = "roles")
    private String roles;

    @Column(nullable = false, name = "password")
    private String password;

    @OneToMany(mappedBy = "adminUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;
    
}
