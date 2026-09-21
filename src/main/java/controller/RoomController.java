package controller;

import repository.impl.RoomRepository;
import service.RoomService;

public class RoomController {



    public void serviceAffichierRooms(RoomRepository roomRepo, RoomService romService){

        romService.repositoryAffichierRooms(roomRepo);
    }
    public void serviceAffichierRoomsAvailable(RoomRepository roomRepo, RoomService romService){
        romService.setRoomsAvailableRepository(roomRepo);
    }

}
