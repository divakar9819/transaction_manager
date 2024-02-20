package com.transaction.transactionManager.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Divakar Verma
 * @created_at : 12/02/2024 - 5:33 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WalletAccountResponse {

    private double balance;
    private String accountNumber;
    private String name;
    private String vpa;
}
