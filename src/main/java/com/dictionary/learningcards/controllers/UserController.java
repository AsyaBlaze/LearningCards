package com.dictionary.learningcards.controllers;

import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.services.CardService;
import com.dictionary.learningcards.services.UserService;
import com.dictionary.learningcards.util.UserValidator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@Controller
public class UserController {
    private final UserService userService;
    private final CardService cardService;
    private final UserValidator userValidator;

    public UserController(UserService userService, CardService cardService, UserValidator userValidator) {
        this.userService = userService;
        this.cardService = cardService;
        this.userValidator = userValidator;
    }

    @GetMapping("/personalAccount")
    public String showPersonalInfo(Model model) {
        model.addAttribute("user", userService.currentUser());
        model.addAttribute("countCards", cardService.findAll().size());
        model.addAttribute("password", "*".repeat(userService.currentUser().getPassword().length()));
        return "users/profile";
    }

    @GetMapping("/{id}/editUsername")
    public String editUsername(@PathVariable("id") int id,
                       Model model) {
        if (userService.currentUser().getIdUser() != id) {
            return "redirect:/user/" + id;
        }
        model.addAttribute("user", userService.currentUser());
        return "users/editUsername";
    }

    @PatchMapping("/{id}")
    public String updateUsername(@PathVariable("id") int id,
                         @ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "users/edit/" + id;
        userService.updateUsername(id, user.getUsername());
        return "redirect:/user/" + id;
    }

    @GetMapping("/{id}/changePassword")
    public String changePassword(@PathVariable("id") int id,
                       Model model) {
        if (userService.currentUser().getIdUser() != id)
            return "redirect:/user/" + id;
        model.addAttribute("id", id);
        model.addAttribute("checkPassword", new CheckPassword());
        return "users/changePassword";
    }

    @PatchMapping("/{id}/acceptChangingPassword")
    public String updatePassword(@PathVariable("id") int id,
                                 @ModelAttribute("checkPassword") @Valid CheckPassword checkPassword,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors() || id != userService.currentUser().getIdUser())
            return "users/changePassword";
        if (!checkPassword.getOldPassword().equals(userService.currentUser().getPassword())) {
            bindingResult.rejectValue("oldPassword", "000", "Old password doesn't match");
            return "users/changePassword";
        }
        if (!checkPassword.isPasswordsAreMatches()) {
            bindingResult.rejectValue("confirmNewPassword", "000", "New password doesn't match");
            return "users/changePassword";
        }
        userService.updatePassword(id, checkPassword.getNewPassword());
        return "redirect:/user/personalAccount";
    }
}

@Getter
@Setter
class CheckPassword {
    @Size(max = 16, min = 8, message = "Password's length must be between 8 and 16")
    private String newPassword;
    private String oldPassword;
    private String confirmNewPassword;

    public CheckPassword() {
    }

    public CheckPassword(String oldPassword, String newPassword, String confirmNewPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public boolean isPasswordsAreMatches() {
        return this.newPassword.equals(this.confirmNewPassword);
    }
}