package com.miniauthorizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    @JsonProperty("numeroCartao")
    private String number;

    @JsonProperty("senhaCartao")
    private String password;

    @JsonProperty("valor")
    private String value;

}
