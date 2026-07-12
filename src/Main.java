import model.Account;
import model.Customer;
import model.Transaction;
import service.BankingService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final BankingService bank    = new BankingService();
    private static final Scanner        scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║     JAVA BANKING SYSTEM      ║");
        System.out.println("╚══════════════════════════════╝");

        while (true) {
            printMainMenu();
            int choice = readInt("Enter choice: ");
            switch (choice) {
                case 1  -> createCustomer();
                case 2  -> listCustomers();
                case 3  -> openAccount();
                case 4  -> viewAccounts();
                case 5  -> deposit();
                case 6  -> withdraw();
                case 7  -> transfer();
                case 8  -> viewHistory();
                case 9  -> viewMonthlySummary();
                case 0  -> { System.out.println("Goodbye!"); return; }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n──────────────────────────────");
        System.out.println("  1. Create Customer");
        System.out.println("  2. List All Customers");
        System.out.println("  3. Open Account");
        System.out.println("  4. View Customer Accounts");
        System.out.println("  5. Deposit");
        System.out.println("  6. Withdraw");
        System.out.println("  7. Transfer");
        System.out.println("  8. Transaction History");
        System.out.println("  9. Monthly Summary");
        System.out.println("  0. Exit");
        System.out.println("──────────────────────────────");
    }

    private static void createCustomer() {
        System.out.println("\n--- Create Customer ---");
        System.out.print("Full Name : "); String name    = scanner.nextLine().trim();
        System.out.print("Email     : "); String email   = scanner.nextLine().trim();
        System.out.print("Phone     : "); String phone   = scanner.nextLine().trim();
        System.out.print("Address   : "); String address = scanner.nextLine().trim();
        try {
            Customer c = bank.createCustomer(name, email, phone, address);
            System.out.println("✔ Customer created: " + c);
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private static void listCustomers() {
        System.out.println("\n--- All Customers ---");
        try {
            List<Customer> list = bank.getAllCustomers();
            if (list.isEmpty()) { System.out.println("No customers found."); return; }
            list.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("✘ DB Error: " + e.getMessage());
        }
    }

    private static void openAccount() {
        System.out.println("\n--- Open Account ---");
        int customerId = readInt("Customer ID: ");
        System.out.print("Account type (SAVINGS / CURRENT): ");
        String type = scanner.nextLine().trim().toUpperCase();
        if (!type.equals("SAVINGS") && !type.equals("CURRENT")) {
            System.out.println("✘ Invalid account type.");
            return;
        }
        try {
            Account a = bank.openAccount(customerId, type);
            System.out.println("✔ Account opened: " + a);
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private static void viewAccounts() {
        System.out.println("\n--- Customer Accounts ---");
        int customerId = readInt("Customer ID: ");
        try {
            Optional<Customer> c = bank.getCustomer(customerId);
            c.ifPresent(cust -> System.out.println("Customer: " + cust));
            List<Account> accounts = bank.getAccountsByCustomer(customerId);
            if (accounts.isEmpty()) { System.out.println("No accounts found."); return; }
            accounts.forEach(System.out::println);
        } catch (SQLException e) {
            System.out.println("✘ DB Error: " + e.getMessage());
        }
    }

    private static void deposit() {
        System.out.println("\n--- Deposit ---");
        int        accountId = readInt("Account ID : ");
        BigDecimal amount    = readAmount("Amount (₹) : ");
        try {
            bank.deposit(accountId, amount);
            System.out.println("✔ Deposit successful.");
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private static void withdraw() {
        System.out.println("\n--- Withdraw ---");
        int        accountId = readInt("Account ID : ");
        BigDecimal amount    = readAmount("Amount (₹) : ");
        try {
            bank.withdraw(accountId, amount);
            System.out.println("✔ Withdrawal successful.");
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private static void transfer() {
        System.out.println("\n--- Transfer ---");
        int        from   = readInt("From Account ID : ");
        int        to     = readInt("To   Account ID : ");
        BigDecimal amount = readAmount("Amount (₹)      : ");
        try {
            bank.transfer(from, to, amount);
            System.out.println("✔ Transfer successful.");
        } catch (Exception e) {
            System.out.println("✘ Error: " + e.getMessage());
        }
    }

    private static void viewHistory() {
        System.out.println("\n--- Transaction History (last 20) ---");
        int accountId = readInt("Account ID: ");
        try {
            List<Transaction> txns = bank.getHistory(accountId);
            if (txns.isEmpty()) { System.out.println("No transactions found."); return; }
            txns.forEach(t -> System.out.println("  " + t));
        } catch (SQLException e) {
            System.out.println("✘ DB Error: " + e.getMessage());
        }
    }

    private static void viewMonthlySummary() {
        System.out.println("\n--- Monthly Summary ---");
        int accountId = readInt("Account ID: ");
        try {
            bank.printMonthlySummary(accountId);
        } catch (SQLException e) {
            System.out.println("✘ DB Error: " + e.getMessage());
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number.");
            }
        }
    }

    private static BigDecimal readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                BigDecimal val = new BigDecimal(scanner.nextLine().trim());
                if (val.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid positive amount.");
            }
        }
    }
}
