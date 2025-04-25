package com.miniauthorizer.controller;

import com.miniauthorizer.dto.TransactionDTO;
import com.miniauthorizer.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody TransactionDTO transactionDTO) {
        transactionService.commit(transactionDTO);
        return ResponseEntity.ok("OK");
    }
}
