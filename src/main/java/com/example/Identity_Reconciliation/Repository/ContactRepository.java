package com.example.Identity_Reconciliation.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Identity_Reconciliation.Model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Integer>{
    List<Contact> findByEmailOrPhoneNumberAndDeletedAtIsNull(String email,String phoneNumber);
}
