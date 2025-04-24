package com.miniauthorizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CardBasicsDTO {

    @JsonProperty("numeroCartao")
    private String number;

    @JsonProperty("senha")
    private String password;
}
