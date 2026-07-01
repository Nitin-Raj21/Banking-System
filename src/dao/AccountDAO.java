package dao;

import db.DBConnection;
import model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountDAO {

    public Account create(int customerId, String accountType) throws SQLException {
        String sql = "INSERT INTO accounts (customer_id, account_type, balance) VALUES (?, ?, 0.00)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, customerId);
            ps.setString(2, accountType);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return findById(keys.getInt(1)).orElseThrow();
            }
            throw new SQLException("Failed to retrieve generated account ID.");
        }
    }

    public Optional<Account> findById(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
        }
        return Optional.empty();
    }

    public List<Account> findByCustomerId(int customerId) throws SQLException {
        List<Account> list = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        }
        return list;
    }

    public void updateBalance(Connection conn, int accountId, java.math.BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newBalance);
            ps.setInt(2, accountId);
            ps.executeUpdate();
        }
    }

    public void deactivate(int accountId) throws SQLException {
        String sql = "UPDATE accounts SET is_active = FALSE WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountId);
            ps.executeUpdate();
        }
    }

    private Account map(ResultSet rs) throws SQLException {
        return new Account(
                rs.getInt("account_id"),
                rs.getInt("customer_id"),
                rs.getString("account_type"),
                rs.getBigDecimal("balance"),
                rs.getBoolean("is_active"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
