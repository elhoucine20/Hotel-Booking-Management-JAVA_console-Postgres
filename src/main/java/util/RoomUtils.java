package util;

import model.Room;
import model.enums.RoomStatus;
import model.enums.RoomType;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomUtils {


    public static Optional<Room> mapRoom(ResultSet result) throws SQLException {
        UUID id = result.getObject("id", UUID.class);
        UUID user_id = result.getObject("user_id", UUID.class);
        String roomNumbr = result.getString("roomNumber");
        RoomType roomType =  RoomType.valueOf(result.getString("roomType"));
        BigDecimal pricePerNight = result.getObject("pricePerNight",BigDecimal.class);
        RoomStatus roomStatus = RoomStatus.valueOf(result.getString("roomStatus"));
        Integer capacity = result.getInt("capacity");
        Room room = new Room(id,user_id,roomNumbr,roomType,capacity,pricePerNight,roomStatus);
        return Optional.of(room);
    }



    public static List<Room> allRooms(ResultSet result) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        while (result.next()){
            UUID id = result.getObject("id", UUID.class);
            UUID user_id = result.getObject("user_id", UUID.class);
            String roomNumbr = result.getString("roomNumber");
            RoomType roomType =  RoomType.valueOf(result.getString("roomType"));
            BigDecimal pricePerNight = result.getBigDecimal("pricePerNight");
            RoomStatus roomStatus = RoomStatus.valueOf(result.getString("roomStatus"));
            Integer capacity = result.getInt("capacity");
            Room room = new Room(id,user_id,roomNumbr,roomType,capacity,pricePerNight,roomStatus);
            rooms.add(room);
        }
        return rooms;
    }

    public  static int updateRoom(PreparedStatement preparedStatement,String roomType, BigDecimal pricePerNight, String roomStatu, int roomCapacity, String roomNumber) throws SQLException {
        preparedStatement.setString(1,roomType);
        preparedStatement.setBigDecimal(2,pricePerNight);
        preparedStatement.setString(3,roomStatu);
        preparedStatement.setInt(4,roomCapacity);
        preparedStatement.setString(5,roomNumber);
        return preparedStatement.executeUpdate();
    }

    public static boolean validatePrice(BigDecimal price){
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("s'il vous plais le prix doit etre superieur a 0 !!");
        return true;
    }
    public static boolean validateCapacity(int capacity){
        if (capacity <= 0 || capacity > 5)
            throw new IllegalArgumentException("s'il vous plais saisir un capacity entre 1 et 5 !!");
        return true;
    }


}
