package com.miniauthorizer.service;

import com.miniauthorizer.TransactionExceptionReason;
import com.miniauthorizer.dto.TransactionDTO;
import com.miniauthorizer.exceptions.TransactionException;
import com.miniauthorizer.model.Card;
import com.miniauthorizer.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
public class TransactionService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public synchronized void commit(TransactionDTO transactionDTO) {
        this.validateTransactionParameters(transactionDTO);
        Card card = cardRepository.findByNumber(transactionDTO.getNumber());
        Double transactionValue = Double.parseDouble(transactionDTO.getValue());
        this.validateTransaction(card, transactionDTO.getPassword(), transactionValue);
        if(transactionValue == 0d)
            return;
        card.setBalance(card.getBalance() - transactionValue);
        cardRepository.save(card);
    }

    private void validateTransactionParameters(TransactionDTO transactionDTO) {
        if( isNull(transactionDTO) || isNull(transactionDTO.getNumber()) || isNull(transactionDTO.getPassword())){
            throw new IllegalArgumentException();
        }
        double transactionValue = Double.parseDouble(transactionDTO.getValue());
        if(transactionValue < 0d){
            throw new RuntimeException("OPERACAO_INVALIDA");
        }
    }

    private void validateTransaction(Card card, String passwordProvided, double transactionValue){
        if(isNull(card) )
            throw new TransactionException(TransactionExceptionReason.CARD_NOT_FOUND);
        if(!passwordEncoder.matches(passwordProvided, card.getPassword()))
            throw new TransactionException(TransactionExceptionReason.INVALID_PASSWORD);
        if(card.getBalance() < transactionValue)
            throw new TransactionException(TransactionExceptionReason.INSUFFICIENT_BALANCE);
    }

}
