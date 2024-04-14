package org.cultro.vault.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "passwords")
public class PasswordEntry {

    @Id
    private String id;

    private String userId;
    private String title;
    private String username;
    private String encryptedPassword;
    private String passwordIV;
    private String encryptedNotes;
    private String notesIV;

    public PasswordEntry() {
    }

    public PasswordEntry(String userId, String title, String username,
                         String encryptedPassword, String passwordIV,
                         String encryptedNotes, String notesIV) {
        this.userId = userId;
        this.title = title;
        this.username = username;
        this.encryptedPassword = encryptedPassword;
        this.passwordIV = passwordIV;
        this.encryptedNotes = encryptedNotes;
        this.notesIV = notesIV;
    }
}
