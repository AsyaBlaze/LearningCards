package com.dictionary.learningcards.controllers;

import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.services.RegistrationService;
import com.dictionary.learningcards.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(UserValidator userValidator,
                          RegistrationService registrationService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") User user) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@RequestBody @Valid User user,
                                      BindingResult bindingResult) {

        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors())
            return "auth/registration";

        registrationService.register(user);
        return "redirect:/cards";
    }
}
