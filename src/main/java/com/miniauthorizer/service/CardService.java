package com.miniauthorizer.service;

import com.miniauthorizer.dto.CardBasicsDTO;
import com.miniauthorizer.exceptions.CardNotFoundException;
import com.miniauthorizer.exceptions.DuplicateCardException;
import com.miniauthorizer.model.Card;
import com.miniauthorizer.repository.CardRepository;
import com.miniauthorizer.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void create(CardBasicsDTO cardDTO) {
        validateNewCard(cardDTO);
        Card card = this.buildDefaultCard(cardDTO);
        cardRepository.save(card);
    }

    private void validateNewCard(CardBasicsDTO newCard) {
        if( Objects.isNull(newCard) || Objects.isNull(newCard.getNumber()) || Objects.isNull(newCard.getPassword())){
            throw new IllegalArgumentException();
        }
        Card card = this.cardRepository.findByNumber(newCard.getNumber());
        if(Objects.nonNull(card)){
            throw new DuplicateCardException(newCard);
        }
    }

    private Card buildDefaultCard(CardBasicsDTO cardDTO) {
        return Card.builder()
                .number(cardDTO.getNumber())
                .password(passwordEncoder.encode(cardDTO.getPassword()))
                .balance(Constants.DEFAULT_INITIAL_CARD_BALANCE)
                .build();
    }

    public Double getBalance(String cardNumber) {
        Card card = Objects.nonNull(cardNumber) ? cardRepository.findByNumber(cardNumber) : null;
        if(Objects.isNull(card))
            throw new CardNotFoundException();
        return card.getBalance();
    }


}
