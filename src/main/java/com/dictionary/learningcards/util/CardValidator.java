package com.dictionary.learningcards.util;

import com.dictionary.learningcards.models.Card;
import com.dictionary.learningcards.services.CardService;
import com.dictionary.learningcards.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CardValidator implements Validator {
    private final CardService cardService;
    private final GroupService groupService;

    @Autowired
    public CardValidator(CardService cardService, GroupService groupService) {
        this.cardService = cardService;
        this.groupService = groupService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Card.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Card card = (Card) target;
        if (cardService.findByWord(card.getWord()).isPresent()) {
            errors.rejectValue("word", "", "Card with this word is already created");
        }
    }
}
