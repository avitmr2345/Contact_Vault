package com.scv.contact_vault.controllers;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.scv.contact_vault.entity.Contact;
import com.scv.contact_vault.entity.User;
import com.scv.contact_vault.forms.ContactForm;
import com.scv.contact_vault.forms.ContactSearchForm;
import com.scv.contact_vault.helpers.AppConstants;
import com.scv.contact_vault.helpers.Helper;
import com.scv.contact_vault.helpers.Message;
import com.scv.contact_vault.helpers.MessageType;
import com.scv.contact_vault.services.ContactService;
import com.scv.contact_vault.services.ImageService;
import com.scv.contact_vault.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    private ContactService contactService;

    private UserService userService;

    private ImageService imageService;

    public ContactController(ContactService theContactService, UserService theUserService,
            ImageService theImageService) {
        contactService = theContactService;
        userService = theUserService;
        imageService = theImageService;
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

        String filename = UUID.randomUUID().toString();
        String fileURL = imageService.uploadImage(contactForm.getProfileImage(), filename);

        Contact contact = new Contact();
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setInstagramUsername(contactForm.getInstagramUsername());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setPicture(fileURL);
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

    @GetMapping()
    public String viewContacts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction, Model model,
            Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> contacts = contactService.getByUser(user, page, size, sortBy, direction);

        model.addAttribute("contacts", contacts);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";
    }

    @GetMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction, Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);

        User user = userService.getUserByEmail(username);

        Page<Contact> pageContact = null;

        if (contactSearchForm.getField() == null || contactSearchForm.getField().isEmpty()
                || contactSearchForm.getValue() == null || contactSearchForm.getValue().isEmpty()) {

            // fetch all without search filter
            pageContact = contactService.getByUser(user, page, size, sortBy, direction);

        } else {
            if (contactSearchForm.getField().equalsIgnoreCase("name")) {
                pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction,
                        user);
            } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
                pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction,
                        user);
            } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
                pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy,
                        direction, user);
            }
        }

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        model.addAttribute("contactSearchForm", contactSearchForm);

        return "user/search";
    }

    @GetMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable String contactId, HttpSession session) {
        contactService.delete(contactId);

        session.setAttribute("message",
                Message.builder()
                        .content("Contact has been deleted successfully !")
                        .type(MessageType.green)
                        .build());

        return "redirect:/user/contacts";
    }

    @GetMapping("/view/{contactId}")
    public String updateContactFormView(@PathVariable String contactId, Model model) {

        Contact contact = contactService.getById(contactId);
        ContactForm contactForm = new ContactForm();

        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setAddress(contact.getAddress());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavourite(contact.isFavourite());
        contactForm.setInstagramUsername(contact.getInstagramUsername());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setPicture(contact.getPicture());

        model.addAttribute("contactForm", contactForm);
        model.addAttribute("contactId", contactId);

        return "user/update_contact";
    }

    @PostMapping(value = "/update/{contactId}")
    public String updateContact(@PathVariable String contactId, @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult, Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "user/update_contact";
        }

        Contact contact = contactService.getById(contactId);
        contact.setId(contactId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setInstagramUsername(contactForm.getInstagramUsername());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        if (contactForm.getProfileImage() != null && !contactForm.getProfileImage().isEmpty()) {
            String fileName = UUID.randomUUID().toString();
            String fileUrl = imageService.uploadImage(contactForm.getProfileImage(), fileName);

            contact.setPicture(fileUrl);
            contactForm.setPicture(fileUrl);
        } else {
            System.out.println("File is empty");
        }
        contact.setFavourite(contactForm.isFavourite());

        contactService.update(contact);

        session.setAttribute("message", Message.builder().content("Contact Updated !").type(MessageType.green).build());

        return "redirect:/user/contacts/view/" + contactId;
    }
}