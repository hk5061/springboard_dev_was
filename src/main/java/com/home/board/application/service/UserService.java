package com.home.board.application.service;

import com.home.board.domain.user.User;
import com.home.board.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken, LocalDateTime expiryDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.updateRefreshToken(refreshToken, expiryDate);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<String> getRefreshTokenForUser(Long userId) {
        return userRepository.findById(userId)
                .map(User::getRefreshToken);
    }
}