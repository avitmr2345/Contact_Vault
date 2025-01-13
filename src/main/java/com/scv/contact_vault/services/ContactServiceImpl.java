package com.scv.contact_vault.services;

import java.util.*;
import org.springframework.stereotype.Service;
import com.scv.contact_vault.entity.Contact;
import com.scv.contact_vault.helpers.ResourceNotFoundException;
import com.scv.contact_vault.repositories.ContactRepo;

@Service
public class ContactServiceImpl implements ContactService {

    private ContactRepo contactRepo;

    public ContactServiceImpl(ContactRepo theContactRepo) {
        contactRepo = theContactRepo;
    }

    @Override
    public Contact save(Contact contact) {
        String contactId = UUID.randomUUID().toString();
        contact.setId(contactId);
        return contactRepo.save(contact);
    }

    @Override
    public Contact update(Contact contact) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Contact> getAll() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getById(String id) {
        return contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
    }

    @Override
    public void delete(String id) {
        var contactId = contactRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id " + id));
        contactRepo.delete(contactId);
    }

    @Override
    public List<Contact> search(String name, String email, String phoneNumber) {
        throw new UnsupportedOperationException("Unimplemented method 'search'");
    }

    @Override
    public List<Contact> getByUserId(String userId) {
        return contactRepo.findByUserId(userId);
    }
}