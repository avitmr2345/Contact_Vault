package com.scv.contact_vault.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    @GetMapping("/add")
    public String addContactView() {
        return "user/add_contact";
    }
    
}