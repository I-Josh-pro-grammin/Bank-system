package com.bank.system.util;

import com.bank.system.exception.InvalidTransactionException;
import com.bank.system.model.Transaction;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransactionLoader {
    private static final String TRANSACTION_PATTERN = "^(TX\\d+),(ACC\\d+),(DEPOSIT|WITHDRAWAL),(\\d+(\\.\\d{1,2})?)$";
    private static final Pattern pattern = Pattern.compile(TRANSACTION_PATTERN);

    public List<Transaction> loadTransactions(File file) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    transactions.add(parseLine(line));
                } catch (InvalidTransactionException e) {
                    System.err.println("Error in file " + file.getName() + " at line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        return transactions;
    }

    private Transaction parseLine(String line) throws InvalidTransactionException {
        Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            throw new InvalidTransactionException("Invalid format: " + line);
        }

        String txId = matcher.group(1);
        String accId = matcher.group(2);
        String typeStr = matcher.group(3);
        String amountStr = matcher.group(4);

        Transaction.Type type = Transaction.Type.valueOf(typeStr);
        double amount = Double.parseDouble(amountStr);

        return new Transaction(txId, accId, type, amount);
    }
}
