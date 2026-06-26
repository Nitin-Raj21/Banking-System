package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int           transactionId;
    private int           accountId;
    private String        type;
    private BigDecimal    amount;
    private BigDecimal    balanceAfter;
    private String        description;
    private LocalDateTime createdAt;

    public Transaction() {}

    public Transaction(int transactionId, int accountId, String type,
                       BigDecimal amount, BigDecimal balanceAfter,
                       String description, LocalDateTime createdAt) {
        this.transactionId = transactionId;
        this.accountId     = accountId;
        this.type          = type;
        this.amount        = amount;
        this.balanceAfter  = balanceAfter;
        this.description   = description;
        this.createdAt     = createdAt;
    }

    public int           getTransactionId() { return transactionId; }
    public int           getAccountId()     { return accountId; }
    public String        getType()          { return type; }
    public BigDecimal    getAmount()        { return amount; }
    public BigDecimal    getBalanceAfter()  { return balanceAfter; }
    public String        getDescription()   { return description; }
    public LocalDateTime getCreatedAt()     { return createdAt; }

    @Override
    public String toString() {
        return String.format("%-12s | ₹%,10.2f | Balance after: ₹%,10.2f | %s | %s",
                type, amount, balanceAfter,
                description != null ? description : "-",
                createdAt);
    }
}
