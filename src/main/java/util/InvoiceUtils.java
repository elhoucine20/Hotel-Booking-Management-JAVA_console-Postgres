package util;

import model.Invoice;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceUtils {

    public static Invoice mapInvoice(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        UUID payment_id = resultSet.getObject("payment_id", UUID.class);
        String invoice_number = resultSet.getString("invoice_number");
        BigDecimal subtotal_ht = resultSet.getBigDecimal("subtotal_ht");
        BigDecimal vat_amount = resultSet.getBigDecimal("vat_amount");
        BigDecimal total_ttc = resultSet.getBigDecimal("total_ttc");
        return new Invoice(id, payment_id, invoice_number, subtotal_ht, vat_amount, total_ttc);
    }

    public static List<Invoice> mapInvoices(ResultSet resultSet) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        while (resultSet.next()){
            invoices.add(InvoiceUtils.mapInvoice(resultSet));
        }
        return invoices;
    }
}
