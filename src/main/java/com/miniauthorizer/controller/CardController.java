package com.miniauthorizer.controller;

import com.miniauthorizer.dto.CardBasicsDTO;
import com.miniauthorizer.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartoes")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public ResponseEntity<CardBasicsDTO> create(@RequestBody CardBasicsDTO card) {
        cardService.create(card);
        return ResponseEntity.ok(card);
    }

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<Double> getBalance(@PathVariable(name = "numeroCartao") String cardNumber) {
        return ResponseEntity.ok(cardService.getBalance(cardNumber));
    }

}
