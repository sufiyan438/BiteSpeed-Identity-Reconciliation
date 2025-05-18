package com.example.Identity_Reconciliation.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostContactRequest {
    private String email;
    private String phoneNumber;
}
