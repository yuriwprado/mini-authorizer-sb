package com.miniauthorizer.controller;

import com.miniauthorizer.AuthorizerTestUtils;
import com.miniauthorizer.TransactionExceptionReason;
import com.miniauthorizer.repository.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest extends AuthorizerTestUtils {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardRepository cardRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(username = "login", password = "password", roles = "USER")
    void shouldCommitTransaction() throws Exception {

        this.initMocks();
        Mockito.when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(this.buildCard(passwordEncoder));

        this.mockMvc.perform(post("/transacoes")
                        .content(this.buildTransactionDTOJson())
                        .contentType("application/json"))
                .andExpect(content().string("OK"))
                .andExpect(status().isCreated());

        Mockito.verify(cardRepository, Mockito.times(1)).save(any());
    }

    @Test
    @WithMockUser(username = "login", password = "password", roles = "USER")
    void shouldNotCommitTransaction_negativeValue() throws Exception {

        this.initMocks();
        Mockito.when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(this.buildCard(passwordEncoder));

        try{
            this.mockMvc.perform(post("/transacoes")
                    .content(this.buildTransactionDTOJson("-50.00"))
                    .contentType("application/json"));
        } catch (Exception e){
            Assertions.assertTrue(e.getCause().getMessage().equals("OPERACAO_INVALIDA"));
        }
    }

    @Test
    @WithMockUser(username = "login", password = "password", roles = "USER")
    void shouldNotCommitTransaction_cardNotFound() throws Exception {

        this.initMocks();
        Mockito.when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(null);

        this.mockMvc.perform(post("/transacoes")
                .content(this.buildTransactionDTOJson())
                .contentType("application/json"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(TransactionExceptionReason.CARD_NOT_FOUND.getDisplayMessage()));
    }

    @Test
    @WithMockUser(username = "login", password = "password", roles = "USER")
    void shouldNotCommitTransaction_invalidPassword() throws Exception {

        this.initMocks();
        Mockito.when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(this.buildCard(passwordEncoder, "DIFFERENT_PASSWORD"));

        this.mockMvc.perform(post("/transacoes")
                        .content(this.buildTransactionDTOJson())
                        .contentType("application/json"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(TransactionExceptionReason.INVALID_PASSWORD.getDisplayMessage()));
    }

    @Test
    @WithMockUser(username = "login", password = "password", roles = "USER")
    void shouldNotCommitTransaction_insufficientBalance() throws Exception {

        this.initMocks();
        Mockito.when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(this.buildCard(passwordEncoder));

        this.mockMvc.perform(post("/transacoes")
                        .content(this.buildTransactionDTOJson("3000.00"))
                        .contentType("application/json"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(TransactionExceptionReason.INSUFFICIENT_BALANCE.getDisplayMessage()));
    }

    @Test
    void shouldNotCommitTransaction_missingAuth() throws Exception {
        this.mockMvc.perform(post("/transacoes"))
                .andExpect(status().isUnauthorized());
    }




    private void initMocks(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

}