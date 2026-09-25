package repository;

import db.DatabaseConnection;
import model.Invoice;
import repository.impl.InvoiceRepository;
import util.InvoiceUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

public class JdbcInvoiceRepository implements InvoiceRepository {

    private Connection connection = DatabaseConnection.getInstance().getConnection();
    @Override
    public boolean save(Invoice invoice) {
        String sql = "INSERT INTO invoices (id, payment_id, invoice_number, subtotal_ht, vat_amount, total_ttc) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setObject(1,invoice.getId());
            statement.setObject(2,invoice.getPayment_id());
            statement.setString(3,invoice.getInvoice_number());
            statement.setBigDecimal(4,invoice.getSubtotal_ht());
            statement.setBigDecimal(5,invoice.getVat_amount());
            statement.setBigDecimal(6,invoice.getTotal_ttc());
            return statement.executeUpdate() == 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Invoice findByPaymentId(UUID paymentId) {
        String sql = "SELECT * FROM invoices WHERE payment_id = ? ";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setObject(1,paymentId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
             return InvoiceUtils.mapInvoice(resultSet);
        }catch (Exception e){e.printStackTrace();}
        return null;
    }

    @Override
    public List<Invoice> findAll() {
        String sql = "SELECT * FROM invoices";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            return InvoiceUtils.mapInvoices(resultSet);
        }catch (Exception e){
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public String generateInvoiceNumber() {
        String sql = "SELECT nextval('invoice_number_seq')";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long number = resultSet.getLong(1);
                return String.format("INV-%04d", number);
            }
        }catch (Exception e){e.printStackTrace();}
        throw new RuntimeException("impossible de generer number invoice");
    }
}
