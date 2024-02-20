package com.transaction.transactionManager.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Divakar Verma
 * @created_at : 13/02/2024 - 12:06 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionResponse {

    private String transactionHeaderId;
    private double requestedAmount;
    private String transactionType;
    private String transactionSubType;
    private String transactionStatus;
    private String senderVpa;
    private String senderAccount;
    private String SenderName;
    private String receiverVpa;
    private String receiverAccount;
    private String receiverName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
