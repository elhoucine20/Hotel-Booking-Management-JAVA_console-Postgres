package repository;

import db.DatabaseConnection;
import model.Reservation;
import model.User;
import model.enums.ReservationStatus;
import repository.impl.ReservationRepository;
import util.ReservationUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;

public class JdbcReservationRepository implements ReservationRepository {

    Connection connection = DatabaseConnection.getInstance().getConnection();
    @Override
    public List<Reservation> allReservationsUser(User user) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE user_id = ? ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // prochaine étape : récupérer les données
                Reservation reservation = ReservationUtils.mapReservation(resultSet);
                reservations.add(reservation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reservations;
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
    public boolean cancelReservationRepository(String codeReservation, User user){
        String sql = "UPDATE reservations SET reservationStatus = 'CANCELLED' WHERE reservationCode = ? AND user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, codeReservation);
            preparedStatement.setObject(2, user.getId());
            int rows = preparedStatement.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateReservationRepository(String code,String roomNumber, int numberOfGuests, BigDecimal totalPrice){

        String sql = "UPDATE reservations r SET room_id = room.id, numberOfGuests = ?, total_amount = ? " +
                "FROM rooms room WHERE r.reservationCode = ? AND room.roomNumber = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, numberOfGuests);
            preparedStatement.setBigDecimal(2, totalPrice);
            preparedStatement.setString(3, code);
            preparedStatement.setString(4, roomNumber);
            int rows = preparedStatement.executeUpdate();
            return rows == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Reservation findReservationsByCode(String code) {

        String sql = "SELECT * FROM reservations WHERE reservationCode = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ReservationUtils.mapReservation(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Reservation> findAll() {

        String sql = "SELECT * FROM reservations";
        try (Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(sql);
            return ReservationUtils.allReservations(resultSet);
        }catch (Exception e){
            e.printStackTrace();
        }
        return List.of();
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

    @Override
    public boolean updateReservationStatus(String reservationCode, ReservationStatus status) {

        String sql = "UPDATE reservations SET reservationStatus = ? WHRE reservationCode = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,status.name());
            preparedStatement.setString(2,reservationCode);
            int rows = preparedStatement.executeUpdate();
            return rows == 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


}
