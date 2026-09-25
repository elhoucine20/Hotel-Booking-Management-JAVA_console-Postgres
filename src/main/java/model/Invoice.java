package model;

import java.math.BigDecimal;
import java.util.UUID;

public class Invoice {
    private UUID id;
    private UUID payment_id;
    private String invoice_number;
    private BigDecimal subtotal_ht;
    private BigDecimal vat_amount;
    private BigDecimal total_ttc;

    public Invoice(UUID id, UUID payment_id, String invoice_number, BigDecimal subtotal_ht, BigDecimal vat_amount, BigDecimal total_ttc) {
        this.id = id;
        this.payment_id = payment_id;
        this.invoice_number = invoice_number;
        this.subtotal_ht = subtotal_ht;
        this.vat_amount = vat_amount;
        this.total_ttc = total_ttc;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public UUID getPayment_id() {
        return payment_id;
    }
    public void setPayment_id(UUID payment_id) {
        this.payment_id = payment_id;
    }
    public String getInvoice_number() {
        return invoice_number;
    }
    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }
    public BigDecimal getSubtotal_ht() {
        return subtotal_ht;
    }
    public void setSubtotal_ht(BigDecimal subtotal_ht) {
        this.subtotal_ht = subtotal_ht;
    }
    public BigDecimal getVat_amount() {
        return vat_amount;
    }
    public void setVat_amount(BigDecimal vat_amount) {
        this.vat_amount = vat_amount;
    }
    public BigDecimal getTotal_ttc() {
        return total_ttc;
    }
    public void setTotal_ttc(BigDecimal total_ttc) {
        this.total_ttc = total_ttc;
    }
}
