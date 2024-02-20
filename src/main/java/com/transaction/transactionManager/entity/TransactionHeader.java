package com.transaction.transactionManager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Divakar Verma
 * @created_at : 12/02/2024 - 4:51 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Entity
@Table(name = "transaction_header")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionHeader {

    @Id
    private String transactionHeaderId;
    private double requestedAmount;
    private String transactionType;
    private String transactionSubType;
    private String transactionStatus;
    private String username;
    private String senderAccount;
    private String SenderName;
    private String receiverAccount;
    private String receiverName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
