package com.bank.system.processor;

import com.bank.system.exception.InsufficientFundsException;
import com.bank.system.model.Transaction;
import com.bank.system.service.BankService;
import com.bank.system.util.TransactionLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TransactionProcessor implements Runnable {
    private File transactionFile;
    private BankService bankService;
    private TransactionLoader transactionLoader;

    public TransactionProcessor(File transactionFile, BankService bankService) {
        this.transactionFile = transactionFile;
        this.bankService = bankService;
        this.transactionLoader = new TransactionLoader();
    }

    @Override
    public void run() {
        System.out.println("Starting processing for file: " + transactionFile.getName());
        try {
            List<Transaction> transactions = transactionLoader.loadTransactions(transactionFile);
            for (Transaction tx : transactions) {
                try {
                    bankService.processTransaction(tx);
                    // Simulate some processing time
                    Thread.sleep(10); 
                } catch (InsufficientFundsException e) {
                    System.err.println("Transaction Failed: " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Processing interrupted for file: " + transactionFile.getName());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + transactionFile.getName());
            e.printStackTrace();
        }
        System.out.println("Finished processing for file: " + transactionFile.getName());
    }
}
