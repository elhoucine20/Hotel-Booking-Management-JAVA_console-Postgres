package repository.impl;

import model.Reservation;
import model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public interface InMemoryReservationRepository {

    public void affichierReservationsUser(User user);
    public boolean saveReservationRepository(UUID id, Reservation reservation);
    public Map<UUID,Reservation> getReservationsByRoomNumber(String roomNumber);
    public boolean cancelReservationRepository(String codeReservation,User user);
    public boolean updateReservationRepository(String code,String roomNumber, int numberOfGuests, BigDecimal totalPrice);
    public Reservation getReservationsByCode(String code);
    public  Map<UUID,Reservation> getReservations();
    public Map<LocalDate,LocalDate> getLesDatesReservationsByRoom(String roomNumber);
}
