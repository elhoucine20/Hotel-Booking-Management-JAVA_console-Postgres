package repository.impl;

import model.Room;

import java.util.Map;
import java.util.UUID;

public interface RoomRepository {

    public void afichierRooms();
    public void afichierRoomsAvailable();
    public  Room getroomByNumber(String NRoom);
    public Map<UUID, Room> getRooms();

    public void save(Room room);
}
