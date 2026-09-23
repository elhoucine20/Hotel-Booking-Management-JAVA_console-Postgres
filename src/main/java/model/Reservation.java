package model;

import model.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
    private UUID id;
    private String reservationCode;
    private UUID userId;
    private  UUID room_id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfGuests;
    private long numberOfNights;
    private BigDecimal totalPrice;
    private ReservationStatus status;

    //======================== Constructer ===========================

    public Reservation(UUID id, String reservationCode, UUID userId, UUID room_id, LocalDate checkIn, LocalDate checkOut,
                       int numberOfGuests, long numberOfNights, BigDecimal totalPrice, ReservationStatus status) {
        this.id = id;
        this.reservationCode = reservationCode;
        this.userId = userId;
        this.room_id = room_id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfGuests = numberOfGuests;
        this.numberOfNights = numberOfNights;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    //======================== getter and setter ===========================
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getReservationCode() {
        return reservationCode;
    }
    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public LocalDate getCheckIn() {
        return checkIn;
    }
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }
    public LocalDate getCheckOut() {
        return checkOut;
    }
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    public long getNumberOfNights() {
        return numberOfNights;
    }
    public void setNumberOfNights(long numberOfNights) {
        this.numberOfNights = numberOfNights;
    }
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    public ReservationStatus getStatus() {
        return status;
    }
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
    public UUID getRoom_id() {
        return room_id;
    }

    public void setRoom_id(UUID room_id) {
        this.room_id = room_id;
    }
    //===========================  ============================
}
