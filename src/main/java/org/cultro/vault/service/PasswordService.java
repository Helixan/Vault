package org.cultro.vault.service;

import org.cultro.vault.model.PasswordEntry;
import org.cultro.vault.model.User;
import org.cultro.vault.repository.PasswordRepository;
import org.cultro.vault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;

@Service
public class PasswordService {

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    public PasswordEntry addPasswordEntry(String username, String title, String entryUsername, String password, String notes) throws Exception {
        User user = userRepository.findByUsername(username);

        SecretKey userKey = encryptionService.decodeKey(user.getEncryptionKey());

        byte[] passwordIV = encryptionService.generateIV();
        String encryptedPassword = encryptionService.encrypt(password, userKey, passwordIV);
        String encodedPasswordIV = Base64.getEncoder().encodeToString(passwordIV);

        byte[] notesIV = encryptionService.generateIV();
        String encryptedNotes = encryptionService.encrypt(notes, userKey, notesIV);
        String encodedNotesIV = Base64.getEncoder().encodeToString(notesIV);

        PasswordEntry entry = new PasswordEntry(
                user.getId(),
                title,
                entryUsername,
                encryptedPassword,
                encodedPasswordIV,
                encryptedNotes,
                encodedNotesIV
        );

        return passwordRepository.save(entry);
    }

    public List<PasswordEntry> getPasswordEntries(String username) {
        User user = userRepository.findByUsername(username);
        return passwordRepository.findByUserId(user.getId());
    }

    public PasswordEntry getPasswordEntryById(String username, String entryId) {
        User user = userRepository.findByUsername(username);
        PasswordEntry entry = passwordRepository.findById(entryId).orElse(null);
        if (entry != null && entry.getUserId().equals(user.getId())) {
            return entry;
        }
        return null;
    }

    public void deletePasswordEntry(String username, String entryId) {
        User user = userRepository.findByUsername(username);
        PasswordEntry entry = passwordRepository.findById(entryId).orElse(null);
        if (entry != null && entry.getUserId().equals(user.getId())) {
            passwordRepository.delete(entry);
        }
    }

    public PasswordEntry updatePasswordEntry(String username, String entryId, String title, String entryUsername, String password, String notes) throws Exception {
        User user = userRepository.findByUsername(username);
        PasswordEntry entry = passwordRepository.findById(entryId).orElse(null);
        if (entry != null && entry.getUserId().equals(user.getId())) {
            SecretKey userKey = encryptionService.decodeKey(user.getEncryptionKey());

            byte[] passwordIV = encryptionService.generateIV();
            String encryptedPassword = encryptionService.encrypt(password, userKey, passwordIV);
            String encodedPasswordIV = Base64.getEncoder().encodeToString(passwordIV);

            byte[] notesIV = encryptionService.generateIV();
            String encryptedNotes = encryptionService.encrypt(notes, userKey, notesIV);
            String encodedNotesIV = Base64.getEncoder().encodeToString(notesIV);

            entry.setTitle(title);
            entry.setUsername(entryUsername);
            entry.setEncryptedPassword(encryptedPassword);
            entry.setPasswordIV(encodedPasswordIV);
            entry.setEncryptedNotes(encryptedNotes);
            entry.setNotesIV(encodedNotesIV);

            return passwordRepository.save(entry);
        }
        return null;
    }

    public String decryptPassword(PasswordEntry entry, User user) throws Exception {
        SecretKey userKey = encryptionService.decodeKey(user.getEncryptionKey());

        byte[] passwordIV = Base64.getDecoder().decode(entry.getPasswordIV());
        return encryptionService.decrypt(entry.getEncryptedPassword(), userKey, passwordIV);
    }

    public String decryptNotes(PasswordEntry entry, User user) throws Exception {
        SecretKey userKey = encryptionService.decodeKey(user.getEncryptionKey());

        byte[] notesIV = Base64.getDecoder().decode(entry.getNotesIV());
        return encryptionService.decrypt(entry.getEncryptedNotes(), userKey, notesIV);
    }
}
