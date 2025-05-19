package com.example.Identity_Reconciliation.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Identity_Reconciliation.DTOs.IdentifyRequestDTO;
import com.example.Identity_Reconciliation.DTOs.IdentifyResponseDTO;
import com.example.Identity_Reconciliation.DTOs.ContactDTO;
import com.example.Identity_Reconciliation.Model.Contact;
import com.example.Identity_Reconciliation.Repository.ContactRepository;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    public IdentifyResponseDTO identifyContact(IdentifyRequestDTO request) {
        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();
        if (email == null && phoneNumber == null) {
            throw new IllegalArgumentException("Either email or phoneNumber must be provided.");
        }
        List<Contact> existingContacts = contactRepository.findByEmailOrPhoneNumberAndDeletedAtIsNull(email, phoneNumber);
        if (existingContacts.isEmpty()) {
            Contact newContact = new Contact();
            newContact.setEmail(email);
            newContact.setPhoneNumber(phoneNumber);
            newContact.setLinkPrecedence(Contact.LinkPrecedence.PRIMARY);
            contactRepository.save(newContact);
            return buildResponse(newContact, Collections.emptyList());
        }
        Contact primaryContact = findRootPrimary(existingContacts);
        List<Contact> secondaryContacts = new ArrayList<>();
        for (Contact contact : existingContacts) {
            if (!contact.getId().equals(primaryContact.getId())) {
                if (contact.getLinkPrecedence() != Contact.LinkPrecedence.SECONDARY) {
                    contact.setLinkPrecedence(Contact.LinkPrecedence.SECONDARY);
                    contact.setLinkedId(primaryContact.getId());
                    contactRepository.save(contact);
                }
                secondaryContacts.add(contact);
            }
        }
        return buildResponse(primaryContact, secondaryContacts);
    }

    private Contact findRootPrimary(List<Contact> contacts) {
        Contact primary = contacts.stream()
                .filter(contact -> contact.getLinkPrecedence() == Contact.LinkPrecedence.PRIMARY)
                .min(Comparator.comparing(Contact::getCreatedAt))
                .orElseThrow(() -> new IllegalStateException("No primary contact found!"));
        while (primary.getLinkedId() != null) {
            primary = contactRepository.findById(primary.getLinkedId())
                    .orElseThrow(() -> new IllegalStateException("Linked primary contact not found."));
        }
        return primary;
    }

    private IdentifyResponseDTO buildResponse(Contact primaryContact, List<Contact> secondaryContacts) {
        IdentifyResponseDTO response = new IdentifyResponseDTO();
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setPrimaryContactId(primaryContact.getId());
        contactDTO.setEmails(collectEmails(primaryContact, secondaryContacts));
        contactDTO.setPhoneNumbers(collectPhoneNumbers(primaryContact, secondaryContacts));
        contactDTO.setSecondaryContactIds(secondaryContacts.stream().map(Contact::getId).collect(Collectors.toList()));
        response.setContact(contactDTO);
        return response;
    }

    private List<String> collectEmails(Contact primaryContact, List<Contact> secondaryContacts) {
        Set<String> emails = new HashSet<>();
        if (primaryContact.getEmail() != null){
            emails.add(primaryContact.getEmail());
        }
        secondaryContacts.stream().map(Contact::getEmail).filter(Objects::nonNull).forEach(emails::add);
        return new ArrayList<>(emails);
    }

    private List<String> collectPhoneNumbers(Contact primaryContact, List<Contact> secondaryContacts) {
        Set<String> phoneNumbers = new HashSet<>();
        if (primaryContact.getPhoneNumber() != null){
            phoneNumbers.add(primaryContact.getPhoneNumber());
        }      
        secondaryContacts.stream().map(Contact::getPhoneNumber).filter(Objects::nonNull).forEach(phoneNumbers::add);
        return new ArrayList<>(phoneNumbers);
    }
}