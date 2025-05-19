package com.example.Identity_Reconciliation.DTOs;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactDTO {
        private Integer primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Integer> secondaryContactIds;
    }
