package repository;

import db.DatabaseConnection;
import model.Payment;
import model.User;
import repository.impl.PaymentRepository;
import util.PaymentUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class JdbcPaymentRepository implements PaymentRepository {
    private Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(Payment payment) {
        String sql = "INSERT INTO payments (id,reservation_id,amount,payment_method,payment_status) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            PaymentUtils.mapToInsertPayment(statement,payment);
            return  statement.executeUpdate() == 1;
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    @Override
    public List<Payment> findByUserId(User user) {

        String sql = "SELECT p.* FROM payments p " +
                "JOIN reservations r ON r.id = p.reservation_id WHERE r.user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setObject(1,user.getId());
            ResultSet resultSet = statement.executeQuery();
            return PaymentUtils.mapPayments(resultSet);
        }catch (Exception e){e.printStackTrace();}
        return List.of();
    }
}
