package model;

import model.enums.PaymentMethod;
import model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {

    private UUID id;
    private UUID reservation_id;
    private BigDecimal amount;
    private PaymentMethod payment_method;
    private PaymentStatus paymentStatus;
    private LocalDateTime payment_date;

    public Payment(UUID id, UUID reservation_id, BigDecimal amount, PaymentMethod payment_method,PaymentStatus paymentStatus,LocalDateTime date) {
        this.id = id;
        this.reservation_id = reservation_id;
        this.amount = amount;
        this.payment_method = payment_method;
        this.paymentStatus = paymentStatus;
        this.payment_date = date;
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

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(LocalDateTime payment_date) {
        this.payment_date = payment_date;
    }
}
