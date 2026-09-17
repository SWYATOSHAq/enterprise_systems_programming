package ru.university.paymentgateway.console;

import java.util.Scanner;
import ru.university.paymentgateway.model.Merchant;
import ru.university.paymentgateway.model.Payment;
import ru.university.paymentgateway.service.PaymentService;

public class ConsoleMenu {
    private final PaymentService paymentService;
    private final Scanner scanner;

    public ConsoleMenu(PaymentService paymentService) {
        this.paymentService = paymentService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Платёжный шлюз. Практика 1");

        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) {
                System.out.println("\nВвод завершён. До свидания!");
                return;
            }

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    showPayment();
                    break;
                case "2":
                    showMerchant();
                    break;
                case "3":
                    changeDescription();
                    break;
                case "0":
                    System.out.println("До свидания!");
                    return;
                default:
                    System.out.println("Неизвестный пункт меню. Выберите 0, 1, 2 или 3.");
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Посмотреть платёж");
        System.out.println("2. Посмотреть магазин");
        System.out.println("3. Изменить описание платежа");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private void showPayment() {
        Payment payment = paymentService.getPayment();
        System.out.println("Платёж: " + payment.getId());
        System.out.println("Сумма: " + payment.getAmount().toPlainString() + " " + payment.getCurrency());
        System.out.println("Описание: " + payment.getDescription());
        System.out.println("Магазин: " + payment.getMerchant().getName());
    }

    private void showMerchant() {
        Merchant merchant = paymentService.getPayment().getMerchant();
        System.out.println("Магазин: " + merchant.getId());
        System.out.println("Название: " + merchant.getName());
        System.out.println("Сайт: " + merchant.getWebsite());
    }

    private void changeDescription() {
        System.out.print("Новое описание: ");
        if (!scanner.hasNextLine()) {
            System.out.println("\nОписание не изменено: ввод завершён.");
            return;
        }

        paymentService.updateDescription(scanner.nextLine());
        System.out.println("Описание платежа изменено.");
    }
}
