package com.transaction.transactionManager.transactionService;

import com.transaction.transactionManager.payload.request.TransactionRequest;
import com.transaction.transactionManager.payload.response.TransactionResponse;
import reactor.core.publisher.Mono;

import java.lang.management.MonitorInfo;

/**
 * @author Divakar Verma
 * @created_at : 20/02/2024 - 12:58 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
public interface TransactionService {
    public Mono<TransactionResponse> doTransaction(TransactionRequest transactionRequest);
}
