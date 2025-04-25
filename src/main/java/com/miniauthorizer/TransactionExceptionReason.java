package com.miniauthorizer;

import lombok.Getter;

@Getter
public enum TransactionExceptionReason {

    INSUFFICIENT_BALANCE("SALDO_INSUFICIENTE"),
    INVALID_PASSWORD("SENHA_INVALIDA"),
    CARD_NOT_FOUND("CARTAO_INEXISTENTE");

    private String displayMessage;

    TransactionExceptionReason(String displayMessage){
        this.displayMessage = displayMessage;
    }

}
