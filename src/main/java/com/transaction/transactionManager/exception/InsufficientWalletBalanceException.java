package com.transaction.transactionManager.exception;

/**
 * @author Divakar Verma
 * @created_at : 20/02/2024 - 1:12 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
public class InsufficientWalletBalanceException extends RuntimeException{

    public InsufficientWalletBalanceException(String message){
        super(message);
    }
}
