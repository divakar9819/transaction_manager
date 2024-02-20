package com.transaction.transactionManager.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Divakar Verma
 * @created_at : 31/01/2024 - 2:20 pm
 * @mail_to: vermadivakar2022@gmail.com
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ValidTokenResponse {

    private String message;
    private boolean success;
    private String username;
}
