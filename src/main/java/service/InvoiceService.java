package service;

import model.Invoice;
import model.Payment;
import repository.JdbcInvoiceRepository;
import repository.impl.InvoiceRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class InvoiceService {

    private InvoiceRepository invoiceRepository = new JdbcInvoiceRepository();

    public Invoice createInvoiceService(Payment payment){
        BigDecimal totalTtc = payment.getAmount();
        BigDecimal subtotalHt = totalTtc.divide(BigDecimal.valueOf(1.20),2, RoundingMode.HALF_UP);
        BigDecimal vatAmount = totalTtc.subtract(subtotalHt);
        UUID id = UUID.randomUUID();
        UUID payment_id = payment.getId();
        String invoice_number = invoiceRepository.generateInvoiceNumber();
        return  new Invoice(id,payment_id,invoice_number,subtotalHt,vatAmount,totalTtc);
    }
}
