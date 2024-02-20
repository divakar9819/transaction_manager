package com.transaction.transactionManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Divakar Verma
 * @created_at : 13/02/2024 - 12:06 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionRequest {

    private String receiverAccount;
    private String receiverVpa;
    private String receiverName;
    private String transactionType;
    private String transactionSubType;
    private double amount;
}
