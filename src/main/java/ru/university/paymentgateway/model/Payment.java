package ru.university.paymentgateway.model;

import java.math.BigDecimal;

public class Payment {
    private final long id;
    private final BigDecimal amount;
    private final String currency;
    private String description;
    private final Merchant merchant;

    public Payment(long id, BigDecimal amount, String currency, String description, Merchant merchant) {
        if (id <= 0) {
            throw new InvalidPaymentException("ID платежа должен быть положительным.");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidPaymentException("Сумма платежа должна быть больше нуля.");
        }
        if (!"RUB".equals(currency)) {
            throw new InvalidPaymentException("Пока поддерживается только валюта RUB.");
        }
        if (merchant == null) {
            throw new InvalidPaymentException("У платежа должен быть магазин.");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidPaymentException("Описание платежа не может быть пустым.");
        }

        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.merchant = merchant;
        this.description = description;
    }

    public long get_id() {
        return id;
    }

    public BigDecimal get_amount() {
        return amount;
    }

    public String get_currency() {
        return currency;
    }

    public String get_description() {
        return description;
    }

    public void set_description(String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidPaymentException("Описание платежа не может быть пустым.");
        }
        this.description = description;
    }

    public Merchant get_merchant() {
        return merchant;
    }
}
