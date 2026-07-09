package dao;

import db.DBConnection;
import model.Transaction;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void insert(Connection conn, int accountId, String type,
                       BigDecimal amount, BigDecimal balanceAfter, String description) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, type, amount, balance_after, description) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.setString(2, type);
            ps.setBigDecimal(3, amount);
            ps.setBigDecimal(4, balanceAfter);
            ps.setString(5, description);
            ps.executeUpdate();
        }
    }

    public List<Transaction> findByAccountId(int accountId) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY created_at DESC LIMIT 20";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void printMonthlySummary(int accountId) throws SQLException {
        String sql = """
                SELECT
                    DATE_FORMAT(created_at, '%Y-%m') AS month,
                    SUM(CASE WHEN type IN ('DEPOSIT','TRANSFER_IN')  THEN amount ELSE 0 END) AS total_in,
                    SUM(CASE WHEN type IN ('WITHDRAWAL','TRANSFER_OUT') THEN amount ELSE 0 END) AS total_out,
                    COUNT(*) AS txn_count
                FROM transactions
                WHERE account_id = ?
                GROUP BY month
                ORDER BY month DESC
                """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            System.out.println("\n  Month       | Money In       | Money Out      | Txns");
            System.out.println("  ------------|----------------|----------------|-----");
            while (rs.next()) {
                System.out.printf("  %-12s | ₹%,12.2f | ₹%,12.2f | %d%n",
                        rs.getString("month"),
                        rs.getBigDecimal("total_in"),
                        rs.getBigDecimal("total_out"),
                        rs.getInt("txn_count"));
            }
        }
    }

    private Transaction map(ResultSet rs) throws SQLException {
        return new Transaction(
                rs.getInt("transaction_id"),
                rs.getInt("account_id"),
                rs.getString("type"),
                rs.getBigDecimal("amount"),
                rs.getBigDecimal("balance_after"),
                rs.getString("description"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
