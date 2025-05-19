package com.example.Identity_Reconciliation.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.Identity_Reconciliation.Service.ContactService;
import com.example.Identity_Reconciliation.DTOs.IdentifyRequestDTO;
import com.example.Identity_Reconciliation.DTOs.IdentifyResponseDTO;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ContactController {
    
    @Autowired
    private ContactService contactService;

    @PostMapping("/identify")
    public IdentifyResponseDTO identify(@RequestBody IdentifyRequestDTO request) {
        return contactService.identifyContact(request);
    }

}