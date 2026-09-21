package controller;

import repository.impl.InMemoryRoomRepository;
import service.RoomService;

public class RoomController {

    public void serviceAffichierRooms(InMemoryRoomRepository roomRepo,RoomService romService){

        romService.repositoryAffichierRooms(roomRepo);
    }
    public void serviceAffichierRoomsAvailable(InMemoryRoomRepository roomRepo, RoomService romService){
        romService.setRoomsAvailableRepository(roomRepo);
    }

}
