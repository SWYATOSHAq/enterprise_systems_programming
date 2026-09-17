package ru.university.paymentgateway.service;

import ru.university.paymentgateway.model.Payment;

public class PaymentService {
    private final Payment payment;

    public PaymentService(Payment payment) {
        this.payment = payment;
    }

    public Payment getPayment() {
        return payment;
    }

    public void updateDescription(String description) {
        payment.setDescription(description);
    }
}
