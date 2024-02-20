package com.transaction.transactionManager.transactionServiceImpl;

import com.transaction.transactionManager.entity.TransactionHeader;
import com.transaction.transactionManager.entity.WalletAccount;
import com.transaction.transactionManager.exception.GlobalException;
import com.transaction.transactionManager.exception.InsufficientWalletBalanceException;
import com.transaction.transactionManager.exception.ResourceNotFoundException;
import com.transaction.transactionManager.payload.request.TransactionRequest;
import com.transaction.transactionManager.payload.request.TransactionStatus;
import com.transaction.transactionManager.payload.request.TransactionType;
import com.transaction.transactionManager.payload.response.TransactionResponse;
import com.transaction.transactionManager.repository.TransactionHeaderRepository;
import com.transaction.transactionManager.repository.WalletAccountRepository;
import com.transaction.transactionManager.transactionService.TransactionService;
import org.hibernate.sql.ast.tree.from.TableReference;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.transaction.transactionManager.interceptor.AuthenticationInterceptor.getUsername;
import static com.transaction.transactionManager.util.Utils.getRandomId;

/**
 * @author Divakar Verma
 * @created_at : 20/02/2024 - 1:05 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private WalletAccountRepository walletAccountRepository;
    @Autowired
    private TransactionHeaderRepository transactionHeaderRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
 * This method is used to perform a transaction between two users.
 *
 * @param transactionRequest The transaction request object containing the details of the transaction
 * @return A transaction response object containing the details of the performed transaction
 */
@Override
public Mono<TransactionResponse> doTransaction(TransactionRequest transactionRequest) {
    String username = getUsername();
    return Mono.fromCallable(() -> walletAccountRepository.findByUsername(username))
            .flatMap(senderWalletAccount -> {
                if (transactionRequest.getTransactionType().equalsIgnoreCase(TransactionType.W2W.toString())) {
                    return Mono.fromCallable(() -> walletAccountRepository.findByVpa(transactionRequest.getReceiverVpa()))
                            .flatMap(receiverWalletAccount -> {
                                return processTransaction(senderWalletAccount, receiverWalletAccount, transactionRequest, username);
                            })
                            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Invalid receiver vpa")));
                } else if (transactionRequest.getTransactionType().equalsIgnoreCase(TransactionType.W2B.toString())) {
                    return Mono.fromCallable(() -> walletAccountRepository.findByAccountNumber(transactionRequest.getReceiverAccount()))
                            .flatMap(receiverWalletAccount -> {
                                return processTransaction(senderWalletAccount, receiverWalletAccount, transactionRequest, username);
                            })
                            .switchIfEmpty(Mono.error(new ResourceNotFoundException("Invalid receiver account number")));
                } else {
                    return Mono.error(new IllegalStateException("Unsupported transaction type"));
                }
            })
            .switchIfEmpty(Mono.error(new ResourceNotFoundException("User wallet not found")))
            .onErrorResume(throwable -> Mono.error(throwable));
}

    private Mono<TransactionResponse> processTransaction(WalletAccount senderWalletAccount, WalletAccount receiverWalletAccount, TransactionRequest transactionRequest, String username) {
        LocalDateTime localDateTime = LocalDateTime.now();
        if (transactionRequest.getAmount() > senderWalletAccount.getBalance()) {
            return Mono.error(new InsufficientWalletBalanceException("User does not have sufficient wallet balance"));
        } else {
            double openingBalance = senderWalletAccount.getBalance();
            double closingBalance = openingBalance - transactionRequest.getAmount();
            senderWalletAccount.setBalance(closingBalance);
            receiverWalletAccount.setBalance(receiverWalletAccount.getBalance() + transactionRequest.getAmount());

            return Mono.fromCallable(() -> walletAccountRepository.save(senderWalletAccount))
                    .flatMap(updatdSenderWalletAccount -> {
                        return Mono.fromCallable(() -> walletAccountRepository.save(receiverWalletAccount))
                                .flatMap(updatReceiverWalletAccount -> {
                                    TransactionHeader transactionHeader = new TransactionHeader();
                                    transactionHeader.setTransactionHeaderId(getRandomId());
                                    transactionHeader.setTransactionType(transactionRequest.getTransactionType());
                                    transactionHeader.setRequestedAmount(transactionRequest.getAmount());
                                    transactionHeader.setTransactionSubType(transactionRequest.getTransactionSubType());
                                    transactionHeader.setSenderAccount(senderWalletAccount.getVpa());
                                    transactionHeader.setSenderName(senderWalletAccount.getName());
                                    transactionHeader.setReceiverAccount(receiverWalletAccount.getVpa());
                                    transactionHeader.setReceiverName(receiverWalletAccount.getName());
                                    transactionHeader.setUsername(username);
                                    transactionHeader.setCreatedAt(localDateTime);
                                    transactionHeader.setUpdatedAt(localDateTime);
                                    transactionHeader.setTransactionStatus(TransactionStatus.SUCCESS.toString());
                                    return Mono.fromCallable(() -> transactionHeaderRepository.save(transactionHeader))
                                            .map(savedTransactionHeader -> transactionHeaderToTransactionResponse(savedTransactionHeader))
                                            .switchIfEmpty(Mono.error(new GlobalException("Error saving transaction header. Try again later.")));
                                })
                                .switchIfEmpty(Mono.error(new GlobalException("Error updating receiver wallet. Try again later.")));
                    })
                    .switchIfEmpty(Mono.error(new GlobalException("Error updating sender wallet. Try again later.")));
        }
    }

    /**
 * This method is used to convert the transaction header entity to transaction response entity.
 *
 * @param transactionHeader The transaction header entity
 * @return The transaction response entity
 */
public TransactionResponse transactionHeaderToTransactionResponse(TransactionHeader transactionHeader){
    TransactionResponse transactionResponse=  this.modelMapper.map(transactionHeader,TransactionResponse.class);
    if(transactionResponse.getTransactionType().equalsIgnoreCase("W2W")){
        transactionResponse.setReceiverVpa(transactionHeader.getReceiverAccount());
        transactionResponse.setSenderVpa(transactionHeader.getSenderAccount());
    }
    return transactionResponse;
}



}
