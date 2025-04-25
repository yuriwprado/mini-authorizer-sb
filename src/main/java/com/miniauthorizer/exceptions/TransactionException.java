package com.miniauthorizer.exceptions;

import com.miniauthorizer.TransactionExceptionReason;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class TransactionException extends RuntimeException{
    private TransactionExceptionReason reason;
}
