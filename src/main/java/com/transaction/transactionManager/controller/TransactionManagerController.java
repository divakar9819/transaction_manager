package com.transaction.transactionManager.controller;

import com.transaction.transactionManager.exception.GlobalException;
import com.transaction.transactionManager.exception.InsufficientWalletBalanceException;
import com.transaction.transactionManager.payload.request.AddMoneyRequest;
import com.transaction.transactionManager.payload.request.CardRequest;
import com.transaction.transactionManager.payload.request.TransactionRequest;
import com.transaction.transactionManager.payload.request.WalletAccountRequest;
import com.transaction.transactionManager.payload.response.*;
import com.transaction.transactionManager.transactionService.AddMoneyService;
import com.transaction.transactionManager.transactionService.CardService;
import com.transaction.transactionManager.transactionService.TransactionService;
import com.transaction.transactionManager.transactionService.WalletAccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Divakar Verma
 * @created_at : 12/02/2024 - 4:42 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@RestController
@RequestMapping("api/v1/tm")
@Validated
public class TransactionManagerController {

    @Autowired
    private AddMoneyService addMoneyService;

    @Autowired
    private WalletAccountService walletAccountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CardService cardService;

    @GetMapping("/home")
    public String home(){
        return "Transaction manager home";
    }

    @PostMapping("/createWallet")
    public Mono<ResponseEntity<WalletAccountResponse>> createWallet(@RequestBody WalletAccountRequest walletAccountRequest){
        return walletAccountService.createWalletAccount(walletAccountRequest)
                .map(apiResponse -> ResponseEntity.status(HttpStatus.CREATED).body(apiResponse))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/addMoney")
    public Mono<ResponseEntity<AddMoneyResponse>> addMoney(@Valid @RequestBody AddMoneyRequest addMoneyRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println("*******************************************");
            return Mono.just(ResponseEntity.badRequest().body((AddMoneyResponse) createValidationErrorResponse(bindingResult)));
        } else {
            return addMoneyService.addMoney(addMoneyRequest)
                    .map(addMoneyResponse -> ResponseEntity.status(HttpStatus.CREATED).body(addMoneyResponse))
                    .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
        }
    }

    @PostMapping("/doTransaction")
    public Mono<ResponseEntity<TransactionResponse>> doTransaction(@RequestBody TransactionRequest transactionRequest){
        return transactionService.doTransaction(transactionRequest)
                .map(transactionResponse -> ResponseEntity.status(HttpStatus.CREATED).body(transactionResponse))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @PostMapping("/cardTransaction")
    public Mono<ResponseEntity<CardResponse>> doCardTransaction(@RequestBody CardRequest cardRequest){
        return cardService.doCardTransaction(cardRequest)
                .map(cardResponse -> ResponseEntity.status(HttpStatus.CREATED).body(cardResponse))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    public Map<String , String> createValidationErrorResponse(BindingResult bindingResult){
        Map<String,String> errors = new HashMap<>();
        for(FieldError fieldError : bindingResult.getFieldErrors()){
            System.out.println("===========================");
            System.out.println(bindingResult.getFieldErrors());
            errors.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return errors;
    }


}
