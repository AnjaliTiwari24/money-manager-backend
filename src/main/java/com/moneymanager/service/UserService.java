package com.moneymanager.service;

import com.moneymanager.model.User;
import com.moneymanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        // In a real app, hash the password here (e.g., BCrypt)
        return userRepository.save(user);
    }

    public User loginUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                // Generate accountId if it doesn't exist (for existing users)
                if (user.getAccountId() == null || user.getAccountId().isEmpty()) {
                    user.setAccountId(generateAccountId());
                    userRepository.save(user);
                }
                return user; // Success
            }
        }
        throw new RuntimeException("Invalid credentials"); // Fail
    }

    // Generate unique account ID (like Paytm)
    private static String generateAccountId() {
        return "ACC" + System.currentTimeMillis() + (int)(Math.random() * 10000);
    }

    public User getUserByAccountId(String accountId) {
        return userRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("User not found with this Account ID"));
    }
}
