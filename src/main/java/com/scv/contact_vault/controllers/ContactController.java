package com.scv.contact_vault.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import com.scv.contact_vault.entity.Contact;
import com.scv.contact_vault.entity.User;
import com.scv.contact_vault.forms.ContactForm;
import com.scv.contact_vault.helpers.Helper;
import com.scv.contact_vault.helpers.Message;
import com.scv.contact_vault.helpers.MessageType;
import com.scv.contact_vault.services.ContactService;
import com.scv.contact_vault.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private ContactService contactService;

    private UserService userService;

    public ContactController(ContactService theContactService, UserService theUserService) {
        contactService = theContactService;
        userService = theUserService;
    }

    @GetMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    @PostMapping("/save")
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult theBindingResult,
            Authentication authentication, HttpSession session) {

        if (theBindingResult.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setInstagramUsername(contactForm.getInstagramUsername());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setFavourite(contactForm.isFavourite());
        contact.setUser(user);

        contactService.save(contact);

        Message message = Message.builder()
                .content("You have successfully added a new contact")
                .type(MessageType.green)
                .build();

        session.setAttribute("message", message);

        return "redirect:/user/contacts/add";
    }
}