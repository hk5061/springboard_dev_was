package com.home.board.infrastructure.persistence;

import com.home.board.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUniqueIdentifier(String uniqueIdentifier);
    Boolean existsByEmail(String email);
    Boolean existsByUniqueIdentifier(String uniqueIdentifier);
}