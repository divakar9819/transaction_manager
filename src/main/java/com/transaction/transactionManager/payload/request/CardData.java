package com.transaction.transactionManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Divakar Verma
 * @created_at : 26/02/2024 - 4:01 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardData {
    private String cardId;
    private String walletId;
    private String cardHolderName;
    private String cardNumber;
    private String cvv;
    private LocalDate expiryDate;
    private boolean isActivated;
    private String username;
}
