package com.transaction.transactionManager.transactionService;

import com.transaction.transactionManager.payload.request.AddMoneyRequest;
import com.transaction.transactionManager.payload.request.TransactionRequest;
import com.transaction.transactionManager.payload.response.AddMoneyResponse;
import com.transaction.transactionManager.payload.response.TransactionResponse;
import reactor.core.publisher.Mono;

/**
 * @author Divakar Verma
 * @created_at : 12/02/2024 - 4:20 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
public interface AddMoneyService {

    public Mono<AddMoneyResponse> addMoney(AddMoneyRequest addMoneyRequest);
}
