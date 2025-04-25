package com.miniauthorizer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.miniauthorizer.dto.CardBasicsDTO;
import com.miniauthorizer.dto.TransactionDTO;
import com.miniauthorizer.model.Card;
import com.miniauthorizer.utils.Constants;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthorizerTestUtils {

    protected String CARD_NUMBER = "6549873025634501";
    protected String CARD_PASSWORD = "1234";

    protected String buildCardBasicsDTOJson() throws JsonProcessingException {
        CardBasicsDTO dto = buildCardBasicsDTO();
        return getObjectWriter().writeValueAsString(dto);
    }

    protected String buildTransactionDTOJson() throws JsonProcessingException {
        return this.buildTransactionDTOJson("10.00");
    }

    protected String buildTransactionDTOJson(String transactionValue) throws JsonProcessingException {
        TransactionDTO dto = buildTransactionDTO(transactionValue);
        return getObjectWriter().writeValueAsString(dto);
    }

    private ObjectWriter getObjectWriter() {
        return new ObjectMapper().writer().withDefaultPrettyPrinter();
    }

    protected CardBasicsDTO buildCardBasicsDTO() {
        return new CardBasicsDTO(CARD_NUMBER, CARD_PASSWORD);
    }

    protected TransactionDTO buildTransactionDTO(String value) {
        return new TransactionDTO(CARD_NUMBER, CARD_PASSWORD, value);
    }

    protected Card buildCard(PasswordEncoder passwordEncoder) {
        return this.buildCard(passwordEncoder, CARD_PASSWORD);
    }

    protected Card buildCard(PasswordEncoder passwordEncoder, String cardPassword) {
        return Card.builder()
                .number(CARD_NUMBER)
                .password(passwordEncoder.encode(cardPassword))
                .balance(Constants.DEFAULT_INITIAL_CARD_BALANCE)
                .build();
    }
}
