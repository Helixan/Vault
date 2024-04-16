package org.cultro.vault.controller;

import lombok.Getter;
import lombok.Setter;
import org.cultro.vault.model.PasswordEntry;
import org.cultro.vault.service.PasswordService;
import org.cultro.vault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/passwords")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserService userService;

    @PostMapping
    public PasswordEntry addPasswordEntry(@RequestBody PasswordEntryRequest request, Principal principal) throws Exception {
        String username = principal.getName();
        return passwordService.addPasswordEntry(username, request.getTitle(), request.getEntryUsername(), request.getPassword(), request.getNotes());
    }

    @GetMapping
    public List<PasswordEntry> getPasswordEntries(Principal principal) {
        String username = principal.getName();
        return passwordService.getPasswordEntries(username);
    }

    @GetMapping("/{id}")
    public PasswordEntry getPasswordEntry(@PathVariable String id, Principal principal) {
        String username = principal.getName();
        return passwordService.getPasswordEntryById(username, id);
    }

    @PutMapping("/{id}")
    public PasswordEntry updatePasswordEntry(@PathVariable String id, @RequestBody PasswordEntryRequest request, Principal principal) throws Exception {
        String username = principal.getName();
        return passwordService.updatePasswordEntry(username, id, request.getTitle(), request.getEntryUsername(), request.getPassword(), request.getNotes());
    }

    @DeleteMapping("/{id}")
    public void deletePasswordEntry(@PathVariable String id, Principal principal) {
        String username = principal.getName();
        passwordService.deletePasswordEntry(username, id);
    }

    @Getter
    @Setter
    public static class PasswordEntryRequest {
        private String title;
        private String entryUsername;
        private String password;
        private String notes;
    }
}
