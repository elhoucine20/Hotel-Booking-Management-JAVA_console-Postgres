package controller;

import model.Room;
import model.User;
import model.enums.RoomStatus;
import model.enums.RoomType;
import repository.impl.RoomRepository;
import service.RoomService;
import util.InputUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class RoomController {

    RoomService roomService = new RoomService();
    public void createRoomController(Scanner scanner, User user){


        try {
            RoomType roomType = null;
            do {
                System.out.println("Choisir Room Type :");
                System.out.println("1 - SINGLE");
                System.out.println("2 - DOUBLE");
                System.out.println("3 - SUITE");
                int choix = InputUtils.lireInt(scanner,"");
                switch (choix){
                    case 1: roomType = RoomType.SINGLE; break;
                    case 2: roomType = RoomType.DOUBLE; break;
                    case 3: roomType = RoomType.SUITE; break;
                    default:
                        System.out.println("s'il vous plais choisir un number entre 1 et 3"); break;
                }

            }while (roomType == null);

            int capacity = InputUtils.lireInt(scanner,"Saisie le capacity de room : ");
            RoomStatus roomStatu = null;
            do {
                System.out.println("Choisir Room Statu :");
                System.out.println("1 - AVAILABLE");
                System.out.println("2 - MAINTENANCE");
                int choise = InputUtils.lireInt(scanner,"");
                switch (choise){
                    case 1: roomStatu = RoomStatus.AVAILABLE; break;
                    case 2: roomStatu = RoomStatus.MAINTENANCE; break;
                    default:
                        System.out.println("s'il vous plais choisir un number entre 1 ou 2"); break;
                }

            }while (roomStatu == null);

            BigDecimal pricePerNight = InputUtils.lireBigDecimal(scanner,"Saisie price by night : ");

            if (roomService.createRoomService(user,roomType,pricePerNight,capacity,roomStatu))
                System.out.println("Room created avec success !!");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    public void allRoomsController(){
        System.out.println("=============== ROOMS ==============");
        if (roomService.findAllService().isEmpty())
            System.out.println("aucun room !!");
        else
            this.affichier(roomService.findAllService());
    }

    public void updateRoomController(Scanner scanner,User user){
        try {
            String roomNumber = InputUtils.lireString(scanner,"saisie le number de room : ");
            RoomType roomType = null;
            do {
                System.out.println("saisie le nouveau roomType :");
                System.out.println("1 - SINGLE");
                System.out.println("2 - DOUBLE");
                System.out.println("3 - SUITE");

                int choix = InputUtils.lireInt(scanner, "");

                switch (choix) {
                    case 1:
                        roomType = RoomType.SINGLE;
                        break;
                    case 2:
                        roomType = RoomType.DOUBLE;
                        break;
                    case 3:
                        roomType = RoomType.SUITE;
                        break;
                    default:
                        System.out.println("s'il vou plais saisie un number  entre 1 et 3.");
                }

            } while (roomType == null);

            RoomStatus roomStatu = null;

            do {
                System.out.println("Saisir le nouveau roomStatus :");
                System.out.println("1 - AVAILABLE");
                System.out.println("2 - MAINTENANCE");

                int choix = InputUtils.lireInt(scanner, "");

                switch (choix) {
                    case 1:
                        roomStatu = RoomStatus.AVAILABLE;
                        break;
                    case 2:
                        roomStatu = RoomStatus.MAINTENANCE;
                        break;
                    default:
                        System.out.println("saisie  choix  1 ou 2.");
                }

            } while (roomStatu == null);

            BigDecimal pricePerNight =
                    InputUtils.lireBigDecimal(scanner, "Saisir le nouveau price PerNight : ");
            int capacity =
                    InputUtils.lireInt(scanner, "Saisir une nouvelle capacity : ");
            if (roomService.updateRoomService(roomNumber,roomType,pricePerNight,capacity,roomStatu))
                System.out.println("update avec sucees !");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void availableRoomsController(){
        if (roomService.availableRoomsService().isEmpty())
            System.out.println("aucun room !!");
        else
            this.affichier(roomService.availableRoomsService());
    }

    private void affichier(List<Room> rooms){
        for (Room room: rooms){
            System.out.println("Number -> '"+room.getRoomNumber()+"' : Type -> '"+room.getType()+"' : Capacity -> '"+room.getCapacity()+"' : Price By Night -> '"+room.getPricePerNight()+"' : Statu -> '"+room.getStatus()+"'");
        }

    }
}
