package ru.university.paymentgateway.service;

import java.math.BigDecimal;
import java.util.List;
import ru.university.paymentgateway.dao.PaymentDao;
import ru.university.paymentgateway.model.InvalidPaymentException;
import ru.university.paymentgateway.model.Payment;

public class PaymentService {
    private final PaymentDao payment_dao;

    public PaymentService(PaymentDao payment_dao) {
        this.payment_dao = payment_dao;
    }

    public void register_payment(Payment payment) {
        if (payment == null) {
            throw new InvalidPaymentException("Нельзя добавить пустой платёж.");
        }
        if (find_payment_by_id(payment.get_id()) != null) {
            throw new InvalidPaymentException("Платёж с таким ID уже существует.");
        }
        payment_dao.insert(payment);
    }

    public List<Payment> list_payments() {
        return payment_dao.find_all();
    }

    public Payment find_payment_by_id(long id) {
        return payment_dao.find_by_id(id);
    }

    public List<Payment> filter_payments_by_min_amount(BigDecimal min_amount) {
        if (min_amount == null || min_amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidPaymentException("Минимальная сумма не может быть отрицательной.");
        }
        return payment_dao.find_by_min_amount(min_amount);
    }

    public List<Payment> sort_payments_by_amount() {
        return payment_dao.find_all_sorted_by_amount();
    }

    public void change_payment_description(long id, String description) {
        if (description == null || description.isBlank()) {
            throw new InvalidPaymentException("Описание платежа не может быть пустым.");
        }
        if (!payment_dao.update_description(id, description)) {
            throw new InvalidPaymentException("Платёж с таким ID не найден.");
        }
    }

    public void delete_payment(long id) {
        if (!payment_dao.delete_by_id(id)) {
            throw new InvalidPaymentException("Платёж с таким ID не найден.");
        }
    }
}
