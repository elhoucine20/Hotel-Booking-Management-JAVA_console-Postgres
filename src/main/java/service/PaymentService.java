package service;

import model.Payment;
import model.User;
import repository.JdbcPaymentRepository;
import repository.impl.PaymentRepository;

import java.util.List;

public class PaymentService {
    PaymentRepository paymentRepository = new JdbcPaymentRepository();
    public List<Payment> paymentsUserService(User user){
       return paymentRepository.findByUserId(user);
    }
}
