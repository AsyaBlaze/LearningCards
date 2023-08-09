package com.dictionary.learningcards.services;

import com.dictionary.learningcards.models.Group;
import com.dictionary.learningcards.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GroupService(GroupRepository groupRepository, JdbcTemplate jdbcTemplate) {
        this.groupRepository = groupRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Group group) {
        jdbcTemplate.execute("INSERT INTO groups (group_name) VALUES ('" + group.getGroupName() + "')");
    }

    public Group findGroupByName(String group) {
        return groupRepository.findByGroupName(group).orElse(null);
    }

    public Group findById(int id) {
        return groupRepository.findById(id).orElse(null);
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
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
