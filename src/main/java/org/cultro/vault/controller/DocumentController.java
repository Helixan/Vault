package org.cultro.vault.controller;

import lombok.Getter;
import lombok.Setter;
import org.cultro.vault.model.UserDocument;
import org.cultro.vault.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public UserDocument addDocument(@RequestBody DocumentRequest request, Principal principal) throws Exception {
        String username = principal.getName();
        return documentService.addDocument(username, request.getTitle(), request.getContent());
    }

    @GetMapping
    public List<UserDocument> getDocuments(Principal principal) {
        String username = principal.getName();
        return documentService.getDocuments(username);
    }

    @GetMapping("/{id}")
    public UserDocument getDocument(@PathVariable String id, Principal principal) {
        String username = principal.getName();
        return documentService.getDocumentById(username, id);
    }

    @PutMapping("/{id}")
    public UserDocument updateDocument(@PathVariable String id, @RequestBody DocumentRequest request, Principal principal) throws Exception {
        String username = principal.getName();
        return documentService.updateDocument(username, id, request.getTitle(), request.getContent());
    }

    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable String id, Principal principal) {
        String username = principal.getName();
        documentService.deleteDocument(username, id);
    }

    @Getter
    @Setter
    public static class DocumentRequest {
        private String title;
        private String content;
    }
}
