package util;

import model.Reservation;
import model.enums.ReservationStatus;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class ReservationUtils {


    public static int saveReservationUtil(PreparedStatement statement, Reservation reservation) throws SQLException {
        statement.setObject(1, reservation.getId());
        statement.setString(2, reservation.getReservationCode());
        statement.setObject(3, reservation.getUserId());
        statement.setObject(4, reservation.getRoom_id());
        statement.setInt(5, reservation.getNumberOfGuests());
        statement.setObject(6,reservation.getCheckIn());
        statement.setObject(7,reservation.getCheckOut());
        statement.setString(8,reservation.getStatus().name());
        statement.setBigDecimal(9,reservation.getTotalPrice());
        return  statement.executeUpdate();
    }
    public static Reservation mapReservation(ResultSet resultSet) throws SQLException {
        UUID id = resultSet.getObject("id", UUID.class);
        String reservationCode =
                resultSet.getString("reservationCode");
        UUID userId =
                resultSet.getObject("user_id", UUID.class);
        UUID roomId =
                resultSet.getObject("room_id", UUID.class);
        int numberOfGuests =
                resultSet.getInt("numberOfGuests");
        LocalDate checkIn =
                resultSet.getObject("check_in", LocalDate.class);
        LocalDate checkOut =
                resultSet.getObject("check_out", LocalDate.class);
        long numberOfNights =
                ChronoUnit.DAYS.between(checkIn, checkOut);
        BigDecimal totalPrice =
                resultSet.getBigDecimal("total_amount");
        ReservationStatus status =
                ReservationStatus.valueOf(
                        resultSet.getString("reservationStatus")
                );
        return new Reservation(
                id,
                reservationCode,
                userId,
                roomId,
                checkIn,
                checkOut,
                numberOfGuests,
                numberOfNights,
                totalPrice,
                status
        );
    }}
