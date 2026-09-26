package util.payment;

import model.enums.PaymentStatus;

import java.math.BigDecimal;

public class PaymentCash implements PaymentStrategy{

    @Override
    public PaymentStatus paye(BigDecimal amount) {
        System.out.println("Paiement cash enregistre avec succes : " + amount);
        return PaymentStatus.PENDING;
    }
}
