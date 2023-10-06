package com.dictionary.learningcards.repositories;

import com.dictionary.learningcards.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findByGroupName(String group);
}
