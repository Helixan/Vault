package org.cultro.vault.service;

import org.cultro.vault.model.User;
import org.cultro.vault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(String username, String password, String email) throws Exception {
        if (userRepository.findByUsername(username) != null) {
            throw new Exception("User already exists");
        }

        String passwordHash = passwordEncoder.encode(password);

        SecretKey userKey = encryptionService.generateKey();
        byte[] iv = encryptionService.generateIV();

        String encodedKey = encryptionService.encodeKey(userKey);
        String encodedIV = Base64.getEncoder().encodeToString(iv);

        User user = new User(username, passwordHash, email, encodedKey, encodedIV);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean checkPassword(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return passwordEncoder.matches(rawPassword, user.getPasswordHash());
        }
        return false;
    }
}
