package org.cultro.vault.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "documents")
public class UserDocument {

    @Id
    private String id;

    private String userId;
    private String title;
    private String encryptedContent;
    private String contentIV;

    public UserDocument() {
    }

    public UserDocument(String userId, String title, String encryptedContent, String contentIV) {
        this.userId = userId;
        this.title = title;
        this.encryptedContent = encryptedContent;
        this.contentIV = contentIV;
    }
}
