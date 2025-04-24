package com.miniauthorizer.exceptions;

import com.miniauthorizer.dto.CardBasicsDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class DuplicateCardException extends RuntimeException {

    CardBasicsDTO card;

    public DuplicateCardException(CardBasicsDTO card) {
        this.card = card;
    }

}
