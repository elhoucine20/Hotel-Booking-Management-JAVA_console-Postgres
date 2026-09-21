package repository;

import model.Reservation;
import model.User;
import repository.impl.InMemoryReservationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class ReservationRepository implements InMemoryReservationRepository {
    @Override
    public void affichierReservationsUser(User user) {

    }

    @Override
    public boolean saveReservationRepository(UUID id, Reservation reservation) {
        return false;
    }

    @Override
    public Map<UUID, Reservation> getReservationsByRoomNumber(String roomNumber) {
        return Map.of();
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
    public Reservation getReservationsByCode(String code) {
        return null;
    }

    @Override
    public Map<UUID, Reservation> getReservations() {
        return Map.of();
    }

    @Override
    public Map<LocalDate, LocalDate> getLesDatesReservationsByRoom(String roomNumber) {
        return Map.of();
    }
}
