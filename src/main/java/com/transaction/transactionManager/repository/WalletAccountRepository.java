package com.transaction.transactionManager.repository;

import com.transaction.transactionManager.entity.WalletAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Divakar Verma
 * @created_at : 13/02/2024 - 4:23 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Repository
public interface WalletAccountRepository extends JpaRepository<WalletAccount,String> {
    public WalletAccount findByUsername(String username);

    public WalletAccount findByVpa(String vpa);
    public WalletAccount findByAccountNumber(String accountNumber);

    public WalletAccount findByWalletId(String walletId);

    @Query("select w from WalletAccount w where w.balance > ?1")
    public List<WalletAccount> findWalletAccountsByBalance(double balance);



}
