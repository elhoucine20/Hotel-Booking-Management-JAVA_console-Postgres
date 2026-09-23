package repository;

import db.DatabaseConnection;
import model.Room;
import model.enums.RoomStatus;
import model.enums.RoomType;
import util.RoomUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JdbcRoomRepository implements repository.impl.RoomRepository {

    Connection connection = DatabaseConnection.getInstance().getConnection();

    @Override
    public boolean save(Room room) {

        String sql = "INSERT INTO rooms (id, roomNumber, user_id, roomType, pricePerNight, roomStatus, capacity) values(?,?,?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            int rows = RoomUtils.saveRoomUtil(statement,room);
            return rows == 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Room> findByNumber(String roomNumbre){

        String sql = "SELECT * FROM rooms WHERE roomNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,roomNumbre);
           ResultSet result =  preparedStatement.executeQuery();
           if (result.next())
             return RoomUtils.mapRoom(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public List<Room> findAll() {

        String sql = "SELECT * FROM rooms";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet resultSet = preparedStatement.executeQuery();
            return RoomUtils.allRooms(resultSet);
        }catch (Exception e){e.printStackTrace();return List.of();}
    }

    @Override
    public List<Room> findAvailable() {
        String sql = "SELECT * FROM rooms WHERE roomStatus = 'AVAILABLE'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            ResultSet resultSet = preparedStatement.executeQuery();
          return   RoomUtils.allRooms(resultSet);
        }catch (Exception e){e.printStackTrace();}
        return List.of();
    }

    @Override
    public boolean update(Room room) {
        String sql = "UPDATE rooms SET roomType = ?, pricePerNight= ?, roomStatus = ?, capacity = ? WHERE roomNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            int rows = RoomUtils.updateRoom(preparedStatement,room.getType().name(),room.getPricePerNight(),room.getStatus().name(),room.getCapacity(),room.getRoomNumber());
            return rows==1;
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    @Override
    public boolean deleteByNumber(String number)
    {
        String sql = "DELETE FROM rooms WHERE roomNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,number);
            int rows = preparedStatement.executeUpdate();
            return rows==1;
        }catch (Exception e){e.printStackTrace();}
        return false;
    }

    public String generateRoomNumber(){
        String sql ="SELECT nextval('room_number_seq')";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                long number = resultSet.getLong(1);
                return String.format("RN%04d",number);
            }
        }catch (SQLException e){e.printStackTrace();}
        throw new RuntimeException("impossible de generer number of room");
    }

}
