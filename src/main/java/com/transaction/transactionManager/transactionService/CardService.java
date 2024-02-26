package com.transaction.transactionManager.transactionService;

import com.transaction.transactionManager.payload.request.CardRequest;
import com.transaction.transactionManager.payload.response.CardResponse;
import reactor.core.publisher.Mono;

/**
 * @author Divakar Verma
 * @created_at : 26/02/2024 - 3:54 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
public interface CardService {

    public Mono<CardResponse> doCardTransaction(CardRequest cardRequest);


}
