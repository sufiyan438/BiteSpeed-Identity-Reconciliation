package com.example.Identity_Reconciliation.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.Identity_Reconciliation.Service.ContactService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
public class ContactController {
    
    @Autowired
    private ContactService contactService;

    @PostMapping("/identify")
    public ResponseEntity<Map<String, Object>> identify(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String phoneNumber = request.get("phoneNumber");
        Map<String, Object> response = contactService.identifyContact(email, phoneNumber);
        return ResponseEntity.ok(response);
    }

}