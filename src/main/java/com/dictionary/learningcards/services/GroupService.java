package com.dictionary.learningcards.services;

import com.dictionary.learningcards.models.Card;
import com.dictionary.learningcards.models.Group;
import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.repositories.GroupRepository;
import com.dictionary.learningcards.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;

    @Autowired
    public GroupService(GroupRepository groupRepository, JdbcTemplate jdbcTemplate, UserService userService) {
        this.groupRepository = groupRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }

    private final User currentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.findByLogin(userDetails.getUsername());
        return userService.findByLogin(userDetails.getUsername()).orElse(null);
    }

    public void save(Group group) {
        group.setOwner(currentUser());
        groupRepository.save(group);
    }

    public Group findGroupByName(String group) {
        return groupRepository.findByGroupName(group).stream().filter(g -> g.getOwner() == currentUser()).findAny().orElse(null);
    }

    public Group findById(int id) {
        return groupRepository.findById(id).stream().filter(group -> group.getOwner() == currentUser()).findAny().orElse(null);
    }

    public List<Group> findAll() {
        return groupRepository.findAll().stream().filter(group -> group.getOwner() == currentUser()).collect(Collectors.toList());
    }

    public void deleteById(int id) {
        groupRepository.deleteById(id);
    }

    public void update(int id, Group group) {
        group.setId(id);
        jdbcTemplate.execute("UPDATE groups set group_name = '" + group.getGroupName()
                + "' WHERE id_group = " + id);
    }

}
