package com.dev.comingsoon.repository;

import com.dev.comingsoon.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAdminUserRepository extends JpaRepository<AdminUser,Long> {
    Optional<AdminUser> findByEmail(String email);
}
