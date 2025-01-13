package com.scv.contact_vault.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.scv.contact_vault.entity.User;
import com.scv.contact_vault.forms.UserForm;
import com.scv.contact_vault.helpers.Message;
import com.scv.contact_vault.helpers.MessageType;
import com.scv.contact_vault.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    private UserService userService;

    public PageController(UserService theUserService) {
        userService = theUserService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home() {
        System.out.println("Home page handler");
        return "home";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        System.out.println("About page loading");
        return "about";
    }

    @GetMapping("/services")
    public String servicesPage() {
        System.out.println("Services page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        // userForm.setName("Avish"); Passing default values
        model.addAttribute("userForm", userForm);
        return "register";
    }

    @PostMapping("/signup")
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult theBindingResult,
            HttpSession session) {
        System.out.println("Processing registration");

        if (theBindingResult.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "register";
        }

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("https://i.pinimg.com/originals/a0/4d/84/a04d849cf591c2f980548b982f461401.jpg");

        userService.saveUser(user);
        System.out.println("User saved");

        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message", message);

        return "redirect:/register";
    }
}