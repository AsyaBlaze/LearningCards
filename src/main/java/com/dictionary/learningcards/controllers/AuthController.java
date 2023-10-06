package com.dictionary.learningcards.controllers;

import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.services.RegistrationService;
import com.dictionary.learningcards.services.UserService;
import com.dictionary.learningcards.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final UserService userService;

    @Autowired
    public AuthController(UserValidator userValidator,
                          RegistrationService registrationService, UserService userService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "auth/registration";

        registrationService.register(user);
        return "redirect:/cards";
    }

}
