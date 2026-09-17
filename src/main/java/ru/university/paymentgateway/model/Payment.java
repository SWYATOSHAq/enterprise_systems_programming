package ru.university.paymentgateway.model;

import java.math.BigDecimal;

public class Payment {
    private final long id;
    private final BigDecimal amount;
    private final String currency;
    private String description;
    private final Merchant merchant;

    public Payment(long id, BigDecimal amount, String currency, String description, Merchant merchant) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.merchant = merchant;
    }

    public long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Merchant getMerchant() {
        return merchant;
    }
}
