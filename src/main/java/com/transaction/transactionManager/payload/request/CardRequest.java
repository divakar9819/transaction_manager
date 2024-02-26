package com.transaction.transactionManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Divakar Verma
 * @created_at : 26/02/2024 - 3:54 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardRequest {
    private double amount;
    private String cardNumber;
    private String cvv;
    private String cardHolderName;
    private LocalDate expiryDate;

}
