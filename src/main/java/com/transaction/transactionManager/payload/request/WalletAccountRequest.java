package com.transaction.transactionManager.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Divakar Verma
 * @created_at : 13/02/2024 - 4:39 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WalletAccountRequest {
    private String walletId;
    private String name;
}
