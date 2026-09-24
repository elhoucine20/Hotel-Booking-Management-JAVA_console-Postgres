package model;

import model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public class Payment {

    private UUID id;
    private UUID reservation_id;
    private BigDecimal amount;
    private PaymentMethod payment_method;

    public Payment(UUID id, UUID reservation_id, BigDecimal amount, PaymentMethod payment_method) {
        this.id = id;
        this.reservation_id = reservation_id;
        this.amount = amount;
        this.payment_method = payment_method;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(UUID reservation_id) {
        this.reservation_id = reservation_id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(PaymentMethod payment_method) {
        this.payment_method = payment_method;
    }
}
