package com.transaction.transactionManager.repository;

import com.transaction.transactionManager.entity.TransactionHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Divakar Verma
 * @created_at : 12/02/2024 - 4:59 pm
 * @mail_to: vermadivakar2022@gmail.com
 */
@Repository
public interface TransactionHeaderRepository extends JpaRepository<TransactionHeader,String> {
}
