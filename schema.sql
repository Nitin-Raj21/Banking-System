-- ============================================================
-- BANKING SYSTEM - MySQL Schema
-- ============================================================

CREATE DATABASE IF NOT EXISTS banking_system;
USE banking_system;

-- ------------------------------------------------------------
-- CUSTOMERS
-- ------------------------------------------------------------
CREATE TABLE customers (
    customer_id   INT AUTO_INCREMENT PRIMARY KEY,
    full_name     VARCHAR(100) NOT NULL,
    email         VARCHAR(100) UNIQUE NOT NULL,
    phone         VARCHAR(15),
    address       VARCHAR(255),
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- ACCOUNTS
-- ------------------------------------------------------------
CREATE TABLE accounts (
    account_id     INT AUTO_INCREMENT PRIMARY KEY,
    customer_id    INT NOT NULL,
    account_type   ENUM('SAVINGS', 'CURRENT') NOT NULL DEFAULT 'SAVINGS',
    balance        DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    is_active      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    CONSTRAINT chk_balance CHECK (balance >= 0)
);

-- ------------------------------------------------------------
-- TRANSACTIONS
-- ------------------------------------------------------------
CREATE TABLE transactions (
    transaction_id   INT AUTO_INCREMENT PRIMARY KEY,
    account_id       INT NOT NULL,
    type             ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT') NOT NULL,
    amount           DECIMAL(15, 2) NOT NULL,
    balance_after    DECIMAL(15, 2) NOT NULL,
    description      VARCHAR(255),
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(account_id),
    CONSTRAINT chk_amount CHECK (amount > 0)
);

-- ------------------------------------------------------------
-- SEED DATA (for testing)
-- ------------------------------------------------------------
INSERT INTO customers (full_name, email, phone) VALUES
    ('Arjun Sharma',  'arjun@example.com',  '9876543210'),
    ('Priya Mehta',   'priya@example.com',  '9123456789');

INSERT INTO accounts (customer_id, account_type, balance) VALUES
    (1, 'SAVINGS', 10000.00),
    (2, 'CURRENT', 25000.00);
