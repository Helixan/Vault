package org.cultro.vault.repository;

import org.cultro.vault.model.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentRepository extends MongoRepository<UserDocument, String> {
    List<UserDocument> findByUserId(String userId);
}
