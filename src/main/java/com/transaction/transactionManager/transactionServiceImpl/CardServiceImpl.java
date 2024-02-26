package com.transaction.transactionManager.transactionServiceImpl;

import com.transaction.transactionManager.entity.TransactionHeader;
import com.transaction.transactionManager.entity.WalletAccount;
import com.transaction.transactionManager.exception.*;
import com.transaction.transactionManager.payload.request.*;
import com.transaction.transactionManager.payload.response.CardResponse;
import com.transaction.transactionManager.payload.response.ValidTokenResponse;
import com.transaction.transactionManager.repository.TransactionHeaderRepository;
import com.transaction.transactionManager.repository.WalletAccountRepository;
import com.transaction.transactionManager.transactionService.CardService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.transaction.transactionManager.interceptor.AuthenticationInterceptor.getAuthToken;
import static com.transaction.transactionManager.interceptor.AuthenticationInterceptor.getUsername;
import static com.transaction.transactionManager.util.Utils.getRandomId;

/**
 * @author Divakar Verma
 * @created_at : 26/02/2024 - 3:59 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Service
public class CardServiceImpl implements CardService {

    @Qualifier("getCardWebClient")
    @Autowired
    private WebClient cardWebClient;

    @Autowired
    private WalletAccountRepository walletAccountRepository;

    @Autowired
    private TransactionHeaderRepository transactionHeaderRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Mono<CardResponse> doCardTransaction(CardRequest cardRequest) {
        String username = getUsername();
        return getCardDataByUsername(username)
                .flatMap(cardData -> {
                    if(!validateCardData(cardData, cardRequest)) {
                        return Mono.error(new GlobalException("User card is not activated"));
                    }
                    return Mono.just(cardData);
                })
                .flatMap(cardData -> {
                    WalletAccount walletAccount = walletAccountRepository.findByWalletId(cardData.getWalletId());
                    if(cardRequest.getAmount() >  walletAccount.getBalance()){
                        return Mono.error(new InsufficientWalletBalanceException("Insufficient wallet balance first add money"));
                    }
                    else {
                        walletAccount.setBalance(walletAccount.getBalance()- cardRequest.getAmount());
                        walletAccount.setLastTransaction(LocalDateTime.now());
                        WalletAccount updatedWallet = walletAccountRepository.save(walletAccount);
                        return Mono.just(updatedWallet);
                    }
                })
                .flatMap(walletAccount -> {
                    TransactionHeader transactionHeader = new TransactionHeader();
                    transactionHeader.setTransactionHeaderId(getRandomId());
                    transactionHeader.setTransactionType(TransactionType.W2C.toString());
                    transactionHeader.setTransactionSubType(TransactionStatusSubType.DEBIT.toString());
                    transactionHeader.setRequestedAmount(cardRequest.getAmount());
                    transactionHeader.setTransactionStatus(TransactionStatus.SUCCESS.toString());
                    transactionHeader.setUsername(username);
                    transactionHeader.setSenderAccount(walletAccount.getAccountNumber());
                    transactionHeader.setSenderName(walletAccount.getName());
                    transactionHeader.setCreatedAt(walletAccount.getLastTransaction());
                    transactionHeader.setUpdatedAt(walletAccount.getLastTransaction());
                    return Mono.just(transactionHeaderRepository.save(transactionHeader));
                })
                .flatMap(transactionHeader -> {
                    if(!transactionHeader.getTransactionStatus().equalsIgnoreCase("SUCCESS")){
                        return  Mono.error(new GlobalException(("Getting error on card transaction")));
                    }
                    else {
                        return  Mono.just(transactionHeaderToCardResponse(transactionHeader));
                    }
                })
                .switchIfEmpty(Mono.error( new GlobalException("Something went wrong")));
    }

    public CardResponse transactionHeaderToCardResponse(TransactionHeader transactionHeader){
        return this.modelMapper.map(transactionHeader,CardResponse.class);
    }

   public boolean validateCardData(CardData cardData, CardRequest cardRequest) {
    if (cardData.getCardNumber().equals(cardRequest.getCardNumber())
            && cardData.getCvv().equals(cardRequest.getCvv())
            && cardData.getCardHolderName().equalsIgnoreCase(cardRequest.getCardHolderName())
            && cardData.getExpiryDate().getMonth() == (cardRequest.getExpiryDate().getMonth())
            && cardData.getExpiryDate().getYear() == (cardRequest.getExpiryDate().getYear())
            && cardData.isActivated()) {
                return true;
    }
    return false;
}

    public Mono<CardData> getCardDataByUsername(String username){
        return cardWebClient.get().uri("/getCardData/"+username)
                .header(HttpHeaders.AUTHORIZATION, getAuthToken())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse ->
                        Mono.error(new InvalidTokenException("Invalid token"))
                )
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
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
                .bodyToMono(CardData.class);
    }
}
