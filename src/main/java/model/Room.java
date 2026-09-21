package model;

import model.enums.RoomStatus;
import model.enums.RoomType;

import java.math.BigDecimal;
import java.util.UUID;

public class Room {
    private UUID id;
    private String roomNumber;
    private RoomType type; // SINGLE or DOUBLE or SUITE
    private int capacity;
    private UUID user_id;
    private BigDecimal pricePerNight;
    private RoomStatus status;  //AVAILABLE  or MAINTENANCE


    public Room(UUID id,UUID user_id,String roomNumber, RoomType type, int capacity, BigDecimal pricePerNight, RoomStatus status) {
        this.id = id;
        this.user_id = user_id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    //======================== getter and setter ============================
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }
}
