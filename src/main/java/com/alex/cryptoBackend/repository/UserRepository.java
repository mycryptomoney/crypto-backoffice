package com.alex.cryptoBackend.repository;

import com.alex.cryptoBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Transactional
    @Query("SELECT a FROM User a WHERE a.email = ?1 OR a.username = ?1")
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    Boolean existsByEmail(String email);
    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.state = 'ACTIVE' WHERE a.email = ?1")
    void enableUser(String email);
}
