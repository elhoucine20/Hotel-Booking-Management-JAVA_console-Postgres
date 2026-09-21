package service;

import repository.impl.RoomRepository;

public class RoomService {


    public void repositoryAffichierRooms(RoomRepository roomRepository){
        roomRepository.afichierRooms();
    }

    public void setRoomsAvailableRepository(RoomRepository roomRepository){
        roomRepository.afichierRoomsAvailable();
    }


}
