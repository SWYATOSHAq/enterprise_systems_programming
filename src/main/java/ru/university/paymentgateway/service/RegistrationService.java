package ru.university.paymentgateway.service;

import java.sql.Connection;
import java.sql.SQLException;
import ru.university.paymentgateway.dao.MerchantDao;
import ru.university.paymentgateway.dao.PaymentDao;
import ru.university.paymentgateway.database.DatabaseConfig;
import ru.university.paymentgateway.database.DatabaseException;
import ru.university.paymentgateway.model.Merchant;
import ru.university.paymentgateway.model.Payment;

public class RegistrationService {
    private final DatabaseConfig database_config;
    private final MerchantDao merchant_dao;
    private final PaymentDao payment_dao;

    public RegistrationService(DatabaseConfig database_config, MerchantDao merchant_dao, PaymentDao payment_dao) {
        this.database_config = database_config;
        this.merchant_dao = merchant_dao;
        this.payment_dao = payment_dao;
    }

    public void register_merchant_with_payment(Merchant merchant, Payment payment) {
        try (Connection connection = database_config.get_connection()) {
            connection.setAutoCommit(false);
            try {
                merchant_dao.insert(connection, merchant);
                payment_dao.insert(connection, payment);
                connection.commit();
            } catch (RuntimeException | SQLException e) {
                rollback(connection);
                if (e instanceof RuntimeException runtime_exception) {
                    throw runtime_exception;
                }
                throw new DatabaseException("Не удалось завершить транзакцию.", e);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось выполнить транзакцию.", e);
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DatabaseException("Не удалось отменить транзакцию.", e);
        }
    }
}
