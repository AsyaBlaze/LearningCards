package com.dictionary.learningcards.controllers;

import com.dictionary.learningcards.models.Group;
import com.dictionary.learningcards.services.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("groups")
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("")
    public String showAllCards(Model model) {
        model.addAttribute("groups", groupService.findAll());
        return "groups/index";
    }

    @GetMapping("/{id}")
    public String showInfo(@PathVariable int id,
                           Model model) {
        model.addAttribute("group", groupService.findById(id));
        return "groups/info";
    }

    @GetMapping("/add")
    public String addNew(@ModelAttribute("gr") Group group) {
        return "groups/new";
    }

    @PostMapping("/add")
    public String createGroup(@ModelAttribute("gr") Group group) {
        if (groupService.findGroupByName(group.getGroupName()) != null) {
            return "groups/new";
        }
        groupService.save(group);
        return "redirect:/groups";
    }

    @DeleteMapping("{id}/delete")
    private String delete(@PathVariable int id) {
        groupService.deleteById(id);
        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String editCard(Model model, @PathVariable int id) {
        model.addAttribute("group", groupService.findById(id));
        return "groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable int id,
                         @ModelAttribute("group") @Valid Group group,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "groups/edit";
        }
        groupService.update(id, group);
        return "redirect:/groups/" + id;
    }

}
