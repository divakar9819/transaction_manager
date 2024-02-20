package com.transaction.transactionManager.transactionServiceImpl;

import com.transaction.transactionManager.entity.TransactionHeader;
import com.transaction.transactionManager.entity.WalletAccount;
import com.transaction.transactionManager.exception.GlobalException;
import com.transaction.transactionManager.payload.request.*;
import com.transaction.transactionManager.payload.response.AddMoneyResponse;
import com.transaction.transactionManager.repository.TransactionHeaderRepository;
import com.transaction.transactionManager.repository.WalletAccountRepository;
import com.transaction.transactionManager.transactionService.AddMoneyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.transaction.transactionManager.interceptor.AuthenticationInterceptor.getUsername;
import static com.transaction.transactionManager.util.Utils.getRandomId;

/**
 * @author Divakar Verma
 * @created_at : 13/02/2024 - 12:17 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Service
public class AddMoneyServiceImpl implements AddMoneyService {

    @Autowired
    private WalletAccountRepository walletAccountRepository;
    @Autowired
    private TransactionHeaderRepository transactionHeaderRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public Mono<AddMoneyResponse> addMoney(AddMoneyRequest addMoneyRequest) {
        String username = getUsername();
        return Mono.fromCallable(() -> walletAccountRepository.findByUsername(username))
                .flatMap(walletAccount -> {
                    double newBalance = addMoneyRequest.getAmount()+walletAccount.getBalance();
                    walletAccount.setBalance(newBalance);
                    WalletAccount updatedWallet = walletAccountRepository.save(walletAccount); // updating wallet balance
                    if(updatedWallet.getBalance() == walletAccount.getBalance()){
                                    TransactionHeader transactionHeader = new TransactionHeader();
                                    transactionHeader.setTransactionHeaderId(getRandomId());
                                    transactionHeader.setRequestedAmount(addMoneyRequest.getAmount());
                                    transactionHeader.setTransactionStatus(TransactionStatus.SUCCESS.toString());
                                    transactionHeader.setTransactionSubType(TransactionStatusSubType.CREDIT.toString());
                                    transactionHeader.setTransactionType(TransactionType.A2W.toString());
                                    transactionHeader.setUsername(updatedWallet.getUsername());
                                    transactionHeader.setReceiverAccount(updatedWallet.getAccountNumber());
                                    transactionHeader.setReceiverName(updatedWallet.getName());
                                    LocalDateTime localDateTime = LocalDateTime.now();
                                    transactionHeader.setCreatedAt(localDateTime);
                                    transactionHeader.setUpdatedAt(localDateTime);
                                    //TransactionHeader createdTransaction = transactionHeaderRepository.save(transactionHeader);
                                    return  transactionHeaderToAddMoneyResponse(transactionHeader);
                    }
                    else {
                        return Mono.error(new GlobalException("Unable to add money to wallet"));
                    }
                })
                .onErrorResume(Mono::error);
    }

    public Mono<AddMoneyResponse> transactionHeaderToAddMoneyResponse(TransactionHeader transactionHeader){
        AddMoneyResponse addMoneyResponse = new AddMoneyResponse();
        addMoneyResponse.setRequestedAmount(transactionHeader.getRequestedAmount());
        addMoneyResponse.setTransactionStatus(transactionHeader.getTransactionStatus());
        addMoneyResponse.setTransactionSubType(transactionHeader.getTransactionSubType());
        addMoneyResponse.setTransactionHeaderId(transactionHeader.getTransactionHeaderId());
        addMoneyResponse.setReceiverAccount(transactionHeader.getReceiverAccount());
        addMoneyResponse.setReceiverName(transactionHeader.getReceiverName());
        addMoneyResponse.setTransactionType(transactionHeader.getTransactionType());
        addMoneyResponse.setCreatedAt(transactionHeader.getCreatedAt());
        addMoneyResponse.setUpdatedAt(transactionHeader.getUpdatedAt());
        return Mono.just(addMoneyResponse);
    }
}
