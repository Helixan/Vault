package org.cultro.vault.repository;

import org.cultro.vault.model.PasswordEntry;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PasswordRepository extends MongoRepository<PasswordEntry, String> {
    List<PasswordEntry> findByUserId(String userId);
}
