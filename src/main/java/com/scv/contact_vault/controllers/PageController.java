package com.scv.contact_vault.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.scv.contact_vault.entity.User;
import com.scv.contact_vault.forms.UserForm;
import com.scv.contact_vault.services.UserService;

@Controller
public class PageController {

    private UserService userService;

    public PageController(UserService theUserService) {
        userService = theUserService;
    }

    @RequestMapping("/home")
    public String home() {
        System.out.println("Home page handler");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        System.out.println("About page loading");
        return "about";
    }

    @RequestMapping("/services")
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
    public String processRegister(@ModelAttribute UserForm userForm) {
        System.out.println("Processing registration");

        User user = User.builder()
                .name(userForm.getName())
                .email(userForm.getEmail())
                .password(userForm.getPassword())
                .about(userForm.getAbout())
                .phoneNumber(userForm.getPhoneNumber())
                .profilePic(
                        "https://i.pinimg.com/originals/a0/4d/84/a04d849cf591c2f980548b982f461401.jpg")
                .build();

        userService.saveUser(user);
        System.out.println("User saved");

        return "redirect:/register";
    }
}