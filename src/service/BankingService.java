package service;

import dao.AccountDAO;
import dao.CustomerDAO;
import dao.TransactionDAO;
import db.DBConnection;
import model.Account;
import model.Customer;
import model.Transaction;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class BankingService {

    private final CustomerDAO    customerDAO    = new CustomerDAO();
    private final AccountDAO     accountDAO     = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();

    public Customer createCustomer(String name, String email, String phone, String address) throws SQLException {
        return customerDAO.create(name, email, phone, address);
    }

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.findAll();
    }

    public Optional<Customer> getCustomer(int id) throws SQLException {
        return customerDAO.findById(id);
    }

    public Account openAccount(int customerId, String type) throws SQLException {
        customerDAO.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + customerId));
        return accountDAO.create(customerId, type);
    }

    public List<Account> getAccountsByCustomer(int customerId) throws SQLException {
        return accountDAO.findByCustomerId(customerId);
    }

    public void deposit(int accountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Deposit amount must be positive.");

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Account account = accountDAO.findById(accountId)
                        .orElseThrow(() -> new IllegalArgumentException("Account not found."));
                if (!account.isActive()) throw new IllegalStateException("Account is inactive.");

                BigDecimal newBalance = account.getBalance().add(amount);
                accountDAO.updateBalance(conn, accountId, newBalance);
                transactionDAO.insert(conn, accountId, "DEPOSIT", amount, newBalance, "Cash deposit");

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void withdraw(int accountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Withdrawal amount must be positive.");

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Account account = accountDAO.findById(accountId)
                        .orElseThrow(() -> new IllegalArgumentException("Account not found."));
                if (!account.isActive()) throw new IllegalStateException("Account is inactive.");
                if (account.getBalance().compareTo(amount) < 0)
                    throw new IllegalStateException("Insufficient funds. Balance: ₹" + account.getBalance());

                BigDecimal newBalance = account.getBalance().subtract(amount);
                accountDAO.updateBalance(conn, accountId, newBalance);
                transactionDAO.insert(conn, accountId, "WITHDRAWAL", amount, newBalance, "Cash withdrawal");

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void transfer(int fromAccountId, int toAccountId, BigDecimal amount) throws SQLException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Transfer amount must be positive.");
        if (fromAccountId == toAccountId)
            throw new IllegalArgumentException("Cannot transfer to the same account.");

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Account from = accountDAO.findById(fromAccountId)
                        .orElseThrow(() -> new IllegalArgumentException("Source account not found."));
                Account to = accountDAO.findById(toAccountId)
                        .orElseThrow(() -> new IllegalArgumentException("Destination account not found."));

                if (!from.isActive()) throw new IllegalStateException("Source account is inactive.");
                if (!to.isActive())   throw new IllegalStateException("Destination account is inactive.");
                if (from.getBalance().compareTo(amount) < 0)
                    throw new IllegalStateException("Insufficient funds in source account.");

                BigDecimal fromNew = from.getBalance().subtract(amount);
                BigDecimal toNew   = to.getBalance().add(amount);

                accountDAO.updateBalance(conn, fromAccountId, fromNew);
                accountDAO.updateBalance(conn, toAccountId,   toNew);

                transactionDAO.insert(conn, fromAccountId, "TRANSFER_OUT", amount, fromNew,
                        "Transfer to Acc#" + toAccountId);
                transactionDAO.insert(conn, toAccountId, "TRANSFER_IN", amount, toNew,
                        "Transfer from Acc#" + fromAccountId);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public List<Transaction> getHistory(int accountId) throws SQLException {
        return transactionDAO.findByAccountId(accountId);
    }

    public void printMonthlySummary(int accountId) throws SQLException {
        transactionDAO.printMonthlySummary(accountId);
    }
}
