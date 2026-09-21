package repository;

import db.DatabaseConnection;
import model.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.UUID;

public class JdbcRoomRepository implements repository.impl.RoomRepository {

    Connection connection = DatabaseConnection.getInstance().getConnection();
    @Override
    public void afichierRooms() {

    }

    @Override
    public void save(Room room){

        String sql = "Insert into rooms values(?,?,?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setObject(1,room.getId());

        }catch (Exception e){
            e.printStackTrace();
        }
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
