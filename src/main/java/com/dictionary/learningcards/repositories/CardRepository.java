package com.dictionary.learningcards.repositories;

import com.dictionary.learningcards.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {
    List<Card> findByWord(String word);
}
