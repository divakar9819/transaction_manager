package com.transaction.transactionManager.transactionService;

import com.transaction.transactionManager.payload.request.WalletAccountRequest;
import com.transaction.transactionManager.payload.response.ApiResponse;
import com.transaction.transactionManager.payload.response.WalletAccountResponse;
import reactor.core.publisher.Mono;

/**
 * @author Divakar Verma
 * @created_at : 13/02/2024 - 4:24 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
public interface WalletAccountService {

    public Mono<WalletAccountResponse> createWalletAccount(WalletAccountRequest walletAccountRequest);
}
