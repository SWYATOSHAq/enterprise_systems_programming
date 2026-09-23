package ru.university.paymentgateway.model;

public class Merchant {
    private final long id;
    private String name;
    private String website;

    public Merchant(long id, String name, String website) {
        if (id <= 0) {
            throw new InvalidPaymentException("ID магазина должен быть положительным.");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidPaymentException("Название магазина не может быть пустым.");
        }
        if (website == null || website.isBlank()) {
            throw new InvalidPaymentException("Сайт магазина не может быть пустым.");
        }
        this.id = id;
        this.name = name;
        this.website = website;
    }

    public long get_id() {
        return id;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_website() {
        return website;
    }

    public void set_website(String website) {
        this.website = website;
    }
}
