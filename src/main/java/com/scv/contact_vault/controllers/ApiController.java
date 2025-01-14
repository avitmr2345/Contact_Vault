package com.scv.contact_vault.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.scv.contact_vault.entity.Contact;
import com.scv.contact_vault.services.ContactService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private ContactService contactService;

    public ApiController(ContactService theContactService) {
        contactService = theContactService;
    }

    @GetMapping("/contacts/{contactId}")
    public Contact getContact(@PathVariable String contactId) {
        return contactService.getById(contactId);
    }
}