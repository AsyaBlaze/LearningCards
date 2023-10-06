package com.dictionary.learningcards.services;

import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.repositories.UserRepository;
import com.dictionary.learningcards.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Transactional
    public void register(User person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        userRepository.save(person);
    }
}
