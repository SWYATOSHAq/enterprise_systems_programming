package ru.university.paymentgateway;

import java.math.BigDecimal;
import ru.university.paymentgateway.console.ConsoleMenu;
import ru.university.paymentgateway.model.Merchant;
import ru.university.paymentgateway.model.Payment;
import ru.university.paymentgateway.service.PaymentService;

public class Main {
    public static void main(String[] args) {
        Merchant merchant = new Merchant(1L, "Учебный магазин", "https://shop.example");
        Payment payment = new Payment(
                1001L,
                new BigDecimal("1490.00"),
                "RUB",
                "Оплата заказа 1001",
                merchant
        );

        PaymentService paymentService = new PaymentService(payment);
        ConsoleMenu menu = new ConsoleMenu(paymentService);
        menu.run();
    }
}
