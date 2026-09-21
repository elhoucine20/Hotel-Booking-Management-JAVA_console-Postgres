package service;

import repository.impl.InMemoryRoomRepository;

public class RoomService {


    public void repositoryAffichierRooms(InMemoryRoomRepository roomRepository){
        roomRepository.afichierRooms();
    }

    public void setRoomsAvailableRepository(InMemoryRoomRepository roomRepository){
        roomRepository.afichierRoomsAvailable();
    }


}
