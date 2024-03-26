package com.dev.comingsoon.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "refresh_token", nullable = false, length = 10000)
    private String refreshToken;

    @Column(name = "revoked")
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = "admin_user_id",referencedColumnName = "id")
    private AdminUser adminUser;
}
