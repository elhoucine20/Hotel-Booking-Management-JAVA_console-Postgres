package repository;

import model.Room;
import repository.impl.InMemoryRoomRepository;

import java.util.Map;
import java.util.UUID;

public class RoomRepository implements InMemoryRoomRepository {

    @Override
    public void afichierRooms() {

    }

    @Override
    public void afichierRoomsAvailable() {

    }

    @Override
    public Room getroomByNumber(String NRoom) {
        return null;
    }

    @Override
    public Map<UUID, Room> getRooms() {
        return Map.of();
    }
}
