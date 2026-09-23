package repository.impl;

import model.Reservation;
import model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReservationRepository {

    public List<Reservation> allReservationsUser(User user);
    public boolean saveReservationRepository(Reservation reservation);
    public boolean cancelReservationRepository(String codeReservation, User user);
    public boolean updateReservationRepository(String code,String roomNumber, int numberOfGuests, BigDecimal totalPrice);


    public Reservation findReservationsByCode(String code);
    public  Map<UUID,Reservation> findAll();
    public Map<LocalDate,LocalDate> findLesDatesReservationsByRoom(String roomNumber);
    public String generateReservationCode();
}
