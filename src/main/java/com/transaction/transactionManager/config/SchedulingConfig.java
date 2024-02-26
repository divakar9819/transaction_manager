package com.transaction.transactionManager.config;

import com.transaction.transactionManager.transactionService.WalletAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Divakar Verma
 * @created_at : 20/02/2024 - 6:22 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private WalletAccountService walletAccountService;


    @Scheduled(fixedRate = 6000000)
    public void sendNotificationToWalletAccount(){
        double amount = 30;
        System.out.println("****************************************************************");
        walletAccountService.findAllWalletAccountsByBalance(amount)
                .doOnError(error -> {
                    System.out.println(error.getMessage());
                })
                .subscribe(System.out::println);
    }


}
