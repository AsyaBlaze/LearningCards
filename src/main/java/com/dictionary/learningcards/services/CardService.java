package com.dictionary.learningcards.services;

import com.dictionary.learningcards.models.Card;
import com.dictionary.learningcards.models.User;
import com.dictionary.learningcards.repositories.CardRepository;
import com.dictionary.learningcards.security.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;

    @Autowired
    public CardService(CardRepository cardRepository, JdbcTemplate jdbcTemplate, UserService userService) {
        this.cardRepository = cardRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.userService = userService;
    }

    public List<Card> findAll() {
        return cardRepository.findAll().stream().filter(card -> card.getOwner() == userService.currentUser()).collect(Collectors.toList());
    }

    public void save(Card card) {
        card.setOwner(userService.currentUser());
        cardRepository.save(card);
    }

    public Card findById(int id) {
        return cardRepository.findById(id).orElse(null);
    }

    public void setLearned(int id, boolean isLearned) {
        Card card = findById(id);
        card.setLearned(isLearned);
        update(id, card);
    }

    public void update(int id, Card card) {
        card.setId(id);
        card.setOwner(userService.currentUser());
        card.setLearned(findById(id).isLearned());
        cardRepository.save(card);
    }

    public void deleteById(int id) {
        cardRepository.deleteById(id);
    }

    public List<Card> findByGroup(String group) {
        // Временно, была проблема с тем что поле isLearned всегда являлось false, хотя остальные данные выгружались корректно
        List<Card> cardsRsl = new ArrayList<>();
        List<Card> cards = findAll();
        for (Card card : cards) {
            if (card.getGroups().stream().anyMatch(group1 -> group1.getGroupName().equals(group))) {
                cardsRsl.add(card);
            }
        }
        return cardsRsl;
    }

    public Optional<Card> findByWord(String word) {
        return cardRepository.findByWord(word).stream().filter(w -> w.getOwner() == userService.currentUser()).findAny();
    }

}
