package controller;

import model.User;
import repository.impl.InMemoryRoomRepository;
import service.ReservationService;
import util.InputUtils;
import util.Menus;

import java.util.Scanner;

public class ReservationController {

    InMemoryRoomRepository romRepository;

    public void reservationServiceAffichier(User user,ReservationService reservationService){
        reservationService.myReservationsService(user);
    }

    public void createReservationController(Scanner scanner , User user, InMemoryRoomRepository roomRepository, ReservationService reservationService){

        romRepository = roomRepository;
        try {
            // String reservationCode  = InputUtils.lireString(scanner,"Saisir code de Reservation :");
            String roomNumber  = InputUtils.lireString(scanner,"Saisir Room Number :");
            int numberOfGuests  = InputUtils.lireInt(scanner,"Saisir Number Of Guests :");
            //long numberOfNighits  = InputUtils.lireLong(scanner,"Saisir Number Of Nights :");
            String dateDebut = InputUtils.lireString(scanner,"saisie la date de checkIn (slvp sous form 'YYYY,MM,DD')");
            String dateFin =InputUtils.lireString(scanner,"saisie la date de checkOut (slvp sous form 'YYYY,MMM,DD')");
            reservationService.createReservationService(roomRepository,user,roomNumber,numberOfGuests,dateDebut,dateFin);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void cancelReservationController(Scanner scanner,User user,ReservationService reservationService) throws Exception {
        String codeReservation = InputUtils.lireString(scanner,"saisir le code de reservation que tu vous avais annuller ");
        reservationService.cancelReservationService(codeReservation,user);
        Menus.menuApresLogin(scanner,user);
    }


    public void updateReservationController(Scanner scanner,ReservationService reservationService){
        String code = InputUtils.lireString(scanner,"saisir code de reservation : ");
        String roomNumber = InputUtils.lireString(scanner,"saisir roomNumber : ");
        int numberOfQuests = InputUtils.lireInt(scanner,"saisir number of guests : ");
        try {
            reservationService.updateReservationService(code,roomNumber,numberOfQuests);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
