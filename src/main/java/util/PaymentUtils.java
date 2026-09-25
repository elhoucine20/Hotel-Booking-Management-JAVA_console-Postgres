package util;

import model.Invoice;
import model.Payment;
import model.enums.PaymentMethod;
import model.enums.PaymentStatus;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaymentUtils {

    public static Payment mapPayment(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id",UUID.class);
        UUID reservation_id = resultSet.getObject("reservation_id", UUID.class);
        BigDecimal amount = resultSet.getBigDecimal("amount");
        LocalDateTime payment_date = resultSet.getObject("payment_date",LocalDateTime.class);
        PaymentMethod payment_method = PaymentMethod.valueOf(resultSet.getString("payment_method"));
        PaymentStatus payment_status = PaymentStatus.valueOf(resultSet.getString("payment_status"));
        return new Payment(id,reservation_id,amount,payment_method,payment_status,payment_date);
    }

    public static void mapToInsertPayment(PreparedStatement statement, Payment payment) throws SQLException {
        statement.setObject(1,payment.getId());
        statement.setObject(2,payment.getReservation_id());
        statement.setBigDecimal(3,payment.getAmount());
        statement.setString(4,payment.getPayment_method().name());
        statement.setString(5,payment.getPaymentStatus().name());
    }

    public static List<Payment> mapPayments(ResultSet resultSet) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        while (resultSet.next()){
            payments.add(PaymentUtils.mapPayment(resultSet));
        }
        return payments;
    }
}
