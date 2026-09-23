package repository;

import db.DatabaseConnection;
import model.Reservation;
import model.User;
import repository.impl.ReservationRepository;
import util.ReservationUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class JdbcReservationRepository implements ReservationRepository {

    Connection connection = DatabaseConnection.getInstance().getConnection();
    @Override
    public void allReservationsUser(User user) {

    }

    @Override
    public boolean saveReservationRepository(Reservation reservation) {

        String sql = "INSERT INTO reservations(id,reservationCode,user_id," +
                " room_id,numberOfGuests,check_in,check_out,reservationStatus,total_amount) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            return ReservationUtils.saveReservationUtil(preparedStatement,reservation) == 1;
        }catch (Exception e){e.printStackTrace();}
        return false;
    }
    @Override
    public boolean cancelReservationRepository(String codeReservation, User user) {
        return false;
    }

    @Override
    public boolean updateReservationRepository(String code, String roomNumber, int numberOfGuests, BigDecimal totalPrice) {
        return false;
    }

    @Override
    public Reservation findReservationsByCode(String code) {
        return null;
    }

    @Override
    public Map<UUID, Reservation> findAll() {
        return Map.of();
    }

    @Override
    public Map<LocalDate, LocalDate> findLesDatesReservationsByRoom(String roomNumber) {

        Map<LocalDate, LocalDate> reservations = new HashMap<>();

        String sql = "SELECT r.* FROM reservations r " +
                "JOIN rooms room ON r.room_id = room.id " +
                "WHERE room.roomNumber = ? " +
                "AND r.reservationStatus = 'CONFIRMED'";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {
            preparedStatement.setString(1, roomNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LocalDate checkIn =
                        resultSet.getObject("check_in", LocalDate.class);
                LocalDate checkOut =
                        resultSet.getObject("check_out", LocalDate.class);
                reservations.put(checkIn, checkOut);
            }
            return reservations;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Map.of();
    }

    public String generateReservationCode() {

        String sql = "SELECT nextval('reservation_code_seq')";

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql)) {

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            if (resultSet.next()) {

                long number = resultSet.getLong(1);

                return String.format("RES-%04d", number);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RuntimeException(
                "Impossible de générer le code de réservation"
        );
    }

}
