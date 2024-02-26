package com.transaction.transactionManager.transactionServiceImpl;

import com.transaction.transactionManager.entity.WalletAccount;
import com.transaction.transactionManager.exception.ConnectionRefusedException;
import com.transaction.transactionManager.exception.GlobalException;
import com.transaction.transactionManager.exception.ResourceNotFoundException;
import com.transaction.transactionManager.exception.ServerErrorException;
import com.transaction.transactionManager.payload.request.WalletAccountRequest;
import com.transaction.transactionManager.payload.response.UserResponse;
import com.transaction.transactionManager.payload.response.WalletAccountResponse;
import com.transaction.transactionManager.repository.WalletAccountRepository;
import com.transaction.transactionManager.transactionService.WalletAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

import static com.transaction.transactionManager.interceptor.AuthenticationInterceptor.getUsername;

/**
 * @author Divakar Verma
 * @created_at : 13/02/2024 - 4:24 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Service
public class WalletAccountServiceImpl implements WalletAccountService {

    @Autowired
    @Qualifier("getAuthWebClient")
    private WebClient webClient;

    @Autowired
    private WalletAccountRepository walletAccountRepository;

    @Override
    public Mono<WalletAccountResponse> createWalletAccount(WalletAccountRequest walletAccountRequest) {
        String username = getUsername();
        WalletAccount walletAccount = new WalletAccount();
        walletAccount.setWalletId(walletAccountRequest.getWalletId());
        walletAccount.setName(walletAccountRequest.getName());
        walletAccount.setUsername(username);
        LocalDateTime localDateTime = LocalDateTime.now();
        walletAccount.setCreatedAt(localDateTime);
        walletAccount.setUpdatedAt(localDateTime);
        return getUserByUsername(username)
                .flatMap(userResponse -> {
                    walletAccount.setAccountNumber(userResponse.getMobileNumber() + walletAccountRequest.getWalletId());
                    walletAccount.setVpa(userResponse.getMobileNumber() + "@mypsp");

                    return Mono.fromCallable(() -> walletAccountRepository.save(walletAccount))
                            .flatMap(savedAccount -> {
                                WalletAccountResponse walletAccountResponse = new WalletAccountResponse();
                                walletAccountResponse.setAccountNumber(savedAccount.getAccountNumber());
                                walletAccountResponse.setVpa(savedAccount.getVpa());
                                walletAccountResponse.setName(savedAccount.getName());
                                walletAccountResponse.setBalance(savedAccount.getBalance());
                                return Mono.just(walletAccountResponse);
                            });
                            //.switchIfEmpty(Mono.error((Supplier<? extends Throwable>) new ApiResponse("Failed to save wallet account", false)));
                })
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")))
                .onErrorResume(throwable -> {
                    if (throwable instanceof ResourceNotFoundException) {
                        return Mono.error(throwable);
                    } else if (throwable instanceof ServerErrorException || throwable instanceof ConnectionRefusedException) {
                        return Mono.error(throwable);
                    } else {
                        return Mono.error(new GlobalException("Global exception"));
                    }
                });
    }

    @Override
    public Mono<List<WalletAccount>> findAllWalletAccountsByBalance(double balance) {
        List<WalletAccount> walletAccountList = walletAccountRepository.findWalletAccountsByBalance(balance);
        return Mono.just(walletAccountList);
    }


    public Mono<UserResponse> getUserByUsername(String username){
        return webClient.get().uri("/getUserByUsername/"+username)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new ResourceNotFoundException("User not found "))
                )
                .onStatus(HttpStatusCode::is5xxServerError,clientResponse ->
                        Mono.error(new ServerErrorException("Server error"))
                )
                .onStatus(HttpStatusCode :: isError, clientResponse -> {
                    if(clientResponse.statusCode()== HttpStatus.SERVICE_UNAVAILABLE){
                        return Mono.error(new ConnectionRefusedException("Connection refused"));
                    }
                    else {
                        return  clientResponse.createException();
                    }
                })
                .bodyToMono(UserResponse.class);
    }


}
