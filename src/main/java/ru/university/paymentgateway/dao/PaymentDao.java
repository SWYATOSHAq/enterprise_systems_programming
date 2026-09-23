package ru.university.paymentgateway.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import ru.university.paymentgateway.database.DatabaseConfig;
import ru.university.paymentgateway.database.DatabaseException;
import ru.university.paymentgateway.model.Merchant;
import ru.university.paymentgateway.model.Payment;

public class PaymentDao {
    private static final String SELECT_PAYMENT = """
            SELECT p.id, p.amount, p.currency, p.description,
                   m.id AS merchant_id, m.name AS merchant_name, m.website AS merchant_website
            FROM payments p
            JOIN merchants m ON m.id = p.merchant_id
            """;

    private final DatabaseConfig database_config;

    public PaymentDao(DatabaseConfig database_config) {
        this.database_config = database_config;
    }

    public void insert(Payment payment) {
        try (Connection connection = database_config.get_connection()) {
            insert(connection, payment);
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось добавить платёж.", e);
        }
    }

    public void insert(Connection connection, Payment payment) {
        String sql = """
                INSERT INTO payments (id, amount, currency, description, merchant_id)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, payment.get_id());
            statement.setBigDecimal(2, payment.get_amount());
            statement.setString(3, payment.get_currency());
            statement.setString(4, payment.get_description());
            statement.setLong(5, payment.get_merchant().get_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось добавить платёж.", e);
        }
    }

    public List<Payment> find_all() {
        return find_list(SELECT_PAYMENT + " ORDER BY p.id", null);
    }

    public Payment find_by_id(long id) {
        String sql = SELECT_PAYMENT + " WHERE p.id = ?";

        try (Connection connection = database_config.get_connection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return map_payment(result);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось найти платёж.", e);
        }
    }

    public List<Payment> find_by_min_amount(BigDecimal min_amount) {
        return find_list(SELECT_PAYMENT + " WHERE p.amount >= ? ORDER BY p.id", min_amount);
    }

    public List<Payment> find_all_sorted_by_amount() {
        return find_list(SELECT_PAYMENT + " ORDER BY p.amount, p.id", null);
    }

    public boolean update_description(long id, String description) {
        String sql = "UPDATE payments SET description = ? WHERE id = ?";

        try (Connection connection = database_config.get_connection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, description);
            statement.setLong(2, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось изменить платёж.", e);
        }
    }

    public boolean delete_by_id(long id) {
        String sql = "DELETE FROM payments WHERE id = ?";

        try (Connection connection = database_config.get_connection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось удалить платёж.", e);
        }
    }

    private List<Payment> find_list(String sql, BigDecimal min_amount) {
        List<Payment> payments = new ArrayList<>();

        try (Connection connection = database_config.get_connection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            if (min_amount != null) {
                statement.setBigDecimal(1, min_amount);
            }
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    payments.add(map_payment(result));
                }
            }
            return payments;
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось прочитать платежи.", e);
        }
    }

    private Payment map_payment(ResultSet result) throws SQLException {
        Merchant merchant = new Merchant(
                result.getLong("merchant_id"),
                result.getString("merchant_name"),
                result.getString("merchant_website"));
        return new Payment(
                result.getLong("id"),
                result.getBigDecimal("amount"),
                result.getString("currency").trim(),
                result.getString("description"),
                merchant);
    }
}
