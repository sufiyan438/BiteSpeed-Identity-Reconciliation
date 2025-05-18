package com.example.Identity_Reconciliation.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.Identity_Reconciliation.Service.ContactService;
import com.example.Identity_Reconciliation.DTOs.PostContactResponse;
import com.example.Identity_Reconciliation.DTOs.PostContactRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
public class ContactController {
    
    @Autowired
    private ContactService contactService;

    @PostMapping("/identify")
    public ResponseEntity<PostContactResponse> identify(@RequestBody PostContactRequest request) {
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();
        PostContactResponse response = contactService.identifyContact(email, phoneNumber);
        return ResponseEntity.ok(response);
    }

}