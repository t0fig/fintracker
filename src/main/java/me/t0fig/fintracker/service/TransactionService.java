package me.t0fig.fintracker.service;

import me.t0fig.fintracker.model.Transaction;
import me.t0fig.fintracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction get(long id) {
        return transactionRepository.findById(id).orElseThrow();
    }

    public void add(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public void update(long id, Transaction transaction) {
        Transaction oldTransaction = get(id);
        oldTransaction.setCategory(transaction.getCategory());
        oldTransaction.setAmount(transaction.getAmount());
        transactionRepository.save(oldTransaction);
    }
}
