package repository.impl;

import model.Payment;
import model.User;

import java.util.List;

public interface PaymentRepository {
    public boolean save(Payment payment);
    public List<Payment> findByUserId(User user);
}
