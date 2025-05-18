package com.example.Identity_Reconciliation.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Identity_Reconciliation.DTOs.PostContactResponse;
import com.example.Identity_Reconciliation.Model.Contact;
import com.example.Identity_Reconciliation.Repository.ContactRepository;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public PostContactResponse identifyContact(String email, String phoneNumber) {
        List<Contact> matchingContacts = contactRepository.findByEmailOrPhoneNumber(email, phoneNumber);

        if (matchingContacts.isEmpty()) {
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence(Contact.LinkPrecedence.PRIMARY);
            contactRepository.save(newContact);

            return new PostContactResponse(buildResponse(newContact, Collections.emptyList()));
        }

        Contact primaryContact = findPrimaryContact(matchingContacts);
        List<Contact> secondaryContacts = matchingContacts.stream()
                .filter(contact -> !contact.equals(primaryContact))
                .collect(Collectors.toList());

        if (matchingContacts.stream().noneMatch(c -> Objects.equals(c.getEmail(), email) && Objects.equals(c.getPhoneNumber(), phoneNumber))) {
            Contact newSecondaryContact = new Contact();
            newSecondaryContact.setEmail(email);
            newSecondaryContact.setPhoneNumber(phoneNumber);
            newSecondaryContact.setLinkPrecedence(Contact.LinkPrecedence.SECONDARY);
            newSecondaryContact.setLinkedContact(primaryContact);
            contactRepository.save(newSecondaryContact);
            secondaryContacts.add(newSecondaryContact);
        }

        return new PostContactResponse(buildResponse(primaryContact, secondaryContacts));
    }

    private Contact findPrimaryContact(List<Contact> contacts) {
        return contacts.stream()
                .filter(contact -> contact.getLinkPrecedence() == Contact.LinkPrecedence.PRIMARY)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No primary contact found"));
    }

    private Map<String, Object> buildResponse(Contact primaryContact, List<Contact> secondaryContacts) {
        Map<String, Object> response = new HashMap<>();
        response.put("primaryContactId", primaryContact.getId());
        response.put("emails", collectEmails(primaryContact, secondaryContacts));
        response.put("phoneNumbers", collectPhoneNumbers(primaryContact, secondaryContacts));
        response.put("secondaryContactIds", secondaryContacts.stream().map(Contact::getId).collect(Collectors.toList()));
        return response;
    }

    private List<String> collectEmails(Contact primaryContact, List<Contact> secondaryContacts) {
        Set<String> emails = new HashSet<>();
        if (primaryContact.getEmail() != null) emails.add(primaryContact.getEmail());
        secondaryContacts.forEach(contact -> {
            if (contact.getEmail() != null) emails.add(contact.getEmail());
        });
        return new ArrayList<>(emails);
    }

    private List<String> collectPhoneNumbers(Contact primaryContact, List<Contact> secondaryContacts) {
        Set<String> phoneNumbers = new HashSet<>();
        if (primaryContact.getPhoneNumber() != null) phoneNumbers.add(primaryContact.getPhoneNumber());
        secondaryContacts.forEach(contact -> {
            if (contact.getPhoneNumber() != null) phoneNumbers.add(contact.getPhoneNumber());
        });
        return new ArrayList<>(phoneNumbers);
    }

}
