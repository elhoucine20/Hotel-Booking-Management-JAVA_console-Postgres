package util.payment;

import model.enums.PaymentStatus;

import java.math.BigDecimal;

public class PaymentPaypal implements PaymentStrategy{

    @Override
    public PaymentStatus paye(BigDecimal amount) {
        System.out.println("Paiement PayPal effectue avec succes : " + amount);
        return PaymentStatus.PAID;
    }
}
