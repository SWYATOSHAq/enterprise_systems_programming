package ru.university.paymentgateway;

import ru.university.paymentgateway.console.ConsoleMenu;
import ru.university.paymentgateway.dao.MerchantDao;
import ru.university.paymentgateway.dao.PaymentDao;
import ru.university.paymentgateway.database.DatabaseConfig;
import ru.university.paymentgateway.database.DatabaseException;
import ru.university.paymentgateway.model.Merchant;
import ru.university.paymentgateway.service.PaymentService;
import ru.university.paymentgateway.service.RegistrationService;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConfig database_config = new DatabaseConfig();
            MerchantDao merchant_dao = new MerchantDao(database_config);
            PaymentDao payment_dao = new PaymentDao(database_config);

            Merchant merchant = merchant_dao.find_by_id(1L);
            if (merchant == null) {
                throw new DatabaseException("Учебный магазин с ID 1 не найден в базе.");
            }

            PaymentService payment_service = new PaymentService(payment_dao);
            RegistrationService registration_service = new RegistrationService(
                    database_config, merchant_dao, payment_dao);
            ConsoleMenu console_menu = new ConsoleMenu(payment_service, registration_service, merchant);
            console_menu.run();
        } catch (DatabaseException e) {
            System.out.println("Ошибка базы данных: " + e.getMessage());
        }
    }

}
