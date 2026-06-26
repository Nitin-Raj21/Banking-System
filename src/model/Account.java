package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {
    private int          accountId;
    private int          customerId;
    private String       accountType;
    private BigDecimal   balance;
    private boolean      isActive;
    private LocalDateTime createdAt;

    public Account() {}

    public Account(int accountId, int customerId, String accountType,
                   BigDecimal balance, boolean isActive, LocalDateTime createdAt) {
        this.accountId   = accountId;
        this.customerId  = customerId;
        this.accountType = accountType;
        this.balance     = balance;
        this.isActive    = isActive;
        this.createdAt   = createdAt;
    }

    public int          getAccountId()   { return accountId; }
    public int          getCustomerId()  { return customerId; }
    public String       getAccountType() { return accountType; }
    public BigDecimal   getBalance()     { return balance; }
    public boolean      isActive()       { return isActive; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    public void setAccountId(int accountId)         { this.accountId = accountId; }
    public void setCustomerId(int customerId)       { this.customerId = customerId; }
    public void setAccountType(String accountType)  { this.accountType = accountType; }
    public void setBalance(BigDecimal balance)      { this.balance = balance; }
    public void setActive(boolean active)           { this.isActive = active; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Account #%d | Type: %-8s | Balance: ₹%,.2f | Active: %s",
                accountId, accountType, balance, isActive ? "Yes" : "No");
    }
}
