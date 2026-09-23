package ru.university.paymentgateway.console;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import ru.university.paymentgateway.database.DatabaseException;
import ru.university.paymentgateway.model.InvalidPaymentException;
import ru.university.paymentgateway.model.Merchant;
import ru.university.paymentgateway.model.Payment;
import ru.university.paymentgateway.service.PaymentService;
import ru.university.paymentgateway.service.RegistrationService;

public class ConsoleMenu {
    private final PaymentService payment_service;
    private final RegistrationService registration_service;
    private final Merchant merchant;
    private final Scanner scanner;
    private boolean input_finished;

    public ConsoleMenu(PaymentService payment_service, RegistrationService registration_service, Merchant merchant) {
        this.payment_service = payment_service;
        this.registration_service = registration_service;
        this.merchant = merchant;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Платёжный шлюз. Практика 3");

        while (!input_finished) {
            print_menu();
            String choice = read_line("Выберите пункт: ");
            if (choice == null) {
                break;
            }

            clear_console();
            try {
                switch (choice.trim()) {
                    case "1" -> show_payments(payment_service.list_payments());
                    case "2" -> show_merchant();
                    case "3" -> change_description_from_input();
                    case "4" -> create_payment_from_input();
                    case "5" -> find_payment_from_input();
                    case "6" -> filter_payments_from_input();
                    case "7" -> show_payments(payment_service.sort_payments_by_amount());
                    case "8" -> delete_payment_from_input();
                    case "9" -> register_merchant_with_payment_from_input();
                    case "0" -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    default -> System.out.println("Неизвестный пункт меню. Выберите число от 0 до 9.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите корректное число.");
            } catch (InvalidPaymentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (DatabaseException e) {
                System.out.println("Ошибка базы данных: " + e.getMessage());
            }
        }

        System.out.println("Ввод завершён. До свидания!");
    }

    private void print_menu() {
        System.out.println();
        System.out.println("1. Показать все платежи");
        System.out.println("2. Показать магазин");
        System.out.println("3. Изменить описание платежа");
        System.out.println("4. Добавить новый платёж");
        System.out.println("5. Найти платёж по ID");
        System.out.println("6. Отфильтровать по минимальной сумме");
        System.out.println("7. Отсортировать по сумме");
        System.out.println("8. Удалить платёж");
        System.out.println("9. Создать магазин с первым платежом (транзакция)");
        System.out.println("0. Выход");
    }

    private void show_payments(List<Payment> payments) {
        if (payments.isEmpty()) {
            System.out.println("Платежей нет.");
            return;
        }

        for (Payment payment : payments) {
            print_payment(payment);
        }
    }

    private void print_payment(Payment payment) {
        System.out.println("ID: " + payment.get_id()
                + ", сумма: " + payment.get_amount().toPlainString() + " " + payment.get_currency()
                + ", описание: " + payment.get_description()
                + ", магазин: " + payment.get_merchant().get_name());
    }

    private void show_merchant() {
        System.out.println("ID магазина: " + merchant.get_id());
        System.out.println("Название: " + merchant.get_name());
        System.out.println("Сайт: " + merchant.get_website());
    }

    private void change_description_from_input() {
        String id_text = read_line("ID платежа: ");
        if (id_text == null) {
            return;
        }
        String description = read_line("Новое описание: ");
        if (description == null) {
            return;
        }

        payment_service.change_payment_description(Long.parseLong(id_text.trim()), description);
        System.out.println("Описание платежа изменено.");
    }

    private void create_payment_from_input() {
        String id_text = read_line("ID платежа: ");
        if (id_text == null) {
            return;
        }
        String amount_text = read_line("Сумма в RUB: ");
        if (amount_text == null) {
            return;
        }
        String description = read_line("Описание: ");
        if (description == null) {
            return;
        }

        Payment payment = new Payment(
                Long.parseLong(id_text.trim()),
                new BigDecimal(amount_text.trim()),
                "RUB",
                description,
                merchant);
        payment_service.register_payment(payment);
        System.out.println("Платёж добавлен.");
    }

    private void find_payment_from_input() {
        String id_text = read_line("ID платежа: ");
        if (id_text == null) {
            return;
        }

        Payment payment = payment_service.find_payment_by_id(Long.parseLong(id_text.trim()));
        if (payment == null) {
            System.out.println("Платёж не найден.");
            return;
        }
        print_payment(payment);
    }

    private void filter_payments_from_input() {
        String amount_text = read_line("Минимальная сумма в RUB: ");
        if (amount_text == null) {
            return;
        }

        BigDecimal min_amount = new BigDecimal(amount_text.trim());
        show_payments(payment_service.filter_payments_by_min_amount(min_amount));
    }

    private void delete_payment_from_input() {
        String id_text = read_line("ID платежа: ");
        if (id_text == null) {
            return;
        }

        payment_service.delete_payment(Long.parseLong(id_text.trim()));
        System.out.println("Платёж удалён.");
    }

    private void register_merchant_with_payment_from_input() {
        String merchant_id_text = read_line("ID нового магазина: ");
        if (merchant_id_text == null) {
            return;
        }
        String merchant_name = read_line("Название магазина: ");
        if (merchant_name == null) {
            return;
        }
        String website = read_line("Сайт магазина: ");
        if (website == null) {
            return;
        }
        String payment_id_text = read_line("ID первого платежа: ");
        if (payment_id_text == null) {
            return;
        }
        String amount_text = read_line("Сумма в RUB: ");
        if (amount_text == null) {
            return;
        }
        String description = read_line("Описание платежа: ");
        if (description == null) {
            return;
        }

        Merchant new_merchant = new Merchant(
                Long.parseLong(merchant_id_text.trim()), merchant_name, website);
        Payment first_payment = new Payment(
                Long.parseLong(payment_id_text.trim()),
                new BigDecimal(amount_text.trim()),
                "RUB",
                description,
                new_merchant);
        registration_service.register_merchant_with_payment(new_merchant, first_payment);
        System.out.println("Магазин и первый платёж сохранены одной транзакцией.");
    }

    private String read_line(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            input_finished = true;
            return null;
        }
        return scanner.nextLine();
    }

    private void clear_console() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
