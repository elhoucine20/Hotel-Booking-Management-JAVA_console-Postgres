package service;

import model.Room;
import model.User;
import model.enums.RoomStatus;
import model.enums.RoomType;
import repository.JdbcRoomRepository;
import repository.impl.RoomRepository;
import util.RoomUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RoomService {


    private final RoomRepository roomRepository;

    public RoomService(){
        this.roomRepository = new JdbcRoomRepository();
    }
    public boolean createRoomService (User user ,RoomType roomType, BigDecimal pricePerNight, int capacity, RoomStatus roomStatu){

        RoomUtils.validatePrice(pricePerNight);
        RoomUtils.validateCapacity(capacity);
        UUID id = UUID.randomUUID();
        UUID user_id = user.getId();
        String roomNumber = roomRepository.generateRoomNumber();
        Room room = new Room(id,user_id,roomNumber,roomType,capacity,pricePerNight,roomStatu);
        return roomRepository.save(room);
    }

    public List<Room> findAllService(){
      return   roomRepository.findAll();
    }

    public boolean updateRoomService(String roomNumber,RoomType roomType,BigDecimal priceNight,int capacity,RoomStatus roomStatus){

        RoomUtils.validatePrice(priceNight);
        RoomUtils.validateCapacity(capacity);
        Optional<Room> existRoom = roomRepository.findByNumber(roomNumber);
        if (existRoom.isEmpty()){
            System.out.println("ce room n'exist pas !!");
            return false;
        }
        Room room = existRoom.get();
        room.setType(roomType);
        room.setCapacity(capacity);
        room.setPricePerNight(priceNight);
        room.setStatus(roomStatus);
        return  roomRepository.update(room);
    }

}
