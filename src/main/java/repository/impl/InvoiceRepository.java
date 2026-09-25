package repository.impl;

import model.Invoice;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository {
    public boolean save(Invoice invoice);
    public Invoice findByPaymentId(UUID paymentId);
    public List<Invoice> findAll();
    public String generateInvoiceNumber();

}
