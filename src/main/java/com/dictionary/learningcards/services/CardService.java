package com.dictionary.learningcards.services;

import com.dictionary.learningcards.models.Card;
import com.dictionary.learningcards.models.Group;
import com.dictionary.learningcards.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepository cardRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CardService(CardRepository cardRepository, JdbcTemplate jdbcTemplate) {
        this.cardRepository = cardRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public void save(Card card) {
        cardRepository.save(card);
    }

    public Card findById(int id) {
        return cardRepository.findById(id).orElse(null);
    }

    public void update(int id, Card card) {
        card.setId(id);
        card.setLearned(findById(id).isLearned());
        cardRepository.save(card);
    }

    public void deleteById(int id) {
        cardRepository.deleteById(id);
    }

    public List<Card> findByGroup(String group) {
        List<Card> list = jdbcTemplate.query("select cards.id_card, cards.word, cards.translation, cards.transcription, cards.examples, cards.is_learned\n" +
                "FROM cards JOIN cards_groups cg on cards.id_card = cg.id_card\n" +
                "JOIN groups g on g.id_group = cg.id_group where group_name = '" + group +"'", new BeanPropertyRowMapper<>(Card.class));
        return list.stream().map(card -> cardRepository.findByWord(card.getWord()).orElse(null)).collect(Collectors.toList());
    }

    public Optional<Card> findByWord(String word) {
        return cardRepository.findByWord(word);
    }

}
