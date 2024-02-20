package com.transaction.transactionManager.transactionService;

import com.transaction.transactionManager.payload.response.ValidTokenResponse;
import reactor.core.publisher.Mono;

/**
 * @author Divakar Verma
 * @created_at : 12/02/2024 - 4:22 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
public interface BaseService {
    public Mono<ValidTokenResponse> validateToken(String token);
}
