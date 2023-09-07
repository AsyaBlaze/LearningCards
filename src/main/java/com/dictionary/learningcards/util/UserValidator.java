package com.dictionary.learningcards.util;

import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.findByLogin(user.getLogin()).isPresent()) {
            errors.rejectValue("username", "User with this login is already exist");
        }
    }
}
