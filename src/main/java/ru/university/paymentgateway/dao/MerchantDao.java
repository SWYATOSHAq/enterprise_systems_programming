package ru.university.paymentgateway.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ru.university.paymentgateway.database.DatabaseConfig;
import ru.university.paymentgateway.database.DatabaseException;
import ru.university.paymentgateway.model.Merchant;

public class MerchantDao {
    private final DatabaseConfig database_config;

    public MerchantDao(DatabaseConfig database_config) {
        this.database_config = database_config;
    }

    public Merchant find_by_id(long id) {
        String sql = "SELECT id, name, website FROM merchants WHERE id = ?";

        try (Connection connection = database_config.get_connection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Merchant(
                            result.getLong("id"),
                            result.getString("name"),
                            result.getString("website"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось прочитать магазин.", e);
        }
    }

    public void insert(Connection connection, Merchant merchant) {
        String sql = "INSERT INTO merchants (id, name, website) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, merchant.get_id());
            statement.setString(2, merchant.get_name());
            statement.setString(3, merchant.get_website());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось добавить магазин.", e);
        }
    }
}
