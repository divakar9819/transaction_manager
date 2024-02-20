package com.transaction.transactionManager.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Divakar Verma
 * @created_at : 13/02/2024 - 12:26 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddMoneyRequest {
    @NotNull(message = "Amount should not be null")
    @Min(value = 1,message = "Amount should be greater then or equal to 1")
    private double amount;
}
