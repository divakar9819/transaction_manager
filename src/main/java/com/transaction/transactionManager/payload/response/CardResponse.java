package com.transaction.transactionManager.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Divakar Verma
 * @created_at : 26/02/2024 - 3:57 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CardResponse {
    private String transactionHeaderId;
    private double requestedAmount;
    private String transactionType;
    private String transactionSubType;
    private String transactionStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
