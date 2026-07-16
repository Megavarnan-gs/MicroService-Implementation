package com.regulareedge.authservice.repository;

import com.regulareedge.authservice.common.enums.UserRole;
import com.regulareedge.authservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Page<User> findByRole(UserRole role, Pageable pageable);

    boolean existsByEmail(String email);
}
