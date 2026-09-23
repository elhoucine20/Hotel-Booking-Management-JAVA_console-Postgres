package repository.impl;

import model.Room;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    public boolean save(Room room);
    public Optional<Room> findByNumber(String number);

    public List<Room> findAll();
    public List<Room> findAvailable();
    public boolean update(Room room);
    public boolean deleteByNumber(String id);

    public String generateRoomNumber();



}
