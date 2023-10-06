package com.dictionary.learningcards.services;

import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.repositories.UserRepository;
import com.dictionary.learningcards.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public final User currentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        findByLogin(userDetails.getUsername());
        return findByLogin(userDetails.getUsername()).orElse(null);
    }

    public Optional<User> findByLogin(String username) {
        return userRepository.findByUsername(username);
    }

    public void updateUsername(int id, String username) {
         User user = userRepository.findByIdUser(id).orElse(null);
        if (user !=  null) {
            user.setUsername(username);
            userRepository.save(user);
        }
    }

    public void updatePassword(int id, String newPassword) {
        User user = userRepository.findByIdUser(id).orElse(null);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
        }
    }
}
