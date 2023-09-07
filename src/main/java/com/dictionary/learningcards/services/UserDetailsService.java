package com.dictionary.learningcards.services;

import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.repositories.UserRepository;
import com.dictionary.learningcards.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElse(null);

        if (user == null)
            throw new UsernameNotFoundException("User not found!");

        return new com.dictionary.learningcards.security.UserDetails(user);
    }
}
