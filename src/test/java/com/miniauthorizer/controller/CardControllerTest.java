package com.miniauthorizer.controller;

import com.miniauthorizer.AuthorizerTestUtils;
import com.miniauthorizer.repository.CardRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CardControllerTest extends AuthorizerTestUtils {

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
    void shouldCreateCard() throws Exception {

        this.initMocks();
        Mockito.when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(null);
        Mockito.when(cardRepository.save(this.buildCard(passwordEncoder))).thenReturn(this.buildCard(passwordEncoder));

        this.mockMvc.perform(post("/cartoes")
                        .content(this.buildCardBasicsDTOJson())
                        .contentType("application/json"))
                .andExpect(content().json(buildCardBasicsDTOJson()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "login", password = "password", roles = "USER")
    void shouldNotCreateCard_existingCard() throws Exception {
        this.initMocks();
        Mockito.when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(this.buildCard(passwordEncoder));

        this.mockMvc.perform(post("/cartoes")
                        .content(this.buildCardBasicsDTOJson())
                        .contentType("application/json"))
                .andExpect(content().json(buildCardBasicsDTOJson()))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldNotCreateCard_missingAuth() throws Exception {
        this.mockMvc.perform(post("/cartoes")
                        .content(this.buildCardBasicsDTOJson())
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "login", password = "password", roles = "USER")
    void shouldGetBalance() throws Exception {

        this.initMocks();
        Mockito.when(cardRepository.findByNumber(CARD_NUMBER)).thenReturn(this.buildCard(passwordEncoder));

        this.mockMvc.perform(get("/cartoes/"+CARD_NUMBER))
                .andExpect(content().json("500.00"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "login", password = "password", roles = "USER")
    void shouldNotGetBalance_cardNotFound() throws Exception {

        String invalidCardNumber = "5555";

        this.initMocks();
        Mockito.when(cardRepository.findByNumber(invalidCardNumber)).thenReturn(null);

        this.mockMvc.perform(get("/cartoes/" + invalidCardNumber))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotGetBalance_missingAuth() throws Exception {
        this.mockMvc.perform(get("/cartoes/"+CARD_NUMBER))
                .andExpect(status().isUnauthorized());
    }

    private void initMocks(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

}