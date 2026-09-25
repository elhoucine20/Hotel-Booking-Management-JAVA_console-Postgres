package util.payment;

import java.math.BigDecimal;

public interface PaymentStrategy {
    public void paye(BigDecimal amount);
}
