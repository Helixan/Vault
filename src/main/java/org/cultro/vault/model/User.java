package org.cultro.vault.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;
    private String passwordHash;
    private String email;
    private String encryptionKey;
    private String iv;

    public User() {
    }

    public User(String username, String passwordHash, String email, String encryptionKey, String iv) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.encryptionKey = encryptionKey;
        this.iv = iv;
    }
}
