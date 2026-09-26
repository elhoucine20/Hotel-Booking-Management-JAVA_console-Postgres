package util.payment;

import model.enums.PaymentStatus;

import java.math.BigDecimal;

public interface PaymentStrategy {
    public PaymentStatus paye(BigDecimal amount);
}
