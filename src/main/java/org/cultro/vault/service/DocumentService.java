package org.cultro.vault.service;

import org.cultro.vault.model.User;
import org.cultro.vault.model.UserDocument;
import org.cultro.vault.repository.DocumentRepository;
import org.cultro.vault.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EncryptionService encryptionService;

    public UserDocument addDocument(String username, String title, String content) throws Exception {
        User user = userRepository.findByUsername(username);

        SecretKey userKey = encryptionService.decodeKey(user.getEncryptionKey());

        byte[] contentIV = encryptionService.generateIV();
        String encryptedContent = encryptionService.encrypt(content, userKey, contentIV);
        String encodedContentIV = Base64.getEncoder().encodeToString(contentIV);

        UserDocument document = new UserDocument(
                user.getId(),
                title,
                encryptedContent,
                encodedContentIV
        );

        return documentRepository.save(document);
    }

    public List<UserDocument> getDocuments(String username) {
        User user = userRepository.findByUsername(username);
        return documentRepository.findByUserId(user.getId());
    }

    public UserDocument getDocumentById(String username, String documentId) {
        User user = userRepository.findByUsername(username);
        UserDocument document = documentRepository.findById(documentId).orElse(null);
        if (document != null && document.getUserId().equals(user.getId())) {
            return document;
        }
        return null;
    }

    public void deleteDocument(String username, String documentId) {
        User user = userRepository.findByUsername(username);
        UserDocument document = documentRepository.findById(documentId).orElse(null);
        if (document != null && document.getUserId().equals(user.getId())) {
            documentRepository.delete(document);
        }
    }

    public UserDocument updateDocument(String username, String documentId, String title, String content) throws Exception {
        User user = userRepository.findByUsername(username);
        UserDocument document = documentRepository.findById(documentId).orElse(null);
        if (document != null && document.getUserId().equals(user.getId())) {
            SecretKey userKey = encryptionService.decodeKey(user.getEncryptionKey());

            byte[] contentIV = encryptionService.generateIV();
            String encryptedContent = encryptionService.encrypt(content, userKey, contentIV);
            String encodedContentIV = Base64.getEncoder().encodeToString(contentIV);

            document.setTitle(title);
            document.setEncryptedContent(encryptedContent);
            document.setContentIV(encodedContentIV);

            return documentRepository.save(document);
        }
        return null;
    }

    public String decryptContent(UserDocument document, User user) throws Exception {
        SecretKey userKey = encryptionService.decodeKey(user.getEncryptionKey());

        byte[] contentIV = Base64.getDecoder().decode(document.getContentIV());
        return encryptionService.decrypt(document.getEncryptedContent(), userKey, contentIV);
    }
}
